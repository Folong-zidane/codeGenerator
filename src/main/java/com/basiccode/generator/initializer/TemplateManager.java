package com.basiccode.generator.initializer;

import org.springframework.stereotype.Service;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class TemplateManager {
    
    private static final String TEMPLATES_DIR = "templates";
    private static final Duration CACHE_TTL = Duration.ofHours(24); // 24h cache
    
    private final Map<String, TemplateCache> templateCache = new ConcurrentHashMap<>();
    
    public Path getTemplate(String language, String version) {
        String key = language + ":" + version;
        TemplateCache cache = templateCache.get(key);
        
        if (cache != null && !cache.isExpired()) {
            return cache.getPath();
        }
        
        // Download/Update template
        Path templatePath = downloadTemplate(language, version);
        templateCache.put(key, new TemplateCache(templatePath, LocalDateTime.now()));
        
        return templatePath;
    }
    
    private Path downloadTemplate(String language, String version) {
        // Implementation will download from registry or Git
        return Paths.get(TEMPLATES_DIR, language, version);
    }
    
    private static class TemplateCache {
        private final Path path;
        private final LocalDateTime timestamp;
        
        public TemplateCache(Path path, LocalDateTime timestamp) {
            this.path = path;
            this.timestamp = timestamp;
        }
        
        public boolean isExpired() {
            return Duration.between(timestamp, LocalDateTime.now()).compareTo(CACHE_TTL) > 0;
        }
        
        public Path getPath() { return path; }
    }
}