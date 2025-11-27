"""
Configuration management for IndexeurSémantique service
"""

from pydantic_settings import BaseSettings
from typing import List
import os


class Settings(BaseSettings):
    """Application settings"""
    
    # Service
    SERVICE_NAME: str = "indexeur-semantique"
    LOG_LEVEL: str = "INFO"
    
    # Database
    DATABASE_URL: str = "postgresql://docqa_admin:changeme@postgres:5432/indexeur"
    
    # RabbitMQ
    RABBITMQ_HOST: str = "rabbitmq"
    RABBITMQ_PORT: int = 5672
    RABBITMQ_USER: str = "docqa_rabbitmq"
    RABBITMQ_PASS: str = "changeme"
    RABBITMQ_CONSUME_QUEUE: str = "anonymized_documents"
    RABBITMQ_PUBLISH_QUEUE: str = "indexed_documents"
    
    # Embedding Model Configuration
    EMBEDDING_MODEL: str = "sentence-transformers/all-MiniLM-L6-v2"
    # Alternative medical models:
    # "dmis-lab/biobert-base-cased-v1.2"
    # "emilyalsentzer/Bio_ClinicalBERT"
    # "sentence-transformers/all-mpnet-base-v2"
    
    EMBEDDING_DEVICE: str = "cpu"  # or "cuda" for GPU
    EMBEDDING_DIMENSION: int = 384  # Depends on model
    EMBEDDING_MAX_LENGTH: int = 512
    EMBEDDING_BATCH_SIZE: int = 32
    
    # FAISS Configuration
    FAISS_INDEX_TYPE: str = "IndexFlatL2"  # or IndexIVFFlat for large datasets
    FAISS_INDEX_PATH: str = "/data/faiss_indices"
    FAISS_NLIST: int = 100  # For IVFFlat
    FAISS_NPROBE: int = 10  # Search parameter
    
    # Chunking Strategy
    CHUNKING_STRATEGY: str = "paragraph"  # paragraph, section, sliding_window, semantic
    CHUNK_SIZE: int = 512  # tokens
    CHUNK_OVERLAP: int = 50  # tokens
    MIN_CHUNK_SIZE: int = 50
    MAX_CHUNK_SIZE: int = 1000
    
    # Search Configuration
    SEARCH_TOP_K: int = 10
    SIMILARITY_THRESHOLD: float = 0.7
    ENABLE_HYBRID_SEARCH: bool = True
    
    # BM25 (Keyword Search) Configuration
    BM25_K1: float = 1.5
    BM25_B: float = 0.75
    
    # Indexing
    INDEX_BATCH_SIZE: int = 100
    WORKERS: int = 4
    
    # CORS
    CORS_ORIGINS: List[str] = ["http://localhost:3000", "http://localhost:8000"]
    
    class Config:
        env_file = ".env"
        case_sensitive = True


settings = Settings()
