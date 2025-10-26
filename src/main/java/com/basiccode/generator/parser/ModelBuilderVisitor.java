package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;
import java.util.*;

public class ModelBuilderVisitor extends MermaidClassBaseVisitor<Object> {
    
    private final Diagram diagram = new Diagram();
    private final Map<String, ClassModel> classMap = new HashMap<>();
    private ClassModel currentClass;
    
    @Override
    public Diagram visitClassDiagram(MermaidClassParser.ClassDiagramContext ctx) {
        // Visit all statements
        ctx.statement().forEach(this::visit);
        return diagram;
    }
    
    // ========== CLASS DECLARATIONS ==========
    
    @Override
    public Object visitFullClass(MermaidClassParser.FullClassContext ctx) {
        String name = ctx.className().getText();
        ClassModel classModel = getOrCreateClass(name);
        
        // Apply stereotype
        if (ctx.stereotype() != null) {
            applyStereotype(classModel, ctx.stereotype());
        }
        
        // Visit class body
        if (ctx.classBody() != null) {
            currentClass = classModel;
            visit(ctx.classBody());
            currentClass = null;
        }
        
        return classModel;
    }
    
    @Override
    public Object visitSimpleClass(MermaidClassParser.SimpleClassContext ctx) {
        String name = ctx.className().getText();
        ClassModel classModel = getOrCreateClass(name);
        
        if (ctx.stereotype() != null) {
            applyStereotype(classModel, ctx.stereotype());
        }
        
        return classModel;
    }
    
    // ========== STEREOTYPES ==========
    
    private void applyStereotype(ClassModel classModel, MermaidClassParser.StereotypeContext ctx) {
        String stereotype = ctx.stereotypeType().getText();
        classModel.setStereotype(stereotype);
        
        switch (stereotype) {
            case "abstract" -> classModel.setAbstract(true);
            case "interface" -> classModel.setInterface(true);
            case "enumeration" -> classModel.setEnumeration(true);
            case "service" -> classModel.setService(true);
            case "repository" -> classModel.setRepository(true);
            case "controller" -> classModel.setController(true);
            case "entity" -> classModel.setEntity(true);
        }
    }
    
    // ========== CLASS MEMBERS ==========
    
    @Override
    public Object visitAttributeMember(MermaidClassParser.AttributeMemberContext ctx) {
        if (currentClass == null) return null;
        
        Field field = new Field();
        
        // Handle both syntax: "name: Type" and "Type name"
        if (ctx.attribute().name != null) {
            field.setName(ctx.attribute().name.getText());
            field.setType(extractType(ctx.attribute().type()));
        } else {
            // "Type name" format
            field.setType(extractType(ctx.attribute().type()));
            field.setName(ctx.attribute().name.getText());
        }
        
        if (ctx.attribute().visibility() != null) {
            field.setVisibility(Visibility.fromSymbol(ctx.attribute().visibility().getText()));
        }
        
        // Process annotations
        if (ctx.attribute().annotation() != null) {
            for (var annotationCtx : ctx.attribute().annotation()) {
                Annotation annotation = processAnnotation(annotationCtx);
                field.addAnnotation(annotation);
            }
        }
        
        // Process constraints
        if (ctx.attribute().constraint() != null) {
            for (var constraintCtx : ctx.attribute().constraint()) {
                Constraint constraint = processConstraint(constraintCtx);
                field.addConstraint(constraint);
            }
        }
        
        // Handle multiplicity
        if (ctx.attribute().multiplicity() != null) {
            field.setMultiplicity(ctx.attribute().multiplicity().getText());
        }
        
        currentClass.addField(field);
        return field;
    }
    
    @Override
    public Object visitMethodMember(MermaidClassParser.MethodMemberContext ctx) {
        if (currentClass == null) return null;
        
        Method method = new Method();
        method.setName(ctx.method().name.getText());
        
        if (ctx.method().visibility() != null) {
            method.setVisibility(Visibility.fromSymbol(ctx.method().visibility().getText()));
        }
        
        // Process annotations
        if (ctx.method().annotation() != null) {
            for (var annotationCtx : ctx.method().annotation()) {
                Annotation annotation = processAnnotation(annotationCtx);
                method.addAnnotation(annotation);
            }
        }
        
        // Process method modifiers
        if (ctx.method().methodModifier() != null) {
            for (var modifierCtx : ctx.method().methodModifier()) {
                method.addModifier(modifierCtx.getText());
            }
        }
        
        // Parameters
        if (ctx.method().parameterList() != null) {
            for (var paramCtx : ctx.method().parameterList().parameter()) {
                Parameter param = new Parameter();
                if (paramCtx.paramName != null) {
                    param.setName(paramCtx.paramName.getText());
                    param.setType(extractType(paramCtx.type()));
                } else {
                    param.setType(extractType(paramCtx.type()));
                    param.setName(paramCtx.paramName.getText());
                }
                method.getParameters().add(param);
            }
        }
        
        // Return type
        if (ctx.method().returnType() != null) {
            method.setReturnType(extractType(ctx.method().returnType().type()));
        } else {
            method.setReturnType("void");
        }
        
        currentClass.addMethod(method);
        return method;
    }
    
    @Override
    public Object visitEnumValueMember(MermaidClassParser.EnumValueMemberContext ctx) {
        if (currentClass == null) return null;
        
        String enumValue = ctx.enumValue().getText();
        // Add to enum values if ClassModel supports it
        return enumValue;
    }
    
