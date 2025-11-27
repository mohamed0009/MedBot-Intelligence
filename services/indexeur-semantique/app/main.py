"""
IndexeurSémantique Service - Main Application
Semantic Indexing and Search Service
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
import structlog

from .database import engine, Base
from .api import search
from .config import settings

# Configure logging
logger = structlog.get_logger()


@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan manager"""
    logger.info("Starting IndexeurSémantique service...")
    
    # Create database tables
    Base.metadata.create_all(bind=engine)
    
    logger.info("IndexeurSémantique service started successfully")
    yield
    logger.info("Shutting down IndexeurSémantique service...")


# Create FastAPI application
app = FastAPI(
    title="MedBot Intelligence: IndexeurSémantique Service",
    description="Semantic Document Indexing and Search Service using FAISS",
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
    search.router,
    prefix="/api/v1/search",
    tags=["search"]
)


@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "service": "IndexeurSémantique",
        "version": "1.0.0",
        "status": "healthy"
    }


@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "service": "indexeur-semantique"
    }
