"""
Embedding generator using SentenceTransformers
"""

from sentence_transformers import SentenceTransformer
from typing import List, Union
import numpy as np
import torch
import structlog

from ..config import settings

logger = structlog.get_logger()


class EmbeddingGenerator:
    """Generate embeddings for text using SentenceTransformers"""
    
    def __init__(self):
        self.model = None
        self.model_name = settings.EMBEDDING_MODEL
        self.device = settings.EMBEDDING_DEVICE
        self.dimension = settings.EMBEDDING_DIMENSION
        self._load_model()
    
    def _load_model(self):
        """Load the sentence transformer model"""
        try:
            logger.info("Loading embedding model", model=self.model_name, device=self.device)
            
            self.model = SentenceTransformer(self.model_name, device=self.device)
            
            # Verify dimension
            test_embedding = self.model.encode("test")
            actual_dim = len(test_embedding)
            
            if actual_dim != self.dimension:
                logger.warning(
                    "Dimension mismatch",
                    expected=self.dimension,
                    actual=actual_dim
                )
                self.dimension = actual_dim
            
            logger.info(
                "Embedding model loaded successfully",
                model=self.model_name,
                dimension=self.dimension
            )
            
        except Exception as e:
            logger.error("Failed to load embedding model", error=str(e))
            raise
    
    def generate_embedding(self, text: str) -> np.ndarray:
        """
        Generate embedding for single text
        
        Args:
            text: Text to embed
            
        Returns:
            Embedding vector as numpy array
        """
        try:
            # Truncate if too long
            if len(text) > settings.EMBEDDING_MAX_LENGTH * 4:  # Rough character estimate
                logger.warning("Text too long, truncating", length=len(text))
                text = text[:settings.EMBEDDING_MAX_LENGTH * 4]
            
            embedding = self.model.encode(
                text,
                convert_to_numpy=True,
                normalize_embeddings=True  # L2 normalization for cosine similarity
            )
            
            return embedding
            
        except Exception as e:
            logger.error("Embedding generation failed", error=str(e))
            raise
    
    def generate_embeddings_batch(self, texts: List[str]) -> np.ndarray:
        """
        Generate embeddings for multiple texts in batch
        
        Args:
            texts: List of texts to embed
            
        Returns:
            Array of embedding vectors
        """
        try:
            logger.info("Generating batch embeddings", batch_size=len(texts))
            
            embeddings = self.model.encode(
                texts,
                batch_size=settings.EMBEDDING_BATCH_SIZE,
                convert_to_numpy=True,
                normalize_embeddings=True,
                show_progress_bar=len(texts) > 100
            )
            
            logger.info("Batch embeddings generated", count=len(embeddings))
            
            return embeddings
            
        except Exception as e:
            logger.error("Batch embedding failed", error=str(e))
            raise
    
    def compute_similarity(self, embedding1: np.ndarray, embedding2: np.ndarray) -> float:
        """
        Compute cosine similarity between two embeddings
        
        Args:
            embedding1: First embedding vector
            embedding2: Second embedding vector
            
        Returns:
            Similarity score (0-1)
        """
        # Since embeddings are normalized, dot product = cosine similarity
        similarity = np.dot(embedding1, embedding2)
        return float(similarity)
    
    def compute_similarities(
        self,
        query_embedding: np.ndarray,
        document_embeddings: np.ndarray
    ) -> np.ndarray:
        """
        Compute similarities between query and multiple documents
        
        Args:
            query_embedding: Query embedding vector
            document_embeddings: Array of document embedding vectors
            
        Returns:
            Array of similarity scores
        """
        # Matrix multiplication for batch similarity
        similarities = np.dot(document_embeddings, query_embedding)
        return similarities


# Global embedding generator instance
_generator = None


def get_embedding_generator() -> EmbeddingGenerator:
    """Get embedding generator singleton"""
    global _generator
    if _generator is None:
        _generator = EmbeddingGenerator()
    return _generator
