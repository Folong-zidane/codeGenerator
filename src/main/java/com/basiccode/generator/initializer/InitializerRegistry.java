package com.basiccode.generator.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InitializerRegistry {
    
    private final Map<String, ProjectInitializer> initializers = new ConcurrentHashMap<>();
    
    @Autowired
    private List<ProjectInitializer> initializerList;
    
    @PostConstruct
    public void registerInitializers() {
        initializerList.forEach(initializer -> 
            initializers.put(initializer.getLanguage(), initializer));
    }
    
    public ProjectInitializer getInitializer(String language) {
        ProjectInitializer initializer = initializers.get(language);
        if (initializer == null) {
            throw new IllegalArgumentException("No initializer found for language: " + language);
        }
        return initializer;
    }
    
    public boolean isSupported(String language) {
        return initializers.containsKey(language);
    }
    
    public Map<String, String> getSupportedLanguagesWithVersions() {
        Map<String, String> result = new ConcurrentHashMap<>();
        initializers.forEach((lang, init) -> 
            result.put(lang, init.getLatestVersion()));
        return result;
    }
    
    // Legacy compatibility methods
    public boolean isAvailable(Object framework) {
        return framework != null && isSupported(framework.toString().toLowerCase());
    }
    
    public Map<String, String> getInitializerStatus() {
        return getSupportedLanguagesWithVersions();
    }
    
    public Map<String, String> getAvailableFrameworks() {
        return getSupportedLanguagesWithVersions();
    }
}