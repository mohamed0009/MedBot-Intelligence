"""
API endpoints for anonymization operations
"""

from fastapi import APIRouter, HTTPException, Depends, status
from sqlalchemy.orm import Session
import time
import structlog

from ..database import get_db
from ..models.anonymization import AnonymizationLog
from ..schemas.anonymization import (
    AnonymizationRequest,
    AnonymizationResponse,
    AnalyzeRequest,
    AnalyzeResponse,
    PIIEntity
)
from ..analyzers import get_analyzer
from ..services import get_anonymizer
from ..config import settings

logger = structlog.get_logger()
router = APIRouter()


@router.post("/anonymize", response_model=AnonymizationResponse)
async def anonymize_text(
    request: AnonymizationRequest,
    db: Session = Depends(get_db)
):
    """
    Anonymize text by detecting and removing PII
    
    - **text**: Text to anonymize
    - **strategy**: redact, replace, hash, or synthesize
    - **language**: Language code (en, fr)
    - **preserve_medical**: Keep medical entities intact
    """
    
    start_time = time.time()
    
    try:
        # Get analyzer and anonymizer
        analyzer = get_analyzer()
        anonymizer = get_anonymizer()
        
        # Detect PII entities
        pii_entities = analyzer.analyze(request.text, request.language)
        
        # Preserve medical entities if requested
        if request.preserve_medical:
            medical_entities = analyzer.detect_medical_entities(request.text)
            # Filter out medical entities from PII list
            pii_entities = [
                e for e in pii_entities
                if not any(
                    m["start"] <= e["start"] < m["end"] or
                    m["start"] < e["end"] <= m["end"]
                    for m in medical_entities
                )
            ]
        
        # Anonymize text
        anonymized_text, metadata = anonymizer.anonymize(
            text=request.text,
            entities=pii_entities,
            strategy=request.strategy
        )
        
        # Calculate processing time
        processing_time_ms = int((time.time() - start_time) * 1000)
        
        # Save to database
        log = AnonymizationLog(
            document_id=None,  # Will be set if called from RabbitMQ consumer
            original_content=request.text,
            anonymized_content=anonymized_text,
            pii_entities=pii_entities,
            anonymization_strategy=request.strategy,
            processing_time_ms=processing_time_ms,
            entities_count=len(pii_entities),
            confidence_avg=sum(e["confidence"] for e in pii_entities) / len(pii_entities) if pii_entities else 0
        )
        db.add(log)
        db.commit()
        
        logger.info(
            "Text anonymized successfully",
            strategy=request.strategy,
            entities_found=len(pii_entities),
            processing_time_ms=processing_time_ms
        )
        
        return AnonymizationResponse(
            original_text=request.text,
            anonymized_text=anonymized_text,
            pii_entities=[PIIEntity(**e) for e in pii_entities],
            strategy=request.strategy,
            entities_count=len(pii_entities),
            processing_time_ms=processing_time_ms,
            metadata=metadata
        )
        
    except Exception as e:
        logger.error("Anonymization failed", error=str(e))
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Anonymization failed: {str(e)}"
        )


@router.post("/analyze", response_model=AnalyzeResponse)
async def analyze_text(request: AnalyzeRequest):
    """
    Analyze text for PII without anonymizing
    
    - **text**: Text to analyze
    - **language**: Language code (en, fr)
    """
    
    start_time = time.time()
    
    try:
        # Get analyzer
        analyzer = get_analyzer()
        
        # Detect PII entities
        pii_entities = analyzer.analyze(request.text, request.language)
        
        # Calculate processing time
        processing_time_ms = int((time.time() - start_time) * 1000)
        
        logger.info(
            "Text analyzed successfully",
            entities_found=len(pii_entities),
            processing_time_ms=processing_time_ms
        )
        
        return AnalyzeResponse(
            text=request.text,
            pii_entities=[PIIEntity(**e) for e in pii_entities],
            entities_count=len(pii_entities),
            processing_time_ms=processing_time_ms
        )
        
    except Exception as e:
        logger.error("Analysis failed", error=str(e))
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Analysis failed: {str(e)}"
        )


@router.get("/strategies")
async def get_strategies():
    """Get available anonymization strategies"""
    return {
        "strategies": [
            {
                "name": "redact",
                "description": "Replace PII with [REDACTED] or [REDACTED_TYPE]",
                "example": "John Smith -> [REDACTED]"
            },
            {
                "name": "replace",
                "description": "Replace PII with generic placeholders",
                "example": "John Smith -> [NAME], john@email.com -> [EMAIL]"
            },
            {
                "name": "hash",
                "description": "Replace PII with cryptographic hashes",
                "example": "John Smith -> [HASH_a1b2c3d4e5f6]"
            },
            {
                "name": "synthesize",
                "description": "Replace PII with realistic fake data",
                "example": "John Smith -> Jane Doe, 555-1234 -> 555-9876"
            }
        ],
        "default_strategy": settings.DEID_STRATEGY
    }


@router.get("/entities")
async def get_entity_types():
    """Get supported PII entity types"""
    return {
        "pii_entities": settings.PII_ENTITIES,
        "medical_entities_preserved": settings.PRESERVE_ENTITIES,
        "confidence_threshold": settings.DEID_CONFIDENCE_THRESHOLD
    }
