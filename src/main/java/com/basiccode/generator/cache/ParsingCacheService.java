package com.basiccode.generator.cache;

import com.basiccode.generator.model.Diagram;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ParsingCacheService {
    
    private final Cache<String, Diagram> parseCache;
    private final ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();
    
    public ParsingCacheService() {
        this.parseCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofHours(1))
            .recordStats()
            .build();
    }
    
    public Diagram getCachedOrParse(String content, String diagramType, ParsingFunction parser) throws Exception {
        String cacheKey = generateCacheKey(content, diagramType);
        
        // Try cache first
        Diagram cached = parseCache.getIfPresent(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        // Use lock to prevent duplicate parsing
        Object lock = lockMap.computeIfAbsent(cacheKey, k -> new Object());
        
        synchronized (lock) {
            // Double-check after acquiring lock
            cached = parseCache.getIfPresent(cacheKey);
            if (cached != null) {
                return cached;
            }
            
            // Parse and cache
            Diagram result = parser.parse(content);
            parseCache.put(cacheKey, result);
            
            return result;
        }
    }
    
    private String generateCacheKey(String content, String diagramType) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String combined = diagramType + ":" + content;
            byte[] hash = md.digest(combined.getBytes());
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return String.valueOf(content.hashCode()) + "_" + diagramType;
        }
    }
    
    public void clearCache() {
        parseCache.invalidateAll();
        lockMap.clear();
    }
    
    public long getCacheSize() {
        return parseCache.estimatedSize();
    }
    
    @FunctionalInterface
    public interface ParsingFunction {
        Diagram parse(String content) throws Exception;
    }
}