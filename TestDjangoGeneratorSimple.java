import com.basiccode.generator.generator.python.django.generators.DjangoRelationshipEnhancedGenerator;

/**
 * Test simple pour vérifier que DjangoRelationshipEnhancedGenerator fonctionne
 */
public class TestDjangoGeneratorSimple {
    
    public static void main(String[] args) {
        System.out.println("=== Test DjangoRelationshipEnhancedGenerator ===");
        
        try {
            // Test d'instantiation
            DjangoRelationshipEnhancedGenerator generator = new DjangoRelationshipEnhancedGenerator("ecommerce", "shop");
            System.out.println("✅ Generator instantiated successfully");
            
            // Test de génération d'un modèle Through simple
            String throughModel = generator.generateThroughModel(
                createSimpleClassModel("User"), 
                createSimpleClassModel("Order"), 
                null
            );
            
            System.out.println("✅ Through model generated:");
            System.out.println(throughModel);
            
            System.out.println("\n🎉 Test réussi - Le générateur Django fonctionne correctement!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static com.basiccode.generator.model.ClassModel createSimpleClassModel(String name) {
        com.basiccode.generator.model.ClassModel model = new com.basiccode.generator.model.ClassModel();
        model.setName(name);
        return model;
    }
}