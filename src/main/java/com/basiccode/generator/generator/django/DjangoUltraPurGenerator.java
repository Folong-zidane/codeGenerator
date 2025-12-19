package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.parser.MetadataAwareDiagramParser;
import java.util.*;

/**
 * Générateur Django Ultra-Pur spécialisé - Appelé par MetadataAwareGenerationOrchestrator
 */
public class DjangoUltraPurGenerator {
    
    private final DjangoEntityGenerator entityGenerator;
    private final DjangoServiceGenerator serviceGenerator;
    private final DjangoControllerGenerator controllerGenerator;
    private final DjangoSerializerGenerator serializerGenerator;
    private final DjangoExceptionGenerator exceptionGenerator;
    private final DjangoConfigGenerator configGenerator;
    private final DjangoUrlsGenerator urlsGenerator;
    private final DjangoTestGenerator testGenerator;
    
    public DjangoUltraPurGenerator() {
        this.entityGenerator = new DjangoEntityGenerator();
        this.serviceGenerator = new DjangoServiceGenerator();
        this.controllerGenerator = new DjangoControllerGenerator();
        this.serializerGenerator = new DjangoSerializerGenerator();
        this.exceptionGenerator = new DjangoExceptionGenerator();
        this.configGenerator = new DjangoConfigGenerator();
        this.urlsGenerator = new DjangoUrlsGenerator();
        this.testGenerator = new DjangoTestGenerator();
    }
    
    /**
     * Génère une application Django ultra-pure complète avec métadonnées
     */
    public Map<String, String> generateCompleteAppWithMetadata(String diagramContent, String packageName, String projectName) {
        Map<String, String> generatedFiles = new HashMap<>();
        
        try {
            // Parse with metadata awareness
            MetadataAwareDiagramParser parser = new MetadataAwareDiagramParser();
            var parsedDiagram = parser.parseWithMetadata(diagramContent);
            
            List<EnhancedClass> entities = parsedDiagram.getClasses();
            Map<String, String> metadata = parsedDiagram.getMetadata();
            
            if (entities.isEmpty()) {
                throw new IllegalArgumentException("No entities found in diagram");
            }
            
            System.out.println("📊 Metadata extracted: " + metadata.size() + " properties");
            System.out.println("🏗️ Entities found: " + entities.size());
            
            // Generate core components for each entity with metadata
            for (EnhancedClass entity : entities) {
                generateEntityComponents(entity, packageName, generatedFiles, metadata);
            }
            
            // Generate configuration files with metadata
            generateConfigurationFiles(generatedFiles, entities, packageName, projectName, metadata);
            
            // Generate aggregated files with metadata
            generateAggregatedFiles(generatedFiles, entities, packageName, metadata);
            
            // Generate deployment files
            generateDeploymentFiles(generatedFiles, packageName, projectName);
            
            System.out.println("✅ Django Ultra-Pur: " + generatedFiles.size() + " files generated");
            
        } catch (Exception e) {
            System.err.println("❌ Django Ultra-Pur generation error: " + e.getMessage());
            throw new RuntimeException("Failed to generate Django ultra-pure application", e);
        }
        
        return generatedFiles;
    }
    
    public Map<String, String> generateCompleteApp(List<EnhancedClass> entities, String packageName, String projectName) {
        // Legacy method - convert entities back to diagram content (simplified)
        StringBuilder diagramContent = new StringBuilder();
        diagramContent.append("classDiagram\n");
        for (EnhancedClass entity : entities) {
            diagramContent.append("    class ").append(entity.getOriginalClass().getName()).append(" {\n");
            if (entity.getOriginalClass().getAttributes() != null) {
                for (var attr : entity.getOriginalClass().getAttributes()) {
                    diagramContent.append("        +").append(attr.getType()).append(" ").append(attr.getName()).append("\n");
                }
            }
            diagramContent.append("    }\n");
        }
        
        return generateCompleteAppWithMetadata(diagramContent.toString(), packageName, projectName);
    }
    
