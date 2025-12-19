package com.basiccode.generator.generator.django;

/**
 * Générateur de fichiers de configuration Django ultra-optimisés
 */
public class DjangoConfigGenerator {
    
    public String generateSettings(String packageName, String projectName) {
        StringBuilder settings = new StringBuilder();
        
        settings.append("import os\n");
        settings.append("from pathlib import Path\n");
        settings.append("from decouple import config\n");
        settings.append("import dj_database_url\n\n");
        
        settings.append("# Build paths inside the project\n");
        settings.append("BASE_DIR = Path(__file__).resolve().parent.parent\n\n");
        
        settings.append("# Security settings\n");
        settings.append("SECRET_KEY = config('SECRET_KEY', default='django-insecure-change-me')\n");
        settings.append("DEBUG = config('DEBUG', default=False, cast=bool)\n");
        settings.append("ALLOWED_HOSTS = config('ALLOWED_HOSTS', default='localhost,127.0.0.1', cast=lambda v: [s.strip() for s in v.split(',')])\n\n");
        
        settings.append("# Application definition\n");
        settings.append("DJANGO_APPS = [\n");
        settings.append("    'django.contrib.admin',\n");
        settings.append("    'django.contrib.auth',\n");
        settings.append("    'django.contrib.contenttypes',\n");
        settings.append("    'django.contrib.sessions',\n");
        settings.append("    'django.contrib.messages',\n");
        settings.append("    'django.contrib.staticfiles',\n");
        settings.append("]\n\n");
        
        settings.append("THIRD_PARTY_APPS = [\n");
        settings.append("    'rest_framework',\n");
        settings.append("    'rest_framework.authtoken',\n");
        settings.append("    'django_filters',\n");
        settings.append("    'corsheaders',\n");
        settings.append("    'drf_spectacular',\n");
        settings.append("    'django_extensions',\n");
        settings.append("    'django_redis',\n");
        settings.append("]\n\n");
        
        settings.append("LOCAL_APPS = [\n");
        settings.append("    '").append(packageName.replace(".", "_")).append("',\n");
        settings.append("]\n\n");
        
        settings.append("INSTALLED_APPS = DJANGO_APPS + THIRD_PARTY_APPS + LOCAL_APPS\n\n");
        
        settings.append("MIDDLEWARE = [\n");
        settings.append("    'corsheaders.middleware.CorsMiddleware',\n");
        settings.append("    'django.middleware.security.SecurityMiddleware',\n");
        settings.append("    'whitenoise.middleware.WhiteNoiseMiddleware',\n");
        settings.append("    'django.contrib.sessions.middleware.SessionMiddleware',\n");
        settings.append("    'django.middleware.common.CommonMiddleware',\n");
        settings.append("    'django.middleware.csrf.CsrfViewMiddleware',\n");
        settings.append("    'django.contrib.auth.middleware.AuthenticationMiddleware',\n");
        settings.append("    'django.contrib.messages.middleware.MessageMiddleware',\n");
        settings.append("    'django.middleware.clickjacking.XFrameOptionsMiddleware',\n");
        settings.append("]\n\n");
        
        settings.append("ROOT_URLCONF = '").append(packageName.replace(".", "_")).append(".urls'\n\n");
        
        settings.append("TEMPLATES = [\n");
        settings.append("    {\n");
        settings.append("        'BACKEND': 'django.template.backends.django.DjangoTemplates',\n");
        settings.append("        'DIRS': [BASE_DIR / 'templates'],\n");
        settings.append("        'APP_DIRS': True,\n");
        settings.append("        'OPTIONS': {\n");
        settings.append("            'context_processors': [\n");
        settings.append("                'django.template.context_processors.debug',\n");
        settings.append("                'django.template.context_processors.request',\n");
        settings.append("                'django.contrib.auth.context_processors.auth',\n");
        settings.append("                'django.contrib.messages.context_processors.messages',\n");
        settings.append("            ],\n");
        settings.append("        },\n");
        settings.append("    },\n");
        settings.append("]\n\n");
        
        settings.append("WSGI_APPLICATION = '").append(packageName.replace(".", "_")).append(".wsgi.application'\n\n");
        
        // Database configuration
        settings.append("# Database\n");
        settings.append("DATABASES = {\n");
        settings.append("    'default': dj_database_url.config(\n");
        settings.append("        default=config('DATABASE_URL', default='sqlite:///db.sqlite3'),\n");
        settings.append("        conn_max_age=600,\n");
        settings.append("        conn_health_checks=True,\n");
        settings.append("    )\n");
        settings.append("}\n\n");
        
        // Cache configuration
        settings.append("# Cache configuration\n");
        settings.append("CACHES = {\n");
        settings.append("    'default': {\n");
        settings.append("        'BACKEND': 'django_redis.cache.RedisCache',\n");
        settings.append("        'LOCATION': config('REDIS_URL', default='redis://127.0.0.1:6379/1'),\n");
        settings.append("        'OPTIONS': {\n");
        settings.append("            'CLIENT_CLASS': 'django_redis.client.DefaultClient',\n");
        settings.append("        },\n");
        settings.append("        'KEY_PREFIX': '").append(projectName).append("',\n");
        settings.append("        'TIMEOUT': 300,\n");
        settings.append("    }\n");
        settings.append("}\n\n");
        
        // REST Framework configuration
        settings.append("# Django REST Framework\n");
        settings.append("REST_FRAMEWORK = {\n");
        settings.append("    'DEFAULT_AUTHENTICATION_CLASSES': [\n");
        settings.append("        'rest_framework.authentication.TokenAuthentication',\n");
        settings.append("        'rest_framework.authentication.SessionAuthentication',\n");
        settings.append("    ],\n");
        settings.append("    'DEFAULT_PERMISSION_CLASSES': [\n");
        settings.append("        'rest_framework.permissions.IsAuthenticated',\n");
        settings.append("    ],\n");
        settings.append("    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',\n");
        settings.append("    'PAGE_SIZE': 20,\n");
        settings.append("    'DEFAULT_FILTER_BACKENDS': [\n");
        settings.append("        'django_filters.rest_framework.DjangoFilterBackend',\n");
        settings.append("        'rest_framework.filters.SearchFilter',\n");
        settings.append("        'rest_framework.filters.OrderingFilter',\n");
        settings.append("    ],\n");
        settings.append("    'DEFAULT_SCHEMA_CLASS': 'drf_spectacular.openapi.AutoSchema',\n");
        settings.append("    'EXCEPTION_HANDLER': '").append(packageName.replace(".", "_")).append(".exceptions.custom_exception_handler',\n");
        settings.append("}\n\n");
        
        // Spectacular settings
        settings.append("# API Documentation\n");
        settings.append("SPECTACULAR_SETTINGS = {\n");
        settings.append("    'TITLE': '").append(projectName).append(" API',\n");
        settings.append("    'DESCRIPTION': 'Generated Django REST API',\n");
        settings.append("    'VERSION': '1.0.0',\n");
        settings.append("    'SERVE_INCLUDE_SCHEMA': False,\n");
        settings.append("}\n\n");
        
        // Logging configuration
        settings.append("# Logging\n");
        settings.append("LOGGING = {\n");
        settings.append("    'version': 1,\n");
        settings.append("    'disable_existing_loggers': False,\n");
        settings.append("    'formatters': {\n");
        settings.append("        'verbose': {\n");
        settings.append("            'format': '{levelname} {asctime} {module} {process:d} {thread:d} {message}',\n");
        settings.append("            'style': '{',\n");
        settings.append("        },\n");
        settings.append("    },\n");
        settings.append("    'handlers': {\n");
        settings.append("        'file': {\n");
        settings.append("            'level': 'INFO',\n");
        settings.append("            'class': 'logging.FileHandler',\n");
        settings.append("            'filename': 'django.log',\n");
        settings.append("            'formatter': 'verbose',\n");
        settings.append("        },\n");
        settings.append("        'console': {\n");
        settings.append("            'level': 'DEBUG',\n");
        settings.append("            'class': 'logging.StreamHandler',\n");
        settings.append("            'formatter': 'verbose',\n");
        settings.append("        },\n");
        settings.append("    },\n");
        settings.append("    'root': {\n");
        settings.append("        'handlers': ['console', 'file'],\n");
        settings.append("        'level': 'INFO',\n");
        settings.append("    },\n");
        settings.append("}\n\n");
        
        // Security settings
        settings.append("# Security\n");
        settings.append("SECURE_BROWSER_XSS_FILTER = True\n");
        settings.append("SECURE_CONTENT_TYPE_NOSNIFF = True\n");
        settings.append("X_FRAME_OPTIONS = 'DENY'\n");
        settings.append("SECURE_HSTS_SECONDS = 31536000 if not DEBUG else 0\n");
        settings.append("SECURE_HSTS_INCLUDE_SUBDOMAINS = True\n");
        settings.append("SECURE_HSTS_PRELOAD = True\n\n");
        
        // CORS settings
        settings.append("# CORS\n");
        settings.append("CORS_ALLOWED_ORIGINS = config('CORS_ALLOWED_ORIGINS', default='http://localhost:3000,http://127.0.0.1:3000', cast=lambda v: [s.strip() for s in v.split(',')])\n");
        settings.append("CORS_ALLOW_CREDENTIALS = True\n\n");
        
        // Internationalization
        settings.append("# Internationalization\n");
        settings.append("LANGUAGE_CODE = 'en-us'\n");
        settings.append("TIME_ZONE = 'UTC'\n");
        settings.append("USE_I18N = True\n");
        settings.append("USE_TZ = True\n\n");
        
        // Static files
        settings.append("# Static files\n");
        settings.append("STATIC_URL = '/static/'\n");
        settings.append("STATIC_ROOT = BASE_DIR / 'staticfiles'\n");
        settings.append("STATICFILES_STORAGE = 'whitenoise.storage.CompressedManifestStaticFilesStorage'\n\n");
        
        settings.append("# Media files\n");
        settings.append("MEDIA_URL = '/media/'\n");
        settings.append("MEDIA_ROOT = BASE_DIR / 'media'\n\n");
        
        settings.append("# Default primary key field type\n");
        settings.append("DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'\n");
        
        return settings.toString();
    }
    
