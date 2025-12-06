package com.basiccode.generator.controller;

import com.basiccode.generator.config.FrameworkRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {
    
    private final FrameworkRegistry frameworkRegistry;
    
    @GetMapping("/languages")
    public Map<String, Object> getAvailableLanguages() {
        return Map.of(
            "availableLanguages", frameworkRegistry.getAvailableLanguages(),
            "availableFrameworks", frameworkRegistry.getAvailableFrameworks()
        );
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "OK", "message", "API is running");
    }
}