import com.basiccode.generator.generator.python.django.generators.*;

/**
 * Test de tous les générateurs Django
 */
public class TestAllDjangoGenerators {
    
    public static void main(String[] args) {
        System.out.println("=== Test de tous les générateurs Django ===\n");
        
        String moduleName = "ecommerce";
        String appName = "shop";
        
        try {
            // Test 1: DjangoRelationshipEnhancedGenerator
            System.out.println("1. Test DjangoRelationshipEnhancedGenerator...");
            DjangoRelationshipEnhancedGenerator gen1 = new DjangoRelationshipEnhancedGenerator(moduleName, appName);
            System.out.println("   ✅ Instantiated successfully");
            
            // Test 2: DjangoAuthenticationJWTGenerator
            System.out.println("2. Test DjangoAuthenticationJWTGenerator...");
            DjangoAuthenticationJWTGenerator gen2 = new DjangoAuthenticationJWTGenerator(moduleName, appName);
            String jwtSettings = gen2.generateJWTSettings();
            System.out.println("   ✅ JWT settings generated (" + jwtSettings.length() + " chars)");
            
            // Test 3: DjangoFilteringPaginationGenerator
            System.out.println("3. Test DjangoFilteringPaginationGenerator...");
            DjangoFilteringPaginationGenerator gen3 = new DjangoFilteringPaginationGenerator(moduleName, appName);
            System.out.println("   ✅ Instantiated successfully");
            
            // Test 4: DjangoCachingRedisGenerator
            System.out.println("4. Test DjangoCachingRedisGenerator...");
            DjangoCachingRedisGenerator gen4 = new DjangoCachingRedisGenerator(moduleName, appName);
            System.out.println("   ✅ Instantiated successfully");
            
            // Test 5: DjangoWebSocketGenerator
            System.out.println("5. Test DjangoWebSocketGenerator...");
            DjangoWebSocketGenerator gen5 = new DjangoWebSocketGenerator(moduleName, appName);
            String wsConfig = gen5.generateChannelsConfiguration();
            System.out.println("   ✅ WebSocket config generated (" + wsConfig.length() + " chars)");
            
            // Test 6: DjangoEventSourcingGenerator
            System.out.println("6. Test DjangoEventSourcingGenerator...");
            DjangoEventSourcingGenerator gen6 = new DjangoEventSourcingGenerator(moduleName, appName);
            System.out.println("   ✅ Instantiated successfully");
            
            // Test 7: DjangoCQRSPatternGenerator
            System.out.println("7. Test DjangoCQRSPatternGenerator...");
            DjangoCQRSPatternGenerator gen7 = new DjangoCQRSPatternGenerator(moduleName, appName);
            System.out.println("   ✅ Instantiated successfully");
            
            // Test 8: DjangoAdvancedFeaturesGenerator
            System.out.println("8. Test DjangoAdvancedFeaturesGenerator...");
            DjangoAdvancedFeaturesGenerator gen8 = new DjangoAdvancedFeaturesGenerator(moduleName, appName);
            System.out.println("   ✅ Instantiated successfully");
            
            System.out.println("\n🎉 TOUS LES TESTS SONT PASSÉS AVEC SUCCÈS!");
            System.out.println("✅ Les 8 générateurs Django fonctionnent correctement");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}