"""
Pydantic schemas for API requests and responses
"""

from pydantic import BaseModel, Field
from typing import Optional, List, Dict, Any
from datetime import datetime
from uuid import UUID


class AnonymizationRequest(BaseModel):
    """Request to anonymize text"""
    text: str = Field(..., description="Text to anonymize")
    strategy: Optional[str] = Field("redact", description="Anonymization strategy: redact, replace, hash, synthesize")
    language: Optional[str] = Field("en", description="Language code")
    preserve_medical: Optional[bool] = Field(True, description="Preserve medical entities")


class PIIEntity(BaseModel):
    """Detected PII entity"""
    type: str
    text: str
    start: int
    end: int
    confidence: float


class AnonymizationResponse(BaseModel):
    """Response with anonymized text"""
    original_text: str
    anonymized_text: str
    pii_entities: List[PIIEntity]
    strategy: str
    entities_count: int
    processing_time_ms: int
    metadata: Dict[str, Any]


class AnalyzeRequest(BaseModel):
    """Request to analyze text for PII without anonymizing"""
    text: str = Field(..., description="Text to analyze")
    language: Optional[str] = Field("en", description="Language code")


class AnalyzeResponse(BaseModel):
    """Response with detected PII entities"""
    text: str
    pii_entities: List[PIIEntity]
    entities_count: int
    processing_time_ms: int