    private void generateEntityComponents(EnhancedClass entity, String packageName, Map<String, String> files, Map<String, String> metadata) {
        String className = entity.getOriginalClass().getName();
        
        // Models with metadata
        String modelCode;
        if (entityGenerator instanceof DjangoEntityGenerator) {
            modelCode = ((DjangoEntityGenerator) entityGenerator).generateEntity(entity, packageName, metadata);
        } else {
            modelCode = entityGenerator.generateEntity(entity, packageName);
        }
        files.put("models/" + className.toLowerCase() + ".py", modelCode);
        
        // Services
        String serviceCode = serviceGenerator.generateService(entity, packageName);
        files.put("services/" + className.toLowerCase() + "_service.py", serviceCode);
        
        // ViewSets
        String controllerCode = controllerGenerator.generateController(entity, packageName);
        files.put("views/" + className.toLowerCase() + "_views.py", controllerCode);
        
        // Serializers
        String serializerCode = serializerGenerator.generateSerializer(entity, packageName);
        files.put("serializers/" + className.toLowerCase() + "_serializers.py", serializerCode);
        
        // Exceptions
        String exceptionCode = exceptionGenerator.generateExceptions(entity, packageName);
        files.put("exceptions/" + className.toLowerCase() + "_exceptions.py", exceptionCode);
        
        // Tests
        String testCode = testGenerator.generateTests(entity, packageName);
        files.put("tests/test_" + className.toLowerCase() + ".py", testCode);
    }
    
    private void generateConfigurationFiles(Map<String, String> files, List<EnhancedClass> entities, String packageName, String projectName, Map<String, String> metadata) {
        // Django settings with metadata
        String settingsCode = generateSettingsWithMetadata(packageName, projectName, metadata);
        files.put("settings.py", settingsCode);
        
        // Requirements
        files.put("requirements.txt", configGenerator.generateRequirements());
        
        // URLs
        files.put("urls.py", urlsGenerator.generateMainUrls(packageName, projectName));
        files.put(packageName.replace(".", "_") + "/urls.py", urlsGenerator.generateUrls(entities, packageName));
        
        // Environment
        files.put(".env.example", generateEnvExample());
        
        // Management
        files.put("manage.py", generateManagePy(packageName));
        files.put(packageName.replace(".", "_") + "/wsgi.py", generateWsgi(packageName));
        files.put(packageName.replace(".", "_") + "/apps.py", generateAppConfig(packageName));
    }
    
    private void generateAggregatedFiles(Map<String, String> files, List<EnhancedClass> entities, String packageName, Map<String, String> metadata) {
        // Combined models with metadata
        StringBuilder modelsFile = new StringBuilder();
        modelsFile.append("# Django Models - Ultra-Pure Generated with Metadata\n");
        modelsFile.append("# Metadata: ").append(metadata.toString()).append("\n\n");
        
        for (EnhancedClass entity : entities) {
            String modelCode;
            if (entityGenerator instanceof DjangoEntityGenerator) {
                modelCode = ((DjangoEntityGenerator) entityGenerator).generateEntity(entity, packageName, metadata);
            } else {
                modelCode = entityGenerator.generateEntity(entity, packageName);
            }
            modelsFile.append(modelCode).append("\n\n");
        }
        files.put("models.py", modelsFile.toString());
        
        // Combined serializers
        StringBuilder serializersFile = new StringBuilder();
        serializersFile.append("# Django Serializers - Ultra-Pure Generated\n\n");
        for (EnhancedClass entity : entities) {
            serializersFile.append(serializerGenerator.generateSerializer(entity, packageName)).append("\n\n");
        }
        files.put("serializers.py", serializersFile.toString());
        
        // Combined views
        StringBuilder viewsFile = new StringBuilder();
        viewsFile.append("# Django Views - Ultra-Pure Generated\n\n");
        for (EnhancedClass entity : entities) {
            viewsFile.append(controllerGenerator.generateController(entity, packageName)).append("\n\n");
        }
        files.put("views.py", viewsFile.toString());
        
        // Admin
        files.put("admin.py", generateAdminConfig(entities));
    }
    
    private void generateDeploymentFiles(Map<String, String> files, String packageName, String projectName) {
        files.put("Dockerfile", generateDockerfile());
        files.put("docker-compose.yml", generateDockerCompose(projectName));
        files.put("gunicorn.conf.py", generateGunicornConfig());
        files.put("Makefile", generateMakefile());
    }
    
    private String generateEnvExample() {
        return "SECRET_KEY=your-secret-key\n" +
               "DEBUG=False\n" +
               "DATABASE_URL=postgresql://user:pass@localhost:5432/db\n" +
               "REDIS_URL=redis://127.0.0.1:6379/1\n";
    }
    
