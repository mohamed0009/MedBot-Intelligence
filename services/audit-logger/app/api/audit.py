"""API endpoints"""
from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
from datetime import datetime, timedelta
from typing import Optional

from ..database import get_db
from ..models.audit import AuditLog, AccessLog
from ..schemas.audit import AuditLogRequest, AuditLogResponse
from ..services import get_audit_service

router = APIRouter()

@router.post("/log", response_model=AuditLogResponse)
async def create_audit_log(request: AuditLogRequest, db: Session = Depends(get_db)):
    """Create an audit log entry"""
    
    service = get_audit_service()
    log = service.log_event(
        db=db,
        event_type=request.event_type,
        user_id=request.user_id,
        action=request.action,
        resource_type=request.resource_type,
        resource_id=request.resource_id,
        details=request.details,
        ip=request.ip_address,
        user_agent=request.user_agent
    )
    
    return AuditLogResponse(
        id=str(log.id),
        event_type=log.event_type,
        user_id=log.user_id,
        action=log.action,
        created_at=log.created_at
    )

@router.get("/logs")
async def get_audit_logs(
    user_id: Optional[str] = None,
    event_type: Optional[str] = None,
    days: int = Query(7, ge=1, le=90),
    db: Session = Depends(get_db)
):
    """Retrieve audit logs with filters"""
    
    query = db.query(AuditLog)
    
    # Filter by date
    since = datetime.utcnow() - timedelta(days=days)
    query = query.filter(AuditLog.created_at >= since)
    
    if user_id:
        query = query.filter(AuditLog.user_id == user_id)
    if event_type:
        query = query.filter(AuditLog.event_type == event_type)
    
    logs = query.order_by(AuditLog.created_at.desc()).limit(100).all()
    
    return {
        "total": len(logs),
        "logs": [
            {
                "id": str(log.id),
                "event_type": log.event_type,
                "user_id": log.user_id,
                "action": log.action,
                "resource_type": log.resource_type,
                "resource_id": log.resource_id,
                "created_at": log.created_at.isoformat()
            }
            for log in logs
        ]
    }

@router.get("/compliance/report")
async def compliance_report(days: int = Query(30, ge=1, le=365), db: Session = Depends(get_db)):
    """Generate compliance report"""
    
    since = datetime.utcnow() - timedelta(days=days)
    
    total_events = db.query(AuditLog).filter(AuditLog.created_at >= since).count()
    total_access = db.query(AccessLog).filter(AccessLog.created_at >= since).count()
    
    return {
        "period_days": days,
        "total_audit_events": total_events,
        "total_api_accesses": total_access,
        "report_generated": datetime.utcnow().isoformat(),
        "compliance_status": "OK" if total_events > 0 else "WARNING"
    }
