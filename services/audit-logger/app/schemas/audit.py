"""Schemas"""
from pydantic import BaseModel
from typing import Dict, Any, Optional
from datetime import datetime

class AuditLogRequest(BaseModel):
    event_type: str
    user_id: str
    action: str
    resource_type: Optional[str] = None
    resource_id: Optional[str] = None
    details: Optional[Dict[str, Any]] = None
    ip_address: Optional[str] = None
    user_agent: Optional[str] = None

class AuditLogResponse(BaseModel):
    id: str
    event_type: str
    user_id: str
    action: str
    created_at: datetime
