"""
LLM QA Module - Main Application
AI-Powered Question Answering with RAG
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
import structlog

from .database import engine, Base
from .api import qa
from .config import settings

# Configure logging
logger = structlog.get_logger()


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan manager"""
    logger.info("Starting LLM QA Module...")
    
    # Create database tables
    Base.metadata.create_all(bind=engine)
    
    # Initialize LLM (warm up)
    from .llm import get_llm
    try:
        get_llm()
        logger.info("LLM initialized successfully")
    except Exception as e:
        logger.warning("LLM initialization failed", error=str(e))
    
    logger.info("LLM QA Module started successfully")
    yield
    logger.info("Shutting down LLM QA Module...")


# Create FastAPI application
app = FastAPI(
    title="MedBot Intelligence: LLM QA Module",
    description="AI-Powered Medical Question Answering with RAG and Citations",
    version="1.0.0",
    lifespan=lifespan,
    docs_url="/docs",
    redoc_url="/redoc",
)

# CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(
    qa.router,
    prefix="/api/v1/qa",
    tags=["qa"]
)


@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "service": "LLM QA Module",
        "version": "1.0.0",
        "status": "healthy",
        "llm_provider": settings.LLM_PROVIDER,
        "model": settings.OPENAI_MODEL if settings.LLM_PROVIDER == "openai" else settings.LOCAL_MODEL_NAME
    }


@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "service": "llm-qa-module"
    }
