"""Audit service"""
import structlog
from sqlalchemy.orm import Session
from ..models.audit import AuditLog, AccessLog
from typing import Dict, Any

logger = structlog.get_logger()

class AuditService:
    """Log audit events and access"""
    
    def log_event(self, db: Session, event_type: str, user_id: str, action: str, 
                  resource_type: str = None, resource_id: str = None, 
                  details: Dict[str, Any] = None, ip: str = None, user_agent: str = None):
        """Log an audit event"""
        
        log = AuditLog(
            event_type=event_type,
            user_id=user_id,
            resource_type=resource_type,
            resource_id=resource_id,
            action=action,
            details=details or {},
            ip_address=ip,
            user_agent=user_agent
        )
        db.add(log)
        db.commit()
        logger.info("Audit logged", event_type=event_type, user=user_id, action=action)
        return log
    
    def log_access(self, db: Session, user_id: str, endpoint: str, method: str,
                   status_code: int, response_time_ms: int):
        """Log API access"""
        
        log = AccessLog(
            user_id=user_id,
            endpoint=endpoint,
            method=method,
            status_code=status_code,
            response_time_ms=response_time_ms
        )
        db.add(log)
        db.commit()
        return log

_service = None

def get_audit_service() -> AuditService:
    global _service
    if _service is None:
        _service = AuditService()
    return _service