    public String generateRequirements() {
        StringBuilder requirements = new StringBuilder();
        
        requirements.append("# Core Django\n");
        requirements.append("Django>=4.2.0,<5.0.0\n");
        requirements.append("djangorestframework>=3.14.0\n");
        requirements.append("django-filter>=23.0\n");
        requirements.append("django-cors-headers>=4.0.0\n\n");
        
        requirements.append("# Database\n");
        requirements.append("psycopg2-binary>=2.9.0\n");
        requirements.append("dj-database-url>=2.0.0\n\n");
        
        requirements.append("# Cache\n");
        requirements.append("django-redis>=5.2.0\n");
        requirements.append("redis>=4.5.0\n\n");
        
        requirements.append("# Configuration\n");
        requirements.append("python-decouple>=3.8\n\n");
        
        requirements.append("# API Documentation\n");
        requirements.append("drf-spectacular>=0.26.0\n\n");
        
        requirements.append("# Development tools\n");
        requirements.append("django-extensions>=3.2.0\n\n");
        
        requirements.append("# Static files\n");
        requirements.append("whitenoise>=6.4.0\n\n");
        
        requirements.append("# Production server\n");
        requirements.append("gunicorn>=20.1.0\n\n");
        
        requirements.append("# Monitoring\n");
        requirements.append("sentry-sdk>=1.25.0\n");
        
        return requirements.toString();
    }
    
    public String generateUrls(String packageName) {
        StringBuilder urls = new StringBuilder();
        
        urls.append("from django.contrib import admin\n");
        urls.append("from django.urls import path, include\n");
        urls.append("from drf_spectacular.views import SpectacularAPIView, SpectacularSwaggerView\n\n");
        
        urls.append("urlpatterns = [\n");
        urls.append("    # Admin\n");
        urls.append("    path('admin/', admin.site.urls),\n\n");
        
        urls.append("    # API Documentation\n");
        urls.append("    path('api/schema/', SpectacularAPIView.as_view(), name='schema'),\n");
        urls.append("    path('api/docs/', SpectacularSwaggerView.as_view(url_name='schema'), name='swagger-ui'),\n\n");
        
        urls.append("    # API endpoints\n");
        urls.append("    path('api/v1/', include('").append(packageName.replace(".", "_")).append(".urls')),\n");
        urls.append("]\n");
        
        return urls.toString();
    }
    
    public String getFileExtension() {
        return ".py";
    }
}