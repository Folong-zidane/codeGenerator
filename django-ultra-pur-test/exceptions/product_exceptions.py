from django.core.exceptions import ValidationError
from rest_framework import status
from rest_framework.views import exception_handler
from rest_framework.response import Response
import logging

logger = logging.getLogger(__name__)

class ProductServiceError(Exception):
    """Base exception for Product service operations"""
    
    def __init__(self, message: str, code: str = None):
        self.message = message
        self.code = code or 'service_error'
        super().__init__(self.message)

class ProductNotFoundError(ProductServiceError):
    """Exception raised when product is not found"""
    
    def __init__(self, product_id: str):
        message = f'Product with ID {product_id} not found'
        super().__init__(message, 'not_found')

class ProductValidationError(ProductServiceError):
    """Exception raised for validation errors"""
    
    def __init__(self, field: str, message: str):
        full_message = f'Validation error in {field}: {message}'
        super().__init__(full_message, 'validation_error')
        self.field = field

class ProductBusinessRuleError(ProductServiceError):
    """Exception raised for business rule violations"""
    
    def __init__(self, rule: str, message: str):
        full_message = f'Business rule violation ({rule}): {message}'
        super().__init__(full_message, 'business_rule_error')
        self.rule = rule

def custom_exception_handler(exc, context):
    """Custom exception handler for API responses"""
    
    # Call REST framework's default exception handler first
    response = exception_handler(exc, context)
    
    # Handle custom service exceptions
    if isinstance(exc, ProductServiceError):
        logger.error(f'Service error: {exc.message}', exc_info=True)
        
        custom_response_data = {
            'success': False,
            'error': {
                'code': exc.code,
                'message': exc.message,
                'type': exc.__class__.__name__
            }
        }
        
        if isinstance(exc, ProductNotFoundError):
            return Response(custom_response_data, status=status.HTTP_404_NOT_FOUND)
        elif isinstance(exc, ProductValidationError):
            custom_response_data['error']['field'] = exc.field
            return Response(custom_response_data, status=status.HTTP_400_BAD_REQUEST)
        elif isinstance(exc, ProductBusinessRuleError):
            custom_response_data['error']['rule'] = exc.rule
            return Response(custom_response_data, status=status.HTTP_422_UNPROCESSABLE_ENTITY)
        else:
            return Response(custom_response_data, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
    # For other exceptions, enhance the response
    if response is not None:
        custom_response_data = {
            'success': False,
            'error': {
                'message': 'An error occurred',
                'details': response.data
            }
        }
        response.data = custom_response_data
    
    return response
