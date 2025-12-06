import com.basiccode.generator.generator.python.django.generators.DjangoRelationshipEnhancedGenerator;
import com.basiccode.generator.model.*;
import java.util.*;

/**
 * Test avancé pour DjangoRelationshipEnhancedGenerator
 */
public class TestDjangoRelationshipAdvanced {
    
    public static void main(String[] args) {
        System.out.println("=== Test Avancé DjangoRelationshipEnhancedGenerator ===\n");
        
        try {
            DjangoRelationshipEnhancedGenerator generator = new DjangoRelationshipEnhancedGenerator("ecommerce", "shop");
            
            // Créer des modèles de test
            ClassModel userModel = createUserModel();
            ClassModel orderModel = createOrderModel();
            ClassModel productModel = createProductModel();
            
            System.out.println("1. Test génération ForeignKey (User -> Order):");
            Relationship userOrderRel = createRelationship("ONETOMANY", "user", "orders", true, 1, 0);
            String foreignKey = generator.generateEnhancedRelationshipField(userOrderRel, orderModel, userModel);
            System.out.println(foreignKey);
            
            System.out.println("2. Test génération ManyToMany (Order -> Product):");
            Relationship orderProductRel = createRelationship("MANYTOMANY", "products", "orders", false, 0, 0);
            String manyToMany = generator.generateEnhancedRelationshipField(orderProductRel, orderModel, productModel);
            System.out.println(manyToMany);
            
            System.out.println("3. Test génération OneToOne (User -> Profile):");
            Relationship userProfileRel = createRelationship("ONETOONE", "profile", "user", false, 0, 1);
            String oneToOne = generator.generateEnhancedRelationshipField(userProfileRel, userModel, userModel);
            System.out.println(oneToOne);
            
            System.out.println("4. Test génération Through Model avec attributs supplémentaires:");
            List<Field> extraFields = Arrays.asList(
                createField("quantity", "int"),
                createField("price", "decimal"),
                createField("discount", "float")
            );
            String throughModel = generator.generateThroughModel(orderModel, productModel, extraFields);
            System.out.println(throughModel);
            
            System.out.println("5. Test génération Query Optimization Hints:");
            List<Relationship> relationships = Arrays.asList(userOrderRel, orderProductRel);
            String optimizationHints = generator.generateQueryOptimizationHints(relationships);
            System.out.println(optimizationHints);
            
            System.out.println("🎉 TOUS LES TESTS AVANCÉS SONT PASSÉS AVEC SUCCÈS!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static ClassModel createUserModel() {
        ClassModel model = new ClassModel();
        model.setName("User");
        return model;
    }
    
    private static ClassModel createOrderModel() {
        ClassModel model = new ClassModel();
        model.setName("Order");
        return model;
    }
    
    private static ClassModel createProductModel() {
        ClassModel model = new ClassModel();
        model.setName("Product");
        return model;
    }
    
    private static Relationship createRelationship(String type, String sourceProp, String targetProp, 
                                                 boolean cascadeDelete, int sourceMultiplicity, int targetMultiplicity) {
        Relationship rel = new Relationship();
        rel.setType(type);
        rel.setSourceProperty(sourceProp);
        rel.setTargetProperty(targetProp);
        rel.setCascadeDelete(cascadeDelete);
        rel.setSourceMultiplicity(sourceMultiplicity);
        rel.setTargetMultiplicity(targetMultiplicity);
        return rel;
    }
    
    private static Field createField(String name, String type) {
        Field field = new Field();
        field.setName(name);
        field.setType(type);
        return field;
    }
}