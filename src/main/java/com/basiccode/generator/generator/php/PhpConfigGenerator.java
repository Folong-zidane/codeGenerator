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
        
        return String.format("""
            <?php
            
            return [
                /*
                |--------------------------------------------------------------------------
                | Default Database Connection Name
                |--------------------------------------------------------------------------
                |
                | Here you may specify which of the database connections below you wish
                | to use as your default connection for all database work.
                |
                */
                'default' => env('DB_CONNECTION', '%s'),
            
                /*
                |--------------------------------------------------------------------------
                | Database Connections
                |--------------------------------------------------------------------------
                |
                | Here are each of the database connections setup for your application.
                |
                */
                'connections' => [
                    'sqlite' => [
                        'driver' => 'sqlite',
                        'url' => env('DATABASE_URL'),
                        'database' => env('DB_DATABASE', database_path('database.sqlite')),
                        'prefix' => '',
                        'foreign_key_constraints' => env('DB_FOREIGN_KEYS', true),
                    ],
            
                    'mysql' => [
                        'driver' => 'mysql',
                        'url' => env('DATABASE_URL'),
                        'host' => env('DB_HOST', '127.0.0.1'),
                        'port' => env('DB_PORT', 3306),
                        'database' => env('DB_DATABASE', 'laravel'),
                        'username' => env('DB_USERNAME', 'root'),
                        'password' => env('DB_PASSWORD', ''),
                        'unix_socket' => env('DB_SOCKET', ''),
                        'charset' => 'utf8mb4',
                        'collation' => 'utf8mb4_unicode_ci',
                        'prefix' => '',
                        'prefix_indexes' => true,
                        'strict' => true,
                        'engine' => null,
                        'options' => extension_loaded('pdo_mysql') ? array_filter([
                            PDO::MYSQL_ATTR_SSL_CA => env('MYSQL_ATTR_SSL_CA'),
                        ]) : [],
                    ],
            
                    'pgsql' => [
                        'driver' => 'pgsql',
                        'url' => env('DATABASE_URL'),
                        'host' => env('DB_HOST', '127.0.0.1'),
                        'port' => env('DB_PORT', 5432),
                        'database' => env('DB_DATABASE', 'laravel'),
                        'username' => env('DB_USERNAME', 'root'),
                        'password' => env('DB_PASSWORD', ''),
                        'charset' => 'utf8',
                        'prefix' => '',
                        'prefix_indexes' => true,
                        'search_path' => 'public',
                        'sslmode' => 'prefer',
                    ],
                ],
            
                /*
                |--------------------------------------------------------------------------
                | Migration Repository Table
                |--------------------------------------------------------------------------
                */
                'migrations' => 'migrations',
            
                /*
                |--------------------------------------------------------------------------
                | Redis Databases
                |--------------------------------------------------------------------------
                */
                'redis' => [
                    'client' => env('REDIS_CLIENT', 'phpredis'),
                    
                    'default' => [
                        'host' => env('REDIS_HOST', '127.0.0.1'),
                        'password' => env('REDIS_PASSWORD', null),
                        'port' => env('REDIS_PORT', 6379),
                        'database' => env('REDIS_DB', 0),
                    ],
                    
                    'cache' => [
                        'host' => env('REDIS_HOST', '127.0.0.1'),
                        'password' => env('REDIS_PASSWORD', null),
                        'port' => env('REDIS_PORT', 6379),
                        'database' => env('REDIS_CACHE_DB', 1),
                    ],
                ],
            ];
            """,
            defaultConnection
        );
    }
    
    /**
     * Generate cache configuration
     */
    public String generateCacheConfig() {
        return """
            <?php
            
            return [
                /*
                |--------------------------------------------------------------------------
                | Default Cache Store
                |--------------------------------------------------------------------------
                */
                'default' => env('CACHE_DRIVER', 'file'),
            
                /*
                |--------------------------------------------------------------------------
                | Cache Stores
                |--------------------------------------------------------------------------
                */
                'stores' => [
                    'array' => [
                        'driver' => 'array',
                        'serialize' => false,
                    ],
            
                    'database' => [
                        'driver' => 'database',
                        'connection' => env('CACHE_DB_CONNECTION'),
                        'table' => env('CACHE_DB_TABLE', 'cache'),
                    ],
            
                    'file' => [
                        'driver' => 'file',
                        'path' => storage_path('framework/cache/data'),
                    ],
            
                    'memcached' => [
                        'driver' => 'memcached',
                        'persistent_id' => env('MEMCACHED_PERSISTENT_ID'),
                        'sasl' => [
                            env('MEMCACHED_USERNAME'),
                            env('MEMCACHED_PASSWORD'),
                        ],
                        'options' => [
                            // Memcached::OPT_CONNECT_TIMEOUT  => 2000,
                        ],
                        'servers' => [
                            [
                                'host' => env('MEMCACHED_HOST', '127.0.0.1'),
                                'port' => env('MEMCACHED_PORT', 11211),
                                'weight' => 100,
                            ],
                        ],
                    ],
            
                    'redis' => [
                        'driver' => 'redis',
                        'connection' => 'cache',
                        'lock_connection' => 'default',
                    ],
            
                    'dynamodb' => [
                        'driver' => 'dynamodb',
                        'key' => env('AWS_ACCESS_KEY_ID'),
                        'secret' => env('AWS_SECRET_ACCESS_KEY'),
                        'region' => env('AWS_DEFAULT_REGION', 'us-east-1'),
                        'table' => env('DYNAMODB_CACHE_TABLE', 'cache'),
                        'expires' => 36000,
                    ],
                ],
            
                /*
                |--------------------------------------------------------------------------
                | Cache Key Prefix
                |--------------------------------------------------------------------------
                */
                'prefix' => env('CACHE_PREFIX', 'laravel_cache_'),
            ];
            """;
    }
    
    /**
     * Generate queue configuration
     */
    public String generateQueueConfig() {
        return """
            <?php
            
            return [
                /*
                |--------------------------------------------------------------------------
                | Default Queue Connection Name
                |--------------------------------------------------------------------------
                */
                'default' => env('QUEUE_CONNECTION', 'sync'),
            
                /*
                |--------------------------------------------------------------------------
                | Queue Connections
                |--------------------------------------------------------------------------
                */
                'connections' => [
                    'sync' => [
                        'driver' => 'sync',
                    ],
            
                    'database' => [
                        'driver' => 'database',
                        'connection' => env('QUEUE_DB_CONNECTION'),
                        'table' => env('QUEUE_TABLE', 'jobs'),
                    ],
            
                    'beanstalkd' => [
                        'driver' => 'beanstalkd',
                        'host' => 'localhost',
                        'queue' => 'default',
                        'retry_after' => 90,
                        'block_for' => 0,
                        'after_commit' => false,
                    ],
            
                    'null' => [
                        'driver' => 'null',
                    ],
                ],
            
                /*
                |--------------------------------------------------------------------------
                | Batching
                |--------------------------------------------------------------------------
                */
                'batching' => [
                    'database' => env('QUEUE_BATCH_DATABASE', 'default'),
                    'table' => 'job_batches',
                ],
            
                /*
                |--------------------------------------------------------------------------
                | Failed Queue Jobs
                |--------------------------------------------------------------------------
                */
                'failed' => [
                    'database' => env('QUEUE_FAILED_DATABASE', 'default'),
                    'table' => 'failed_jobs',
                ],
            ];
            """;
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
        return String.format("""
            <?php
            
            return [
                /*
                |--------------------------------------------------------------------------
                | Application Name
                |--------------------------------------------------------------------------
                */
                'name' => env('APP_NAME', '%s'),
            
                /*
                |--------------------------------------------------------------------------
                | Application Environment
                |--------------------------------------------------------------------------
                */
                'env' => env('APP_ENV', 'production'),
            
                /*
                |--------------------------------------------------------------------------
                | Application Debug Mode
                |--------------------------------------------------------------------------
                */
                'debug' => (bool) env('APP_DEBUG', false),
            
                /*
                |--------------------------------------------------------------------------
                | Application URL
                |--------------------------------------------------------------------------
                */
                'url' => env('APP_URL', 'http://localhost'),
                
                'asset_url' => env('ASSET_URL'),
            
                /*
                |--------------------------------------------------------------------------
                | Application Timezone
                |--------------------------------------------------------------------------
                */
                'timezone' => 'UTC',
            
                /*
                |--------------------------------------------------------------------------
                | Application Locale Configuration
                |--------------------------------------------------------------------------
                */
                'locale' => 'en',
                'fallback_locale' => 'en',
                'faker_locale' => 'en_US',
            
                /*
                |--------------------------------------------------------------------------
                | Encryption Key
                |--------------------------------------------------------------------------
                */
                'key' => env('APP_KEY'),
                'cipher' => 'AES-256-CBC',
            
                /*
                |--------------------------------------------------------------------------
                | Maintenance Mode Driver
                |--------------------------------------------------------------------------
                */
                'maintenance' => [
                    'driver' => 'file',
                ],
            ];
            """,
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
