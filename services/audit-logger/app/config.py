"""Configuration"""
from pydantic_settings import BaseSettings
from typing import List

class Settings(BaseSettings):
    SERVICE_NAME: str = "audit-logger"
    DATABASE_URL: str = "postgresql://docqa_admin:changeme@postgres:5432/audit"
    CORS_ORIGINS: List[str] = ["http://localhost:3000"]
    
    class Config:
        env_file = ".env"

settings = Settings()
