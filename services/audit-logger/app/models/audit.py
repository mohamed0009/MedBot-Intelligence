"""Audit models"""
from sqlalchemy import Column, String, Text, TIMESTAMP, Integer
from sqlalchemy.dialects.postgresql import UUID, JSONB
from sqlalchemy.sql import func
import uuid
from ..database import Base

class AuditLog(Base):
    __tablename__ = "audit_logs"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    event_type = Column(String(100), nullable=False, index=True)
    user_id = Column(String(100), index=True)
    resource_type = Column(String(50))
    resource_id = Column(String(255))
    action = Column(String(50), nullable=False)
    details = Column(JSONB)
    ip_address = Column(String(45))
    user_agent = Column(Text)
    created_at = Column(TIMESTAMP, server_default=func.now(), index=True)

class AccessLog(Base):
    __tablename__ = "access_logs"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    user_id = Column(String(100), index=True)
    endpoint = Column(String(255), nullable=False)
    method = Column(String(10))
    status_code = Column(Integer)
    response_time_ms = Column(Integer)
    created_at = Column(TIMESTAMP, server_default=func.now(), index=True)
