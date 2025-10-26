package com.basiccode.generator.enhanced;

import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;

public class SecurityGenerator {
    
    public JavaFile generateSecurityConfig(String basePackage) {
        TypeSpec securityConfig = TypeSpec.classBuilder("SecurityConfig")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.context.annotation", "Configuration"))
            .addAnnotation(ClassName.get("org.springframework.security.config.annotation.web.configuration", "EnableWebSecurity"))
            .addMethod(generateSecurityFilterChain())
            .addMethod(generatePasswordEncoder())
            .addMethod(generateCorsConfiguration())
            .build();
        
        return JavaFile.builder(basePackage + ".config", securityConfig)
            .addFileComment("Generated Security Configuration")
            .build();
    }
    
    private MethodSpec generateSecurityFilterChain() {
        return MethodSpec.methodBuilder("filterChain")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.context.annotation", "Bean"))
            .addParameter(ClassName.get("org.springframework.security.config.annotation.web.builders", "HttpSecurity"), "http")
            .addException(ClassName.get("java.lang", "Exception"))
            .returns(ClassName.get("org.springframework.security.web", "SecurityFilterChain"))
            .addStatement("return http.csrf().disable()")
            .addStatement("    .authorizeHttpRequests(auth -> auth")
            .addStatement("        .requestMatchers(\"/api/auth/**\").permitAll()")
            .addStatement("        .requestMatchers(\"/api/public/**\").permitAll()")
            .addStatement("        .anyRequest().authenticated())")
            .addStatement("    .sessionManagement(session -> session")
            .addStatement("        .sessionCreationPolicy($T.STATELESS))", 
                ClassName.get("org.springframework.security.config.http", "SessionCreationPolicy"))
            .addStatement("    .build()")
            .build();
    }
    
    private MethodSpec generatePasswordEncoder() {
        return MethodSpec.methodBuilder("passwordEncoder")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.context.annotation", "Bean"))
            .returns(ClassName.get("org.springframework.security.crypto.password", "PasswordEncoder"))
            .addStatement("return new $T()", 
                ClassName.get("org.springframework.security.crypto.bcrypt", "BCryptPasswordEncoder"))
            .build();
    }
    
    private MethodSpec generateCorsConfiguration() {
        return MethodSpec.methodBuilder("corsConfigurationSource")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.context.annotation", "Bean"))
            .returns(ClassName.get("org.springframework.web.cors", "CorsConfigurationSource"))
            .addStatement("$T configuration = new $T()", 
                ClassName.get("org.springframework.web.cors", "CorsConfiguration"),
                ClassName.get("org.springframework.web.cors", "CorsConfiguration"))
            .addStatement("configuration.setAllowedOriginPatterns($T.singletonList(\"*\"))", 
                ClassName.get("java.util", "Collections"))
            .addStatement("configuration.setAllowedMethods($T.asList(\"GET\", \"POST\", \"PUT\", \"DELETE\", \"OPTIONS\"))", 
                ClassName.get("java.util", "Arrays"))
            .addStatement("configuration.setAllowedHeaders($T.singletonList(\"*\"))", 
                ClassName.get("java.util", "Collections"))
            .addStatement("configuration.setAllowCredentials(true)")
            .addStatement("$T source = new $T()", 
                ClassName.get("org.springframework.web.cors", "UrlBasedCorsConfigurationSource"),
                ClassName.get("org.springframework.web.cors", "UrlBasedCorsConfigurationSource"))
            .addStatement("source.registerCorsConfiguration(\"/**\", configuration)")
            .addStatement("return source")
            .build();
    }
}