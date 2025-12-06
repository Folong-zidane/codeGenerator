package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.Attribute;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.parser.DjangoModelParser.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 🚀 PHASE 1: Adapter pour intégrer DjangoModelGenerator avancé
 * 
 * Convertit les entités UML vers le format DjangoModelParser
 * et utilise le générateur avancé existant (350+ lignes)
 */
@Component
@Slf4j
public class DjangoModelGeneratorAdapter implements IEntityGenerator {
    
    private final DjangoModelGenerator advancedGenerator = new DjangoModelGenerator();
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
    
    @Override
    public String getEntityDirectory() {
        return "models";
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        return ""; // Django enums sont inclus dans les modèles
    }
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        log.info("🚀 Using advanced DjangoModelGenerator for: {}", enhancedClass.getOriginalClass().getName());
        
        // Convertir Entity UML vers DjangoModel
        DjangoModel djangoModel = convertToDjangoModel(enhancedClass);
        DjangoModels models = new DjangoModels(Arrays.asList(djangoModel));
        
        // Utiliser le générateur avancé
        return advancedGenerator.generateModels(models);
    }
    
    private DjangoModel convertToDjangoModel(EnhancedClass enhancedClass) {
        DjangoModel model = new DjangoModel();
        String name = enhancedClass.getOriginalClass().getName();
        model.setName(name);
        model.setDocstring(name + " model with advanced Django features");
        
        // Convertir les attributs
        List<DjangoField> fields = new ArrayList<>();
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            fields = enhancedClass.getOriginalClass().getAttributes().stream()
                .map(this::convertToDjangoField)
                .collect(Collectors.toList());
        }
        model.setFields(fields);
        
        // Ajouter méthodes métier depuis UML (si disponibles)
        List<DjangoMethod> methods = new ArrayList<>();
        model.setMethods(methods);
        
        return model;
    }
    
    private DjangoField convertToDjangoField(Object attr) {
        DjangoField field = new DjangoField();
        
        // Gérer les deux types: UmlAttribute et Attribute
        if (attr instanceof com.basiccode.generator.model.UmlAttribute) {
            com.basiccode.generator.model.UmlAttribute umlAttr = (com.basiccode.generator.model.UmlAttribute) attr;
            field.setName(umlAttr.getName());
            field.setFieldType(mapToDjangoFieldType(umlAttr.getType()));
        } else if (attr instanceof Attribute) {
            Attribute modelAttr = (Attribute) attr;
            field.setName(modelAttr.getName());
            field.setFieldType(mapToDjangoFieldType(modelAttr.getType()));
            if ("email".equalsIgnoreCase(modelAttr.getName())) {
                field.addValidator("EmailValidator()");
            }
        }
        
        return field;
    }
    
    private String mapToDjangoFieldType(String umlType) {
        switch (umlType.toLowerCase()) {
            case "string": return "CharField";
            case "int": case "integer": return "IntegerField";
            case "float": case "double": return "FloatField";
            case "boolean": return "BooleanField";
            case "date": return "DateField";
            case "datetime": return "DateTimeField";
            case "uuid": return "UUIDField";
            case "email": return "EmailField";
            case "url": return "URLField";
            case "text": return "TextField";
            default: return "CharField";
        }
    }
    
}