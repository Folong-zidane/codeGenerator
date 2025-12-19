package com.basiccode.generator.generator.php;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PhpAdvancedFeaturesGenerator - Generates Laravel advanced features
 * 
 * Phase 2 Week 3 - ADVANCED FEATURES
 * 
 * Generates:
 * - Job classes for async operations
 * - Event classes with listeners
 * - Observer classes for model events
 * - Middleware for API
 * - API Resources (transformers)
 * 
 * @version 1.0.0
 * @since PHP Phase 2
 */
@Slf4j
@Component
public class PhpAdvancedFeaturesGenerator {
    
    /**
     * Generate Job class for async operations
     */
    public String generateJobClass(String jobName, String modelName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Jobs;\n\n" +
            "use App\\Models\\%s;\n" +
            "use Illuminate\\Bus\\Queueable;\n" +
            "use Illuminate\\Contracts\\Queue\\ShouldQueue;\n\n" +
            "class %s implements ShouldQueue\n" +
            "{\n" +
            "    use Queueable;\n\n" +
            "    private %s $model;\n\n" +
            "    public function __construct(%s $model)\n" +
            "    {\n" +
            "        $this->model = $model;\n" +
            "    }\n\n" +
            "    public function handle(): void\n" +
            "    {\n" +
            "        // TODO: Implement job logic\n" +
            "    }\n" +
            "}\n",
            modelName, jobName, modelName, modelName
        );
    }
    
    /**
     * Generate Event class
     */
    public String generateEventClass(String eventName, String modelName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Events;\n\n" +
            "use App\\Models\\%s;\n" +
            "use Illuminate\\Broadcasting\\InteractsWithSockets;\n" +
            "use Illuminate\\Foundation\\Events\\Dispatchable;\n\n" +
            "class %s\n" +
            "{\n" +
            "    use Dispatchable, InteractsWithSockets;\n\n" +
            "    public %s $model;\n\n" +
            "    public function __construct(%s $model)\n" +
            "    {\n" +
            "        $this->model = $model;\n" +
            "    }\n" +
            "}\n",
            modelName, eventName, modelName, modelName
        );
    }
    
    /**
     * Generate Listener class for event
     */
    public String generateListenerClass(String listenerName, String eventName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Listeners;\n\n" +
            "use App\\Events\\%s;\n" +
            "use Illuminate\\Contracts\\Queue\\ShouldQueue;\n\n" +
            "class %s implements ShouldQueue\n" +
            "{\n" +
            "    public function handle(%s $event): void\n" +
            "    {\n" +
            "        // TODO: Implement listener logic\n" +
            "    }\n" +
            "}\n",
            eventName, listenerName, eventName
        );
    }
    
    /**
     * Generate Observer class for model events
     */
    public String generateObserverClass(String modelName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Observers;\n\n" +
            "use App\\Models\\%s;\n\n" +
            "class %sObserver\n" +
            "{\n" +
            "    public function created(%s $%s): void\n" +
            "    {\n" +
            "        // Handle created event\n" +
            "    }\n\n" +
            "    public function updated(%s $%s): void\n" +
            "    {\n" +
            "        // Handle updated event\n" +
            "    }\n" +
            "}\n",
            modelName, modelName, modelName, lowerFirst(modelName),
            modelName, lowerFirst(modelName)
        );
    }
    
    /**
     * Generate API Resource class (transformer)
     */
    public String generateResourceClass(String modelName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Http\\Resources;\n\n" +
            "use Illuminate\\Http\\Request;\n" +
            "use Illuminate\\Http\\Resources\\Json\\JsonResource;\n\n" +
            "class %sResource extends JsonResource\n" +
            "{\n" +
            "    public function toArray(Request $request): array\n" +
            "    {\n" +
            "        return [\n" +
            "            'id' => $this->id,\n" +
            "            'created_at' => $this->created_at,\n" +
            "            'updated_at' => $this->updated_at,\n" +
            "        ];\n" +
            "    }\n" +
            "}\n",
            modelName
        );
    }
    
    /**
     * Generate API Resource Collection
     */
    public String generateResourceCollectionClass(String modelName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Http\\Resources;\n\n" +
            "use Illuminate\\Http\\Resources\\Json\\ResourceCollection;\n\n" +
            "class %sCollection extends ResourceCollection\n" +
            "{\n" +
            "    public function toArray($request): array\n" +
            "    {\n" +
            "        return [\n" +
            "            'data' => $this->collection,\n" +
            "        ];\n" +
            "    }\n" +
            "}\n",
            modelName
        );
    }
    
    /**
     * Generate Middleware class
     */
    public String generateMiddlewareClass(String middlewareName) {
        return String.format(
            "<?php\n\n" +
            "namespace App\\Http\\Middleware;\n\n" +
            "use Closure;\n" +
            "use Illuminate\\Http\\Request;\n\n" +
            "class %s\n" +
            "{\n" +
            "    public function handle(Request $request, Closure $next)\n" +
            "    {\n" +
            "        // TODO: Implement middleware logic\n" +
            "        return $next($request);\n" +
            "    }\n" +
            "}\n",
            middlewareName
        );
    }
    
    /**
     * Generate API Response Trait
     */
    public String generateApiResponseTrait() {
        return "<?php\n\n" +
            "namespace App\\Traits;\n\n" +
            "use Illuminate\\Http\\JsonResponse;\n\n" +
            "trait ApiResponse\n" +
            "{\n" +
            "    protected function success($data = null): JsonResponse\n" +
            "    {\n" +
            "        return response()->json(['success' => true, 'data' => $data]);\n" +
            "    }\n\n" +
            "    protected function error(string $message = 'Error'): JsonResponse\n" +
            "    {\n" +
            "        return response()->json(['success' => false, 'message' => $message]);\n" +
            "    }\n" +
            "}\n";
    }
    
    /**
     * Generate service provider registration for observers
     */
    public String generateObserverRegistration(List<String> modelNames) {
        StringBuilder code = new StringBuilder();
        code.append("// Add to app/Providers/AppServiceProvider.php in boot() method:\n\n");
        
        for (String modelName : modelNames) {
            code.append(String.format(
                "%s::observe(%sObserver::class);\n",
                modelName, modelName
            ));
        }
        
        return code.toString();
    }
    
    /**
     * Lowercase first character
     */
    private String lowerFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
