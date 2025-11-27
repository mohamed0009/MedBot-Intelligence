"""
RAG (Retrieval Augmented Generation) pipeline
"""

import httpx
from typing import List, Dict, Any
import structlog

from ..config import settings
from ..llm import get_llm

logger = structlog.get_logger()


class RAGPipeline:
    """RAG pipeline for question answering"""
    
    def __init__(self):
        self.llm = get_llm()
        self.search_url = f"{settings.SEARCH_SERVICE_URL}/api/v1/search/search"
    
    async def retrieve_context(self, query: str) -> List[Dict[str, Any]]:
        """
        Retrieve relevant document chunks for query
        
        Args:
            query: User question
            
        Returns:
            List of relevant chunks with metadata
        """
        try:
            logger.info("Retrieving context", query=query)
            
            async with httpx.AsyncClient() as client:
                response = await client.post(
                    self.search_url,
                    json={
                        "query": query,
                        "top_k": settings.RETRIEVAL_TOP_K,
                        "similarity_threshold": settings.RETRIEVAL_MIN_SIMILARITY
                    },
                    timeout=30.0
                )
                
                response.raise_for_status()
                data = response.json()
                
                chunks = data.get("results", [])
                
                logger.info("Context retrieved", chunks_found=len(chunks))
                
                return chunks
                
        except Exception as e:
            logger.error("Context retrieval failed", error=str(e))
            raise
    
    def format_context(self, chunks: List[Dict[str, Any]]) -> str:
        """
        Format retrieved chunks into context string
        
        Args:
            chunks: List of retrieved chunks
            
        Returns:
            Formatted context string with source citations
        """
        if not chunks:
            return "No relevant context found."
        
        context_parts = []
        
        for idx, chunk in enumerate(chunks, 1):
            text = chunk.get("chunk_text", "")
            source_id = f"Source {idx}"
            
            context_parts.append(f"[{source_id}] {text}")
        
        return "\n\n".join(context_parts)
    
    def build_prompt(self, question: str, context: str) -> str:
        """
        Build complete prompt for LLM
        
        Args:
            question: User question
            context: Retrieved context
            
        Returns:
            Complete prompt string
        """
        return settings.QUESTION_PROMPT_TEMPLATE.format(
            context=context,
            question=question
        )
    
    async def answer_question(
        self,
        question: str,
        include_sources: bool = True
    ) -> Dict[str, Any]:
        """
        Answer question using RAG pipeline
        
        Args:
            question: User question
            include_sources: Whether to include source citations
            
        Returns:
            Dict with answer, sources, and metadata
        """
        try:
            # 1. Retrieve relevant chunks
            chunks = await self.retrieve_context(question)
            
            if not chunks:
                return {
                    "answer": "I don't have enough information in the available documents to answer this question.",
                    "sources": [],
                    "chunks_retrieved": 0,
                    "has_answer": False
                }
            
            # 2. Format context
            context = self.format_context(chunks)
            
            # 3. Build prompt
            prompt = self.build_prompt(question, context)
            
            # 4. Generate answer
            llm_response = self.llm.generate(
                prompt=prompt,
                system_prompt=settings.SYSTEM_PROMPT
            )
            
            # 5. Extract citations
            citations = self._extract_citations(
                llm_response["response"],
                chunks
            ) if include_sources else []
            
            return {
                "answer": llm_response["response"],
                "sources": citations,
                "chunks_retrieved": len(chunks),
                "tokens_used": llm_response.get("tokens_used", 0),
                "model": llm_response.get("model"),
                "has_answer": True,
                "retrieved_chunks": [
                    {
                        "chunk_id": c.get("chunk_id"),
                        "document_id": c.get("document_id"),
                        "similarity": c.get("similarity"),
                        "text": c.get("chunk_text", "")[:200] + "..."  # Preview
                    }
                    for c in chunks
                ]
            }
            
        except Exception as e:
            logger.error("Question answering failed", error=str(e))
            raise
    
    def _extract_citations(
        self,
        answer: str,
        chunks: List[Dict[str, Any]]
    ) -> List[Dict[str, Any]]:
        """
        Extract citations from LLM response
        
        Args:
            answer: LLM generated answer
            chunks: Retrieved chunks
            
        Returns:
            List of citation objects
        """
        import re
        
        citations = []
        
        # Find all [Source X] patterns
        pattern = r'\[Source (\d+)\]'
        matches = re.findall(pattern, answer)
        
        for match in matches:
            source_idx = int(match) - 1  # Convert to 0-indexed
            
            if 0 <= source_idx < len(chunks):
                chunk = chunks[source_idx]
                
                citation = {
                    "source_id": f"Source {match}",
                    "chunk_id": chunk.get("chunk_id"),
                    "document_id": chunk.get("document_id"),
                    "chunk_text": chunk.get("chunk_text", ""),
                    "similarity": chunk.get("similarity", 0)
                }
                
                citations.append(citation)
        
        return citations
    
    async def answer_question_stream(self, question: str):
        """
        Stream answer for real-time display
        
        Args:
            question: User question
            
        Yields:
            Chunks of generated answer
        """
        # 1. Retrieve context
        chunks = await self.retrieve_context(question)
        
        if not chunks:
            yield {
                "type": "error",
                "content": "No relevant information found"
            }
            return
        
        # 2. Build prompt
        context = self.format_context(chunks)
        prompt = self.build_prompt(question, context)
        
        # 3. Stream response
        yield {"type": "sources", "content": chunks}
        
        for chunk in self.llm.generate_stream(prompt, settings.SYSTEM_PROMPT):
            yield {"type": "text", "content": chunk}


# Global RAG pipeline instance
_rag_pipeline = None


def get_rag_pipeline() -> RAGPipeline:
    """Get RAG pipeline singleton"""
    global _rag_pipeline
    if _rag_pipeline is None:
        _rag_pipeline = RAGPipeline()
    return _rag_pipeline
