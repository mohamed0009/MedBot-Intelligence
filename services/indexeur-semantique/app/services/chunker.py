"""
Text chunking strategies for semantic indexing
"""

from typing import List, Dict, Any
import re
import structlog

from ..config import settings

logger = structlog.get_logger()


class TextChunker:
    """Text chunking with multiple strategies"""
    
    def __init__(self):
        self.strategy = settings.CHUNKING_STRATEGY
        self.chunk_size = settings.CHUNK_SIZE
        self.chunk_overlap = settings.CHUNK_OVERLAP
    
    def chunk_text(self, text: str, strategy: str = None) -> List[Dict[str, Any]]:
        """
        Chunk text using specified strategy
        
        Args:
            text: Text to chunk
            strategy: Chunking strategy (paragraph, section, sliding_window, semantic)
            
        Returns:
            List of chunks with metadata
        """
        strategy = strategy or self.strategy
        
        try:
            if strategy == "paragraph":
                return self._chunk_by_paragraph(text)
            elif strategy == "section":
                return self._chunk_by_section(text)
            elif strategy == "sliding_window":
                return self._chunk_by_sliding_window(text)
            elif strategy == "semantic":
                return self._chunk_by_semantic(text)
            else:
                raise ValueError(f"Unknown chunking strategy: {strategy}")
                
        except Exception as e:
            logger.error("Chunking failed", error=str(e), strategy=strategy)
            raise
    
    def _chunk_by_paragraph(self, text: str) -> List[Dict[str, Any]]:
        """Split text by paragraphs"""
        
        # Split by double newlines
        paragraphs = re.split(r'\n\s*\n', text)
        
        chunks = []
        position = 0
        
        for idx, para in enumerate(paragraphs):
            para = para.strip()
            if len(para) < settings.MIN_CHUNK_SIZE:
                continue
            
            # If paragraph too long, split further
            if len(para) > settings.MAX_CHUNK_SIZE:
                sub_chunks = self._split_long_text(para)
                for sub_idx, sub_chunk in enumerate(sub_chunks):
                    chunks.append({
                        "index": len(chunks),
                        "text": sub_chunk,
                        "start": position,
                        "end": position + len(sub_chunk),
                        "strategy": "paragraph",
                        "metadata": {"paragraph_index": idx, "sub_index": sub_idx}
                    })
                    position += len(sub_chunk)
            else:
                chunks.append({
                    "index": idx,
                    "text": para,
                    "start": position,
                    "end": position + len(para),
                    "strategy": "paragraph",
                    "metadata": {"paragraph_index": idx}
                })
                position += len(para)
        
        logger.info("Chunked by paragraph", chunks_count=len(chunks))
        return chunks
    
    def _chunk_by_section(self, text: str) -> List[Dict[str, Any]]:
        """Split text by sections (headers, markers)"""
        
        # Detect section headers (lines starting with numbers, caps, or markers)
        section_pattern = r'(?:^|\n)(?:#{1,6}\s+|\d+\.\s+|[A-Z][A-Z\s]+:|\*\*[^*]+\*\*)'
        
        sections = re.split(section_pattern, text)
        headers = re.findall(section_pattern, text)
        
        chunks = []
        position = 0
        
        for idx, section in enumerate(sections):
            section = section.strip()
            if len(section) < settings.MIN_CHUNK_SIZE:
                continue
            
            # Add header if available
            header = headers[idx - 1] if idx > 0 and idx <= len(headers) else ""
            full_text = f"{header} {section}".strip()
            
            chunks.append({
                "index": idx,
                "text": full_text,
                "start": position,
                "end": position + len(full_text),
                "strategy": "section",
                "metadata": {"section_index": idx, "header": header.strip()}
            })
            position += len(full_text)
        
        logger.info("Chunked by section", chunks_count=len(chunks))
        return chunks
    
    def _chunk_by_sliding_window(self, text: str) -> List[Dict[str, Any]]:
        """Split text using sliding window with overlap"""
        
        words = text.split()
        chunks = []
        
        start_idx = 0
        chunk_idx = 0
        
        while start_idx < len(words):
            end_idx = min(start_idx + self.chunk_size, len(words))
            
            chunk_words = words[start_idx:end_idx]
            chunk_text = " ".join(chunk_words)
            
            if len(chunk_text) >= settings.MIN_CHUNK_SIZE:
                chunks.append({
                    "index": chunk_idx,
                    "text": chunk_text,
                    "start": start_idx,
                    "end": end_idx,
                    "strategy": "sliding_window",
                    "metadata": {"word_start": start_idx, "word_end": end_idx}
                })
                chunk_idx += 1
            
            # Move window
            start_idx += (self.chunk_size - self.chunk_overlap)
        
        logger.info("Chunked by sliding window", chunks_count=len(chunks))
        return chunks
    
    def _chunk_by_semantic(self, text: str) -> List[Dict[str, Any]]:
        """
        Split text by semantic boundaries (experimental)
        Uses simple heuristics for topic changes
        """
        
        # For now, use paragraph strategy with semantic hints
        # In production, could use more sophisticated NLP
        
        chunks = self._chunk_by_paragraph(text)
        
        # Add semantic similarity markers (placeholder)
        for chunk in chunks:
            chunk["strategy"] = "semantic"
        
        return chunks
    
    def _split_long_text(self, text: str) -> List[str]:
        """Split long text into smaller chunks"""
        
        words = text.split()
        chunks = []
        
        for i in range(0, len(words), self.chunk_size - self.chunk_overlap):
            chunk = " ".join(words[i:i + self.chunk_size])
            if len(chunk) >= settings.MIN_CHUNK_SIZE:
                chunks.append(chunk)
        
        return chunks


# Global chunker instance
_chunker = None


def get_chunker() -> TextChunker:
    """Get text chunker singleton"""
    global _chunker
    if _chunker is None:
        _chunker = TextChunker()
    return _chunker
