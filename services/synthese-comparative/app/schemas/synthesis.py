"""API schemas"""

from pydantic import BaseModel
from typing import List, Dict, Any
from uuid import UUID

class SummaryRequest(BaseModel):
    patient_id: str

class ComparisonRequest(BaseModel):
    patient_ids: List[str]

class SynthesisResponse(BaseModel):
    report_id: UUID
    report_type: str
    synthesis_text: str
    key_findings: Dict[str, Any]
