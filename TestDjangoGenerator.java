import com.basiccode.generator.generator.python.django.generators.DjangoRelationshipEnhancedGenerator;
import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import com.basiccode.generator.model.Relationship;
import java.util.ArrayList;
import java.util.List;

/**
 * Test simple pour vérifier que DjangoRelationshipEnhancedGenerator fonctionne
 */
public class TestDjangoGenerator {
    
    public static void main(String[] args) {
        System.out.println("=== Test DjangoRelationshipEnhancedGenerator ===");
        
        try {
            // Test 1: Instantiation
            DjangoRelationshipEnhancedGenerator generator = new DjangoRelationshipEnhancedGenerator("ecommerce", "shop");
            System.out.println("✅ Generator instantiated successfully");
            
            // Test 2: Créer des modèles de test
            ClassModel userModel = new ClassModel();
            userModel.setName("User");
            
            ClassModel orderModel = new ClassModel();
            orderModel.setName("Order");
            
            // Test 3: Générer un champ ForeignKey
            Relationship relationship = new Relationship();
            relationship.setType("ONETOMANY");
            relationship.setSourceProperty("user");
            relationship.setTargetProperty("orders");
            relationship.setCascadeDelete(true);
            relationship.setSourceMultiplicity(1);
            
            String foreignKeyField = generator.generateEnhancedRelationshipField(relationship, orderModel, userModel);
            System.out.println("✅ ForeignKey field generated:");
            System.out.println(foreignKeyField);
            
            // Test 4: Générer un champ ManyToMany
            relationship.setType("MANYTOMANY");
            relationship.setSourceProperty("tags");
            relationship.setTargetProperty("products");
            
            String manyToManyField = generator.generateEnhancedRelationshipField(relationship, orderModel, userModel);
            System.out.println("✅ ManyToMany field generated:");
            System.out.println(manyToManyField);
            
            // Test 5: Générer un modèle Through
            List<Field> extraFields = new ArrayList<>();
            Field quantityField = new Field();
            quantityField.setName("quantity");
            quantityField.setType("int");
            extraFields.add(quantityField);
            
            String throughModel = generator.generateThroughModel(userModel, orderModel, extraFields);
            System.out.println("✅ Through model generated:");
            System.out.println(throughModel);
            
            // Test 6: Générer des hints d'optimisation
            List<Relationship> relationships = new ArrayList<>();
            relationships.add(relationship);
            
            String optimizationHints = generator.generateQueryOptimizationHints(relationships);
            System.out.println("✅ Query optimization hints generated:");
            System.out.println(optimizationHints);
            
            System.out.println("\n🎉 Tous les tests sont passés avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}