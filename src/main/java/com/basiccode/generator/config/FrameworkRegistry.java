package com.basiccode.generator.config;

import com.basiccode.generator.generator.LanguageGeneratorFactory;
import com.basiccode.generator.generator.FileWriterFactory;
import com.basiccode.generator.util.FileExtensionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * 🏭 Registre des factories de génération par langage/framework
 * Centralise l'accès aux générateurs spécialisés
 */
@Service
@Slf4j
public class FrameworkRegistry {
    
    private final Map<String, LanguageGeneratorFactory> factories = new HashMap<>();
    
    /**
     * Enregistre une factory pour un langage donné
     */
    public void registerFactory(String language, LanguageGeneratorFactory factory) {
        factories.put(language.toLowerCase(), factory);
        log.info("🏭 Registered factory for language: {}", language);
    }
    
    /**
     * Obtient la factory pour un langage donné
     */
    public LanguageGeneratorFactory factoryFor(String language) {
        LanguageGeneratorFactory factory = factories.get(language.toLowerCase());
        
        if (factory == null) {
            log.warn("⚠️ No factory found for language: {}, creating mock factory", language);
            return createMockFactory(language);
        }
        
        return factory;
    }
    
    /**
     * Vérifie si un langage est supporté
     */
    public boolean isSupported(String language) {
        return factories.containsKey(language.toLowerCase());
    }
    
    /**
     * Obtient la liste des langages supportés
     */
    public String[] getSupportedLanguages() {
        return factories.keySet().toArray(new String[0]);
    }
    
    /**
     * Crée une factory mock pour les langages non encore implémentés
     */
    private LanguageGeneratorFactory createMockFactory(String language) {
        return new MockLanguageGeneratorFactory(language);
    }
    
    /**
     * Factory mock pour les tests et le développement
     */
    private static class MockLanguageGeneratorFactory implements LanguageGeneratorFactory {
        private final String language;
        
        public MockLanguageGeneratorFactory(String language) {
            this.language = language;
        }
        
        @Override
        public com.basiccode.generator.generator.IEntityGenerator createEntityGenerator() {
            return new MockEntityGenerator(language);
        }
        
        @Override
        public com.basiccode.generator.generator.IRepositoryGenerator createRepositoryGenerator() {
            return new MockRepositoryGenerator(language);
        }
        
        @Override
        public com.basiccode.generator.generator.IServiceGenerator createServiceGenerator() {
            return new MockServiceGenerator(language);
        }
        
        @Override
        public com.basiccode.generator.generator.IControllerGenerator createControllerGenerator() {
            return new MockControllerGenerator(language);
        }
        
        @Override
        public com.basiccode.generator.generator.IMigrationGenerator createMigrationGenerator() {
            return new MockMigrationGenerator(language);
        }
        
        @Override
        public String getLanguage() {
            return language;
        }
        
        @Override
        public Framework getSupportedFramework() {
            return Framework.SPRING_BOOT;
        }
        
        @Override
        public com.basiccode.generator.generator.IFileWriter createFileWriter() {
            try {
                return FileWriterFactory.createFileWriter(language);
            } catch (IllegalArgumentException e) {
                log.warn("⚠️ No real FileWriter found for language: {}, using mock", language);
                return new MockFileWriter(language);
            }
        }
    }
    
    // ========== GÉNÉRATEURS MOCK ==========
    
    private static class MockEntityGenerator implements com.basiccode.generator.generator.IEntityGenerator {
        private final String language;
        
        public MockEntityGenerator(String language) { this.language = language; }
        
        @Override
        public String generateEntity(com.basiccode.generator.model.EnhancedClass enhancedClass, String packageName) {
            return "// Mock " + language + " Entity: " + enhancedClass.getOriginalClass().getName();
        }
        
        @Override
        public String generateStateEnum(com.basiccode.generator.model.EnhancedClass enhancedClass, String packageName) {
            return "// Mock " + language + " State Enum: " + enhancedClass.getStateEnum().getName();
        }
        
        @Override
        public String getFileExtension() {
            return FileExtensionUtil.getExtensionForLanguage(language);
        }
        
        @Override
        public String getEntityDirectory() {
            return FileExtensionUtil.getEntityDirectoryForLanguage(language);
        }
    }
    
    private static class MockRepositoryGenerator implements com.basiccode.generator.generator.IRepositoryGenerator {
        private final String language;
        
        public MockRepositoryGenerator(String language) { this.language = language; }
        
        @Override
        public String generateRepository(com.basiccode.generator.model.EnhancedClass enhancedClass, String packageName) {
            return "// Mock " + language + " Repository: " + enhancedClass.getOriginalClass().getName() + "Repository";
        }
        
        @Override
        public String getFileExtension() {
            return FileExtensionUtil.getExtensionForLanguage(language);
        }
        
        @Override
        public String getRepositoryDirectory() {
            return FileExtensionUtil.getRepositoryDirectoryForLanguage(language);
        }
    }
    
    private static class MockServiceGenerator implements com.basiccode.generator.generator.IServiceGenerator {
        private final String language;
        
        public MockServiceGenerator(String language) { this.language = language; }
        
        @Override
        public String generateService(com.basiccode.generator.model.EnhancedClass enhancedClass, String packageName) {
            return "// Mock " + language + " Service: " + enhancedClass.getOriginalClass().getName() + "Service";
        }
        
        @Override
        public String getFileExtension() {
            return FileExtensionUtil.getExtensionForLanguage(language);
        }
        
        @Override
        public String getServiceDirectory() {
            return FileExtensionUtil.getServiceDirectoryForLanguage(language);
        }
    }
    
    private static class MockControllerGenerator implements com.basiccode.generator.generator.IControllerGenerator {
        private final String language;
        
        public MockControllerGenerator(String language) { this.language = language; }
        
        @Override
        public String generateController(com.basiccode.generator.model.EnhancedClass enhancedClass, String packageName) {
            return "// Mock " + language + " Controller: " + enhancedClass.getOriginalClass().getName() + "Controller";
        }
        
        @Override
        public String getFileExtension() {
            return FileExtensionUtil.getExtensionForLanguage(language);
        }
        
        @Override
        public String getControllerDirectory() {
            return FileExtensionUtil.getControllerDirectoryForLanguage(language);
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
    
    private static class MockFileWriter implements com.basiccode.generator.generator.IFileWriter {
        private final String language;
        
        public MockFileWriter(String language) { this.language = language; }
        
        @Override
        public void writeFiles(java.util.Map<String, String> files, String baseDirectory) {
            log.info("📝 Mock FileWriter for {}: would write {} files to {}", language, files.size(), baseDirectory);
        }
        
        @Override
        public String getFileExtension() {
            return FileExtensionUtil.getExtensionForLanguage(language);
        }
    }
}