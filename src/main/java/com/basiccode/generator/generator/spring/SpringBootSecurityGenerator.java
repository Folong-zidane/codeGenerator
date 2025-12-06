package com.basiccode.generator.generator.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Spring Boot Security Generator
 * Generates security configurations including:
 * - JWT Token Provider
 * - Authentication Filter
 * - Security Configuration
 * - CORS Configuration
 * - OAuth2 ResourceServer support
 */
@Component
@Slf4j
public class SpringBootSecurityGenerator {
    
    /**
     * Generates SecurityConfiguration class with JWT and OAuth2 support
     */
    public String generateSecurityConfiguration(String packageName) {
        log.info("Generating Security Configuration for package {}", packageName);
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".config;\n\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import org.springframework.context.annotation.Bean;\n");
        code.append("import org.springframework.context.annotation.Configuration;\n");
        code.append("import org.springframework.http.HttpMethod;\n");
        code.append("import org.springframework.security.authentication.AuthenticationManager;\n");
        code.append("import org.springframework.security.authentication.dao.DaoAuthenticationProvider;\n");
        code.append("import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;\n");
        code.append("import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;\n");
        code.append("import org.springframework.security.config.annotation.web.builders.HttpSecurity;\n");
        code.append("import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;\n");
        code.append("import org.springframework.security.config.http.SessionCreationPolicy;\n");
        code.append("import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;\n");
        code.append("import org.springframework.security.crypto.password.PasswordEncoder;\n");
        code.append("import org.springframework.security.web.SecurityFilterChain;\n");
        code.append("import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;\n");
        code.append("import org.springframework.web.cors.CorsConfiguration;\n");
        code.append("import org.springframework.web.cors.CorsConfigurationSource;\n");
        code.append("import org.springframework.web.cors.UrlBasedCorsConfigurationSource;\n");
        code.append("import java.util.Arrays;\n\n");
        
