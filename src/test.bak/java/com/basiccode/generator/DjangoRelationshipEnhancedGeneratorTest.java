package com.basiccode.generator;

import com.basiccode.generator.generator.python.django.generators.DjangoRelationshipEnhancedGenerator;
import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import com.basiccode.generator.model.Relationship;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test simple pour DjangoRelationshipEnhancedGenerator
 */
class DjangoRelationshipEnhancedGeneratorTest {

    private DjangoRelationshipEnhancedGenerator generator;
    private ClassModel userModel;
    private ClassModel orderModel;

    @BeforeEach
    void setUp() {
        generator = new DjangoRelationshipEnhancedGenerator("ecommerce", "shop");
        
        // Créer des modèles de test
        userModel = new ClassModel();
        userModel.setName("User");
        
        orderModel = new ClassModel();
        orderModel.setName("Order");
    }

    @Test
    @DisplayName("Generator can be instantiated")
    void testGeneratorInstantiation() {
        assertNotNull(generator, "Generator should be created");
    }

    @Test
    @DisplayName("Generate ForeignKey field")
    void testGenerateForeignKeyField() {
        Relationship relationship = new Relationship();
        relationship.setType("ONETOMANY");
        relationship.setSourceProperty("user");
        relationship.setTargetProperty("orders");
        relationship.setCascadeDelete(true);
        relationship.setSourceMultiplicity(1);

        String result = generator.generateEnhancedRelationshipField(relationship, orderModel, userModel);
        
        assertNotNull(result);
        assertTrue(result.contains("models.ForeignKey"));
        assertTrue(result.contains("'User'"));
        assertTrue(result.contains("on_delete=models.CASCADE"));
        assertTrue(result.contains("related_name='orders'"));
    }

    @Test
    @DisplayName("Generate ManyToMany field")
    void testGenerateManyToManyField() {
        Relationship relationship = new Relationship();
        relationship.setType("MANYTOMANY");
        relationship.setSourceProperty("tags");
        relationship.setTargetProperty("products");
        relationship.setSourceMultiplicity(0);

        String result = generator.generateEnhancedRelationshipField(relationship, orderModel, userModel);
        
        assertNotNull(result);
        assertTrue(result.contains("models.ManyToManyField"));
        assertTrue(result.contains("'User'"));
        assertTrue(result.contains("related_name='products'"));
    }

    @Test
    @DisplayName("Generate OneToOne field")
    void testGenerateOneToOneField() {
        Relationship relationship = new Relationship();
        relationship.setType("ONETOONE");
        relationship.setSourceProperty("profile");
        relationship.setTargetProperty("user");
        relationship.setCascadeDelete(false);
        relationship.setSourceMultiplicity(0);

        String result = generator.generateEnhancedRelationshipField(relationship, orderModel, userModel);
        
        assertNotNull(result);
        assertTrue(result.contains("models.OneToOneField"));
        assertTrue(result.contains("'User'"));
        assertTrue(result.contains("on_delete=models.PROTECT"));
        assertTrue(result.contains("related_name='user'"));
    }

    @Test
    @DisplayName("Generate through model")
    void testGenerateThroughModel() {
        List<Field> extraFields = new ArrayList<>();
        Field quantityField = new Field();
        quantityField.setName("quantity");
        quantityField.setType("int");
        extraFields.add(quantityField);

        String result = generator.generateThroughModel(userModel, orderModel, extraFields);
        
        assertNotNull(result);
        assertTrue(result.contains("class UserOrder(models.Model):"));
        assertTrue(result.contains("user = models.ForeignKey('User'"));
        assertTrue(result.contains("order = models.ForeignKey('Order'"));
        assertTrue(result.contains("quantity = models.IntegerField()"));
        assertTrue(result.contains("created_at = models.DateTimeField(auto_now_add=True)"));
        assertTrue(result.contains("def __str__(self):"));
    }

    @Test
    @DisplayName("Generate query optimization hints")
    void testGenerateQueryOptimizationHints() {
        List<Relationship> relationships = new ArrayList<>();
        
        Relationship rel1 = new Relationship();
        rel1.setType("ONETOMANY");
        rel1.setSourceProperty("user");
        relationships.add(rel1);
        
        Relationship rel2 = new Relationship();
        rel2.setType("MANYTOMANY");
        rel2.setSourceProperty("tags");
        relationships.add(rel2);

        String result = generator.generateQueryOptimizationHints(relationships);
        
        assertNotNull(result);
        assertTrue(result.contains("@classmethod"));
        assertTrue(result.contains("def get_optimized_queryset(cls):"));
        assertTrue(result.contains("select_related"));
        assertTrue(result.contains("prefetch_related"));
    }
}