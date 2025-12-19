package com.basiccode.generator.config;

import com.basiccode.generator.generator.csharp.CSharpGeneratorFactory;
import com.basiccode.generator.generator.django.DjangoGeneratorFactory;
import com.basiccode.generator.generator.php.PhpGeneratorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 🔧 Configuration automatique des générateurs
 * Enregistre toutes les factories disponibles au démarrage
 */
@Component
@Slf4j
public class GeneratorConfiguration implements CommandLineRunner {
    
    @Autowired
    private FrameworkRegistry frameworkRegistry;
    
    @Override
    public void run(String... args) {
        registerAvailableFactories();
    }
    
    /**
     * Enregistre toutes les factories de générateurs disponibles
     */
    private void registerAvailableFactories() {
        log.info("🚀 Registering available generator factories...");
        
        // C# .NET Core
        try {
            frameworkRegistry.registerFactory("csharp", new CSharpGeneratorFactory());
            frameworkRegistry.registerFactory("dotnet", new CSharpGeneratorFactory());
            frameworkRegistry.registerFactory("c#", new CSharpGeneratorFactory());
        } catch (Exception e) {
            log.warn("⚠️ Failed to register C# factory: {}", e.getMessage());
        }
        
        // Django Python
        try {
            DjangoGeneratorFactory djangoFactory = new DjangoGeneratorFactory();
            frameworkRegistry.registerFactory("django", new DjangoLanguageGeneratorFactoryAdapter(djangoFactory));
        } catch (Exception e) {
            log.warn("⚠️ Failed to register Django factory: {}", e.getMessage());
        }
        
        // PHP
        try {
            PhpGeneratorFactory phpFactory = new PhpGeneratorFactory();
            frameworkRegistry.registerFactory("php", new PhpLanguageGeneratorFactoryAdapter(phpFactory));
        } catch (Exception e) {
            log.warn("⚠️ Failed to register PHP factory: {}", e.getMessage());
        }
        
        log.info("✅ Generator factories registration completed. Supported languages: {}", 
                String.join(", ", frameworkRegistry.getSupportedLanguages()));
    }
    
    /**
     * Adaptateur pour DjangoGeneratorFactory vers LanguageGeneratorFactory
     */
    private static class DjangoLanguageGeneratorFactoryAdapter implements com.basiccode.generator.generator.LanguageGeneratorFactory {
        private final DjangoGeneratorFactory djangoFactory;
        
        public DjangoLanguageGeneratorFactoryAdapter(DjangoGeneratorFactory djangoFactory) {
            this.djangoFactory = djangoFactory;
        }
        
        @Override
        public com.basiccode.generator.generator.IEntityGenerator createEntityGenerator() {
            return djangoFactory.createEntityGenerator();
        }
        
        @Override
        public com.basiccode.generator.generator.IRepositoryGenerator createRepositoryGenerator() {
            return new MockRepositoryGenerator("django");
        }
        
        @Override
        public com.basiccode.generator.generator.IServiceGenerator createServiceGenerator() {
            return djangoFactory.createServiceGenerator();
        }
        
        @Override
        public com.basiccode.generator.generator.IControllerGenerator createControllerGenerator() {
            return djangoFactory.createControllerGenerator();
        }
        
        @Override
        public com.basiccode.generator.generator.IMigrationGenerator createMigrationGenerator() {
            return new MockMigrationGenerator("django");
        }
        
        @Override
        public com.basiccode.generator.generator.IFileWriter createFileWriter() {
            return djangoFactory.createFileWriter();
        }
        
        @Override
        public String getLanguage() {
            return djangoFactory.getLanguage();
        }
        
        @Override
        public Framework getSupportedFramework() {
            return Framework.DJANGO;
        }
    }
    
    /**
     * Adaptateur pour PhpGeneratorFactory vers LanguageGeneratorFactory
     */
    private static class PhpLanguageGeneratorFactoryAdapter implements com.basiccode.generator.generator.LanguageGeneratorFactory {
        private final PhpGeneratorFactory phpFactory;
        
        public PhpLanguageGeneratorFactoryAdapter(PhpGeneratorFactory phpFactory) {
            this.phpFactory = phpFactory;
        }
        
        @Override
        public com.basiccode.generator.generator.IEntityGenerator createEntityGenerator() {
            return phpFactory.createEntityGenerator();
        }
        
        @Override
        public com.basiccode.generator.generator.IRepositoryGenerator createRepositoryGenerator() {
            return new MockRepositoryGenerator("php");
        }
        
        @Override
        public com.basiccode.generator.generator.IServiceGenerator createServiceGenerator() {
            return phpFactory.createServiceGenerator();
        }
        
        @Override
        public com.basiccode.generator.generator.IControllerGenerator createControllerGenerator() {
            return phpFactory.createControllerGenerator();
        }
        
        @Override
        public com.basiccode.generator.generator.IMigrationGenerator createMigrationGenerator() {
            return new MockMigrationGenerator("php");
        }
        
        @Override
        public com.basiccode.generator.generator.IFileWriter createFileWriter() {
            return phpFactory.createFileWriter();
        }
        
        @Override
        public String getLanguage() {
            return phpFactory.getLanguage();
        }
        
        @Override
        public Framework getSupportedFramework() {
            return Framework.LARAVEL;
        }
    }
    
    // Mock generators pour les adaptateurs
    private static class MockRepositoryGenerator implements com.basiccode.generator.generator.IRepositoryGenerator {
        private final String language;
        
        public MockRepositoryGenerator(String language) { this.language = language; }
        
        @Override
        public String generateRepository(com.basiccode.generator.model.EnhancedClass enhancedClass, String packageName) {
            return "// Mock " + language + " Repository: " + enhancedClass.getOriginalClass().getName() + "Repository";
        }
        
        @Override
        public String getFileExtension() {
            return com.basiccode.generator.util.FileExtensionUtil.getExtensionForLanguage(language);
        }
        
        @Override
        public String getRepositoryDirectory() {
            return com.basiccode.generator.util.FileExtensionUtil.getRepositoryDirectoryForLanguage(language);
        }
    }
    
    private static class MockMigrationGenerator implements com.basiccode.generator.generator.IMigrationGenerator {
        private final String language;
        
        public MockMigrationGenerator(String language) { this.language = language; }
        
        @Override
        public String generateMigration(java.util.List<com.basiccode.generator.model.EnhancedClass> enhancedClasses, String packageName) {
            return "// Mock " + language + " Migration for " + enhancedClasses.size() + " entities";
        }
        
        @Override
        public String getFileExtension() {
            return ".sql";
        }
    }
}