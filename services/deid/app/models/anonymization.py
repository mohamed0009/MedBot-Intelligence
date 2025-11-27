"""
Database models for DeID service
"""

from sqlalchemy import Column, String, Integer, Text, TIMESTAMP, Float
from sqlalchemy.dialects.postgresql import UUID, JSONB
from sqlalchemy.sql import func
import uuid

from ..database import Base


class AnonymizationLog(Base):
    """Anonymization log model"""
    
    __tablename__ = "anonymization_logs"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    
    # Document reference
    document_id = Column(UUID(as_uuid=True), nullable=False, index=True)
    
    # Content
    original_content = Column(Text, nullable=False)
    anonymized_content = Column(Text, nullable=False)
    
    # PII entities detected (array of {type, text, start, end, confidence})
    pii_entities = Column(JSONB, nullable=False)
    
    # Configuration
    anonymization_strategy = Column(String(50), nullable=False)  # redact, replace, hash
    
    # Performance metrics
    processing_time_ms = Column(Integer)
    entities_count = Column(Integer)
    confidence_avg = Column(Float)
    
    # Audit
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.now(), index=True)
    
    def __repr__(self):
        return f"<AnonymizationLog {self.id} ({self.anonymization_strategy})>"
