package com.basiccode.generator.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InitializerService {
    
    private final Map<String, ProjectInitializer> initializers;
    private final TemplateManager templateManager;
    
    @Autowired
    public InitializerService(List<ProjectInitializer> initializerList, TemplateManager templateManager) {
        this.initializers = initializerList.stream()
                .collect(Collectors.toMap(ProjectInitializer::getLanguage, Function.identity()));
        this.templateManager = templateManager;
    }
    
    /**
     * Initialize project with latest framework version and merge generated code
     */
    public Path createProject(String language, String projectName, String packageName, Path generatedCodePath) {
        ProjectInitializer initializer = initializers.get(language);
        if (initializer == null) {
            throw new IllegalArgumentException("Unsupported language: " + language);
        }
        
        // Get or download latest template
        Path templatePath = templateManager.getTemplate(language, initializer.getLatestVersion());
        
        // Initialize fresh project
        Path projectPath = initializer.initializeProject(projectName, packageName);
        
        // Merge generated code
        initializer.mergeGeneratedCode(projectPath, generatedCodePath);
        
        return projectPath;
    }
    
    /**
     * Get supported languages with their versions
     */
    public Map<String, String> getSupportedLanguages() {
        return initializers.entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> entry.getValue().getLatestVersion()
                ));
    }
    
    /**
     * Check if language is supported
     */
    public boolean isLanguageSupported(String language) {
        return initializers.containsKey(language);
    }
}