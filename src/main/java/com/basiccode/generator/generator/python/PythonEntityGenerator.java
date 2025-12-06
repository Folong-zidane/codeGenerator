package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.generator.InheritanceAwareEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

public class PythonEntityGenerator implements InheritanceAwareEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("from sqlalchemy import Column, Integer, String, DateTime, Enum, ForeignKey\n");
        code.append("from sqlalchemy.orm import relationship\n");
        code.append("from sqlalchemy.ext.declarative import declarative_base\n");
        code.append("from datetime import datetime\n");
        code.append("from enum import Enum as PyEnum\n\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("class ").append(enumName).append("(PyEnum):\n");
            if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
                for (var value : enhancedClass.getStateEnum().getValues()) {
                    code.append("    ").append(value.getName()).append(" = \"").append(value.getName()).append("\"\n");
                }
            } else {
                code.append("    ACTIVE = \"ACTIVE\"\n");
                code.append("    INACTIVE = \"INACTIVE\"\n");
                code.append("    SUSPENDED = \"SUSPENDED\"\n");
            }
            code.append("\n");
        }
        
        // Handle inheritance
        if (enhancedClass.getOriginalClass().isInterface()) {
            // Python doesn't have interfaces, use ABC
            code.append("from abc import ABC, abstractmethod\n\n");
            code.append("class ").append(className).append("(ABC):\n");
            generatePythonInterfaceMethods(code, enhancedClass);
            return code.toString();
        }
        
        code.append("Base = declarative_base()\n\n");
        
        String inheritanceDecl = generateInheritanceDeclaration(enhancedClass, className);
        code.append(inheritanceDecl).append("\n");
        
        if (!enhancedClass.getOriginalClass().isAbstract()) {
            code.append("    __tablename__ = '").append(className.toLowerCase()).append("s'\n\n");
        }
        
        // Generate fields (skip inherited ones)
        boolean hasInheritedFields = shouldSkipInheritedFields(enhancedClass);
        
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            // Skip inherited fields
            if (hasInheritedFields && isInheritedField(attr.getName())) {
                continue;
            }
            
            // Generate SQLAlchemy relationship or column
            if (attr.isRelationship()) {
                generatePythonRelationship(code, attr, className);
            } else {
                String pythonType = mapToPythonType(attr.getType());
                if ("id".equals(attr.getName())) {
                    code.append("    ").append(attr.getName()).append(" = Column(Integer, primary_key=True, autoincrement=True)\n");
                } else {
                    code.append("    ").append(attr.getName()).append(" = Column(").append(pythonType).append(")\n");
                }
            }
        }
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    status = Column(Enum(").append(enumName).append("), default=")
                .append(enumName).append(".ACTIVE)\n");
        }
        
        // Add audit fields only if not inherited
        if (!shouldSkipInheritedFields(enhancedClass)) {
            code.append("    created_at = Column(DateTime, default=datetime.utcnow)\n");
            code.append("    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)\n\n");
        }
        
        // Generate state methods if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    def suspend(self):\n");
            code.append("        if self.status != ").append(enumName).append(".ACTIVE:\n");
            code.append("            raise ValueError(f'Cannot suspend user in state: {self.status}')\n");
            code.append("        self.status = ").append(enumName).append(".SUSPENDED\n\n");
            
            code.append("    def activate(self):\n");
            code.append("        if self.status != ").append(enumName).append(".SUSPENDED:\n");
            code.append("            raise ValueError(f'Cannot activate user in state: {self.status}')\n");
            code.append("        self.status = ").append(enumName).append(".ACTIVE\n");
        }
        
        return code.toString();
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        return ""; // Already included in entity
    }
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
    
    @Override
    public String getEntityDirectory() {
        return "models";
    }
    
    @Override
    public String generateInheritanceDeclaration(EnhancedClass enhancedClass, String className) {
        if (enhancedClass.getOriginalClass().isInterface()) {
            return "class " + className + "(ABC):";
        } else if (enhancedClass.getOriginalClass().isAbstract()) {
            return "class " + className + "(Base):";
        } else {
            String superClass = enhancedClass.getOriginalClass().getSuperClass();
            if (superClass != null && !superClass.isEmpty()) {
                return "class " + className + "(" + superClass + "):";
            } else {
                return "class " + className + "(Base):";
            }
        }
    }
    
    private void generatePythonInterfaceMethods(StringBuilder code, EnhancedClass enhancedClass) {
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (var method : enhancedClass.getOriginalClass().getMethods()) {
                code.append("    @abstractmethod\n");
                code.append("    def ").append(method.getName()).append("(self");
                if (method.getParameters() != null && !method.getParameters().isEmpty()) {
                    for (var param : method.getParameters()) {
                        code.append(", ").append(param.getName());
                    }
                }
                code.append("):\n");
                code.append("        pass\n\n");
            }
        }
    }
    
    private void generatePythonRelationship(StringBuilder code, UmlAttribute attr, String currentClassName) {
        String relationshipType = attr.getRelationshipType();
        String targetClass = attr.getTargetClass();
        
        switch (relationshipType) {
            case "OneToMany":
                code.append("    ").append(attr.getName()).append(" = relationship('").append(targetClass).append("', back_populates='").append(currentClassName.toLowerCase()).append("')\n");
                break;
            case "ManyToOne":
                code.append("    ").append(targetClass.toLowerCase()).append("_id = Column(Integer, ForeignKey('").append(targetClass.toLowerCase()).append("s.id'))\n");
                code.append("    ").append(attr.getName()).append(" = relationship('").append(targetClass).append("', back_populates='").append(attr.getName()).append("s')\n");
                break;
            case "OneToOne":
                code.append("    ").append(targetClass.toLowerCase()).append("_id = Column(Integer, ForeignKey('").append(targetClass.toLowerCase()).append("s.id'), unique=True)\n");
                code.append("    ").append(attr.getName()).append(" = relationship('").append(targetClass).append("', uselist=False)\n");
                break;
            case "ManyToMany":
                String tableName = currentClassName.toLowerCase() + "_" + targetClass.toLowerCase();
                code.append("    ").append(attr.getName()).append(" = relationship('").append(targetClass).append("', secondary='").append(tableName).append("', back_populates='").append(currentClassName.toLowerCase()).append("s')\n");
                break;
        }
    }
    
    private String mapToPythonType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "long", "integer", "int" -> "Integer";
            case "string" -> "String";
            case "float", "double" -> "Float";
            case "boolean" -> "Boolean";
            default -> "String";
        };
    }
}