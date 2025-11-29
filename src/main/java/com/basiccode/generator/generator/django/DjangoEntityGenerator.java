package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlClass;
import com.basiccode.generator.model.UmlAttribute;

public class DjangoEntityGenerator implements IEntityGenerator {
    
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
        return ""; // Django enums are included in models
    }
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        UmlClass umlClass = enhancedClass.getOriginalClass();
        StringBuilder code = new StringBuilder();
        
        code.append("from django.db import models\n");
        code.append("from django.core.validators import MinValueValidator, MaxValueValidator\n");
        code.append("import uuid\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("from .enums import ").append(umlClass.getName()).append("Status\n\n");
        }
        
        code.append("class ").append(umlClass.getName()).append("(models.Model):\n");
        
        for (UmlAttribute attr : umlClass.getAttributes()) {
            code.append("    ").append(generateField(attr, enhancedClass.isStateful() && attr.getName().equals("status"))).append("\n");
        }
        
        code.append("\n");
        code.append("    class Meta:\n");
        code.append("        db_table = '").append(umlClass.getName().toLowerCase()).append("s'\n");
        code.append("        ordering = ['-id']\n\n");
        
        code.append("    def __str__(self):\n");
        code.append("        return f\"").append(umlClass.getName()).append("({self.id})\"\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("    def can_suspend(self):\n");
            code.append("        return self.status == ").append(umlClass.getName()).append("Status.ACTIVE\n\n");
            
            code.append("    def can_activate(self):\n");
            code.append("        return self.status in [").append(umlClass.getName()).append("Status.SUSPENDED, ").append(umlClass.getName()).append("Status.INACTIVE]\n");
        }
        
        return code.toString();
    }
    
    private String generateField(UmlAttribute attr, boolean isStatusField) {
        String fieldName = attr.getName();
        String fieldType = attr.getType();
        
        if (isStatusField) {
            return fieldName + " = models.CharField(max_length=20, choices=" + 
                   fieldType.replace("Status", "") + "Status.choices, default=" + 
                   fieldType.replace("Status", "") + "Status.ACTIVE)";
        }
        
        switch (fieldType.toLowerCase()) {
            case "uuid":
                return fieldName + " = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)";
            case "string":
                return fieldName + " = models.CharField(max_length=255)";
            case "int":
            case "integer":
                return fieldName + " = models.IntegerField()";
            case "float":
            case "double":
                return fieldName + " = models.FloatField()";
            case "boolean":
            case "bool":
                return fieldName + " = models.BooleanField(default=False)";
            case "date":
                return fieldName + " = models.DateField()";
            case "datetime":
                return fieldName + " = models.DateTimeField(auto_now_add=True)";
            case "text":
                return fieldName + " = models.TextField()";
            case "email":
                return fieldName + " = models.EmailField()";
            default:
                return fieldName + " = models.CharField(max_length=255)";
        }
    }
}