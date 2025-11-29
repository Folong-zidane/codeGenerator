package com.basiccode.generator.initializer;

import org.springframework.stereotype.Component;
import java.nio.file.*;
import java.io.IOException;

@Component
public class DjangoInitializer implements ProjectInitializer {
    
    private static final String DJANGO_VERSION = "5.0";
    
    @Override
    public Path initializeProject(String projectName, String packageName) {
        try {
            Path projectPath = Paths.get("temp", projectName);
            Files.createDirectories(projectPath);
            
            // Create Django project structure
            createDjangoStructure(projectPath, projectName);
            
            return projectPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Django project", e);
        }
    }
    
    @Override
    public String getLanguage() {
        return "django";
    }
    
    @Override
    public String getLatestVersion() {
        return DJANGO_VERSION;
    }
    
    @Override
    public void mergeGeneratedCode(Path templatePath, Path generatedCodePath) {
        try {
            // Merge models, views, serializers into Django apps
            Path appsDir = templatePath.resolve("apps");
            Files.createDirectories(appsDir);
            
            Files.walk(generatedCodePath)
                .filter(Files::isRegularFile)
                .forEach(source -> {
                    try {
                        Path target = appsDir.resolve(generatedCodePath.relativize(source));
                        Files.createDirectories(target.getParent());
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to merge Django code", e);
                    }
                });
        } catch (IOException e) {
            throw new RuntimeException("Failed to merge generated code", e);
        }
    }
    
    private void createDjangoStructure(Path projectPath, String projectName) throws IOException {
        // Create manage.py
        String managePy = String.format("""
            #!/usr/bin/env python
            import os
            import sys
            
            if __name__ == '__main__':
                os.environ.setdefault('DJANGO_SETTINGS_MODULE', '%s.settings')
                try:
                    from django.core.management import execute_from_command_line
                except ImportError as exc:
                    raise ImportError("Django not found") from exc
                execute_from_command_line(sys.argv)
            """, projectName);
        Files.write(projectPath.resolve("manage.py"), managePy.getBytes());
        
        // Create settings.py
        createDjangoSettings(projectPath, projectName);
        
        // Create requirements.txt
        String requirements = String.format("""
            Django==%s
            djangorestframework==3.14.0
            django-cors-headers==4.3.1
            python-decouple==3.8
            """, DJANGO_VERSION);
        Files.write(projectPath.resolve("requirements.txt"), requirements.getBytes());
    }
    
    private void createDjangoSettings(Path projectPath, String projectName) throws IOException {
        Path settingsDir = projectPath.resolve(projectName);
        Files.createDirectories(settingsDir);
        
        String settings = String.format("""
            from pathlib import Path
            
            BASE_DIR = Path(__file__).resolve().parent.parent
            
            SECRET_KEY = 'your-secret-key-here'
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
            
            DATABASES = {
                'default': {
                    'ENGINE': 'django.db.backends.sqlite3',
                    'NAME': BASE_DIR / 'db.sqlite3',
                }
            }
            
            REST_FRAMEWORK = {
                'DEFAULT_PAGINATION_CLASS': 'rest_framework.pagination.PageNumberPagination',
                'PAGE_SIZE': 20
            }
            """, projectName);
        Files.write(settingsDir.resolve("settings.py"), settings.getBytes());
    }
}