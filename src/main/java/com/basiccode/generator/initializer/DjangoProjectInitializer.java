package com.basiccode.generator.initializer;

import com.basiccode.generator.model.ProjectConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * ✅ Django Project Initializer
 * 
 * Generates complete Django 5.0 project structure with modern architecture:
 * - Asynchronous views (async/await support)
 * - Django REST Framework (DRF)
 * - Celery for task queues
 * - Redis caching
 * - PostgreSQL database
 * - Docker support (PostgreSQL + Redis + Celery)
 * - Poetry for dependency management
 * - Pytest for testing
 * 
 * Architecture:
 * ├── config/                    (Django settings)
 * │   ├── settings/
 * │   │   ├── base.py           (shared settings)
 * │   │   ├── development.py    (dev config)
 * │   │   └── production.py     (prod config)
 * │   └── asgi.py               (async application)
 * ├── apps/
 * │   ├── core/                 (core models & permissions)
 * │   ├── api/                  (DRF serializers & viewsets)
 * │   └── tasks/                (Celery tasks)
 * ├── static/                   (static files)
 * ├── media/                    (uploaded files)
 * ├── tests/                    (pytest configuration)
 * ├── docker-compose.yml        (PostgreSQL + Redis)
 * ├── Dockerfile                (production image)
 * ├── pyproject.toml            (Poetry configuration)
 * └── pytest.ini                (test configuration)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DjangoProjectInitializer implements ProjectInitializer {
    
    @Override
    public void mergeGeneratedCode(Path existingProject, Path generatedCode) {
        // Implementation simple pour la Phase 1
        // Code would merge generatedCode into existingProject
        log.info("Merging generated Django code into {}", existingProject);
    }
    
    @Override
    public String getLatestVersion() {
        return DJANGO_VERSION;
    }
    
    @Override
    public String getLanguage() {
        return "Python";
    }
    
    private static final String DJANGO_VERSION = "5.0";
    private static final String DRF_VERSION = "3.14.0";
    private static final String PYTHON_VERSION = "3.12";
    private static final String CELERY_VERSION = "5.3.4";
    
    /**
     * Initialize a new Django project with modern architecture
     */
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            log.info("🐍 Initializing Django project: {}", projectName);
                
                Path projectRoot = Files.createDirectories(
                    Path.of("generated", projectName)
                );
                
                // Create directory structure
                createDirectoryStructure(projectRoot, packageName);
                
                // Generate configuration files
                generatePyprojectToml(projectRoot, projectName);
                generateDockerCompose(projectRoot, projectName);
                generateDockerfile(projectRoot);
                generateEnvFile(projectRoot);
                generateGitignore(projectRoot);
                generateREADME(projectRoot, projectName, packageName);
                
                // Generate Django configuration
                generateDjangoSettings(projectRoot, packageName);
                generateAsgiConfig(projectRoot, packageName);
                generateUrls(projectRoot, packageName);
                generateCeleryConfig(projectRoot, packageName);
                
                // Generate core app structure
                generateCoreApp(projectRoot, packageName);
                generateApiApp(projectRoot, packageName);
                generateTasksApp(projectRoot, packageName);
                
                // Generate test configuration
                generatePytestConfig(projectRoot);
                generateTestBase(projectRoot, packageName);
                
            // Generate utility modules
            generateMiddleware(projectRoot, packageName);
            generateExceptions(projectRoot, packageName);
            generateSerializers(projectRoot, packageName);
            
            log.info("✅ Django project initialized at: {}", projectRoot.toAbsolutePath());
            return projectRoot;
        } catch (IOException e) {
            log.error("Failed to initialize Django project", e);
            throw new RuntimeException("Django project initialization failed", e);
        }
    }
    
    private void createDirectoryStructure(Path root, String packageName) throws IOException {
        String[] dirs = {
            "config/settings",
            "apps/core/models",
            "apps/core/views",
            "apps/core/management/commands",
            "apps/core/migrations",
            "apps/api/viewsets",
            "apps/api/serializers",
            "apps/api/filters",
            "apps/api/permissions",
            "apps/tasks",
            "apps/tasks/jobs",
            "static/css",
            "static/js",
            "media",
            "tests/unit",
            "tests/integration",
            "tests/fixtures"
        };
        
        for (String dir : dirs) {
            Files.createDirectories(root.resolve(dir));
        }
    }
    
    private void generatePyprojectToml(Path root, String projectName) throws IOException {
        String content = """
            [tool.poetry]
            name = "%s"
            version = "0.1.0"
            description = "Modern Django API with DRF, Celery, and Redis"
            authors = ["Generated by basicCode"]
            readme = "README.md"
            packages = [{include = "config"}, {include = "apps"}]
            
            [tool.poetry.dependencies]
            python = "^3.12"
            django = "~5.0"
            djangorestframework = "~3.14.0"
            django-cors-headers = "^4.3.1"
            django-environ = "^0.21.0"
            django-extensions = "^3.2.3"
            psycopg = {extras = ["binary"], version = "^3.1.12"}
            psycopg-pool = "^3.1.8"
            asyncpg = "^0.29.0"
            aioredis = "^2.0.1"
            django-redis = "^5.4.0"
            celery = "~5.3.4"
            redis = "~5.0.1"
            gunicorn = "^21.2.0"
            whitenoise = "^6.6.0"
            python-dotenv = "^1.0.0"
            pydantic = "^2.5.0"
            pydantic-settings = "^2.1.0"
            pillow = "^10.1.0"
            requests = "^2.31.0"
            httpx = "^0.25.2"
            
            [tool.poetry.group.dev.dependencies]
            pytest = "^7.4.3"
            pytest-django = "^4.7.0"
            pytest-asyncio = "^0.21.1"
            pytest-cov = "^4.1.0"
            factory-boy = "^3.3.0"
            faker = "^21.0.0"
            black = "^23.12.0"
            isort = "^5.13.2"
            flake8 = "^6.1.0"
            pylint = "^3.0.3"
            mypy = "^1.7.1"
            django-stubs = "^4.2.7"
            
            [build-system]
            requires = ["poetry-core"]
            build-backend = "poetry.core.masonry.api"
            
            [tool.pytest.ini_options]
            DJANGO_SETTINGS_MODULE = "config.settings.test"
            python_files = ["tests.py", "test_*.py", "*_tests.py"]
            addopts = "--strict-markers --cov=apps --cov-report=html --cov-report=term-missing"
            markers = [
                "slow: marks tests as slow",
                "integration: marks tests as integration",
                "unit: marks tests as unit"
            ]
            
            [tool.black]
            line-length = 100
            target-version = ['py312']
            include = '\\.pyi?$'
            exclude = '''
            /(
              | \\.git
              | \\.hg
              | \\.mypy_cache
              | \\.tox
              | \\.venv
              | _build
              | buck-out
              | build
              | dist
              | migrations
            )/
            '''
            
            [tool.isort]
            profile = "black"
            line_length = 100
            skip_gitignore = true
            
            [tool.mypy]
            python_version = "3.12"
            check_untyped_defs = true
            disallow_untyped_defs = false
            disallow_untyped_calls = false
            disallow_incomplete_defs = false
            no_implicit_optional = true
            warn_redundant_casts = true
            warn_unused_ignores = true
            warn_return_any = true
            plugins = ["mypy_django_plugin.main"]
            
            [tool.django-stubs]
            django_settings_module = "config.settings.base"
            """;
        
        content = content.replace("%s", projectName);
        
        Files.write(root.resolve("pyproject.toml"), content.getBytes());
    }
    
    private void generateDockerCompose(Path root, String projectName) throws IOException {
        String content = """
            version: '3.9'
            
            services:
              db:
                image: postgres:15-alpine
                container_name: %s-db
                environment:
                  POSTGRES_DB: %s
                  POSTGRES_USER: postgres
                  POSTGRES_PASSWORD: postgres
                  POSTGRES_INITDB_ARGS: "--encoding=UTF8"
                ports:
                  - "5432:5432"
                volumes:
                  - postgres_data:/var/lib/postgresql/data
                healthcheck:
                  test: ["CMD-SHELL", "pg_isready -U postgres"]
                  interval: 10s
                  timeout: 5s
                  retries: 5
                networks:
                  - backend
            
              redis:
                image: redis:7-alpine
                container_name: %s-redis
                command: redis-server --appendonly yes --requirepass redis_password
                ports:
                  - "6379:6379"
                volumes:
                  - redis_data:/data
                healthcheck:
                  test: ["CMD", "redis-cli", "ping"]
                  interval: 10s
                  timeout: 5s
                  retries: 5
                networks:
                  - backend
            
              web:
                build:
                  context: .
                  dockerfile: Dockerfile
                container_name: %s-web
                command: gunicorn config.wsgi:application --bind 0.0.0.0:8000 --workers 4 --timeout 120
                environment:
                  DEBUG: "false"
                  DJANGO_SETTINGS_MODULE: config.settings.production
                  DATABASE_URL: postgresql://postgres:postgres@db:5432/%s
                  REDIS_URL: redis://:redis_password@redis:6379/0
                  SECRET_KEY: your-secret-key-change-in-production
                ports:
                  - "8000:8000"
                depends_on:
                  db:
                    condition: service_healthy
                  redis:
                    condition: service_healthy
                volumes:
                  - .:/app
                  - static_volume:/app/static
                  - media_volume:/app/media
                networks:
                  - backend
            
              celery:
                build:
                  context: .
                  dockerfile: Dockerfile
                container_name: %s-celery
                command: celery -A config worker -l info -c 4
                environment:
                  DEBUG: "false"
                  DJANGO_SETTINGS_MODULE: config.settings.production
                  DATABASE_URL: postgresql://postgres:postgres@db:5432/%s
                  REDIS_URL: redis://:redis_password@redis:6379/0
                depends_on:
                  - db
                  - redis
                volumes:
                  - .:/app
                networks:
                  - backend
            
              celery-beat:
                build:
                  context: .
                  dockerfile: Dockerfile
                container_name: %s-celery-beat
                command: celery -A config beat -l info --scheduler django_celery_beat.schedulers:DatabaseScheduler
                environment:
                  DEBUG: "false"
                  DJANGO_SETTINGS_MODULE: config.settings.production
                  DATABASE_URL: postgresql://postgres:postgres@db:5432/%s
                  REDIS_URL: redis://:redis_password@redis:6379/0
                depends_on:
                  - db
                  - redis
                volumes:
                  - .:/app
                networks:
                  - backend
            
            volumes:
              postgres_data:
              redis_data:
              static_volume:
              media_volume:
            
            networks:
              backend:
                driver: bridge
            """;
        
        content = String.format(content, 
            projectName, projectName, projectName, projectName,
            projectName, projectName, projectName, projectName,
            projectName
        );
        
        Files.write(root.resolve("docker-compose.yml"), content.getBytes());
    }
    
    private void generateDockerfile(Path root) throws IOException {
        String content = """
            # Build stage
            FROM python:3.12-slim as builder
            
            WORKDIR /app
            
            RUN apt-get update && apt-get install -y \\
                build-essential \\
                postgresql-client \\
                libpq-dev && \\
                rm -rf /var/lib/apt/lists/*
            
            RUN pip install --upgrade pip setuptools wheel poetry
            
            COPY pyproject.toml poetry.lock* ./
            
            RUN poetry export -f requirements.txt --output requirements.txt && \\
                poetry export -f requirements.txt --with dev --output requirements-dev.txt
            
            # Runtime stage
            FROM python:3.12-slim
            
            WORKDIR /app
            
            ENV PYTHONUNBUFFERED=1 \\
                PYTHONDONTWRITEBYTECODE=1 \\
                PIP_NO_CACHE_DIR=1
            
            RUN apt-get update && apt-get install -y \\
                postgresql-client \\
                libpq5 && \\
                rm -rf /var/lib/apt/lists/*
            
            COPY --from=builder /app/requirements.txt .
            
            RUN pip install -r requirements.txt
            
            COPY . .
            
            # Create non-root user
            RUN useradd -m -u 1000 django && chown -R django:django /app
            USER django
            
            # Collect static files
            RUN python manage.py collectstatic --noinput --clear
            
            EXPOSE 8000
            
            CMD ["gunicorn", "config.wsgi:application", "--bind", "0.0.0.0:8000", "--workers", "4", "--timeout", "120", "--access-logfile", "-", "--error-logfile", "-"]
            """;
        
        Files.write(root.resolve("Dockerfile"), content.getBytes());
    }
    
    private void generateEnvFile(Path root) throws IOException {
        String content = """
            # Django Settings
            DEBUG=True
            SECRET_KEY=your-secret-key-change-in-production
            ALLOWED_HOSTS=localhost,127.0.0.1
            
            # Database
            DATABASE_URL=postgresql://postgres:postgres@localhost:5432/django_db
            
            # Redis
            REDIS_URL=redis://localhost:6379/0
            
            # Email (optional)
            EMAIL_BACKEND=django.core.mail.backends.console.EmailBackend
            EMAIL_HOST=smtp.gmail.com
            EMAIL_PORT=587
            EMAIL_USE_TLS=True
            EMAIL_HOST_USER=your-email@gmail.com
            EMAIL_HOST_PASSWORD=your-password
            
            # CORS
            CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8000
            
            # JWT (if using)
            JWT_SECRET=your-jwt-secret
            JWT_ALGORITHM=HS256
            JWT_EXPIRATION_HOURS=24
            
            # Celery
            CELERY_BROKER_URL=redis://localhost:6379/0
            CELERY_RESULT_BACKEND=redis://localhost:6379/0
            """;
        
        Files.write(root.resolve(".env.example"), content.getBytes());
    }
    
    private void generateGitignore(Path root) throws IOException {
        String content = """
            # Python
            __pycache__/
            *.py[cod]
            *$py.class
            *.so
            .Python
            build/
            develop-eggs/
            dist/
            downloads/
            eggs/
            .eggs/
            lib/
            lib64/
            parts/
            sdist/
            var/
            wheels/
            *.egg-info/
            .installed.cfg
            *.egg
            
            # Virtual environments
            venv/
            ENV/
            env/
            .venv
            
            # IDE
            .vscode/
            .idea/
            *.swp
            *.swo
            *~
            .DS_Store
            
            # Django
            *.log
            local_settings.py
            db.sqlite3
            db.sqlite3-journal
            /media
            /staticfiles
            
            # Testing
            .pytest_cache/
            .coverage
            htmlcov/
            .tox/
            
            # Environment
            .env
            .env.local
            .env.*.local
            
            # Docker
            .docker/
            
            # IDE specific
            *.sublime-project
            *.sublime-workspace
            """;
        
        Files.write(root.resolve(".gitignore"), content.getBytes());
    }
    
    private void generateREADME(Path root, String projectName, String packageName) throws IOException {
        String content = """
            # %s - Django API
            
            Modern Django REST API with async support, Celery, Redis caching, and PostgreSQL.
            
            ## 🚀 Quick Start
            
            ### Prerequisites
            - Python 3.12+
            - Poetry
            - Docker & Docker Compose (optional)
            
            ### Local Development
            
            ```bash
            # Install dependencies
            poetry install
            
            # Create .env file
            cp .env.example .env
            
            # Run migrations
            poetry run python manage.py migrate
            
            # Create superuser
            poetry run python manage.py createsuperuser
            
            # Start development server
            poetry run python manage.py runserver
            
            # In another terminal, start Celery
            poetry run celery -A config worker -l info
            ```
            
            ### Docker Setup
            
            ```bash
            # Build and start services
            docker-compose up -d
            
            # Run migrations
            docker-compose exec web python manage.py migrate
            
            # Create superuser
            docker-compose exec web python manage.py createsuperuser
            
            # Access Django at http://localhost:8000
            # Admin at http://localhost:8000/admin
            ```
            
            ## 📁 Project Structure
            
            ```
            %s/
            ├── config/                    # Django configuration
            │   ├── settings/
            │   │   ├── base.py           # Shared settings
            │   │   ├── development.py    # Dev overrides
            │   │   └── production.py     # Prod overrides
            │   ├── asgi.py               # Async application
            │   ├── urls.py               # URL routing
            │   ├── celery.py             # Celery config
            │   └── wsgi.py               # WSGI application
            ├── apps/
            │   ├── core/                 # Core app
            │   │   ├── models.py
            │   │   ├── views.py
            │   │   ├── urls.py
            │   │   └── admin.py
            │   ├── api/                  # REST API
            │   │   ├── serializers.py
            │   │   ├── viewsets.py
            │   │   ├── filters.py
            │   │   └── permissions.py
            │   └── tasks/                # Celery tasks
            │       ├── jobs/
            │       ├── tasks.py
            │       └── handlers.py
            ├── tests/
            │   ├── unit/
            │   ├── integration/
            │   └── fixtures/
            ├── static/                   # Static files (CSS, JS)
            ├── media/                    # Uploaded files
            ├── docker-compose.yml
            ├── Dockerfile
            ├── pyproject.toml
            └── manage.py
            ```
            
            ## 🔧 Configuration
            
            ### Environment Variables
            
            Create `.env` file based on `.env.example`:
            
            ```bash
            DEBUG=True
            SECRET_KEY=your-secret-key
            DATABASE_URL=postgresql://user:pass@localhost/dbname
            REDIS_URL=redis://localhost:6379/0
            ```
            
            ### Settings Profiles
            
            - **Development** (`config.settings.development`): Debug mode, SQLite, console email
            - **Production** (`config.settings.production`): Security hardening, PostgreSQL, real email
            - **Test** (`config.settings.test`): In-memory database, no email
            
            ## 🗄️ Database
            
            ### PostgreSQL
            
            ```bash
            # Create migrations
            python manage.py makemigrations
            
            # Apply migrations
            python manage.py migrate
            
            # Run raw SQL
            python manage.py dbshell
            ```
            
            ## 📦 API Endpoints
            
            Base URL: `/api/v1/`
            
            ### Authentication
            - JWT token-based authentication
            - Token endpoint: `/api/v1/auth/token/`
            
            ### Available Endpoints
            
            | Method | Endpoint | Description |
            |--------|----------|-------------|
            | GET | `/api/v1/users/` | List users |
            | POST | `/api/v1/users/` | Create user |
            | GET | `/api/v1/users/{id}/` | User details |
            | PUT | `/api/v1/users/{id}/` | Update user |
            | DELETE | `/api/v1/users/{id}/` | Delete user |
            
            ## 🔄 Celery Tasks
            
            ### Start Worker
            
            ```bash
            celery -A config worker -l info
            ```
            
            ### Start Beat Scheduler
            
            ```bash
            celery -A config beat -l info
            ```
            
            ### Monitoring
            
            ```bash
            celery -A config events
            ```
            
            ### Example Task
            
            ```python
            from celery import shared_task
            
            @shared_task
            def send_email_task(user_id):
                user = User.objects.get(id=user_id)
                # Send email logic
                return f"Email sent to {user.email}"
            ```
            
            ## 🧪 Testing
            
            ```bash
            # Run all tests
            pytest
            
            # Run with coverage
            pytest --cov=apps
            
            # Run specific test
            pytest tests/unit/test_users.py
            
            # Run integration tests only
            pytest -m integration
            ```
            
            ## 🔍 Code Quality
            
            ```bash
            # Format code
            black .
            isort .
            
            # Lint
            flake8 apps/ config/
            pylint apps/ config/
            
            # Type checking
            mypy apps/ config/
            ```
            
            ## 🚀 Deployment
            
            ### Gunicorn
            
            ```bash
            gunicorn config.wsgi:application --bind 0.0.0.0:8000 --workers 4 --timeout 120
            ```
            
            ### Nginx Reverse Proxy
            
            ```nginx
            upstream django {
              server web:8000;
            }
            
            server {
              listen 80;
              server_name _;
              
              location / {
                proxy_pass http://django;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
              }
            }
            ```
            
            ### Environment Setup
            
            1. Set `DEBUG=False`
            2. Set strong `SECRET_KEY`
            3. Configure `ALLOWED_HOSTS`
            4. Use PostgreSQL production instance
            5. Enable HTTPS/TLS
            6. Set up proper logging
            
            ## 📚 Documentation
            
            - [Django Documentation](https://docs.djangoproject.com/)
            - [Django REST Framework](https://www.django-rest-framework.org/)
            - [Celery Documentation](https://docs.celeryproject.io/)
            - [Redis Documentation](https://redis.io/documentation)
            
            ## 📝 License
            
            MIT License
            
            ## 👤 Author
            
            Generated by basicCode v2.0
            """;
        
        content = String.format(content, projectName, projectName);
        
        Files.write(root.resolve("README.md"), content.getBytes());
    }
    
    private void generateDjangoSettings(Path root, String packageName) throws IOException {
        // Base settings
        String baseSettings = """
            from pathlib import Path
            import environ
            import os
            
            env = environ.Env(DEBUG=(bool, False))
            
            BASE_DIR = Path(__file__).resolve().parent.parent.parent
            
            # SECURITY
            SECRET_KEY = env('SECRET_KEY', default='dev-secret-key-change-in-production')
            DEBUG = env('DEBUG', default=False)
            ALLOWED_HOSTS = env.list('ALLOWED_HOSTS', default=['localhost', '127.0.0.1'])
            
            # Application definition
            INSTALLED_APPS = [
                'django.contrib.admin',
                'django.contrib.auth',
                'django.contrib.contenttypes',
                'django.contrib.sessions',
                'django.contrib.messages',
                'django.contrib.staticfiles',
                
                # Third-party
                'rest_framework',
                'corsheaders',
                'django_extensions',
                'django_filters',
                
                # Local apps
                'apps.core',
                'apps.api',
                'apps.tasks',
            ]
            
            MIDDLEWARE = [
                'django.middleware.security.SecurityMiddleware',
                'whitenoise.middleware.WhiteNoiseMiddleware',
                'corsheaders.middleware.CorsMiddleware',
                'django.middleware.common.CommonMiddleware',
                'django.middleware.csrf.CsrfViewMiddleware',
                'django.contrib.sessions.middleware.SessionMiddleware',
                'django.contrib.auth.middleware.AuthenticationMiddleware',
                'django.contrib.messages.middleware.MessageMiddleware',
                'django.middleware.clickjacking.XFrameOptionsMiddleware',
            ]
            
            ROOT_URLCONF = 'config.urls'
            
            TEMPLATES = [
                {
                    'BACKEND': 'django.template.backends.django.DjangoTemplates',
                    'DIRS': [BASE_DIR / 'templates'],
                    'APP_DIRS': True,
                    'OPTIONS': {
                        'context_processors': [
                            'django.template.context_processors.debug',
                            'django.template.context_processors.request',
                            'django.contrib.auth.context_processors.auth',
                            'django.contrib.messages.context_processors.messages',
                        ],
                    },
                },
            ]
            
            WSGI_APPLICATION = 'config.wsgi.application'
            ASGI_APPLICATION = 'config.asgi.application'
            
            # Database
            DATABASES = {
                'default': env.db(
                    'DATABASE_URL',
                    default='postgresql://postgres:postgres@localhost:5432/django_db'
                )
            }
            
            DATABASES['default']['ATOMIC_REQUESTS'] = True
            DATABASES['default']['CONN_MAX_AGE'] = 600
            
            # Cache
            CACHES = {
                'default': env.cache(
                    'REDIS_URL',
                    default='redis://localhost:6379/0'
                )
            }
            
            # Password validation
            AUTH_PASSWORD_VALIDATORS = [
                {'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator'},
                {'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator'},
                {'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator'},
                {'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator'},
            ]
            
            # Internationalization
            LANGUAGE_CODE = 'en-us'
            TIME_ZONE = 'UTC'
            USE_I18N = True
            USE_TZ = True
            
            # Static files
            STATIC_URL = '/static/'
            STATIC_ROOT = BASE_DIR / 'staticfiles'
            STATICFILES_DIRS = [BASE_DIR / 'static'] if (BASE_DIR / 'static').exists() else []
            STATICFILES_STORAGE = 'whitenoise.storage.CompressedManifestStaticFilesStorage'
            
            # Media files
            MEDIA_URL = '/media/'
            MEDIA_ROOT = BASE_DIR / 'media'
            
            # Default primary key field type
            DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'
            
            # REST Framework
            REST_FRAMEWORK = {
                'DEFAULT_AUTHENTICATION_CLASSES': [
                    'rest_framework_simplejwt.authentication.JWTAuthentication',
                ],
                'DEFAULT_PERMISSION_CLASSES': [
                    'rest_framework.permissions.IsAuthenticated',
                ],
                'DEFAULT_FILTER_BACKENDS': [
                    'django_filters.rest_framework.DjangoFilterBackend',
                    'rest_framework.filters.SearchFilter',
                    'rest_framework.filters.OrderingFilter',
                ],
                'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',
                'PAGE_SIZE': 20,
                'DEFAULT_THROTTLE_CLASSES': [
                    'rest_framework.throttling.AnonRateThrottle',
                    'rest_framework.throttling.UserRateThrottle'
                ],
                'DEFAULT_THROTTLE_RATES': {
                    'anon': '100/hour',
                    'user': '1000/hour'
                },
                'EXCEPTION_HANDLER': 'apps.api.exceptions.custom_exception_handler',
            }
            
            # CORS
            CORS_ALLOWED_ORIGINS = env.list(
                'CORS_ALLOWED_ORIGINS',
                default=['http://localhost:3000', 'http://localhost:8000']
            )
            CORS_ALLOW_CREDENTIALS = True
            
            # Celery
            CELERY_BROKER_URL = env('REDIS_URL', default='redis://localhost:6379/0')
            CELERY_RESULT_BACKEND = env('REDIS_URL', default='redis://localhost:6379/0')
            CELERY_ACCEPT_CONTENT = ['json']
            CELERY_TASK_SERIALIZER = 'json'
            CELERY_RESULT_SERIALIZER = 'json'
            CELERY_TASK_TRACK_STARTED = True
            CELERY_TASK_TIME_LIMIT = 30 * 60
            
            # Logging
            LOGGING = {
                'version': 1,
                'disable_existing_loggers': False,
                'formatters': {
                    'verbose': {
                        'format': '{levelname} {asctime} {module} {process:d} {thread:d} {message}',
                        'style': '{',
                    },
                },
                'handlers': {
                    'console': {
                        'class': 'logging.StreamHandler',
                        'formatter': 'verbose',
                    },
                },
                'root': {
                    'handlers': ['console'],
                    'level': 'INFO',
                },
            }
            """;
        
        Files.createDirectories(root.resolve("config/settings"));
        Files.write(root.resolve("config/settings/base.py"), baseSettings.getBytes());
        Files.write(root.resolve("config/settings/__init__.py"), "".getBytes());
        
        // Development settings
        String devSettings = """
            from .base import *
            
            DEBUG = True
            
            INSTALLED_APPS += [
                'django_extensions',
                'debug_toolbar',
            ]
            
            MIDDLEWARE += [
                'debug_toolbar.middleware.DebugToolbarMiddleware',
            ]
            
            INTERNAL_IPS = ['127.0.0.1', 'localhost']
            
            # SQLite for development
            DATABASES = {
                'default': {
                    'ENGINE': 'django.db.backends.sqlite3',
                    'NAME': BASE_DIR / 'db.sqlite3',
                }
            }
            
            # Console email backend for development
            EMAIL_BACKEND = 'django.core.mail.backends.console.EmailBackend'
            """;
        
        Files.write(root.resolve("config/settings/development.py"), devSettings.getBytes());
        
        // Production settings
        String prodSettings = """
            from .base import *
            
            DEBUG = False
            
            # Security
            SECURE_SSL_REDIRECT = True
            SESSION_COOKIE_SECURE = True
            CSRF_COOKIE_SECURE = True
            SECURE_HSTS_SECONDS = 31536000
            SECURE_HSTS_INCLUDE_SUBDOMAINS = True
            SECURE_HSTS_PRELOAD = True
            X_FRAME_OPTIONS = 'DENY'
            
            # Allowed hosts from environment
            ALLOWED_HOSTS = env.list('ALLOWED_HOSTS', default=['example.com'])
            
            # Email backend
            EMAIL_BACKEND = 'django.core.mail.backends.smtp.EmailBackend'
            EMAIL_HOST = env('EMAIL_HOST', default='smtp.gmail.com')
            EMAIL_PORT = env.int('EMAIL_PORT', default=587)
            EMAIL_USE_TLS = True
            EMAIL_HOST_USER = env('EMAIL_HOST_USER')
            EMAIL_HOST_PASSWORD = env('EMAIL_HOST_PASSWORD')
            """;
        
        Files.write(root.resolve("config/settings/production.py"), prodSettings.getBytes());
        
        // Test settings
        String testSettings = """
            from .base import *
            
            DEBUG = True
            
            # In-memory SQLite for tests
            DATABASES = {
                'default': {
                    'ENGINE': 'django.db.backends.sqlite3',
                    'NAME': ':memory:',
                }
            }
            
            # Disable migrations for faster tests
            class DisableMigrations:
                def __contains__(self, item):
                    return True
                
                def __getitem__(self, item):
                    return None
            
            MIGRATION_MODULES = DisableMigrations()
            
            # No cache for tests
            CACHES = {
                'default': {
                    'BACKEND': 'django.core.cache.backends.locmem.LocMemCache',
                }
            }
            """;
        
        Files.write(root.resolve("config/settings/test.py"), testSettings.getBytes());
    }
    
    private void generateAsgiConfig(Path root, String packageName) throws IOException {
        String content = """
            import os
            from django.core.asgi import get_asgi_application
            
            os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'config.settings.production')
            
            application = get_asgi_application()
            """;
        
        Files.write(root.resolve("config/asgi.py"), content.getBytes());
        
        // WSGI for backward compatibility
        String wsgiContent = """
            import os
            from django.core.wsgi import get_wsgi_application
            
            os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'config.settings.production')
            
            application = get_wsgi_application()
            """;
        
        Files.write(root.resolve("config/wsgi.py"), wsgiContent.getBytes());
    }
    
    private void generateUrls(Path root, String packageName) throws IOException {
        String content = """
            from django.contrib import admin
            from django.urls import path, include
            from django.conf import settings
            from django.conf.urls.static import static
            from rest_framework.routers import DefaultRouter
            from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView
            
            router = DefaultRouter()
            
            urlpatterns = [
                path('admin/', admin.site.urls),
                path('api/v1/', include('apps.api.urls')),
                path('api/v1/auth/token/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
                path('api/v1/auth/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
            ]
            
            if settings.DEBUG:
                urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
                urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
            """;
        
        Files.write(root.resolve("config/urls.py"), content.getBytes());
        Files.write(root.resolve("config/__init__.py"), "".getBytes());
        Files.write(root.resolve("config/manage.py"), "".getBytes());
    }
    
    private void generateCeleryConfig(Path root, String packageName) throws IOException {
        String content = """
            import os
            from celery import Celery
            from celery.schedules import crontab
            
            os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'config.settings.production')
            
            app = Celery('config')
            app.config_from_object('django.conf:settings', namespace='CELERY')
            app.autodiscover_tasks()
            
            @app.task(bind=True)
            def debug_task(self):
                print(f'Request: {self.request!r}')
            
            # Scheduled tasks
            app.conf.beat_schedule = {
                'cleanup-sessions': {
                    'task': 'apps.tasks.tasks.cleanup_sessions',
                    'schedule': crontab(hour=2, minute=0),  # Run at 2 AM daily
                },
                'sync-cache': {
                    'task': 'apps.tasks.tasks.sync_cache',
                    'schedule': 300.0,  # Every 5 minutes
                },
            }
            """;
        
        Files.write(root.resolve("config/celery.py"), content.getBytes());
    }
    
    private void generateCoreApp(Path root, String packageName) throws IOException {
        // Models
        String models = """
            from django.db import models
            from django.contrib.auth.models import AbstractUser, BaseUserManager
            from django.utils.translation import gettext_lazy as _
            import uuid
            
            class CustomUserManager(BaseUserManager):
                def create_user(self, email, password=None, **extra_fields):
                    if not email:
                        raise ValueError(_('Email is required'))
                    email = self.normalize_email(email)
                    user = self.model(email=email, **extra_fields)
                    user.set_password(password)
                    user.save(using=self._db)
                    return user
                
                def create_superuser(self, email, password=None, **extra_fields):
                    extra_fields.setdefault('is_staff', True)
                    extra_fields.setdefault('is_superuser', True)
                    return self.create_user(email, password, **extra_fields)
            
            class User(AbstractUser):
                id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
                email = models.EmailField(unique=True, max_length=255)
                username = None
                
                USERNAME_FIELD = 'email'
                REQUIRED_FIELDS = []
                
                objects = CustomUserManager()
                
                created_at = models.DateTimeField(auto_now_add=True)
                updated_at = models.DateTimeField(auto_now=True)
                is_active = models.BooleanField(default=True)
                
                class Meta:
                    verbose_name = _('user')
                    verbose_name_plural = _('users')
                    ordering = ['-created_at']
                
                def __str__(self):
                    return self.email
            
            class BaseModel(models.Model):
                id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
                created_at = models.DateTimeField(auto_now_add=True)
                updated_at = models.DateTimeField(auto_now=True)
                
                class Meta:
                    abstract = True
            """;
        
        Files.createDirectories(root.resolve("apps/core"));
        Files.write(root.resolve("apps/core/models.py"), models.getBytes());
        Files.write(root.resolve("apps/core/__init__.py"), "".getBytes());
        Files.write(root.resolve("apps/core/models/__init__.py"), "".getBytes());
        
        // Admin
        String admin = """
            from django.contrib import admin
            from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
            from .models import User
            
            @admin.register(User)
            class UserAdmin(BaseUserAdmin):
                fieldsets = (
                    (None, {'fields': ('email', 'password')}),
                    ('Personal info', {'fields': ('first_name', 'last_name')}),
                    ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
                    ('Important dates', {'fields': ('last_login', 'date_joined')}),
                )
                add_fieldsets = (
                    (None, {
                        'classes': ('wide',),
                        'fields': ('email', 'password1', 'password2'),
                    }),
                )
                list_display = ('email', 'first_name', 'last_name', 'is_staff', 'is_active')
                list_filter = ('is_staff', 'is_superuser', 'is_active', 'groups')
                search_fields = ('email', 'first_name', 'last_name')
                ordering = ('-created_at',)
            """;
        
        Files.write(root.resolve("apps/core/admin.py"), admin.getBytes());
        Files.write(root.resolve("apps/core/apps.py"), "from django.apps import AppConfig\n\nclass CoreConfig(AppConfig):\n    default_auto_field = 'django.db.models.BigAutoField'\n    name = 'apps.core'".getBytes());
    }
    
    private void generateApiApp(Path root, String packageName) throws IOException {
        Files.createDirectories(root.resolve("apps/api"));
        
        // Serializers
        String serializers = """
            from rest_framework import serializers
            from apps.core.models import User
            
            class UserSerializer(serializers.ModelSerializer):
                class Meta:
                    model = User
                    fields = ['id', 'email', 'first_name', 'last_name', 'is_active', 'created_at']
                    read_only_fields = ['id', 'created_at']
            
            class UserCreateSerializer(serializers.ModelSerializer):
                password = serializers.CharField(write_only=True, min_length=8)
                password_confirm = serializers.CharField(write_only=True, min_length=8)
                
                class Meta:
                    model = User
                    fields = ['email', 'password', 'password_confirm', 'first_name', 'last_name']
                
                def validate(self, data):
                    if data['password'] != data.pop('password_confirm'):
                        raise serializers.ValidationError("Passwords don't match")
                    return data
                
                def create(self, validated_data):
                    return User.objects.create_user(**validated_data)
            """;
        
        Files.write(root.resolve("apps/api/serializers.py"), serializers.getBytes());
        
        // Viewsets
        String viewsets = """
            from rest_framework import viewsets, status
            from rest_framework.decorators import action
            from rest_framework.response import Response
            from rest_framework.permissions import IsAuthenticated, AllowAny
            from django_filters.rest_framework import DjangoFilterBackend
            from rest_framework.filters import SearchFilter, OrderingFilter
            from apps.core.models import User
            from .serializers import UserSerializer, UserCreateSerializer
            
            class UserViewSet(viewsets.ModelViewSet):
                queryset = User.objects.all()
                permission_classes = [IsAuthenticated]
                filter_backends = [DjangoFilterBackend, SearchFilter, OrderingFilter]
                filterset_fields = ['is_active']
                search_fields = ['email', 'first_name', 'last_name']
                ordering_fields = ['created_at', 'email']
                ordering = ['-created_at']
                
                def get_serializer_class(self):
                    if self.action == 'create':
                        return UserCreateSerializer
                    return UserSerializer
                
                def get_permissions(self):
                    if self.action == 'create':
                        permission_classes = [AllowAny]
                    else:
                        permission_classes = [IsAuthenticated]
                    return [permission() for permission in permission_classes]
                
                @action(detail=False, methods=['get'])
                def me(self, request):
                    serializer = self.get_serializer(request.user)
                    return Response(serializer.data)
                
                @action(detail=False, methods=['post'])
                def set_password(self, request):
                    user = request.user
                    serializer = PasswordChangeSerializer(data=request.data)
                    if serializer.is_valid():
                        user.set_password(serializer.validated_data['new_password'])
                        user.save()
                        return Response({'status': 'password set'})
                    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
            """;
        
        Files.write(root.resolve("apps/api/viewsets.py"), viewsets.getBytes());
        
        // URLs
        String urls = """
            from django.urls import path, include
            from rest_framework.routers import DefaultRouter
            from .viewsets import UserViewSet
            
            router = DefaultRouter()
            router.register(r'users', UserViewSet, basename='user')
            
            urlpatterns = [
                path('', include(router.urls)),
            ]
            """;
        
        Files.write(root.resolve("apps/api/urls.py"), urls.getBytes());
        Files.write(root.resolve("apps/api/__init__.py"), "".getBytes());
        Files.write(root.resolve("apps/api/apps.py"), "from django.apps import AppConfig\n\nclass ApiConfig(AppConfig):\n    default_auto_field = 'django.db.models.BigAutoField'\n    name = 'apps.api'".getBytes());
    }
    
    private void generateTasksApp(Path root, String packageName) throws IOException {
        Files.createDirectories(root.resolve("apps/tasks"));
        
        String tasks = "from celery import shared_task\n" +
            "from django.core.cache import cache\n" +
            "from django.contrib.sessions.models import Session\n" +
            "import logging\n\n" +
            "logger = logging.getLogger(__name__)\n\n" +
            "@shared_task\n" +
            "def cleanup_sessions():\n" +
            "    \"\"\"Remove expired sessions\"\"\"\n" +
            "    Session.objects.filter(expire_date__lt=timezone.now()).delete()\n" +
            "    logger.info('Cleaned up expired sessions')\n\n" +
            "@shared_task\n" +
            "def sync_cache():\n" +
            "    \"\"\"Sync cache with database\"\"\"\n" +
            "    cache.clear()\n" +
            "    logger.info('Cache synced')\n\n" +
            "@shared_task\n" +
            "def send_email(recipient, subject, message):\n" +
            "    \"\"\"Send email asynchronously\"\"\"\n" +
            "    from django.core.mail import send_mail\n" +
            "    try:\n" +
            "        send_mail(subject, message, 'noreply@example.com', [recipient])\n" +
            "        logger.info(f'Email sent to {recipient}')\n" +
            "    except Exception as e:\n" +
            "        logger.error(f'Failed to send email: {e}')\n";
        
        Files.write(root.resolve("apps/tasks/tasks.py"), tasks.getBytes());
        Files.write(root.resolve("apps/tasks/__init__.py"), "".getBytes());
        Files.write(root.resolve("apps/tasks/apps.py"), "from django.apps import AppConfig\n\nclass TasksConfig(AppConfig):\n    default_auto_field = 'django.db.models.BigAutoField'\n    name = 'apps.tasks'".getBytes());
    }
    
    private void generatePytestConfig(Path root) throws IOException {
        String content = """
            [pytest]
            DJANGO_SETTINGS_MODULE = config.settings.test
            python_files = tests.py test_*.py *_tests.py
            addopts = --strict-markers --cov=apps --cov-report=term-missing
            testpaths = tests
            
            markers =
                slow: marks tests as slow
                integration: marks tests as integration tests
                unit: marks tests as unit tests
            """;
        
        Files.write(root.resolve("pytest.ini"), content.getBytes());
    }
    
    private void generateTestBase(Path root, String packageName) throws IOException {
        String content = """
            import pytest
            from django.test import Client
            from django.contrib.auth import get_user_model
            from rest_framework.test import APIClient
            from rest_framework_simplejwt.tokens import RefreshToken
            
            User = get_user_model()
            
            @pytest.fixture
            def api_client():
                return APIClient()
            
            @pytest.fixture
            def test_user():
                return User.objects.create_user(
                    email='test@example.com',
                    password='testpass123'
                )
            
            @pytest.fixture
            def authenticated_client(api_client, test_user):
                refresh = RefreshToken.for_user(test_user)
                api_client.credentials(HTTP_AUTHORIZATION=f'Bearer {refresh.access_token}')
                return api_client
            """;
        
        Files.write(root.resolve("tests/conftest.py"), content.getBytes());
    }
    
    private void generateMiddleware(Path root, String packageName) throws IOException {
        Files.createDirectories(root.resolve("apps/core/middleware"));
        
        String middleware = """
            import logging
            import uuid
            from django.utils.deprecation import MiddlewareMixin
            
            logger = logging.getLogger(__name__)
            
            class RequestLoggingMiddleware(MiddlewareMixin):
                def process_request(self, request):
                    request.request_id = uuid.uuid4()
                    logger.info(
                        f"Request ID: {request.request_id} | "
                        f"Method: {request.method} | "
                        f"Path: {request.path}"
                    )
                
                def process_response(self, request, response):
                    logger.info(
                        f"Request ID: {request.request_id} | "
                        f"Status: {response.status_code}"
                    )
                    response['X-Request-ID'] = str(request.request_id)
                    return response
            """;
        
        Files.write(root.resolve("apps/core/middleware.py"), middleware.getBytes());
    }
    
    private void generateExceptions(Path root, String packageName) throws IOException {
        String exceptions = """
            from rest_framework.views import exception_handler
            from rest_framework.response import Response
            
            class APIException(Exception):
                def __init__(self, message, status_code=400, error_code=None):
                    self.message = message
                    self.status_code = status_code
                    self.error_code = error_code
            
            def custom_exception_handler(exc, context):
                response = exception_handler(exc, context)
                
                if response is not None:
                    if 'detail' in response.data:
                        response.data = {
                            'error': True,
                            'message': str(response.data['detail']),
                            'status_code': response.status_code
                        }
                
                return response
            """;
        
        Files.write(root.resolve("apps/api/exceptions.py"), exceptions.getBytes());
    }
    
    private void generateSerializers(Path root, String packageName) throws IOException {
        String serializers = """
            from rest_framework import serializers
            
            class PasswordChangeSerializer(serializers.Serializer):
                old_password = serializers.CharField(write_only=True, required=True)
                new_password = serializers.CharField(write_only=True, required=True, min_length=8)
                new_password_confirm = serializers.CharField(write_only=True, required=True, min_length=8)
                
                def validate(self, data):
                    if data['new_password'] != data['new_password_confirm']:
                        raise serializers.ValidationError("Passwords don't match")
                    return data
            """;
        
        Files.write(root.resolve("apps/core/password_serializer.py"), serializers.getBytes());
    }
}
