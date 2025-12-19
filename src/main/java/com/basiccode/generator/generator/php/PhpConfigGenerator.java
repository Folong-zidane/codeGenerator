package com.basiccode.generator.generator.php;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * PhpConfigGenerator - Generates Laravel configuration files
 * 
 * Phase 2 Week 2 - CONFIGURATION
 * 
 * Generates:
 * - config/database.php - Database configuration
 * - config/cache.php - Caching configuration
 * - config/queue.php - Queue configuration
 * - .env - Environment variables
 * - .env.example - Example environment file
 * - config/app.php - Application configuration
 * 
 * @version 1.0.0
 * @since PHP Phase 2
 */
@Slf4j
@Component
public class PhpConfigGenerator {
    
    private String projectName = "LaravelApp";
    
    /**
     * Generate database configuration
     */
    public String generateDatabaseConfig(String defaultConnection) {
        if (defaultConnection == null) {
            defaultConnection = "sqlite";
        }
        
        return String.format(
            "<?php\n\n" +
            "return [\n" +
            "    'default' => env('DB_CONNECTION', '%s'),\n\n" +
            "    'connections' => [\n" +
            "        'sqlite' => [\n" +
            "            'driver' => 'sqlite',\n" +
            "            'database' => env('DB_DATABASE', database_path('database.sqlite')),\n" +
            "        ],\n" +
            "        'mysql' => [\n" +
            "            'driver' => 'mysql',\n" +
            "            'host' => env('DB_HOST', '127.0.0.1'),\n" +
            "            'port' => env('DB_PORT', 3306),\n" +
            "            'database' => env('DB_DATABASE', 'laravel'),\n" +
            "            'username' => env('DB_USERNAME', 'root'),\n" +
            "            'password' => env('DB_PASSWORD', ''),\n" +
            "        ],\n" +
            "    ],\n" +
            "];\n",
            defaultConnection
        );
    }
    
    /**
     * Generate cache configuration
     */
    public String generateCacheConfig() {
        return "<?php\n\n" +
            "return [\n" +
            "    'default' => env('CACHE_DRIVER', 'file'),\n" +
            "    'stores' => [\n" +
            "        'array' => ['driver' => 'array'],\n" +
            "        'file' => ['driver' => 'file'],\n" +
            "        'redis' => ['driver' => 'redis'],\n" +
            "    ],\n" +
            "];\n";
    }
    
    /**
     * Generate queue configuration
     */
    public String generateQueueConfig() {
        return "<?php\n\n" +
            "return [\n" +
            "    'default' => env('QUEUE_CONNECTION', 'sync'),\n" +
            "    'connections' => [\n" +
            "        'sync' => ['driver' => 'sync'],\n" +
            "        'database' => ['driver' => 'database'],\n" +
            "    ],\n" +
            "];\n";
    }
    
    /**
     * Generate environment file (.env)
     */
    public String generateEnvFile(String dbConnection, Map<String, String> additionalVars) {
        StringBuilder env = new StringBuilder();
        
        env.append("APP_NAME=\"").append(projectName).append("\"\n");
        env.append("APP_ENV=local\n");
        env.append("APP_KEY=base64:KEY_PLACEHOLDER\n");
        env.append("APP_DEBUG=true\n");
        env.append("APP_URL=http://localhost\n\n");
        
        env.append("LOG_CHANNEL=stack\n");
        env.append("LOG_LEVEL=debug\n\n");
        
        // Database configuration
        if ("mysql".equals(dbConnection)) {
            env.append("DB_CONNECTION=mysql\n");
            env.append("DB_HOST=127.0.0.1\n");
            env.append("DB_PORT=3306\n");
            env.append("DB_DATABASE=").append(projectName.toLowerCase()).append("\n");
            env.append("DB_USERNAME=root\n");
            env.append("DB_PASSWORD=\n");
        } else if ("pgsql".equals(dbConnection)) {
            env.append("DB_CONNECTION=pgsql\n");
            env.append("DB_HOST=127.0.0.1\n");
            env.append("DB_PORT=5432\n");
            env.append("DB_DATABASE=").append(projectName.toLowerCase()).append("\n");
            env.append("DB_USERNAME=postgres\n");
            env.append("DB_PASSWORD=\n");
        } else {
            env.append("DB_CONNECTION=sqlite\n");
            env.append("DB_DATABASE=").append(projectName.toLowerCase()).append(".sqlite\n");
        }
        
        env.append("\n");
        env.append("BROADCAST_DRIVER=log\n");
        env.append("CACHE_DRIVER=file\n");
        env.append("FILESYSTEM_DISK=local\n");
        env.append("QUEUE_CONNECTION=sync\n");
        env.append("SESSION_DRIVER=file\n");
        env.append("SESSION_LIFETIME=120\n\n");
        
        env.append("MEMCACHED_HOST=127.0.0.1\n");
        env.append("REDIS_HOST=127.0.0.1\n");
        env.append("REDIS_PASSWORD=null\n");
        env.append("REDIS_PORT=6379\n\n");
        
        env.append("MAIL_MAILER=log\n");
        env.append("MAIL_HOST=127.0.0.1\n");
        env.append("MAIL_PORT=2525\n");
        env.append("MAIL_USERNAME=null\n");
        env.append("MAIL_PASSWORD=null\n");
        env.append("MAIL_ENCRYPTION=null\n");
        env.append("MAIL_FROM_ADDRESS=\"hello@example.com\"\n");
        env.append("MAIL_FROM_NAME=\"").append(projectName).append("\"\n\n");
        
        env.append("AWS_ACCESS_KEY_ID=\n");
        env.append("AWS_SECRET_ACCESS_KEY=\n");
        env.append("AWS_DEFAULT_REGION=us-east-1\n");
        env.append("AWS_BUCKET=\n");
        env.append("AWS_USE_PATH_STYLE_ENDPOINT=false\n");
        
        if (additionalVars != null) {
            env.append("\n# Custom Variables\n");
            additionalVars.forEach((key, value) -> 
                env.append(key).append("=").append(value).append("\n")
            );
        }
        
        return env.toString();
    }
    
    /**
     * Generate example environment file
     */
    public String generateEnvExampleFile() {
        return generateEnvFile("sqlite", null)
            .replace("APP_KEY=base64:KEY_PLACEHOLDER", "APP_KEY=");
    }
    
    /**
     * Generate application configuration
     */
    public String generateAppConfig() {
        return String.format(
            "<?php\n\n" +
            "return [\n" +
            "    'name' => env('APP_NAME', '%s'),\n" +
            "    'env' => env('APP_ENV', 'production'),\n" +
            "    'debug' => (bool) env('APP_DEBUG', false),\n" +
            "    'url' => env('APP_URL', 'http://localhost'),\n" +
            "    'timezone' => 'UTC',\n" +
            "    'locale' => 'en',\n" +
            "    'key' => env('APP_KEY'),\n" +
            "    'cipher' => 'AES-256-CBC',\n" +
            "];\n",
            projectName
        );
    }
    
    /**
     * Generate all configuration files as Map
     */
    public Map<String, String> generateAllConfigs(String dbConnection) {
        return Map.of(
            "database", generateDatabaseConfig(dbConnection),
            "cache", generateCacheConfig(),
            "queue", generateQueueConfig(),
            "app", generateAppConfig()
        );
    }
}
