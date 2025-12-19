package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;

/**
 * Générateur d'exceptions personnalisées Django
 */
public class DjangoExceptionGenerator {
    
    public String generateExceptions(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder exceptions = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Imports
        exceptions.append("from django.core.exceptions import ValidationError\n");
        exceptions.append("from rest_framework import status\n");
        exceptions.append("from rest_framework.views import exception_handler\n");
        exceptions.append("from rest_framework.response import Response\n");
        exceptions.append("import logging\n\n");
        
        exceptions.append("logger = logging.getLogger(__name__)\n\n");
        
        // Base service exception
        exceptions.append("class ").append(className).append("ServiceError(Exception):\n");
        exceptions.append("    \"\"\"Base exception for ").append(className).append(" service operations\"\"\"\n");
        exceptions.append("    \n");
        exceptions.append("    def __init__(self, message: str, code: str = None):\n");
        exceptions.append("        self.message = message\n");
        exceptions.append("        self.code = code or 'service_error'\n");
        exceptions.append("        super().__init__(self.message)\n\n");
        
        // Specific exceptions
        exceptions.append("class ").append(className).append("NotFoundError(").append(className).append("ServiceError):\n");
        exceptions.append("    \"\"\"Exception raised when ").append(className.toLowerCase()).append(" is not found\"\"\"\n");
        exceptions.append("    \n");
        exceptions.append("    def __init__(self, ").append(className.toLowerCase()).append("_id: str):\n");
        exceptions.append("        message = f'").append(className).append(" with ID {").append(className.toLowerCase()).append("_id} not found'\n");
        exceptions.append("        super().__init__(message, 'not_found')\n\n");
        
        exceptions.append("class ").append(className).append("ValidationError(").append(className).append("ServiceError):\n");
        exceptions.append("    \"\"\"Exception raised for validation errors\"\"\"\n");
        exceptions.append("    \n");
        exceptions.append("    def __init__(self, field: str, message: str):\n");
        exceptions.append("        full_message = f'Validation error in {field}: {message}'\n");
        exceptions.append("        super().__init__(full_message, 'validation_error')\n");
        exceptions.append("        self.field = field\n\n");
        
        exceptions.append("class ").append(className).append("BusinessRuleError(").append(className).append("ServiceError):\n");
        exceptions.append("    \"\"\"Exception raised for business rule violations\"\"\"\n");
        exceptions.append("    \n");
        exceptions.append("    def __init__(self, rule: str, message: str):\n");
        exceptions.append("        full_message = f'Business rule violation ({rule}): {message}'\n");
        exceptions.append("        super().__init__(full_message, 'business_rule_error')\n");
        exceptions.append("        self.rule = rule\n\n");
        
        // Custom exception handler
        exceptions.append("def custom_exception_handler(exc, context):\n");
        exceptions.append("    \"\"\"Custom exception handler for API responses\"\"\"\n");
        exceptions.append("    \n");
        exceptions.append("    # Call REST framework's default exception handler first\n");
        exceptions.append("    response = exception_handler(exc, context)\n");
        exceptions.append("    \n");
        exceptions.append("    # Handle custom service exceptions\n");
        exceptions.append("    if isinstance(exc, ").append(className).append("ServiceError):\n");
        exceptions.append("        logger.error(f'Service error: {exc.message}', exc_info=True)\n");
        exceptions.append("        \n");
        exceptions.append("        custom_response_data = {\n");
        exceptions.append("            'success': False,\n");
        exceptions.append("            'error': {\n");
        exceptions.append("                'code': exc.code,\n");
        exceptions.append("                'message': exc.message,\n");
        exceptions.append("                'type': exc.__class__.__name__\n");
        exceptions.append("            }\n");
        exceptions.append("        }\n");
        exceptions.append("        \n");
        exceptions.append("        if isinstance(exc, ").append(className).append("NotFoundError):\n");
        exceptions.append("            return Response(custom_response_data, status=status.HTTP_404_NOT_FOUND)\n");
        exceptions.append("        elif isinstance(exc, ").append(className).append("ValidationError):\n");
        exceptions.append("            custom_response_data['error']['field'] = exc.field\n");
        exceptions.append("            return Response(custom_response_data, status=status.HTTP_400_BAD_REQUEST)\n");
        exceptions.append("        elif isinstance(exc, ").append(className).append("BusinessRuleError):\n");
        exceptions.append("            custom_response_data['error']['rule'] = exc.rule\n");
        exceptions.append("            return Response(custom_response_data, status=status.HTTP_422_UNPROCESSABLE_ENTITY)\n");
        exceptions.append("        else:\n");
        exceptions.append("            return Response(custom_response_data, status=status.HTTP_500_INTERNAL_SERVER_ERROR)\n");
        exceptions.append("    \n");
        exceptions.append("    # For other exceptions, enhance the response\n");
        exceptions.append("    if response is not None:\n");
        exceptions.append("        custom_response_data = {\n");
        exceptions.append("            'success': False,\n");
        exceptions.append("            'error': {\n");
        exceptions.append("                'message': 'An error occurred',\n");
        exceptions.append("                'details': response.data\n");
        exceptions.append("            }\n");
        exceptions.append("        }\n");
        exceptions.append("        response.data = custom_response_data\n");
        exceptions.append("    \n");
        exceptions.append("    return response\n");
        
        return exceptions.toString();
    }
    
    public String getFileExtension() {
        return ".py";
    }
    
    public String getExceptionDirectory() {
        return "exceptions";
    }
}