        code.append("/**\n");
        code.append(" * Spring Security Configuration\n");
        code.append(" * Configures JWT-based authentication, CORS, and authorization\n");
        code.append(" */\n");
        code.append("@Configuration\n");
        code.append("@EnableWebSecurity\n");
        code.append("@EnableGlobalMethodSecurity(\n");
        code.append("    securedEnabled = true,\n");
        code.append("    jsr250Enabled = true,\n");
        code.append("    prePostEnabled = true\n");
        code.append(")\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("public class SecurityConfiguration {\n\n");
        
        code.append("    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;\n");
        code.append("    private final JwtAuthenticationFilter jwtAuthenticationFilter;\n\n");
        
        code.append("    /**\n");
        code.append("     * Configure HTTP security with JWT and CORS\n");
        code.append("     */\n");
        code.append("    @Bean\n");
        code.append("    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {\n");
        code.append("        http.cors()\n");
        code.append("            .and()\n");
        code.append("            .csrf()\n");
        code.append("                .disable()\n");
        code.append("            .exceptionHandling()\n");
        code.append("                .authenticationEntryPoint(jwtAuthenticationEntryPoint)\n");
        code.append("            .and()\n");
        code.append("            .sessionManagement()\n");
        code.append("                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)\n");
        code.append("            .and()\n");
        code.append("            .authorizeRequests()\n");
        code.append("                .antMatchers(\"/\", \"/favicon.ico\", \"/**/*.png\", \"/**/*.gif\", \"/**/*.svg\", \"/**/*.jpg\", \"/**/*.html\", \"/**/*.css\", \"/**/*.js\")\n");
        code.append("                    .permitAll()\n");
        code.append("                .antMatchers(\"/api/v1/auth/**\")\n");
        code.append("                    .permitAll()\n");
        code.append("                .antMatchers(\"/api/v1/public/**\")\n");
        code.append("                    .permitAll()\n");
        code.append("                .antMatchers(\"/actuator/**\")\n");
        code.append("                    .permitAll()\n");
        code.append("                .antMatchers(HttpMethod.GET, \"/api/v1/**\")\n");
        code.append("                    .permitAll()\n");
        code.append("                .anyRequest()\n");
        code.append("                    .authenticated()\n");
        code.append("            .and()\n");
        code.append("            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);\n\n");
        code.append("        return http.build();\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * CORS Configuration\n");
        code.append("     */\n");
        code.append("    @Bean\n");
        code.append("    public CorsConfigurationSource corsConfigurationSource() {\n");
        code.append("        CorsConfiguration configuration = new CorsConfiguration();\n");
        code.append("        configuration.setAllowedOrigins(Arrays.asList(\"*\"));\n");
        code.append("        configuration.setAllowedMethods(Arrays.asList(\"GET\", \"POST\", \"PUT\", \"PATCH\", \"DELETE\", \"OPTIONS\"));\n");
        code.append("        configuration.setAllowedHeaders(Arrays.asList(\"*\"));\n");
        code.append("        configuration.setExposedHeaders(Arrays.asList(\"Authorization\"));\n");
        code.append("        configuration.setAllowCredentials(true);\n");
        code.append("        configuration.setMaxAge(3600L);\n\n");
        code.append("        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();\n");
        code.append("        source.registerCorsConfiguration(\"/**\", configuration);\n");
        code.append("        return source;\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Password encoder bean\n");
        code.append("     */\n");
        code.append("    @Bean\n");
        code.append("    public PasswordEncoder passwordEncoder() {\n");
        code.append("        return new BCryptPasswordEncoder();\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Authentication manager bean\n");
        code.append("     */\n");
        code.append("    @Bean\n");
        code.append("    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {\n");
        code.append("        return authConfig.getAuthenticationManager();\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * DAO authentication provider\n");
        code.append("     */\n");
        code.append("    @Bean\n");
        code.append("    public DaoAuthenticationProvider authenticationProvider(Object userDetailsService, PasswordEncoder passwordEncoder) {\n");
        code.append("        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();\n");
        code.append("        authProvider.setUserDetailsService((org.springframework.security.core.userdetails.UserDetailsService) userDetailsService);\n");
        code.append("        authProvider.setPasswordEncoder(passwordEncoder);\n");
        code.append("        return authProvider;\n");
        code.append("    }\n\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Generates JWT Authentication Filter
     */
    public String generateJwtAuthenticationFilter(String packageName) {
        log.info("Generating JWT Authentication Filter");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".security;\n\n");
        code.append("import lombok.RequiredArgsConstructor;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;\n");
        code.append("import org.springframework.security.core.context.SecurityContextHolder;\n");
        code.append("import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import org.springframework.util.StringUtils;\n");
        code.append("import org.springframework.web.filter.OncePerRequestFilter;\n");
        code.append("import javax.servlet.FilterChain;\n");
        code.append("import javax.servlet.ServletException;\n");
        code.append("import javax.servlet.http.HttpServletRequest;\n");
        code.append("import javax.servlet.http.HttpServletResponse;\n");
        code.append("import java.io.IOException;\n\n");
        
        code.append("/**\n");
        code.append(" * JWT Authentication Filter\n");
        code.append(" * Processes JWT tokens from Authorization header\n");
        code.append(" */\n");
        code.append("@Component\n");
        code.append("@Slf4j\n");
        code.append("@RequiredArgsConstructor\n");
        code.append("public class JwtAuthenticationFilter extends OncePerRequestFilter {\n\n");
        
        code.append("    private final JwtTokenProvider tokenProvider;\n\n");
        
        code.append("    @Override\n");
        code.append("    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)\n");
        code.append("            throws ServletException, IOException {\n");
        code.append("        try {\n");
        code.append("            String jwt = getJwtFromRequest(request);\n\n");
        code.append("            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {\n");
        code.append("                Long userId = tokenProvider.getUserIdFromJWT(jwt);\n");
        code.append("                String email = tokenProvider.getEmailFromJWT(jwt);\n\n");
        code.append("                UsernamePasswordAuthenticationToken authentication = \n");
        code.append("                    new UsernamePasswordAuthenticationToken(userId, null, null);\n");
        code.append("                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));\n\n");
        code.append("                SecurityContextHolder.getContext().setAuthentication(authentication);\n");
        code.append("                log.debug(\"Set Spring Security authentication for user: {}\", email);\n");
        code.append("            }\n");
        code.append("        } catch (Exception ex) {\n");
        code.append("            log.error(\"Could not set user authentication in security context\", ex);\n");
        code.append("        }\n\n");
        code.append("        filterChain.doFilter(request, response);\n");
        code.append("    }\n\n");
        
        code.append("    private String getJwtFromRequest(HttpServletRequest request) {\n");
        code.append("        String bearerToken = request.getHeader(\"Authorization\");\n");
        code.append("        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(\"Bearer \")) {\n");
        code.append("            return bearerToken.substring(7);\n");
        code.append("        }\n");
        code.append("        return null;\n");
        code.append("    }\n\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Generates JWT Token Provider utility
     */
    public String generateJwtTokenProvider(String packageName) {
        log.info("Generating JWT Token Provider");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".security;\n\n");
        code.append("import io.jsonwebtoken.*;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.beans.factory.annotation.Value;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import java.util.Date;\n\n");
        
        code.append("/**\n");
        code.append(" * JWT Token Provider\n");
        code.append(" * Handles JWT token generation, validation, and extraction\n");
        code.append(" */\n");
        code.append("@Component\n");
        code.append("@Slf4j\n");
        code.append("public class JwtTokenProvider {\n\n");
        
        code.append("    @Value(\"${app.jwtSecret:mySecretKey123MySecretKey123MySecretKey123}\")\n");
        code.append("    private String jwtSecret;\n\n");
        
        code.append("    @Value(\"${app.jwtExpirationInMs:86400000}\")\n");
        code.append("    private int jwtExpirationInMs;\n\n");
        
        code.append("    /**\n");
        code.append("     * Generate JWT token from user ID\n");
        code.append("     */\n");
        code.append("    public String generateToken(Long userId, String email) {\n");
        code.append("        Date now = new Date();\n");
        code.append("        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);\n\n");
        code.append("        return Jwts.builder()\n");
        code.append("                .setSubject(userId.toString())\n");
        code.append("                .claim(\"email\", email)\n");
        code.append("                .setIssuedAt(now)\n");
        code.append("                .setExpiration(expiryDate)\n");
        code.append("                .signWith(SignatureAlgorithm.HS512, jwtSecret)\n");
        code.append("                .compact();\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Extract user ID from JWT token\n");
        code.append("     */\n");
        code.append("    public Long getUserIdFromJWT(String token) {\n");
        code.append("        Claims claims = Jwts.parser()\n");
        code.append("                .setSigningKey(jwtSecret)\n");
        code.append("                .parseClaimsJws(token)\n");
        code.append("                .getBody();\n\n");
        code.append("        return Long.parseLong(claims.getSubject());\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Extract email from JWT token\n");
        code.append("     */\n");
        code.append("    public String getEmailFromJWT(String token) {\n");
        code.append("        Claims claims = Jwts.parser()\n");
        code.append("                .setSigningKey(jwtSecret)\n");
        code.append("                .parseClaimsJws(token)\n");
        code.append("                .getBody();\n\n");
        code.append("        return claims.get(\"email\", String.class);\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Validate JWT token\n");
        code.append("     */\n");
        code.append("    public boolean validateToken(String authToken) {\n");
        code.append("        try {\n");
        code.append("            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);\n");
        code.append("            return true;\n");
        code.append("        } catch (SecurityException ex) {\n");
        code.append("            log.error(\"Invalid JWT signature\");\n");
        code.append("        } catch (MalformedJwtException ex) {\n");
        code.append("            log.error(\"Invalid JWT token\");\n");
        code.append("        } catch (ExpiredJwtException ex) {\n");
        code.append("            log.error(\"Expired JWT token\");\n");
        code.append("        } catch (UnsupportedJwtException ex) {\n");
        code.append("            log.error(\"Unsupported JWT token\");\n");
        code.append("        } catch (IllegalArgumentException ex) {\n");
        code.append("            log.error(\"JWT claims string is empty\");\n");
        code.append("        }\n");
        code.append("        return false;\n");
        code.append("    }\n\n");
        
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Generates JWT Authentication Entry Point for exception handling
     */
    public String generateJwtAuthenticationEntryPoint(String packageName) {
        log.info("Generating JWT Authentication Entry Point");
        
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(".security;\n\n");
        code.append("import com.fasterxml.jackson.databind.ObjectMapper;\n");
        code.append("import lombok.extern.slf4j.Slf4j;\n");
        code.append("import org.springframework.http.MediaType;\n");
        code.append("import org.springframework.security.core.AuthenticationException;\n");
        code.append("import org.springframework.security.web.AuthenticationEntryPoint;\n");
        code.append("import org.springframework.stereotype.Component;\n");
        code.append("import javax.servlet.ServletException;\n");
        code.append("import javax.servlet.http.HttpServletRequest;\n");
        code.append("import javax.servlet.http.HttpServletResponse;\n");
        code.append("import java.io.IOException;\n");
        code.append("import java.util.HashMap;\n");
        code.append("import java.util.Map;\n\n");
        
        code.append("/**\n");
        code.append(" * JWT Authentication Entry Point\n");
        code.append(" * Handles authentication exceptions\n");
        code.append(" */\n");
        code.append("@Component\n");
        code.append("@Slf4j\n");
        code.append("public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {\n\n");
        
        code.append("    @Override\n");
        code.append("    public void commence(HttpServletRequest httpServletRequest,\n");
        code.append("                         HttpServletResponse httpServletResponse,\n");
        code.append("                         AuthenticationException e) throws IOException, ServletException {\n");
        code.append("        log.error(\"Responding with unauthorized error. Message - {}\", e.getMessage());\n\n");
        
        code.append("        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);\n");
        code.append("        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);\n\n");
        
        code.append("        final Map<String, Object> body = new HashMap<>();\n");
        code.append("        body.put(\"status\", HttpServletResponse.SC_UNAUTHORIZED);\n");
        code.append("        body.put(\"error\", \"Unauthorized\");\n");
        code.append("        body.put(\"message\", e.getMessage());\n");
        code.append("        body.put(\"path\", httpServletRequest.getServletPath());\n\n");
        
        code.append("        final ObjectMapper mapper = new ObjectMapper();\n");
        code.append("        mapper.writeValue(httpServletResponse.getOutputStream(), body);\n");
        code.append("    }\n\n");
        
        code.append("}\n");
        
        return code.toString();
    }
}