    private String generateManagePy(String packageName) {
        return "#!/usr/bin/env python\n" +
               "import os, sys\n" +
               "if __name__ == '__main__':\n" +
               "    os.environ.setdefault('DJANGO_SETTINGS_MODULE', '" + packageName.replace(".", "_") + ".settings')\n" +
               "    from django.core.management import execute_from_command_line\n" +
               "    execute_from_command_line(sys.argv)\n";
    }
    
    private String generateWsgi(String packageName) {
        return "import os\n" +
               "from django.core.wsgi import get_wsgi_application\n" +
               "os.environ.setdefault('DJANGO_SETTINGS_MODULE', '" + packageName.replace(".", "_") + ".settings')\n" +
               "application = get_wsgi_application()\n";
    }
    
    private String generateAppConfig(String packageName) {
        String appName = packageName.replace(".", "_");
        return "from django.apps import AppConfig\n\n" +
               "class " + capitalize(appName) + "Config(AppConfig):\n" +
               "    default_auto_field = 'django.db.models.BigAutoField'\n" +
               "    name = '" + appName + "'\n";
    }
    
    private String generateAdminConfig(List<EnhancedClass> entities) {
        StringBuilder admin = new StringBuilder();
        admin.append("from django.contrib import admin\n");
        for (EnhancedClass entity : entities) {
            admin.append("from .models import ").append(entity.getOriginalClass().getName()).append("\n");
        }
        admin.append("\n");
        for (EnhancedClass entity : entities) {
            String className = entity.getOriginalClass().getName();
            admin.append("@admin.register(").append(className).append(")\n");
            admin.append("class ").append(className).append("Admin(admin.ModelAdmin):\n");
            admin.append("    list_display = ['id', 'created_at']\n");
            admin.append("    ordering = ['-created_at']\n\n");
        }
        return admin.toString();
    }
    
    private String generateDockerfile() {
        return "FROM python:3.11-slim\n" +
               "WORKDIR /app\n" +
               "COPY requirements.txt .\n" +
               "RUN pip install -r requirements.txt\n" +
               "COPY . .\n" +
               "EXPOSE 8000\n" +
               "CMD [\"gunicorn\", \"wsgi:application\"]\n";
    }
    
    private String generateDockerCompose(String projectName) {
        return "version: '3.8'\n" +
               "services:\n" +
               "  web:\n" +
               "    build: .\n" +
               "    ports: [\"8000:8000\"]\n" +
               "    depends_on: [db, redis]\n" +
               "  db:\n" +
               "    image: postgres:15\n" +
               "    environment:\n" +
               "      POSTGRES_DB: " + projectName + "\n" +
               "  redis:\n" +
               "    image: redis:7-alpine\n";
    }
    
    private String generateGunicornConfig() {
        return "bind = \"0.0.0.0:8000\"\n" +
               "workers = 4\n" +
               "timeout = 30\n";
    }
    
    private String generateMakefile() {
        return ".PHONY: install test run\n" +
               "install:\n\tpip install -r requirements.txt\n" +
               "test:\n\tpython manage.py test\n" +
               "run:\n\tpython manage.py runserver\n";
    }
    
    private String generateSettingsWithMetadata(String packageName, String projectName, Map<String, String> metadata) {
        StringBuilder settings = new StringBuilder();
        
        // Base settings
        String baseSettings = configGenerator.generateSettings(packageName, projectName);
        settings.append(baseSettings);
        
        // Add metadata-specific settings
        if (metadata != null) {
            settings.append("\n# Metadata-driven configuration\n");
            
            String cacheStrategy = metadata.getOrDefault("cache-strategy", "redis");
            if (!"none".equals(cacheStrategy)) {
                settings.append("CACHE_ENABLED = True\n");
                settings.append("CACHE_TTL = ").append(metadata.getOrDefault("cache-ttl-seconds", "3600")).append("\n");
            }
            
            boolean auditFields = Boolean.parseBoolean(metadata.getOrDefault("audit-fields", "true"));
            if (auditFields) {
                settings.append("AUDIT_ENABLED = True\n");
            }
            
            String validationLevel = metadata.getOrDefault("validation-level", "strict");
            settings.append("VALIDATION_LEVEL = '").append(validationLevel).append("'\n");
        }
        
        return settings.toString();
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}