    // ========== RELATIONSHIPS ==========
    
    @Override
    public Object visitRelationshipStmt(MermaidClassParser.RelationshipStmtContext ctx) {
        MermaidClassParser.RelationshipContext rel = ctx.relationship();
        
        String fromClass = rel.className(0).getText();
        String toClass = rel.className(1).getText();
        RelationshipType relType = mapRelationType(rel.relType());
        
        Relationship relationship = new Relationship();
        relationship.setFromClass(fromClass);
        relationship.setToClass(toClass);
        relationship.setType(relType);
        
        // Handle inheritance
        if (relType == RelationshipType.INHERITANCE) {
            ClassModel child = getOrCreateClass(fromClass);
            child.setParentClass(toClass);
        }
        
        diagram.addRelationship(relationship);
        return relationship;
    }
    
    // ========== UTILITIES ==========
    
    private ClassModel getOrCreateClass(String name) {
        return classMap.computeIfAbsent(name, k -> {
            ClassModel model = new ClassModel();
            model.setName(name);
            diagram.addClass(model);
            return model;
        });
    }
    
    private String extractType(MermaidClassParser.TypeContext ctx) {
        if (ctx.primitiveType() != null) {
            return ctx.primitiveType().getText();
        } else if (ctx.collectionType() != null) {
            return extractCollectionType(ctx.collectionType());
        } else if (ctx.customType() != null) {
            return extractCustomType(ctx.customType());
        }
        return "Object";
    }
    
    private String extractCollectionType(MermaidClassParser.CollectionTypeContext ctx) {
        String collectionName = ctx.getChild(0).getText(); // List, Set, Map, Collection
        String innerType = extractType(ctx.type(0));
        
        if (collectionName.equals("Map") && ctx.type().size() > 1) {
            String valueType = extractType(ctx.type(1));
            return collectionName + "<" + innerType + ", " + valueType + ">";
        }
        
        return collectionName + "<" + innerType + ">";
    }
    
    private String extractCustomType(MermaidClassParser.CustomTypeContext ctx) {
        StringBuilder type = new StringBuilder(ctx.IDENTIFIER().getText());
        
        // Handle generics: List<String>
        if (ctx.typeArgs() != null) {
            type.append("<");
            List<String> args = new ArrayList<>();
            for (var argCtx : ctx.typeArgs().type()) {
                args.add(extractType(argCtx));
            }
            type.append(String.join(", ", args));
            type.append(">");
        }
        
        // Handle arrays: String[]
        if (ctx.LBRACKET() != null) {
            int arrayCount = ctx.LBRACKET().size();
            for (int i = 0; i < arrayCount; i++) {
                type.append("[]");
            }
        }
        
        return type.toString();
    }
    
    private Annotation processAnnotation(MermaidClassParser.AnnotationContext ctx) {
        String type = ctx.annotationType().getText();
        Annotation annotation = new Annotation(type);
        
        if (ctx.annotationParams() != null) {
            for (var paramCtx : ctx.annotationParams().annotationParam()) {
                String key = paramCtx.IDENTIFIER().getText();
                String valueText = paramCtx.getChild(2).getText(); // After '='
                
                Object value;
                if (valueText.startsWith("\"") && valueText.endsWith("\"")) {
                    value = valueText.substring(1, valueText.length() - 1);
                } else if (valueText.equals("true") || valueText.equals("false")) {
                    value = Boolean.parseBoolean(valueText);
                } else {
                    try {
                        value = Integer.parseInt(valueText);
                    } catch (NumberFormatException e) {
                        value = valueText;
                    }
                }
                
                annotation.addParameter(key, value);
            }
        }
        
        return annotation;
    }
    
    private Constraint processConstraint(MermaidClassParser.ConstraintContext ctx) {
        String type = ctx.constraintType().getText();
        
        if (ctx.constraintValue() != null) {
            String valueText = ctx.constraintValue().getText();
            Object value;
            
            if (valueText.startsWith("\"") && valueText.endsWith("\"")) {
                value = valueText.substring(1, valueText.length() - 1);
            } else if (valueText.equals("true") || valueText.equals("false")) {
                value = Boolean.parseBoolean(valueText);
            } else {
                try {
                    value = Integer.parseInt(valueText);
                } catch (NumberFormatException e) {
                    value = valueText;
                }
            }
            
            return new Constraint(type, value);
        }
        
        return new Constraint(type);
    }
    
    private RelationshipType mapRelationType(MermaidClassParser.RelTypeContext ctx) {
        if (ctx instanceof MermaidClassParser.InheritanceRelContext) {
            return RelationshipType.INHERITANCE;
        } else if (ctx instanceof MermaidClassParser.CompositionLeftContext ||
                   ctx instanceof MermaidClassParser.CompositionRightContext) {
            return RelationshipType.COMPOSITION;
        } else if (ctx instanceof MermaidClassParser.AggregationLeftContext ||
                   ctx instanceof MermaidClassParser.AggregationRightContext) {
            return RelationshipType.AGGREGATION;
        } else if (ctx instanceof MermaidClassParser.CardinalityRelContext) {
            return RelationshipType.ASSOCIATION;
        }
        return RelationshipType.ASSOCIATION;
    }
}