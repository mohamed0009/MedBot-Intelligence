"""Database models"""

from sqlalchemy import Column, String, Text, TIMESTAMP
from sqlalchemy.dialects.postgresql import UUID, JSONB
from sqlalchemy.sql import func
import uuid
from ..database import Base

class SynthesisReport(Base):
    __tablename__ = "synthesis_reports"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    report_type = Column(String(50), nullable=False, index=True)  # patient_summary, comparison
    patient_ids = Column(JSONB)  # Array of patient IDs
    synthesis_text = Column(Text, nullable=False)
    key_findings = Column(JSONB)
    llm_model = Column(String(100))
    created_at = Column(TIMESTAMP, server_default=func.now(), index=True)
