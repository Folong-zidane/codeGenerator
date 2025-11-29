package com.basiccode.generator.config;

import com.basiccode.generator.generator.LanguageGeneratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry for language generator factories
 * Implements Registry pattern for factory resolution
 */
@Component
public class FrameworkRegistry {
    
    private final Map<String, LanguageGeneratorFactory> factoriesByLanguage;
    private final Map<Framework, LanguageGeneratorFactory> factoriesByFramework;
    
    @Autowired
    public FrameworkRegistry(List<LanguageGeneratorFactory> factories) {
        this.factoriesByLanguage = factories.stream()
            .collect(Collectors.toMap(
                LanguageGeneratorFactory::getLanguage,
                Function.identity()
            ));
        
        this.factoriesByFramework = factories.stream()
            .collect(Collectors.toMap(
                LanguageGeneratorFactory::getSupportedFramework,
                Function.identity()
            ));
    }
    
    /**
     * Get factory by language identifier
     */
    public LanguageGeneratorFactory factoryFor(String language) {
        LanguageGeneratorFactory factory = factoriesByLanguage.get(language.toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("No generator factory available for language: " + language);
        }
        return factory;
    }
    
    /**
     * Get factory by framework
     */
    public LanguageGeneratorFactory factoryFor(Framework framework) {
        LanguageGeneratorFactory factory = factoriesByFramework.get(framework);
        if (factory == null) {
            throw new IllegalArgumentException("No generator factory available for framework: " + framework);
        }
        return factory;
    }
    
    /**
     * Get all available languages
     */
    public List<String> getAvailableLanguages() {
        return List.copyOf(factoriesByLanguage.keySet());
    }
    
    /**
     * Get all available frameworks
     */
    public List<Framework> getAvailableFrameworks() {
        return List.copyOf(factoriesByFramework.keySet());
    }
}