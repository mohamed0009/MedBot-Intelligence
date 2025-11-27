"""
DeID Service - Main Application
Medical Document De-identification Service
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
import structlog

from .database import engine, Base
from .api import anonymization
from .config import settings

# Configure logging
logger = structlog.get_logger()


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan manager"""
    logger.info("Starting DeID service...")
    
    # Create database tables
    Base.metadata.create_all(bind=engine)
    
    logger.info("DeID service started successfully")
    yield
    logger.info("Shutting down DeID service...")


# Create FastAPI application
app = FastAPI(
    title="MedBot Intelligence: DeID Service",
    description="Medical Document De-identification and Anonymization Service",
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
    anonymization.router,
    prefix="/api/v1/anonymization",
    tags=["anonymization"]
)


@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "service": "DeID",
        "version": "1.0.0",
        "status": "healthy"
    }


@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "service": "deid"
    }
