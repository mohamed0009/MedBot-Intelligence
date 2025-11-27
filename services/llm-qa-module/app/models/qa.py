"""
Database models for LLM QA Module
"""

from sqlalchemy import Column, String, Integer, Text, TIMESTAMP, Float, Boolean
from sqlalchemy.dialects.postgresql import UUID, JSONB
from sqlalchemy.sql import func
import uuid

from ..database import Base


class QASession(Base):
    """QA session model - groups related queries"""
    
    __tablename__ = "qa_sessions"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    
    # Session info
    user_id = Column(String(100), index=True)
    session_name = Column(String(255))
    
    # Statistics
    total_queries = Column(Integer, default=0)
    total_tokens_used = Column(Integer, default=0)
    
    # Audit
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.now(), index=True)
    updated_at = Column(TIMESTAMP, nullable=False, server_default=func.now(), onupdate=func.now())
    
    def __repr__(self):
        return f"<QASession {self.id} ({self.user_id})>"


class QAQuery(Base):
    """Individual Q&A query model"""
    
    __tablename__ = "qa_queries"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    
    # Session reference
    session_id = Column(UUID(as_uuid=True), index=True)
    
    # Query
    question = Column(Text, nullable=False)
    
    # Retrieved context
    retrieved_chunks = Column(JSONB)  # Array of {chunk_id, document_id, similarity, text}
    retrieval_count = Column(Integer)
    
    # LLM Response
    answer = Column(Text, nullable=False)
    llm_model = Column(String(100), nullable=False)
    
    # Citations
    citations = Column(JSONB)  # Array of {source_id, document_id, chunk_text}
    
    # Performance metrics
    retrieval_time_ms = Column(Integer)
    llm_time_ms = Column(Integer)
    total_time_ms = Column(Integer)
    tokens_used = Column(Integer)
    
    # Quality metrics
    confidence_score = Column(Float)
    has_citations = Column(Boolean, default=True)
    
    # User feedback
    user_rating = Column(Integer)  # 1-5 stars
    user_feedback = Column(Text)
    
    # Audit
    created_at = Column(TIMESTAMP, nullable=False, server_default=func.now(), index=True)
    
    def __repr__(self):
        return f"<QAQuery {self.id}>"
