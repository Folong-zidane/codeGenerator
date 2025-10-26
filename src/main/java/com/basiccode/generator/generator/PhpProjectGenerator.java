package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PhpProjectGenerator {
    
    public void generateCompleteProject(List<ClassModel> classes, String packageName, Path outputDir) throws IOException {
        createProjectStructure(outputDir);
        generateComposerJson(outputDir);
        generateIndexPhp(outputDir, classes);
        generateDatabase(outputDir);
        generateModels(classes, outputDir);
        generateRepositories(classes, outputDir);
        generateServices(classes, outputDir);
        generateControllers(classes, outputDir);
    }
    
    private void createProjectStructure(Path outputDir) throws IOException {
        Files.createDirectories(outputDir.resolve("src/Models"));
        Files.createDirectories(outputDir.resolve("src/Repositories"));
        Files.createDirectories(outputDir.resolve("src/Services"));
        Files.createDirectories(outputDir.resolve("src/Controllers"));
        Files.createDirectories(outputDir.resolve("src/Config"));
        Files.createDirectories(outputDir.resolve("public"));
    }
    
    private void generateComposerJson(Path outputDir) throws IOException {
        String composerJson = """
            {
              "name": "generated/php-api",
              "description": "Generated PHP API",
              "type": "project",
              "require": {
                "php": ">=8.1",
                "slim/slim": "^4.12",
                "slim/psr7": "^1.6",
                "illuminate/database": "^10.0",
                "ramsey/uuid": "^4.7"
              },
              "autoload": {
                "psr-4": {
                  "App\\\\": "src/"
                }
              },
              "scripts": {
                "start": "php -S localhost:8000 -t public"
              }
            }
            """;
        Files.writeString(outputDir.resolve("composer.json"), composerJson);
    }
    
    private void generateIndexPhp(Path outputDir, List<ClassModel> classes) throws IOException {
        StringBuilder routes = new StringBuilder();
        for (ClassModel cls : classes) {
            String route = cls.getName().toLowerCase();
            routes.append("$app->group('/api/").append(route).append("', function (Group $group) {\n");
            routes.append("    $group->get('', \\App\\Controllers\\").append(cls.getName()).append("Controller::class . ':getAll');\n");
            routes.append("    $group->get('/{id}', \\App\\Controllers\\").append(cls.getName()).append("Controller::class . ':getById');\n");
            routes.append("    $group->post('', \\App\\Controllers\\").append(cls.getName()).append("Controller::class . ':create');\n");
            routes.append("    $group->put('/{id}', \\App\\Controllers\\").append(cls.getName()).append("Controller::class . ':update');\n");
            routes.append("    $group->delete('/{id}', \\App\\Controllers\\").append(cls.getName()).append("Controller::class . ':delete');\n");
            routes.append("});\n\n");
        }
        
        String indexPhp = String.format("""
            <?php
            
            use Psr\\Http\\Message\\ResponseInterface as Response;
            use Psr\\Http\\Message\\ServerRequestInterface as Request;
            use Slim\\Factory\\AppFactory;
            use Slim\\Routing\\RouteCollectorProxy as Group;
            
            require __DIR__ . '/../vendor/autoload.php';
            require __DIR__ . '/../src/Config/Database.php';
            
            $app = AppFactory::create();
            
            // Add error middleware
            $app->addErrorMiddleware(true, true, true);
            
            // Add body parsing middleware
            $app->addBodyParsingMiddleware();
            
            // Routes
            $app->get('/', function (Request $request, Response $response) {
                $data = ['message' => 'Generated PHP API is running'];
                $response->getBody()->write(json_encode($data));
                return $response->withHeader('Content-Type', 'application/json');
            });
            
            %s
            
            $app->run();
            """, routes.toString());
        
        Files.writeString(outputDir.resolve("public/index.php"), indexPhp);
    }
    
    private void generateDatabase(Path outputDir) throws IOException {
        String database = """
            <?php
            
            use Illuminate\\Database\\Capsule\\Manager as Capsule;
            
            $capsule = new Capsule;
            
            $capsule->addConnection([
                'driver' => 'sqlite',
                'database' => __DIR__ . '/../../database.sqlite',
                'prefix' => '',
            ]);
            
            $capsule->setAsGlobal();
            $capsule->bootEloquent();
            
            // Create database file if it doesn't exist
            $dbPath = __DIR__ . '/../../database.sqlite';
            if (!file_exists($dbPath)) {
                touch($dbPath);
            }
            """;
        Files.writeString(outputDir.resolve("src/Config/Database.php"), database);
    }
    
    private void generateModels(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            StringBuilder model = new StringBuilder();
            model.append("<?php\n\n");
            model.append("namespace App\\Models;\n\n");
            model.append("use Illuminate\\Database\\Eloquent\\Model;\n");
            model.append("use Ramsey\\Uuid\\Uuid;\n\n");
            
            model.append("class ").append(cls.getName()).append(" extends Model\n{\n");
            model.append("    protected $table = '").append(toSnakeCase(cls.getName())).append("';\n");
            model.append("    protected $keyType = 'string';\n");
            model.append("    public $incrementing = false;\n\n");
            
            model.append("    protected $fillable = [\n");
            for (Field field : cls.getFields()) {
                model.append("        '").append(field.getName()).append("',\n");
            }
            model.append("    ];\n\n");
            
            model.append("    protected $casts = [\n");
            model.append("        'id' => 'string',\n");
            for (Field field : cls.getFields()) {
                model.append("        '").append(field.getName()).append("' => '").append(getPhpCastType(field.getType())).append("',\n");
            }
            model.append("        'created_at' => 'datetime',\n");
            model.append("        'updated_at' => 'datetime',\n");
            model.append("    ];\n\n");
            
            model.append("    protected static function boot()\n");
            model.append("    {\n");
            model.append("        parent::boot();\n");
            model.append("        static::creating(function ($model) {\n");
            model.append("            if (empty($model->id)) {\n");
            model.append("                $model->id = Uuid::uuid4()->toString();\n");
            model.append("            }\n");
            model.append("        });\n");
            model.append("    }\n");
            model.append("}\n");
            
            Files.writeString(outputDir.resolve("src/Models/" + cls.getName() + ".php"), model.toString());
        }
    }
    
    private void generateRepositories(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            String repository = String.format("""
                <?php
                
                namespace App\\Repositories;
                
                use App\\Models\\%s;
                
                class %sRepository
                {
                    public function findById(string $id): ?%s
                    {
                        return %s::find($id);
                    }
                
                    public function findAll(): array
                    {
                        return %s::all()->toArray();
                    }
                
                    public function create(array $data): %s
                    {
                        return %s::create($data);
                    }
                
                    public function update(string $id, array $data): ?%s
                    {
                        $entity = %s::find($id);
                        if ($entity) {
                            $entity->update($data);
                            return $entity;
                        }
                        return null;
                    }
                
                    public function delete(string $id): bool
                    {
                        $entity = %s::find($id);
                        if ($entity) {
                            return $entity->delete();
                        }
                        return false;
                    }
                }
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("src/Repositories/" + cls.getName() + "Repository.php"), repository);
        }
    }
    
    private void generateServices(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            String service = String.format("""
                <?php
                
                namespace App\\Services;
                
                use App\\Repositories\\%sRepository;
                use App\\Models\\%s;
                
                class %sService
                {
                    private %sRepository $repository;
                
                    public function __construct()
                    {
                        $this->repository = new %sRepository();
                    }
                
                    public function create(array $data): %s
                    {
                        return $this->repository->create($data);
                    }
                
                    public function findById(string $id): ?%s
                    {
                        return $this->repository->findById($id);
                    }
                
                    public function findAll(): array
                    {
                        return $this->repository->findAll();
                    }
                
                    public function update(string $id, array $data): ?%s
                    {
                        return $this->repository->update($id, $data);
                    }
                
                    public function delete(string $id): bool
                    {
                        return $this->repository->delete($id);
                    }
                }
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName(),
                cls.getName(), cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("src/Services/" + cls.getName() + "Service.php"), service);
        }
    }
    
    private void generateControllers(List<ClassModel> classes, Path outputDir) throws IOException {
        for (ClassModel cls : classes) {
            String controller = String.format("""
                <?php
                
                namespace App\\Controllers;
                
                use Psr\\Http\\Message\\ResponseInterface as Response;
                use Psr\\Http\\Message\\ServerRequestInterface as Request;
                use App\\Services\\%sService;
                
                class %sController
                {
                    private %sService $service;
                
                    public function __construct()
                    {
                        $this->service = new %sService();
                    }
                
                    public function getAll(Request $request, Response $response): Response
                    {
                        try {
                            $entities = $this->service->findAll();
                            $response->getBody()->write(json_encode($entities));
                            return $response->withHeader('Content-Type', 'application/json');
                        } catch (\\Exception $e) {
                            $response->getBody()->write(json_encode(['error' => $e->getMessage()]));
                            return $response->withStatus(500)->withHeader('Content-Type', 'application/json');
                        }
                    }
                
                    public function getById(Request $request, Response $response, array $args): Response
                    {
                        try {
                            $entity = $this->service->findById($args['id']);
                            if (!$entity) {
                                $response->getBody()->write(json_encode(['error' => 'Not found']));
                                return $response->withStatus(404)->withHeader('Content-Type', 'application/json');
                            }
                            $response->getBody()->write(json_encode($entity));
                            return $response->withHeader('Content-Type', 'application/json');
                        } catch (\\Exception $e) {
                            $response->getBody()->write(json_encode(['error' => $e->getMessage()]));
                            return $response->withStatus(500)->withHeader('Content-Type', 'application/json');
                        }
                    }
                
                    public function create(Request $request, Response $response): Response
                    {
                        try {
                            $data = $request->getParsedBody();
                            $entity = $this->service->create($data);
                            $response->getBody()->write(json_encode($entity));
                            return $response->withStatus(201)->withHeader('Content-Type', 'application/json');
                        } catch (\\Exception $e) {
                            $response->getBody()->write(json_encode(['error' => $e->getMessage()]));
                            return $response->withStatus(500)->withHeader('Content-Type', 'application/json');
                        }
                    }
                
                    public function update(Request $request, Response $response, array $args): Response
                    {
                        try {
                            $data = $request->getParsedBody();
                            $entity = $this->service->update($args['id'], $data);
                            if (!$entity) {
                                $response->getBody()->write(json_encode(['error' => 'Not found']));
                                return $response->withStatus(404)->withHeader('Content-Type', 'application/json');
                            }
                            $response->getBody()->write(json_encode($entity));
                            return $response->withHeader('Content-Type', 'application/json');
                        } catch (\\Exception $e) {
                            $response->getBody()->write(json_encode(['error' => $e->getMessage()]));
                            return $response->withStatus(500)->withHeader('Content-Type', 'application/json');
                        }
                    }
                
                    public function delete(Request $request, Response $response, array $args): Response
                    {
                        try {
                            $deleted = $this->service->delete($args['id']);
                            if (!$deleted) {
                                $response->getBody()->write(json_encode(['error' => 'Not found']));
                                return $response->withStatus(404)->withHeader('Content-Type', 'application/json');
                            }
                            return $response->withStatus(204);
                        } catch (\\Exception $e) {
                            $response->getBody()->write(json_encode(['error' => $e->getMessage()]));
                            return $response->withStatus(500)->withHeader('Content-Type', 'application/json');
                        }
                    }
                }
                """, 
                cls.getName(), cls.getName(), cls.getName(), cls.getName());
            
            Files.writeString(outputDir.resolve("src/Controllers/" + cls.getName() + "Controller.php"), controller);
        }
    }
    
    private String getPhpCastType(String type) {
        return switch (type) {
            case "String" -> "string";
            case "Integer", "int" -> "integer";
            case "Float", "float" -> "float";
            case "Boolean", "boolean" -> "boolean";
            default -> "string";
        };
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}