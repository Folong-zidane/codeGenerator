package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IFileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class PhpFileWriter implements IFileWriter {
    
    @Override
    public void writeFiles(Map<String, String> files, String outputPath) {
        files.forEach((filePath, content) -> {
            try {
                writeFile(filePath, content, outputPath);
            } catch (Exception e) {
                throw new RuntimeException("Failed to write file: " + filePath, e);
            }
        });
    }
    
    @Override
    public void writeFile(String fileName, String content, String outputPath) {
        try {
            Path basePath = Paths.get(outputPath);
            Path filePath;
            
            // Determine the correct file path based on content type
            if (content.contains("class") && content.contains("extends Model")) {
                filePath = basePath.resolve("app/Models").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("Controller")) {
                filePath = basePath.resolve("app/Http/Controllers/Api").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("Service")) {
                filePath = basePath.resolve("app/Services").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("Repository")) {
                filePath = basePath.resolve("app/Repositories").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("enum")) {
                filePath = basePath.resolve("app/Enums").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("Migration")) {
                filePath = basePath.resolve("database/migrations").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("ServiceProvider")) {
                filePath = basePath.resolve("app/Providers").resolve(ensurePhpExtension(fileName));
            } else if (content.contains("Route::")) {
                filePath = basePath.resolve("routes").resolve(ensurePhpExtension(fileName));
            } else {
                filePath = basePath.resolve(ensurePhpExtension(fileName));
            }
            
            // Create directories if they don't exist
            Files.createDirectories(filePath.getParent());
            
            // Write the file
            Files.writeString(filePath, content);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to write PHP file: " + fileName, e);
        }
    }
    
    @Override
    public void createDirectories(String basePath, String... directories) {
        try {
            Path base = Paths.get(basePath);
            
            // Create standard Laravel directory structure
            Files.createDirectories(base.resolve("app/Models"));
            Files.createDirectories(base.resolve("app/Http/Controllers/Api"));
            Files.createDirectories(base.resolve("app/Http/Requests"));
            Files.createDirectories(base.resolve("app/Http/Resources"));
            Files.createDirectories(base.resolve("app/Services"));
            Files.createDirectories(base.resolve("app/Repositories"));
            Files.createDirectories(base.resolve("app/Enums"));
            Files.createDirectories(base.resolve("app/Providers"));
            Files.createDirectories(base.resolve("database/migrations"));
            Files.createDirectories(base.resolve("database/seeders"));
            Files.createDirectories(base.resolve("routes"));
            Files.createDirectories(base.resolve("config"));
            Files.createDirectories(base.resolve("resources/views"));
            Files.createDirectories(base.resolve("storage/logs"));
            Files.createDirectories(base.resolve("tests/Feature"));
            Files.createDirectories(base.resolve("tests/Unit"));
            
            // Create additional directories if specified
            for (String directory : directories) {
                Files.createDirectories(base.resolve(directory));
            }
            
            // Create Laravel project files
            createLaravelProjectFiles(base);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Laravel project directories", e);
        }
    }
    
    @Override
    public String getOutputFormat() {
        return "laravel-project";
    }
    
    private String ensurePhpExtension(String fileName) {
        return fileName.endsWith(".php") ? fileName : fileName + ".php";
    }
    
    private void createLaravelProjectFiles(Path basePath) throws IOException {
        // Create composer.json
        String composerContent = generateComposerJson();
        Files.writeString(basePath.resolve("composer.json"), composerContent);
        
        // Create .env.example
        String envExampleContent = generateEnvExample();
        Files.writeString(basePath.resolve(".env.example"), envExampleContent);
        
        // Create artisan
        String artisanContent = generateArtisan();
        Path artisanPath = basePath.resolve("artisan");
        Files.writeString(artisanPath, artisanContent);
        artisanPath.toFile().setExecutable(true);
        
        // Create .gitignore
        String gitignoreContent = generateGitignore();
        Files.writeString(basePath.resolve(".gitignore"), gitignoreContent);
        
        // Create README.md
        String readmeContent = generateReadme();
        Files.writeString(basePath.resolve("README.md"), readmeContent);
        
        // Create start script
        String startScript = generateStartScript();
        Path startScriptPath = basePath.resolve("start.sh");
        Files.writeString(startScriptPath, startScript);
        startScriptPath.toFile().setExecutable(true);
    }
    
    private String generateComposerJson() {
        return """
            {
                "name": "generated/laravel-api",
                "type": "project",
                "description": "Generated Laravel API with UML-based code generation",
                "keywords": ["laravel", "framework", "api", "uml", "generated"],
                "license": "MIT",
                "require": {
                    "php": "^8.1",
                    "guzzlehttp/guzzle": "^7.2",
                    "laravel/framework": "^10.10",
                    "laravel/sanctum": "^3.2",
                    "laravel/tinker": "^2.8"
                },
                "require-dev": {
                    "fakerphp/faker": "^1.9.1",
                    "laravel/pint": "^1.0",
                    "laravel/sail": "^1.18",
                    "mockery/mockery": "^1.4.4",
                    "nunomaduro/collision": "^7.0",
                    "phpunit/phpunit": "^10.1",
                    "spatie/laravel-ignition": "^2.0"
                },
                "autoload": {
                    "psr-4": {
                        "App\\\\": "app/",
                        "Database\\\\Factories\\\\": "database/factories/",
                        "Database\\\\Seeders\\\\": "database/seeders/"
                    }
                },
                "autoload-dev": {
                    "psr-4": {
                        "Tests\\\\": "tests/"
                    }
                },
                "scripts": {
                    "post-autoload-dump": [
                        "Illuminate\\\\Foundation\\\\ComposerScripts::postAutoloadDump",
                        "@php artisan package:discover --ansi"
                    ],
                    "post-update-cmd": [
                        "@php artisan vendor:publish --tag=laravel-assets --ansi --force"
                    ],
                    "post-root-package-install": [
                        "@php -r \\"file_exists('.env') || copy('.env.example', '.env');\""
                    ],
                    "post-create-project-cmd": [
                        "@php artisan key:generate --ansi"
                    ]
                },
                "extra": {
                    "laravel": {
                        "dont-discover": []
                    }
                },
                "config": {
                    "optimize-autoloader": true,
                    "preferred-install": "dist",
                    "sort-packages": true,
                    "allow-plugins": {
                        "pestphp/pest-plugin": true,
                        "php-http/discovery": true
                    }
                },
                "minimum-stability": "stable",
                "prefer-stable": true
            }
            """;
    }
    
    private String generateEnvExample() {
        return """
            APP_NAME="Generated Laravel API"
            APP_ENV=local
            APP_KEY=
            APP_DEBUG=true
            APP_URL=http://localhost
            
            LOG_CHANNEL=stack
            LOG_DEPRECATIONS_CHANNEL=null
            LOG_LEVEL=debug
            
            DB_CONNECTION=mysql
            DB_HOST=127.0.0.1
            DB_PORT=3306
            DB_DATABASE=generated_laravel_api
            DB_USERNAME=root
            DB_PASSWORD=
            
            BROADCAST_DRIVER=log
            CACHE_DRIVER=file
            FILESYSTEM_DISK=local
            QUEUE_CONNECTION=sync
            SESSION_DRIVER=file
            SESSION_LIFETIME=120
            
            MEMCACHED_HOST=127.0.0.1
            
            REDIS_HOST=127.0.0.1
            REDIS_PASSWORD=null
            REDIS_PORT=6379
            
            MAIL_MAILER=smtp
            MAIL_HOST=mailpit
            MAIL_PORT=1025
            MAIL_USERNAME=null
            MAIL_PASSWORD=null
            MAIL_ENCRYPTION=null
            MAIL_FROM_ADDRESS="hello@example.com"
            MAIL_FROM_NAME="${APP_NAME}"
            
            AWS_ACCESS_KEY_ID=
            AWS_SECRET_ACCESS_KEY=
            AWS_DEFAULT_REGION=us-east-1
            AWS_BUCKET=
            AWS_USE_PATH_STYLE_ENDPOINT=false
            
            PUSHER_APP_ID=
            PUSHER_APP_KEY=
            PUSHER_APP_SECRET=
            PUSHER_HOST=
            PUSHER_PORT=443
            PUSHER_SCHEME=https
            PUSHER_APP_CLUSTER=mt1
            
            VITE_PUSHER_APP_KEY="${PUSHER_APP_KEY}"
            VITE_PUSHER_HOST="${PUSHER_HOST}"
            VITE_PUSHER_PORT="${PUSHER_PORT}"
            VITE_PUSHER_SCHEME="${PUSHER_SCHEME}"
            VITE_PUSHER_APP_CLUSTER="${PUSHER_APP_CLUSTER}"
            """;
    }
    
    private String generateArtisan() {
        return """
            #!/usr/bin/env php
            <?php
            
            define('LARAVEL_START', microtime(true));
            
            /*
            |--------------------------------------------------------------------------
            | Register The Auto Loader
            |--------------------------------------------------------------------------
            |
            | Composer provides a convenient, automatically generated class loader
            | for our application. We just need to utilize it! We'll require it
            | into the script here so that we do not have to worry about the
            | loading of any our classes "manually". Feels great to relax.
            |
            */
            
            require __DIR__.'/vendor/autoload.php';
            
            $app = require_once __DIR__.'/bootstrap/app.php';
            
            /*
            |--------------------------------------------------------------------------
            | Run The Artisan Application
            |--------------------------------------------------------------------------
            |
            | When we run the console application, the current CLI command will be
            | executed in this console and the response sent back to a terminal
            | or another output device for the developers. Here goes nothing!
            |
            */
            
            $kernel = $app->make(Illuminate\\Contracts\\Console\\Kernel::class);
            
            $status = $kernel->handle(
                $input = new Symfony\\Component\\Console\\Input\\ArgvInput,
                new Symfony\\Component\\Console\\Output\\ConsoleOutput
            );
            
            /*
            |--------------------------------------------------------------------------
            | Shutdown The Application
            |--------------------------------------------------------------------------
            |
            | Once Artisan has finished running, we will fire off the shutdown events
            | so that any final work may be done by the application before we shut
            | down the process. This is the last thing to happen to the request.
            |
            */
            
            $kernel->terminate($input, $status);
            
            exit($status);
            """;
    }
    
    private String generateGitignore() {
        return """
            /node_modules
            /public/build
            /public/hot
            /public/storage
            /storage/*.key
            /vendor
            .env
            .env.backup
            .env.production
            .phpunit.result.cache
            Homestead.json
            Homestead.yaml
            auth.json
            npm-debug.log
            yarn-error.log
            /.fleet
            /.idea
            /.vscode
            """;
    }
    
    private String generateReadme() {
        return """
            # 🚀 Generated Laravel API
            
            This project was generated using UML-to-Code generator with comprehensive Laravel support.
            
            ## 🏗️ Architecture
            
            - **Models**: Eloquent models with relationships and state management
            - **Controllers**: API controllers with Laravel Resources
            - **Services**: Business logic layer with dependency injection
            - **Repositories**: Data access layer with interface contracts
            - **Requests**: Form request validation classes
            - **Resources**: API resource transformers
            
            ## 🚀 Quick Start
            
            ```bash
            # Install dependencies
            composer install
            
            # Copy environment file
            cp .env.example .env
            
            # Generate application key
            php artisan key:generate
            
            # Run migrations
            php artisan migrate
            
            # Start development server
            php artisan serve
            
            # Or use the start script
            ./start.sh
            ```
            
            ## 📊 API Endpoints
            
            Once running, the API will be available at:
            - Base URL: http://localhost:8000/api
            - Documentation: Available via API routes
            
            ## 🗄️ Database
            
            The application uses Laravel's database abstraction layer.
            Configure your database in the `.env` file.
            
            ## 🔧 Artisan Commands
            
            ```bash
            # Run migrations
            php artisan migrate
            
            # Seed database
            php artisan db:seed
            
            # Clear cache
            php artisan cache:clear
            
            # Generate API documentation
            php artisan route:list
            ```
            
            ## 📝 Features
            
            ✅ Complete CRUD operations
            ✅ State management (if applicable)
            ✅ Laravel Eloquent ORM
            ✅ API Resources for data transformation
            ✅ Form Request validation
            ✅ Repository pattern
            ✅ Service layer architecture
            ✅ Dependency injection
            ✅ Database migrations
            ✅ Error handling and logging
            ✅ RESTful API design
            """;
    }
    
    private String generateStartScript() {
        return """
            #!/bin/bash
            echo "🚀 Starting Laravel API..."
            
            # Check if PHP is installed
            if ! command -v php &> /dev/null; then
                echo "❌ PHP is not installed. Please install PHP 8.1 or higher."
                exit 1
            fi
            
            # Check if Composer is installed
            if ! command -v composer &> /dev/null; then
                echo "❌ Composer is not installed. Please install Composer first."
                exit 1
            fi
            
            echo "📦 Installing dependencies..."
            composer install
            
            echo "📋 Setting up environment..."
            if [ ! -f .env ]; then
                cp .env.example .env
                echo "📝 Created .env file from .env.example"
                echo "⚠️  Please update the database configuration in .env"
            fi
            
            echo "🔑 Generating application key..."
            php artisan key:generate
            
            echo "🗄️ Running migrations..."
            php artisan migrate --force
            
            echo "🌐 Starting development server..."
            echo "📊 API will be available at: http://localhost:8000/api"
            php artisan serve
            """;
    }
}