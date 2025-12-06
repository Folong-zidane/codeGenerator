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
        return String.format("""
            <?php
            
            namespace App\\Jobs;
            
            use App\\Models\\%s;
            use Illuminate\\Bus\\Queueable;
            use Illuminate\\Contracts\\Queue\\ShouldQueue;
            use Illuminate\\Foundation\\Bus\\Dispatchable;
            use Illuminate\\Queue\\InteractsWithQueue;
            use Illuminate\\Queue\\SerializesModels;
            
            /**
             * %s Job
             * 
             * Handles async operations for %s model
             */
            class %s implements ShouldQueue
            {
                use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;
            
                private %s $model;
            
                /**
                 * Create a new job instance.
                 */
                public function __construct(%s $model)
                {
                    $this->model = $model;
                    $this->queue = 'default';
                    $this->delay = now()->addSeconds(5);
                }
            
                /**
                 * Execute the job.
                 */
                public function handle(): void
                {
                    // TODO: Implement job logic
                    logger('Job executed for ' . $this->model->id);
                }
            
                /**
                 * Handle a job failure.
                 */
                public function failed(\\Exception $exception): void
                {
                    logger('Job failed: ' . $exception->getMessage());
                }
            }
            """,
            modelName, jobName, modelName, jobName, modelName, modelName
        );
    }
    
    /**
     * Generate Event class
     */
    public String generateEventClass(String eventName, String modelName) {
        return String.format("""
            <?php
            
            namespace App\\Events;
            
            use App\\Models\\%s;
            use Illuminate\\Broadcasting\\InteractsWithSockets;
            use Illuminate\\Broadcasting\\PrivateChannel;
            use Illuminate\\Contracts\\Broadcasting\\ShouldBroadcast;
            use Illuminate\\Foundation\\Events\\Dispatchable;
            use Illuminate\\Queue\\SerializesModels;
            
            /**
             * %s Event
             * 
             * Event for %s model
             */
            class %s implements ShouldBroadcast
            {
                use Dispatchable, InteractsWithSockets, SerializesModels;
            
                public %s $model;
            
                /**
                 * Create a new event instance.
                 */
                public function __construct(%s $model)
                {
                    $this->model = $model;
                }
            
                /**
                 * Get the channels the event should broadcast on.
                 */
                public function broadcastOn(): array
                {
                    return [
                        new PrivateChannel('%s-' . $this->model->id),
                    ];
                }
            
                /**
                 * Get the data to broadcast.
                 */
                public function broadcastWith(): array
                {
                    return [
                        'id' => $this->model->id,
                        'timestamp' => now(),
                    ];
                }
            }
            """,
            modelName, eventName, modelName, eventName, modelName, modelName, modelName.toLowerCase()
        );
    }
    
    /**
     * Generate Listener class for event
     */
    public String generateListenerClass(String listenerName, String eventName) {
        return String.format("""
            <?php
            
            namespace App\\Listeners;
            
            use App\\Events\\%s;
            use Illuminate\\Contracts\\Queue\\ShouldQueue;
            use Illuminate\\Queue\\SerializesModels;
            
            /**
             * %s Listener
             * 
             * Handles %s events
             */
            class %s implements ShouldQueue
            {
                use SerializesModels;
            
                /**
                 * Create the event listener.
                 */
                public function __construct()
                {
                    //
                }
            
                /**
                 * Handle the event.
                 */
                public function handle(%s $event): void
                {
                    // TODO: Implement listener logic
                    logger('%s event handled: ' . $event->model->id);
                }
            }
            """,
            eventName, listenerName, eventName, listenerName, eventName, eventName
        );
    }
    
    /**
     * Generate Observer class for model events
     */
    public String generateObserverClass(String modelName) {
        return String.format("""
            <?php
            
            namespace App\\Observers;
            
            use App\\Models\\%s;
            use Illuminate\\Database\\Eloquent\\Model;
            
            /**
             * %s Observer
             * 
             * Observes %s model events (create, update, delete, etc.)
             */
            class %sObserver
            {
                /**
                 * Handle the %s "created" event.
                 */
                public function created(%s $%s): void
                {
                    logger('%s created: ' . $%s->id);
                }
            
                /**
                 * Handle the %s "updated" event.
                 */
                public function updated(%s $%s): void
                {
                    logger('%s updated: ' . $%s->id);
                }
            
                /**
                 * Handle the %s "deleted" event.
                 */
                public function deleted(%s $%s): void
                {
                    logger('%s deleted: ' . $%s->id);
                }
            
                /**
                 * Handle the %s "restored" event.
                 */
                public function restored(%s $%s): void
                {
                    logger('%s restored: ' . $%s->id);
                }
            
                /**
                 * Handle the %s "force deleted" event.
                 */
                public function forceDeleted(%s $%s): void
                {
                    logger('%s force deleted: ' . $%s->id);
                }
                
                /**
                 * Handle the %s "saving" event.
                 */
                public function saving(%s $%s): bool
                {
                    // TODO: Add pre-save logic (validation, transformation, etc.)
                    return true;
                }
            }
            """,
            modelName, modelName, modelName, modelName,
            modelName, modelName, lowerFirst(modelName),
            modelName, lowerFirst(modelName),
            modelName, modelName, lowerFirst(modelName),
            modelName, lowerFirst(modelName),
            modelName, modelName, lowerFirst(modelName),
            modelName, lowerFirst(modelName),
            modelName, modelName, lowerFirst(modelName),
            modelName, lowerFirst(modelName),
            modelName, modelName, lowerFirst(modelName),
            modelName, lowerFirst(modelName)
        );
    }
    
    /**
     * Generate API Resource class (transformer)
     */
    public String generateResourceClass(String modelName) {
        return String.format("""
            <?php
            
            namespace App\\Http\\Resources;
            
            use Illuminate\\Http\\Request;
            use Illuminate\\Http\\Resources\\Json\\JsonResource;
            
            /**
             * %s Resource
             * 
             * Transforms %s model for API responses
             */
            class %sResource extends JsonResource
            {
                /**
                 * Transform the resource into an array.
                 */
                public function toArray(Request $request): array
                {
                    return [
                        'id' => $this->id,
                        // TODO: Add other fields from %s model
                        'created_at' => $this->created_at,
                        'updated_at' => $this->updated_at,
                    ];
                }
            }
            """,
            modelName, modelName, modelName, modelName
        );
    }
    
    /**
     * Generate API Resource Collection
     */
    public String generateResourceCollectionClass(String modelName) {
        return String.format("""
            <?php
            
            namespace App\\Http\\Resources;
            
            use Illuminate\\Http\\Resources\\Json\\ResourceCollection;
            
            /**
             * %s Collection Resource
             * 
             * Transforms collection of %s models for API responses
             */
            class %sCollection extends ResourceCollection
            {
                /**
                 * Transform the resource collection into an array.
                 */
                public function toArray($request): array
                {
                    return [
                        'data' => $this->collection,
                        'meta' => [
                            'total' => $this->collection->count(),
                            'timestamp' => now(),
                        ],
                    ];
                }
            }
            """,
            modelName, modelName, modelName
        );
    }
    
    /**
     * Generate Middleware class
     */
    public String generateMiddlewareClass(String middlewareName) {
        return String.format("""
            <?php
            
            namespace App\\Http\\Middleware;
            
            use Closure;
            use Illuminate\\Http\\Request;
            use Symfony\\Component\\HttpFoundation\\Response;
            
            /**
             * %s Middleware
             */
            class %s
            {
                /**
                 * Handle an incoming request.
                 */
                public function handle(Request $request, Closure $next): Response
                {
                    // TODO: Implement middleware logic
                    
                    return $next($request);
                }
            }
            """,
            middlewareName, middlewareName
        );
    }
    
    /**
     * Generate API Response Trait
     */
    public String generateApiResponseTrait() {
        return """
            <?php
            
            namespace App\\Traits;
            
            use Illuminate\\Http\\JsonResponse;
            
            /**
             * API Response Trait
             * 
             * Provides consistent JSON response methods
             */
            trait ApiResponse
            {
                /**
                 * Send a successful JSON response
                 */
                protected function success($data = null, string $message = 'Success', int $statusCode = 200): JsonResponse
                {
                    return response()->json([
                        'success' => true,
                        'message' => $message,
                        'data' => $data,
                    ], $statusCode);
                }
            
                /**
                 * Send an error JSON response
                 */
                protected function error(string $message = 'Error', int $statusCode = 400, $errors = null): JsonResponse
                {
                    return response()->json([
                        'success' => false,
                        'message' => $message,
                        'errors' => $errors,
                    ], $statusCode);
                }
            
                /**
                 * Send paginated JSON response
                 */
                protected function paginated($data, string $message = 'Success'): JsonResponse
                {
                    return response()->json([
                        'success' => true,
                        'message' => $message,
                        'data' => $data->items(),
                        'pagination' => [
                            'total' => $data->total(),
                            'per_page' => $data->perPage(),
                            'current_page' => $data->currentPage(),
                            'last_page' => $data->lastPage(),
                            'from' => $data->firstItem(),
                            'to' => $data->lastItem(),
                        ],
                    ]);
                }
            
                /**
                 * Send not found response
                 */
                protected function notFound(string $message = 'Resource not found'): JsonResponse
                {
                    return $this->error($message, 404);
                }
            
                /**
                 * Send unauthorized response
                 */
                protected function unauthorized(string $message = 'Unauthorized'): JsonResponse
                {
                    return $this->error($message, 401);
                }
            
                /**
                 * Send forbidden response
                 */
                protected function forbidden(string $message = 'Forbidden'): JsonResponse
                {
                    return $this->error($message, 403);
                }
            }
            """;
    }
    
    /**
     * Generate service provider registration for observers
     */
    public String generateObserverRegistration(List<String> modelNames) {
        StringBuilder code = new StringBuilder();
        code.append("// Add to app/Providers/AppServiceProvider.php in boot() method:\n\n");
        
        for (String modelName : modelNames) {
            code.append(String.format("""
                %s::observe(%sObserver::class);
                """,
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
