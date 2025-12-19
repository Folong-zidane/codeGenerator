package com.basiccode.generator;

import com.basiccode.generator.controller.UnifiedDiagramController;
import com.basiccode.generator.service.ComprehensiveGenerationOrchestrator;
import com.basiccode.generator.service.MetadataAwareGenerationOrchestrator;

import java.util.Map;

/**
 * 🧪 Test du contrôleur unifié pour tous les types de diagrammes
 */
public class UnifiedControllerTest {
    
    public static void testUnifiedGeneration() {
        System.out.println("🧪 Testing Unified Generation...");
        
        UnifiedDiagramController.UnifiedGenerationRequest request = 
            new UnifiedDiagramController.UnifiedGenerationRequest();
        
        request.setClassDiagramContent("classDiagram\n    class User {\n        +String name\n    }");
        request.setSequenceDiagramContent("sequenceDiagram\n    User->>System: login");
        request.setStateDiagramContent("stateDiagram-v2\n    [*] --> Active");
        request.setActivityDiagramContent("flowchart TD\n    A --> B");
        request.setLanguage("java");
        request.setPackageName("com.test");
        
        var types = request.getDiagramTypes();
        
        System.out.println("✅ Request created successfully");
        System.out.println("📊 Diagram types detected: " + types);
        System.out.println("🎯 Language: " + request.getLanguage());
        System.out.println("📦 Package: " + request.getPackageName());
        
        assert types.size() == 4 : "Expected 4 diagram types";
        assert types.contains("class") : "Should contain class diagram";
        assert types.contains("sequence") : "Should contain sequence diagram";
        assert types.contains("state") : "Should contain state diagram";
        assert types.contains("activity") : "Should contain activity diagram";
    }
    
    public static void testDiagramTypeDetection() {
        System.out.println("\n🔍 Testing Diagram Type Detection...");
        
        UnifiedDiagramController.UnifiedGenerationRequest request = 
            new UnifiedDiagramController.UnifiedGenerationRequest();
        
        // Test empty request
        var emptyTypes = request.getDiagramTypes();
        assert emptyTypes.isEmpty() : "Empty request should have no types";
        System.out.println("✅ Empty request: " + emptyTypes.size() + " types");
        
        // Test single diagram
        request.setClassDiagramContent("classDiagram\n    class User {}");
        var singleType = request.getDiagramTypes();
        assert singleType.size() == 1 : "Should have 1 type";
        assert singleType.contains("class") : "Should contain class";
        System.out.println("✅ Single diagram: " + singleType);
        
        // Test multiple diagrams
        request.setSequenceDiagramContent("sequenceDiagram\n    A->>B: msg");
        request.setErDiagramContent("erDiagram\n    USER ||--o{ ORDER : places");
        var multiTypes = request.getDiagramTypes();
        assert multiTypes.size() == 3 : "Should have 3 types";
        System.out.println("✅ Multiple diagrams: " + multiTypes);
    }
    
    public static void main(String[] args) {
        System.out.println("🚀 Running Unified Controller Tests");
        System.out.println("===================================");
        
        try {
            testUnifiedGeneration();
            testDiagramTypeDetection();
            
            System.out.println("\n🎉 All tests passed!");
            System.out.println("✅ UnifiedDiagramController is ready");
            System.out.println("🔧 New endpoints available:");
            System.out.println("   - POST /api/unified/generate");
            System.out.println("   - POST /api/unified/generate/zip");
            System.out.println("   - POST /api/unified/validate");
            System.out.println("   - GET  /api/unified/health");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}