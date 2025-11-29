package com.basiccode.generator.initializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InitializerTest {
    
    @Autowired
    private InitializerService initializerService;
    
    @Autowired
    private SpringBootInitializer springBootInitializer;
    
    @Autowired
    private DjangoInitializer djangoInitializer;
    
    @Autowired
    private PhpInitializer phpInitializer;
    
    @Test
    public void testSupportedLanguages() {
        Map<String, String> languages = initializerService.getSupportedLanguages();
        
        assertTrue(languages.containsKey("java"));
        assertTrue(languages.containsKey("django"));
        assertTrue(languages.containsKey("python"));
        assertTrue(languages.containsKey("typescript"));
        assertTrue(languages.containsKey("php"));
        
        System.out.println("✅ Langages supportés: " + languages);
    }
    
    @Test
    public void testSpringBootInitializer() {
        assertEquals("java", springBootInitializer.getLanguage());
        assertNotNull(springBootInitializer.getLatestVersion());
        
        Path projectPath = springBootInitializer.initializeProject("test-spring", "com.test");
        assertNotNull(projectPath);
        
        System.out.println("✅ Spring Boot initializer: " + springBootInitializer.getLatestVersion());
    }
    
    @Test
    public void testDjangoInitializer() {
        assertEquals("django", djangoInitializer.getLanguage());
        assertNotNull(djangoInitializer.getLatestVersion());
        
        Path projectPath = djangoInitializer.initializeProject("test-django", "com.test");
        assertNotNull(projectPath);
        
        System.out.println("✅ Django initializer: " + djangoInitializer.getLatestVersion());
    }
    
    @Test
    public void testPhpInitializer() {
        assertEquals("php", phpInitializer.getLanguage());
        assertNotNull(phpInitializer.getLatestVersion());
        
        Path projectPath = phpInitializer.initializeProject("test-php", "com.test");
        assertNotNull(projectPath);
        
        System.out.println("✅ PHP initializer: " + phpInitializer.getLatestVersion());
    }
    
    @Test
    public void testLanguageSupport() {
        assertTrue(initializerService.isLanguageSupported("java"));
        assertTrue(initializerService.isLanguageSupported("django"));
        assertTrue(initializerService.isLanguageSupported("php"));
        assertFalse(initializerService.isLanguageSupported("unknown"));
        
        System.out.println("✅ Support des langages validé");
    }
}