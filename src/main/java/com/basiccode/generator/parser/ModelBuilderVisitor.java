package com.basiccode.generator.parser;

import com.basiccode.generator.model.*;

public class ModelBuilderVisitor extends MermaidClassBaseVisitor<Object> {
    
    private Diagram diagram = new Diagram();
    private ClassModel currentClass;
    
    @Override
    public Diagram visitDiagram(MermaidClassParser.DiagramContext ctx) {
        // Visit all class declarations
        for (var classDecl : ctx.classDecl()) {
            visit(classDecl);
        }
        
        // Visit all relationships
        for (var rel : ctx.relationship()) {
            visit(rel);
        }
        
        return diagram;
    }
    
    @Override
    public ClassModel visitClassDecl(MermaidClassParser.ClassDeclContext ctx) {
        currentClass = new ClassModel();
        currentClass.setName(ctx.className().getText());
        
        // Visit members
        for (var member : ctx.member()) {
            visit(member);
        }
        
        diagram.addClass(currentClass);
        return currentClass;
    }
    
    @Override
    public Field visitField(MermaidClassParser.FieldContext ctx) {
        Field field = new Field();
        field.setName(ctx.identifier().getText());
        field.setType(ctx.type().getText());
        
        if (ctx.visibility() != null) {
            field.setVisibility(Visibility.fromSymbol(ctx.visibility().getText()));
        }
        
        currentClass.addField(field);
        return field;
    }
    
    @Override
    public Method visitMethod(MermaidClassParser.MethodContext ctx) {
        Method method = new Method();
        method.setName(ctx.identifier().getText());
        method.setReturnType(ctx.returnType().getText());
        
        if (ctx.visibility() != null) {
            method.setVisibility(Visibility.fromSymbol(ctx.visibility().getText()));
        }
        
        // Handle parameters
        if (ctx.paramList() != null) {
            for (var paramCtx : ctx.paramList().parameter()) {
                Parameter param = new Parameter();
                param.setName(paramCtx.identifier().getText());
                param.setType(paramCtx.type().getText());
                method.getParameters().add(param);
            }
        }
        
        currentClass.addMethod(method);
        return method;
    }
    
    @Override
    public Relationship visitRelationship(MermaidClassParser.RelationshipContext ctx) {
        Relationship relationship = new Relationship();
        relationship.setSourceClass(ctx.className(0).getText());
        relationship.setTargetClass(ctx.className(1).getText());
        relationship.setType(RelationshipType.fromSymbol(ctx.relType().getText()));
        
        diagram.addRelationship(relationship);
        return relationship;
    }
}