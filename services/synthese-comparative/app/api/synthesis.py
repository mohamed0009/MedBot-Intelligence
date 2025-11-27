"""API endpoints for synthesis"""

from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
import structlog

from ..database import get_db
from ..models.synthesis import SynthesisReport
from ..schemas.synthesis import SummaryRequest, ComparisonRequest, SynthesisResponse
from ..services import get_synthesis_service
from ..config import settings

logger = structlog.get_logger()
router = APIRouter()

@router.post("/summary", response_model=SynthesisResponse)
async def generate_summary(request: SummaryRequest, db: Session = Depends(get_db)):
    """Generate patient summary"""
    
    service = get_synthesis_service()
    result = await service.generate_patient_summary(request.patient_id)
    
    report = SynthesisReport(
        report_type="patient_summary",
        patient_ids=[request.patient_id],
        synthesis_text=result["summary"],
        key_findings={"sources": result.get("sources", [])},
        llm_model=settings.OPENAI_MODEL
    )
    db.add(report)
    db.commit()
    db.refresh(report)
    
    logger.info("Summary generated", report_id=str(report.id))
    
    return SynthesisResponse(
        report_id=report.id,
        report_type=report.report_type,
        synthesis_text=report.synthesis_text,
        key_findings=report.key_findings
    )

@router.post("/compare", response_model=SynthesisResponse)
async def compare_patients(request: ComparisonRequest, db: Session = Depends(get_db)):
    """Compare multiple patients"""
    
    service = get_synthesis_service()
    result = await service.compare_patients(request.patient_ids)
    
    report = SynthesisReport(
        report_type="comparison",
        patient_ids=request.patient_ids,
        synthesis_text=result["comparison"],
        key_findings={"summaries": result.get("individual_summaries", [])},
        llm_model=settings.OPENAI_MODEL
    )
    db.add(report)
    db.commit()
    db.refresh(report)
    
    logger.info("Comparison generated", report_id=str(report.id), patients=len(request.patient_ids))
    
    return SynthesisResponse(
        report_id=report.id,
        report_type=report.report_type,
        synthesis_text=report.synthesis_text,
        key_findings=report.key_findings
    )
