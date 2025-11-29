package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class PhpControllerGenerator implements IControllerGenerator {
    
    @Override
    public String generateController(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Http\\Controllers\\Api;\n\n");
        code.append("use ").append(packageName).append("\\Http\\Controllers\\Controller;\n");
        code.append("use ").append(packageName).append("\\Models\\").append(className).append(";\n");
        code.append("use ").append(packageName).append("\\Services\\").append(className).append("Service;\n");
        code.append("use ").append(packageName).append("\\Http\\Resources\\").append(className).append("Resource;\n");
        code.append("use ").append(packageName).append("\\Http\\Requests\\Store").append(className).append("Request;\n");
        code.append("use ").append(packageName).append("\\Http\\Requests\\Update").append(className).append("Request;\n");
        code.append("use Illuminate\\Http\\Request;\n");
        code.append("use Illuminate\\Http\\JsonResponse;\n");
        code.append("use Illuminate\\Http\\Resources\\Json\\AnonymousResourceCollection;\n");
        code.append("use Illuminate\\Support\\Facades\\Log;\n\n");
        
        code.append("class ").append(className).append("Controller extends Controller\n");
        code.append("{\n");
        code.append("    protected ").append(className).append("Service $service;\n\n");
        
        // Constructor
        code.append("    public function __construct(").append(className).append("Service $service)\n");
        code.append("    {\n");
        code.append("        $this->service = $service;\n");
        code.append("    }\n\n");
        
        // Index method
        code.append("    /**\n");
        code.append("     * Display a listing of the resource.\n");
        code.append("     */\n");
        code.append("    public function index(Request $request): AnonymousResourceCollection\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $perPage = $request->get('per_page', 15);\n");
        code.append("            $entities = $this->service->getPaginated($perPage);\n");
        code.append("            \n");
        code.append("            return ").append(className).append("Resource::collection($entities);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error fetching ").append(className.toLowerCase()).append("s: ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Internal server error'], 500);\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Show method
        code.append("    /**\n");
        code.append("     * Display the specified resource.\n");
        code.append("     */\n");
        code.append("    public function show(int $id): JsonResponse\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->service->findById($id);\n");
        code.append("            \n");
        code.append("            if (!$entity) {\n");
        code.append("                return response()->json(['error' => '").append(className).append(" not found'], 404);\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            return response()->json(new ").append(className).append("Resource($entity));\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error fetching ").append(className.toLowerCase()).append(": ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Internal server error'], 500);\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Store method
        code.append("    /**\n");
        code.append("     * Store a newly created resource in storage.\n");
        code.append("     */\n");
        code.append("    public function store(Store").append(className).append("Request $request): JsonResponse\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->service->create($request->validated());\n");
        code.append("            \n");
        code.append("            return response()->json(new ").append(className).append("Resource($entity), 201);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error creating ").append(className.toLowerCase()).append(": ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Bad request', 'message' => $e->getMessage()], 400);\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Update method
        code.append("    /**\n");
        code.append("     * Update the specified resource in storage.\n");
        code.append("     */\n");
        code.append("    public function update(Update").append(className).append("Request $request, int $id): JsonResponse\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->service->update($id, $request->validated());\n");
        code.append("            \n");
        code.append("            if (!$entity) {\n");
        code.append("                return response()->json(['error' => '").append(className).append(" not found'], 404);\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            return response()->json(new ").append(className).append("Resource($entity));\n");
        code.append("        } catch (\\InvalidArgumentException $e) {\n");
        code.append("            return response()->json(['error' => 'Entity not found', 'message' => $e->getMessage()], 404);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error updating ").append(className.toLowerCase()).append(": ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Bad request', 'message' => $e->getMessage()], 400);\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Destroy method
        code.append("    /**\n");
        code.append("     * Remove the specified resource from storage.\n");
        code.append("     */\n");
        code.append("    public function destroy(int $id): JsonResponse\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $deleted = $this->service->delete($id);\n");
        code.append("            \n");
        code.append("            if (!$deleted) {\n");
        code.append("                return response()->json(['error' => '").append(className).append(" not found'], 404);\n");
        code.append("            }\n");
        code.append("            \n");
        code.append("            return response()->json(null, 204);\n");
        code.append("        } catch (\\InvalidArgumentException $e) {\n");
        code.append("            return response()->json(['error' => 'Entity not found', 'message' => $e->getMessage()], 404);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error deleting ").append(className.toLowerCase()).append(": ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Internal server error'], 500);\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // State management methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateManagementMethods(code, className);
        }
        
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getControllerDirectory() {
        return "Http/Controllers/Api";
    }
    
    private void generateStateManagementMethods(StringBuilder code, String className) {
        String lowerClassName = className.toLowerCase();
        
        // Suspend method
        code.append("    /**\n");
        code.append("     * Suspend the specified ").append(lowerClassName).append(".\n");
        code.append("     */\n");
        code.append("    public function suspend(int $id): JsonResponse\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->service->suspend").append(className).append("($id);\n");
        code.append("            \n");
        code.append("            return response()->json(new ").append(className).append("Resource($entity));\n");
        code.append("        } catch (\\InvalidArgumentException $e) {\n");
        code.append("            return response()->json(['error' => 'Entity not found', 'message' => $e->getMessage()], 404);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error suspending ").append(lowerClassName).append(": ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Invalid state transition', 'message' => $e->getMessage()], 400);\n");
        code.append("        }\n");
        code.append("    }\n\n");
        
        // Activate method
        code.append("    /**\n");
        code.append("     * Activate the specified ").append(lowerClassName).append(".\n");
        code.append("     */\n");
        code.append("    public function activate(int $id): JsonResponse\n");
        code.append("    {\n");
        code.append("        try {\n");
        code.append("            $entity = $this->service->activate").append(className).append("($id);\n");
        code.append("            \n");
        code.append("            return response()->json(new ").append(className).append("Resource($entity));\n");
        code.append("        } catch (\\InvalidArgumentException $e) {\n");
        code.append("            return response()->json(['error' => 'Entity not found', 'message' => $e->getMessage()], 404);\n");
        code.append("        } catch (\\Exception $e) {\n");
        code.append("            Log::error('Error activating ").append(lowerClassName).append(": ' . $e->getMessage());\n");
        code.append("            return response()->json(['error' => 'Invalid state transition', 'message' => $e->getMessage()], 400);\n");
        code.append("        }\n");
        code.append("    }\n\n");
    }
}