package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IMigrationGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;

public class PhpMigrationGenerator implements IMigrationGenerator {
    
    @Override
    public String generateMigration(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        // Generate database migrations
        for (EnhancedClass enhancedClass : enhancedClasses) {
            code.append(generateTableMigration(enhancedClass, packageName));
            code.append("\n\n");
        }
        
        // Generate service provider
        code.append(generateServiceProvider(enhancedClasses, packageName));
        code.append("\n\n");
        
        // Generate routes
        code.append(generateRoutes(enhancedClasses, packageName));
        
        return code.toString();
    }
    
    @Override
    public String getMigrationDirectory() {
        return "database/migrations";
    }
    
    private String generateTableMigration(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String tableName = className.toLowerCase() + "s";
        
        code.append("<?php\n\n");
        code.append("use Illuminate\\Database\\Migrations\\Migration;\n");
        code.append("use Illuminate\\Database\\Schema\\Blueprint;\n");
        code.append("use Illuminate\\Support\\Facades\\Schema;\n\n");
        
        code.append("return new class extends Migration\n");
        code.append("{\n");
        code.append("    /**\n");
        code.append("     * Run the migrations.\n");
        code.append("     */\n");
        code.append("    public function up(): void\n");
        code.append("    {\n");
        code.append("        Schema::create('").append(tableName).append("', function (Blueprint $table) {\n");
        code.append("            $table->id();\n");
        
        // Generate columns for attributes
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!"id".equalsIgnoreCase(attr.getName())) {
                String columnType = mapColumnType(attr.getType());
                code.append("            $table->").append(columnType).append("('").append(attr.getName()).append("')");
                
                // Add nullable if appropriate
                if (!attr.getName().toLowerCase().contains("name") && !attr.getName().toLowerCase().contains("title")) {
                    code.append("->nullable()");
                }
                
                // Add unique constraint for email fields
                if (attr.getName().toLowerCase().contains("email")) {
                    code.append("->unique()");
                }
                
                code.append(";\n");
            }
        }
        
        // Add status column if stateful
        if (enhancedClass.isStateful()) {
            code.append("            $table->string('status')->default('ACTIVE');\n");
        }
        
        code.append("            $table->timestamps();\n");
        
        // Add indexes
        code.append("\n            // Indexes\n");
        if (enhancedClass.isStateful()) {
            code.append("            $table->index('status');\n");
        }
        code.append("            $table->index('created_at');\n");
        
        code.append("        });\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Reverse the migrations.\n");
        code.append("     */\n");
        code.append("    public function down(): void\n");
        code.append("    {\n");
        code.append("        Schema::dropIfExists('").append(tableName).append("');\n");
        code.append("    }\n");
        code.append("};\n");
        
        return code.toString();
    }
    
    private String generateServiceProvider(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Providers;\n\n");
        code.append("use Illuminate\\Support\\ServiceProvider;\n");
        
        // Import repositories and services
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            code.append("use ").append(packageName).append("\\Repositories\\").append(className).append("Repository;\n");
            code.append("use ").append(packageName).append("\\Repositories\\").append(className).append("RepositoryInterface;\n");
        }
        
        code.append("\nclass AppServiceProvider extends ServiceProvider\n");
        code.append("{\n");
        code.append("    /**\n");
        code.append("     * Register any application services.\n");
        code.append("     */\n");
        code.append("    public function register(): void\n");
        code.append("    {\n");
        
        // Bind repositories
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            code.append("        $this->app->bind(").append(className).append("RepositoryInterface::class, ").append(className).append("Repository::class);\n");
        }
        
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Bootstrap any application services.\n");
        code.append("     */\n");
        code.append("    public function boot(): void\n");
        code.append("    {\n");
        code.append("        //\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateRoutes(List<EnhancedClass> enhancedClasses, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("<?php\n\n");
        code.append("use Illuminate\\Http\\Request;\n");
        code.append("use Illuminate\\Support\\Facades\\Route;\n");
        
        // Import controllers
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            code.append("use ").append(packageName).append("\\Http\\Controllers\\Api\\").append(className).append("Controller;\n");
        }
        
        code.append("\n/*\n");
        code.append("|--------------------------------------------------------------------------\n");
        code.append("| API Routes\n");
        code.append("|--------------------------------------------------------------------------\n");
        code.append("|\n");
        code.append("| Here is where you can register API routes for your application. These\n");
        code.append("| routes are loaded by the RouteServiceProvider and all of them will\n");
        code.append("| be assigned to the \"api\" middleware group. Make something great!\n");
        code.append("|\n");
        code.append("*/\n\n");
        
        code.append("Route::middleware('auth:sanctum')->get('/user', function (Request $request) {\n");
        code.append("    return $request->user();\n");
        code.append("});\n\n");
        
        // Generate routes for each entity
        for (EnhancedClass enhancedClass : enhancedClasses) {
            String className = enhancedClass.getOriginalClass().getName();
            String lowerClassName = className.toLowerCase();
            
            code.append("// ").append(className).append(" routes\n");
            code.append("Route::apiResource('").append(lowerClassName).append("s', ").append(className).append("Controller::class);\n");
            
            if (enhancedClass.isStateful()) {
                code.append("Route::patch('").append(lowerClassName).append("s/{id}/suspend', [").append(className).append("Controller::class, 'suspend']);\n");
                code.append("Route::patch('").append(lowerClassName).append("s/{id}/activate', [").append(className).append("Controller::class, 'activate']);\n");
            }
            
            code.append("\n");
        }
        
        return code.toString();
    }
    
    private String mapColumnType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "long", "integer", "int" -> "integer";
            case "float", "double" -> "decimal";
            case "boolean" -> "boolean";
            case "date" -> "date";
            case "datetime" -> "timestamp";
            default -> "string";
        };
    }
}