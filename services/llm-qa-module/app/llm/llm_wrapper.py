"""
LLM wrapper supporting multiple providers (OpenAI, HuggingFace, Local)
"""

from typing import Optional, Dict, Any, List
import structlog
from langchain.llms import OpenAI
from langchain.chat_models import ChatOpenAI
from langchain.callbacks import get_openai_callback

from ..config import settings

logger = structlog.get_logger()


class LLMWrapper:
    """Unified LLM interface supporting multiple providers"""
    
    def __init__(self):
        self.provider = settings.LLM_PROVIDER
        self.model = None
        self._initialize()
    
    def _initialize(self):
        """Initialize LLM based on provider"""
        try:
            if self.provider == "openai":
                self._initialize_openai()
            elif self.provider == "huggingface" or self.provider == "local":
                self._initialize_local()
            else:
                raise ValueError(f"Unsupported LLM provider: {self.provider}")
                
            logger.info("LLM initialized", provider=self.provider)
            
        except Exception as e:
            logger.error("LLM initialization failed", error=str(e))
            raise
    
    def _initialize_openai(self):
        """Initialize OpenAI LLM"""
        if not settings.OPENAI_API_KEY:
            raise ValueError("OPENAI_API_KEY not set")
        
        logger.info("Initializing OpenAI", model=settings.OPENAI_MODEL)
        
        self.model = ChatOpenAI(
            model_name=settings.OPENAI_MODEL,
            temperature=settings.OPENAI_TEMPERATURE,
            max_tokens=settings.OPENAI_MAX_TOKENS,
            openai_api_key=settings.OPENAI_API_KEY
        )
    
    def _initialize_local(self):
        """Initialize local/HuggingFace LLM"""
        from langchain.llms import HuggingFacePipeline
        from transformers import AutoTokenizer, AutoModelForCausalLM, pipeline
        
        logger.info("Initializing local LLM", model=settings.LOCAL_MODEL_NAME)
        
        # Load tokenizer and model
        tokenizer = AutoTokenizer.from_pretrained(settings.LOCAL_MODEL_NAME)
        model = AutoModelForCausalLM.from_pretrained(
            settings.LOCAL_MODEL_NAME,
            device_map=settings.LOCAL_MODEL_DEVICE,
            token=settings.HF_TOKEN
        )
        
        # Create pipeline
        pipe = pipeline(
            "text-generation",
            model=model,
            tokenizer=tokenizer,
            max_new_tokens=settings.OPENAI_MAX_TOKENS,
            temperature=settings.OPENAI_TEMPERATURE,
        )
        
        self.model = HuggingFacePipeline(pipeline=pipe)
    
    def generate(
        self,
        prompt: str,
        system_prompt: Optional[str] = None
    ) -> Dict[str, Any]:
        """
        Generate response from LLM
        
        Args:
            prompt: User prompt
            system_prompt: Optional system prompt
            
        Returns:
            Dict with response and metadata
        """
        try:
            if self.provider == "openai":
                return self._generate_openai(prompt, system_prompt)
            else:
                return self._generate_local(prompt, system_prompt)
                
        except Exception as e:
            logger.error("Generation failed", error=str(e))
            raise
    
    def _generate_openai(
        self,
        prompt: str,
        system_prompt: Optional[str] = None
    ) -> Dict[str, Any]:
        """Generate using OpenAI"""
        from langchain.schema import HumanMessage, SystemMessage
        
        messages = []
        
        if system_prompt:
            messages.append(SystemMessage(content=system_prompt))
        
        messages.append(HumanMessage(content=prompt))
        
        # Track token usage
        with get_openai_callback() as cb:
            response = self.model(messages)
            
            return {
                "response": response.content,
                "tokens_used": cb.total_tokens,
                "prompt_tokens": cb.prompt_tokens,
                "completion_tokens": cb.completion_tokens,
                "model": settings.OPENAI_MODEL
            }
    
    def _generate_local(
        self,
        prompt: str,
        system_prompt: Optional[str] = None
    ) -> Dict[str, Any]:
        """Generate using local LLM"""
        
        # Combine prompts
        full_prompt = prompt
        if system_prompt:
            full_prompt = f"{system_prompt}\n\n{prompt}"
        
        response = self.model(full_prompt)
        
        return {
            "response": response,
            "tokens_used": 0,  # Would need tokenizer to count
            "model": settings.LOCAL_MODEL_NAME
        }
    
    def generate_stream(self, prompt: str, system_prompt: Optional[str] = None):
        """
        Generate streaming response (for real-time UI)
        
        Args:
            prompt: User prompt
            system_prompt: Optional system prompt
            
        Yields:
            Chunks of generated text
        """
        if self.provider != "openai":
            # Streaming not supported for local models in this implementation
            result = self.generate(prompt, system_prompt)
            yield result["response"]
            return
        
        from langchain.schema import HumanMessage, SystemMessage
        from langchain.callbacks.streaming_stdout import StreamingStdOutCallbackHandler
        
        messages = []
        if system_prompt:
            messages.append(SystemMessage(content=system_prompt))
        messages.append(HumanMessage(content=prompt))
        
        # Stream tokens
        for chunk in self.model.stream(messages):
            yield chunk.content


# Global LLM wrapper instance
_llm_wrapper = None


def get_llm() -> LLMWrapper:
    """Get LLM wrapper singleton"""
    global _llm_wrapper
    if _llm_wrapper is None:
        _llm_wrapper = LLMWrapper()
    return _llm_wrapper
