"""SyntheseComparative - Main Application"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager
import structlog

from .database import engine, Base
from .api import synthesis
from .config import settings

logger = structlog.get_logger()

@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info("Starting SyntheseComparative service...")
    Base.metadata.create_all(bind=engine)
    logger.info("SyntheseComparative started")
    yield
    logger.info("Shutting down SyntheseComparative...")

app = FastAPI(
    title="MedBot Intelligence: SyntheseComparative",
    description="Patient Summary and Comparative Analysis Service",
    version="1.0.0",
    lifespan=lifespan,
    docs_url="/docs",
    redoc_url="/redoc",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(synthesis.router, prefix="/api/v1/synthesis", tags=["synthesis"])

@app.get("/")
async def root():
    return {"service": "SyntheseComparative", "version": "1.0.0", "status": "healthy"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "synthese-comparative"}
