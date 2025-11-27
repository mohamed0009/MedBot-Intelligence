"""Configuration for SyntheseComparative"""

from pydantic_settings import BaseSettings
from typing import List

class Settings(BaseSettings):
    SERVICE_NAME: str = "synthese-comparative"
    LOG_LEVEL: str = "INFO"
    
    DATABASE_URL: str = "postgresql://docqa_admin:changeme@postgres:5432/synthese"
    
    # LLM Configuration
    OPENAI_API_KEY: str = ""
    OPENAI_MODEL: str = "gpt-4-turbo-preview"
    
    # Services URLs
    LLM_QA_SERVICE_URL: str = "http://llm-qa-module:8000"
    SEARCH_SERVICE_URL: str = "http://indexeur-semantique:8000"
    
    # CORS
    CORS_ORIGINS: List[str] = ["http://localhost:3000", "http://localhost:8000"]
    
    class Config:
        env_file = ".env"
        case_sensitive = True

settings = Settings()
