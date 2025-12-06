package com.basiccode.generator.generator.python.django.generators;

import java.util.*;

/**
 * DjangoAdvancedFeaturesGenerator - Phase 2
 * Advanced API features: CORS, Throttling, Rate Limiting, Error Handling
 *
 * Generates:
 * - CORS configuration
 * - Throttling classes
 * - Rate limiting strategies
 * - Custom exception handling
 * - API error responses
 * - Request/Response logging
 * - API versioning
 *
 * @version 1.0
 */
public class DjangoAdvancedFeaturesGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoAdvancedFeaturesGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    /**
     * Generates CORS configuration for settings.py
     */
    public String generateCORSConfiguration() {
        StringBuilder code = new StringBuilder();

        code.append("# ===== CORS Configuration =====\n");
        code.append("import os\n\n");

        code.append("# Django CORS settings\n");
        code.append("CORS_ALLOWED_ORIGINS = [\n");
        code.append("    'http://localhost:3000',\n");
        code.append("    'http://localhost:8080',\n");
        code.append("    'http://127.0.0.1:3000',\n");
        code.append("    os.getenv('FRONTEND_URL', 'http://localhost:3000'),\n");
        code.append("]\n\n");

        code.append("CORS_ALLOW_CREDENTIALS = True\n");
        code.append("CORS_ALLOW_HEADERS = [\n");
        code.append("    'accept',\n");
        code.append("    'accept-encoding',\n");
        code.append("    'authorization',\n");
        code.append("    'content-type',\n");
        code.append("    'dnt',\n");
        code.append("    'origin',\n");
        code.append("    'user-agent',\n");
        code.append("    'x-csrftoken',\n");
        code.append("    'x-requested-with',\n");
        code.append("    'x-api-key',\n");
        code.append("]\n\n");

        code.append("CORS_ALLOW_METHODS = [\n");
        code.append("    'DELETE',\n");
        code.append("    'GET',\n");
        code.append("    'OPTIONS',\n");
        code.append("    'PATCH',\n");
        code.append("    'POST',\n");
        code.append("    'PUT',\n");
        code.append("]\n\n");

        code.append("CORS_MAX_AGE = 600  # 10 minutes preflight cache\n");
        code.append("CORS_EXPOSE_HEADERS = [\n");
        code.append("    'content-type',\n");
        code.append("    'x-total-count',\n");
        code.append("    'x-request-id',\n");
        code.append("]\n");

        return code.toString();
    }

    /**
     * Generates throttling classes
     */
    public String generateThrottlingClasses() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework.throttling import UserRateThrottle, AnonRateThrottle, SimpleRateThrottle\n");
        code.append("from django.core.cache import cache\n\n");

        // User throttle
        code.append("class UserThrottle(UserRateThrottle):\n");
        code.append("    \"\"\"\n");
        code.append("    Throttle for authenticated users\n");
        code.append("    \"\"\"\n");
        code.append("    scope = 'user'\n");
        code.append("    THROTTLE_RATES = {'user': '1000/hour'}\n\n");

        // Anonymous throttle
        code.append("class AnonThrottle(AnonRateThrottle):\n");
        code.append("    \"\"\"\n");
        code.append("    Throttle for anonymous users\n");
        code.append("    \"\"\"\n");
        code.append("    scope = 'anon'\n");
        code.append("    THROTTLE_RATES = {'anon': '100/hour'}\n\n");

        // Strict throttle for sensitive operations
        code.append("class StrictThrottle(SimpleRateThrottle):\n");
        code.append("    \"\"\"\n");
        code.append("    Stricter throttle for sensitive operations (login, password reset)\n");
        code.append("    \"\"\"\n");
        code.append("    scope = 'strict'\n");
        code.append("    THROTTLE_RATES = {'strict': '5/minute'}\n\n");

        code.append("    def get_cache_key(self):\n");
        code.append("        if self.request.user.is_authenticated:\n");
        code.append("            ident = self.request.user.pk\n");
        code.append("        else:\n");
        code.append("            ident = self.get_ident(self.request)\n");
        code.append("        return self.cache_format % {'scope': self.scope, 'ident': ident}\n\n");

        // Burst throttle
        code.append("class BurstThrottle(SimpleRateThrottle):\n");
        code.append("    \"\"\"\n");
        code.append("    Allow bursts but enforce strict cumulative limit\n");
        code.append("    \"\"\"\n");
        code.append("    scope = 'burst'\n");
        code.append("    THROTTLE_RATES = {\n");
        code.append("        'burst': '20/minute',  # 20 per minute (burst)\n");
        code.append("    }\n\n");

        code.append("    def throttle_success(self):\n");
        code.append("        # Track cumulative usage\n");
        code.append("        ident = self.get_ident(self.request)\n");
        code.append("        cumulative_key = f'cumulative_{ident}'\n");
        code.append("        cumulative = cache.get(cumulative_key, 0)\n");
        code.append("        cache.set(cumulative_key, cumulative + 1, 3600)  # 1 hour\n");
        code.append("        if cumulative > 500:  # 500 per hour limit\n");
        code.append("            return False\n");
        code.append("        return super().throttle_success()\n");

        return code.toString();
    }

    /**
     * Generates custom exception handlers
     */
    public String generateExceptionHandlers() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework.views import exception_handler\n");
        code.append("from rest_framework.response import Response\n");
        code.append("from rest_framework import status\n");
        code.append("import logging\n");
        code.append("import uuid\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        code.append("def custom_exception_handler(exc, context):\n");
        code.append("    \"\"\"\n");
        code.append("    Custom DRF exception handler with:\n");
        code.append("    - Request ID tracking\n");
        code.append("    - Standardized error format\n");
        code.append("    - Error logging\n");
        code.append("    - User-friendly messages\n");
        code.append("    \"\"\"\n");
        code.append("    response = exception_handler(exc, context)\n");
        code.append("    request_id = str(uuid.uuid4())\n\n");

        code.append("    if response is None:\n");
        code.append("        logger.exception('Unhandled exception', extra={'request_id': request_id})\n");
        code.append("        return Response(\n");
        code.append("            {\n");
        code.append("                'error': 'An unexpected error occurred',\n");
        code.append("                'request_id': request_id,\n");
        code.append("            },\n");
        code.append("            status=status.HTTP_500_INTERNAL_SERVER_ERROR,\n");
        code.append("        )\n\n");

        code.append("    # Add request ID to response\n");
        code.append("    response.data['request_id'] = request_id\n");
        code.append("    response['X-Request-ID'] = request_id\n\n");

        code.append("    # Log the error\n");
        code.append("    if response.status_code >= 400:\n");
        code.append("        logger.warning(\n");
        code.append("            f'API Error: {response.status_code}',\n");
        code.append("            extra={\n");
        code.append("                'request_id': request_id,\n");
        code.append("                'error': response.data,\n");
        code.append("                'method': context.get('request').method,\n");
        code.append("                'path': context.get('request').path,\n");
        code.append("            }\n");
        code.append("        )\n\n");

        code.append("    return response\n");

        return code.toString();
    }

    /**
     * Generates standardized error response classes
     */
    public String generateErrorResponseClasses() {
        StringBuilder code = new StringBuilder();

        code.append("from rest_framework import serializers\n");
        code.append("from rest_framework import status\n\n");

        code.append("class ErrorDetailSerializer(serializers.Serializer):\n");
        code.append("    field = serializers.CharField(required=False)\n");
        code.append("    code = serializers.CharField()\n");
        code.append("    message = serializers.CharField()\n\n");

        code.append("class ErrorResponseSerializer(serializers.Serializer):\n");
        code.append("    \"\"\"\n");
        code.append("    Standardized error response format\n");
        code.append("    \"\"\"\n");
        code.append("    status_code = serializers.IntegerField()\n");
        code.append("    error = serializers.CharField()\n");
        code.append("    message = serializers.CharField(required=False)\n");
        code.append("    details = serializers.ListField(child=ErrorDetailSerializer(), required=False)\n");
        code.append("    request_id = serializers.CharField(required=False)\n");
        code.append("    timestamp = serializers.DateTimeField(required=False)\n\n");

        code.append("class SuccessResponseSerializer(serializers.Serializer):\n");
        code.append("    \"\"\"\n");
        code.append("    Standardized success response format\n");
        code.append("    \"\"\"\n");
        code.append("    status_code = serializers.IntegerField()\n");
        code.append("    message = serializers.CharField(required=False)\n");
        code.append("    data = serializers.JSONField(required=False)\n");
        code.append("    request_id = serializers.CharField(required=False)\n");
        code.append("    timestamp = serializers.DateTimeField(required=False)\n");

        return code.toString();
    }

    /**
     * Generates API versioning configuration
     */
    public String generateAPIVersioning() {
        StringBuilder code = new StringBuilder();

        code.append("# ===== API Versioning Configuration =====\n");
        code.append("REST_FRAMEWORK = {\n");
        code.append("    'DEFAULT_VERSIONING_CLASS': 'rest_framework.versioning.NamespaceVersioning',\n");
        code.append("    'DEFAULT_VERSION': 'v1',\n");
        code.append("    'ALLOWED_VERSIONS': ['v1', 'v2'],\n");
        code.append("}\n\n");

        code.append("# URL patterns example:\n");
        code.append("# path('api/v1/', include('app.urls_v1')),\n");
        code.append("# path('api/v2/', include('app.urls_v2')),\n");

        return code.toString();
    }

    /**
     * Generates request/response logging middleware
     */
    public String generateLoggingMiddleware() {
        StringBuilder code = new StringBuilder();

        code.append("import logging\n");
        code.append("import time\n");
        code.append("import json\n");
        code.append("from django.http import QueryDict\n");
        code.append("from django.utils.deprecation import MiddlewareMixin\n\n");

        code.append("logger = logging.getLogger(__name__)\n\n");

        code.append("class RequestResponseLoggingMiddleware(MiddlewareMixin):\n");
        code.append("    \"\"\"\n");
        code.append("    Middleware to log all API requests and responses\n");
        code.append("    \"\"\"\n\n");

        code.append("    def process_request(self, request):\n");
        code.append("        request.start_time = time.time()\n");
        code.append("        request.request_id = request.META.get('HTTP_X_REQUEST_ID', str(uuid.uuid4()))\n");
        code.append("        \n");
        code.append("        # Log request\n");
        code.append("        logger.info(\n");
        code.append("            f'{request.method} {request.path}',\n");
        code.append("            extra={\n");
        code.append("                'request_id': request.request_id,\n");
        code.append("                'method': request.method,\n");
        code.append("                'path': request.path,\n");
        code.append("                'remote_addr': request.META.get('REMOTE_ADDR'),\n");
        code.append("                'user_agent': request.META.get('HTTP_USER_AGENT'),\n");
        code.append("            }\n");
        code.append("        )\n\n");

        code.append("    def process_response(self, request, response):\n");
        code.append("        elapsed_time = time.time() - request.start_time\n");
        code.append("        \n");
        code.append("        # Log response\n");
        code.append("        logger.info(\n");
        code.append("            f'{request.method} {request.path} {response.status_code}',\n");
        code.append("            extra={\n");
        code.append("                'request_id': getattr(request, 'request_id', 'unknown'),\n");
        code.append("                'status_code': response.status_code,\n");
        code.append("                'elapsed_time': elapsed_time,\n");
        code.append("            }\n");
        code.append("        )\n");
        code.append("        response['X-Process-Time'] = elapsed_time\n");
        code.append("        return response\n");

        return code.toString();
    }

    /**
     * Generates complete settings configuration
     */
    public String generateCompleteSettings() {
        StringBuilder code = new StringBuilder();

        code.append("# ===== REST Framework Settings =====\n");
        code.append("REST_FRAMEWORK = {\n");
        code.append("    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',\n");
        code.append("    'PAGE_SIZE': 20,\n\n");

        code.append("    'DEFAULT_AUTHENTICATION_CLASSES': [\n");
        code.append("        'rest_framework_simplejwt.authentication.JWTAuthentication',\n");
        code.append("    ],\n\n");

        code.append("    'DEFAULT_PERMISSION_CLASSES': [\n");
        code.append("        'rest_framework.permissions.IsAuthenticated',\n");
        code.append("    ],\n\n");

        code.append("    'DEFAULT_THROTTLE_CLASSES': [\n");
        code.append("        'app.throttles.UserThrottle',\n");
        code.append("        'app.throttles.AnonThrottle',\n");
        code.append("    ],\n\n");

        code.append("    'DEFAULT_FILTER_BACKENDS': [\n");
        code.append("        'django_filters.rest_framework.DjangoFilterBackend',\n");
        code.append("        'rest_framework.filters.SearchFilter',\n");
        code.append("        'rest_framework.filters.OrderingFilter',\n");
        code.append("    ],\n\n");

        code.append("    'DEFAULT_RENDERER_CLASSES': [\n");
        code.append("        'rest_framework.renderers.JSONRenderer',\n");
        code.append("        'rest_framework.renderers.BrowsableAPIRenderer',\n");
        code.append("    ],\n\n");

        code.append("    'EXCEPTION_HANDLER': 'app.exceptions.custom_exception_handler',\n\n");

        code.append("    'DEFAULT_VERSIONING_CLASS': 'rest_framework.versioning.NamespaceVersioning',\n\n");

        code.append("    'DEFAULT_METADATA_CLASS': 'rest_framework.metadata.SimpleMetadata',\n\n");

        code.append("    'SEARCH_PARAM': 'search',\n");
        code.append("    'ORDERING_PARAM': 'ordering',\n");
        code.append("    'TEST_REQUEST_RENDERER_CLASSES': [\n");
        code.append("        'rest_framework.renderers.JSONRenderer',\n");
        code.append("    ],\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generates production deployment checklist
     */
    public String generateProductionChecklist() {
        StringBuilder code = new StringBuilder();

        code.append("\"\"\"\n");
        code.append("Production Deployment Checklist for Advanced Features\n");
        for (int i = 0; i < 60; i++) {
            code.append("=");
        }
        code.append("\n\n");

        code.append("CORS SECURITY\n");
        code.append("☐ Review CORS_ALLOWED_ORIGINS\n");
        code.append("☐ Set specific domain URLs (not wildcards in production)\n");
        code.append("☐ Use environment variables for URLs\n");
        code.append("☐ Enable CORS_ALLOW_CREDENTIALS only when needed\n\n");

        code.append("AUTHENTICATION\n");
        code.append("☐ Use strong SECRET_KEY (generate new for production)\n");
        code.append("☐ Set JWT_SIGNING_KEY securely\n");
        code.append("☐ Configure appropriate token expiration times\n");
        code.append("☐ Enable token rotation\n");
        code.append("☐ Use HTTPS only for token transmission\n\n");

        code.append("RATE LIMITING\n");
        code.append("☐ Configure appropriate throttle rates per endpoint\n");
        code.append("☐ Adjust for expected user load\n");
        code.append("☐ Monitor throttle violations in logs\n");
        code.append("☐ Use stricter rates for sensitive endpoints\n\n");

        code.append("CACHING\n");
        code.append("☐ Configure Redis connection pooling\n");
        code.append("☐ Set appropriate cache timeout values\n");
        code.append("☐ Monitor cache hit rates (target: >80%)\n");
        code.append("☐ Implement cache invalidation strategy\n");
        code.append("☐ Monitor Redis memory usage\n\n");

        code.append("ERROR HANDLING\n");
        code.append("☐ Review custom exception handler\n");
        code.append("☐ Configure appropriate error responses\n");
        code.append("☐ Enable request ID tracking\n");
        code.append("☐ Set up error logging to external service\n");
        code.append("☐ Monitor 5xx errors in real-time\n\n");

        code.append("LOGGING\n");
        code.append("☐ Configure centralized logging\n");
        code.append("☐ Set appropriate log levels\n");
        code.append("☐ Log sensitive operations\n");
        code.append("☐ Archive old logs\n");
        code.append("☐ Monitor log file size\n\n");

        code.append("MONITORING\n");
        code.append("☐ Set up performance monitoring\n");
        code.append("☐ Monitor API response times\n");
        code.append("☐ Track 4xx/5xx error rates\n");
        code.append("☐ Monitor database connection pool\n");
        code.append("☐ Set up alerts for anomalies\n\n");

        code.append("DATABASE\n");
        code.append("☐ Use connection pooling\n");
        code.append("☐ Configure appropriate timeout values\n");
        code.append("☐ Set up backups\n");
        code.append("☐ Monitor query performance\n");
        code.append("☐ Ensure indexes are in place\n");
        code.append("\"\"\"\n");

        return code.toString();
    }
}
