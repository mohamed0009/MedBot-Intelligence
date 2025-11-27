"""Synthesis service - generates comparative reports"""

import httpx
from typing import List, Dict, Any
import structlog
from ..config import settings

logger = structlog.get_logger()

class SynthesisService:
    """Generate patient summaries and comparisons using LLM"""
    
    async def generate_patient_summary(self, patient_id: str) -> Dict[str, Any]:
        """Generate comprehensive patient summary"""
        
        # Query LLM service for patient information
        prompt = f"Generate a comprehensive medical summary for patient {patient_id}. Include: diagnoses, treatments, outcomes, timeline."
        
        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{settings.LLM_QA_SERVICE_URL}/api/v1/qa/ask",
                json={"question": prompt, "include_sources": True},
                timeout=60.0
            )
            result = response.json()
        
        return {
            "patient_id": patient_id,
            "summary": result.get("answer", ""),
            "sources": result.get("sources", [])
        }
    
    async def compare_patients(self, patient_ids: List[str]) -> Dict[str, Any]:
        """Compare multiple patients"""
        
        summaries = []
        for pid in patient_ids:
            summary = await self.generate_patient_summary(pid)
            summaries.append(summary)
        
        # Generate comparison using LLM
        comparison_prompt = f"Compare the following {len(patient_ids)} patients. Identify similarities, differences, and notable patterns.\n\n"
        for idx, s in enumerate(summaries, 1):
            comparison_prompt += f"Patient {idx}: {s['summary']}\n\n"
        
        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{settings.LLM_QA_SERVICE_URL}/api/v1/qa/ask",
                json={"question": comparison_prompt},
                timeout=120.0
            )
            result = response.json()
        
        return {
            "patient_ids": patient_ids,
            "comparison": result.get("answer", ""),
            "individual_summaries": summaries
        }

_service = None

def get_synthesis_service() -> SynthesisService:
    global _service
    if _service is None:
        _service = SynthesisService()
    return _service
