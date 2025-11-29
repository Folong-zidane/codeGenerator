package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class DjangoFileWriter implements IFileWriter {
    
    @Override
    public String getOutputFormat() {
        return "django";
    }
    
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        files.forEach((fileName, content) -> {
            try {
                writeFile(fileName, content, outputPath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write file: " + fileName, e);
            }
        });
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            writeFileInternal(content, fileName, outputPath, "");
        } catch (Exception e) {
            throw new RuntimeException("Failed to write file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            for (String dir : directories) {
                Files.createDirectories(Paths.get(basePath, dir));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create directories", e);
        }
    }
    
    private void writeFileInternal(String content, String fileName, String directory, String packageName) throws IOException {
        Path dirPath = Paths.get(directory, packageName.replace(".", "/"));
        Files.createDirectories(dirPath);
        
        String extension = getFileExtension(fileName);
        Path filePath = dirPath.resolve(fileName + extension);
        Files.write(filePath, content.getBytes());
    }
    
    public void createProjectStructure(String baseDirectory, String packageName) throws IOException {
        String projectPath = baseDirectory + "/" + packageName.replace(".", "/");
        
        // Create Django project structure
        Files.createDirectories(Paths.get(projectPath));
        Files.createDirectories(Paths.get(projectPath, "migrations"));
        Files.createDirectories(Paths.get(projectPath, "static"));
        Files.createDirectories(Paths.get(projectPath, "templates"));
        
        // Create __init__.py files
        Files.write(Paths.get(projectPath, "__init__.py"), "".getBytes());
        Files.write(Paths.get(projectPath, "migrations", "__init__.py"), "".getBytes());
        
        // Create Django configuration files
        createDjangoSettings(projectPath, packageName);
        createManagePy(baseDirectory, packageName);
        createRequirementsTxt(baseDirectory);
        createWsgiPy(projectPath, packageName);
        createAppsPy(projectPath, packageName);
    }
    
    private String getFileExtension(String fileName) {
        if (fileName.contains("urls")) return ".py";
        if (fileName.contains("views")) return ".py";
        if (fileName.contains("models")) return ".py";
        if (fileName.contains("serializers")) return ".py";
        if (fileName.contains("services")) return ".py";
        if (fileName.contains("admin")) return ".py";
        return ".py";
    }
    
    private void createDjangoSettings(String projectPath, String packageName) throws IOException {
        String settings = """
import os
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

SECRET_KEY = 'django-insecure-change-this-in-production'
DEBUG = True
ALLOWED_HOSTS = []

INSTALLED_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'rest_framework',
    'corsheaders',
    '%s',
]

MIDDLEWARE = [
    'corsheaders.middleware.CorsMiddleware',
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = '%s.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
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

WSGI_APPLICATION = '%s.wsgi.application'

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': BASE_DIR / 'db.sqlite3',
    }
}

REST_FRAMEWORK = {
    'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',
    'PAGE_SIZE': 20,
    'DEFAULT_RENDERER_CLASSES': [
        'rest_framework.renderers.JSONRenderer',
    ],
}

CORS_ALLOW_ALL_ORIGINS = True

LANGUAGE_CODE = 'en-us'
TIME_ZONE = 'UTC'
USE_I18N = True
USE_TZ = True

STATIC_URL = '/static/'
DEFAULT_AUTO_FIELD = 'django.db.models.BigAutoField'
""".formatted(packageName.split("\\.")[packageName.split("\\.").length - 1], packageName, packageName);
        
        Files.write(Paths.get(projectPath, "settings.py"), settings.getBytes());
    }
    
    private void createManagePy(String baseDirectory, String packageName) throws IOException {
        String managePy = """
#!/usr/bin/env python
import os
import sys

if __name__ == '__main__':
    os.environ.setdefault('DJANGO_SETTINGS_MODULE', '%s.settings')
    try:
        from django.core.management import execute_from_command_line
    except ImportError as exc:
        raise ImportError(
            "Couldn't import Django. Are you sure it's installed and "
            "available on your PYTHONPATH environment variable? Did you "
            "forget to activate a virtual environment?"
        ) from exc
    execute_from_command_line(sys.argv)
""".formatted(packageName);
        
        Files.write(Paths.get(baseDirectory, "manage.py"), managePy.getBytes());
    }
    
    private void createRequirementsTxt(String baseDirectory) throws IOException {
        String requirements = """
Django>=4.2.0
djangorestframework>=3.14.0
django-cors-headers>=4.0.0
""";
        Files.write(Paths.get(baseDirectory, "requirements.txt"), requirements.getBytes());
    }
    
    private void createWsgiPy(String projectPath, String packageName) throws IOException {
        String wsgi = """
import os
from django.core.wsgi import get_wsgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', '%s.settings')
application = get_wsgi_application()
""".formatted(packageName);
        
        Files.write(Paths.get(projectPath, "wsgi.py"), wsgi.getBytes());
    }
    
    private void createAppsPy(String projectPath, String packageName) throws IOException {
        String apps = """
from django.apps import AppConfig

class %sConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = '%s'
""".formatted(capitalize(packageName.split("\\.")[packageName.split("\\.").length - 1]), packageName);
        
        Files.write(Paths.get(projectPath, "apps.py"), apps.getBytes());
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}