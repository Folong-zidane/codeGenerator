package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

public class PythonEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("from sqlalchemy import Column, Integer, String, DateTime, Enum\n");
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
        
        code.append("Base = declarative_base()\n\n");
        code.append("class ").append(className).append("(Base):\n");
        code.append("    __tablename__ = '").append(className.toLowerCase()).append("s'\n\n");
        
        // Generate fields
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            String pythonType = mapToPythonType(attr.getType());
            if ("id".equals(attr.getName())) {
                code.append("    ").append(attr.getName()).append(" = Column(Integer, primary_key=True, autoincrement=True)\n");
            } else {
                code.append("    ").append(attr.getName()).append(" = Column(").append(pythonType).append(")\n");
            }
        }
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    status = Column(Enum(").append(enumName).append("), default=")
                .append(enumName).append(".ACTIVE)\n");
        }
        
        code.append("    created_at = Column(DateTime, default=datetime.utcnow)\n");
        code.append("    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)\n\n");
        
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