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
            if (attr.isRelationship()) {
                code.append("    ").append(generateDjangoRelationship(attr, umlClass.getName())).append("\n");
            } else {
                code.append("    ").append(generateField(attr, enhancedClass.isStateful() && attr.getName().equals("status"))).append("\n");
            }
        }
        
        code.append("\n");
        code.append("    class Meta:\n");
        code.append("        db_table = '").append(umlClass.getName().toLowerCase()).append("s'\n");
        code.append("        ordering = ['-id']\n\n");
        
        code.append("    def __str__(self):\n");
        code.append("        return f\"").append(umlClass.getName()).append("({self.id})\"\n\n");
        
        // Add business methods from UML diagram
        generateUmlMethods(code, enhancedClass, umlClass.getName());
        
        // Add predefined business methods
        generateBusinessMethods(code, umlClass.getName());
        
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
    
    private void generateBusinessMethods(StringBuilder code, String className) {
        if ("User".equals(className)) {
            code.append("\n    def validate_email(self):\n");
            code.append("        import re\n");
            code.append("        if not self.email:\n");
            code.append("            raise ValueError('Email cannot be empty')\n");
            code.append("        pattern = r'^[A-Za-z0-9+_.-]+@(.+)$'\n");
            code.append("        return bool(re.match(pattern, self.email))\n\n");
            
            code.append("    def change_password(self, new_password):\n");
            code.append("        if not new_password or len(new_password) < 8:\n");
            code.append("            raise ValueError('Password must be at least 8 characters')\n");
            code.append("        # TODO: Hash password with Django's make_password\n");
            code.append("        self.save()\n\n");
            
        } else if ("Product".equals(className)) {
            code.append("\n    def update_stock(self, new_quantity):\n");
            code.append("        if new_quantity is None or new_quantity < 0:\n");
            code.append("            raise ValueError('Stock cannot be negative')\n");
            code.append("        self.stock = new_quantity\n");
            code.append("        self.save()\n\n");
            
        } else if ("Order".equals(className)) {
            code.append("\n    def calculate_total(self, products):\n");
            code.append("        if not products:\n");
            code.append("            raise ValueError('Cannot calculate total: no products')\n");
            code.append("        total = sum(product.price for product in products)\n");
            code.append("        self.total = total\n");
            code.append("        return total\n\n");
        }
    }
    
    private void generateUmlMethods(StringBuilder code, EnhancedClass enhancedClass, String className) {
        // Generate methods from UML diagram
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (var method : enhancedClass.getOriginalClass().getMethods()) {
                generateUmlMethod(code, method, className);
            }
        }
    }
    
    private void generateUmlMethod(StringBuilder code, com.basiccode.generator.model.Method method, String className) {
        code.append("\n    def ").append(method.getName()).append("(self");
        
        // Add parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (var param : method.getParameters()) {
                code.append(", ").append(param.getName());
            }
        }
        
        code.append("):\n");
        
        // Generate method body based on method name
        generateDjangoMethodBody(code, method, className);
        
        code.append("\n");
    }
    
    private void generateDjangoMethodBody(StringBuilder code, com.basiccode.generator.model.Method method, String className) {
        String methodName = method.getName().toLowerCase();
        String returnType = method.getReturnType() != null ? method.getReturnType().toLowerCase() : "none";
        
        switch (methodName) {
            case "authenticate":
                code.append("        if not password:\n");
                code.append("            return False\n");
                code.append("        # TODO: Implement password verification logic\n");
                code.append("        return True\n");
                break;
                
            case "updateprofile":
                code.append("        if not profile:\n");
                code.append("            raise ValueError('Profile cannot be None')\n");
                code.append("        # TODO: Update user profile fields\n");
                code.append("        self.save()\n");
                break;
                
            case "calculatetotal":
                code.append("        # TODO: Calculate order total\n");
                if (!"void".equals(returnType) && !"none".equals(returnType)) {
                    code.append("        return 0\n");
                }
                break;
                
            default:
                code.append("        # TODO: Implement ").append(methodName).append(" logic\n");
                if (!"void".equals(returnType) && !"none".equals(returnType)) {
                    if ("boolean".equals(returnType) || "bool".equals(returnType)) {
                        code.append("        return False\n");
                    } else if ("string".equals(returnType) || "str".equals(returnType)) {
                        code.append("        return ''\n");
                    } else if (returnType.contains("int") || returnType.contains("float")) {
                        code.append("        return 0\n");
                    } else {
                        code.append("        return None\n");
                    }
                }
                break;
        }
    }
    
    private String generateDjangoRelationship(UmlAttribute attr, String currentClassName) {
        String relationshipType = attr.getRelationshipType();
        String targetClass = attr.getTargetClass();
        String fieldName = attr.getName();
        
        switch (relationshipType) {
            case "OneToMany":
                return fieldName + " = models.ForeignKey('" + targetClass + "', on_delete=models.CASCADE, related_name='" + fieldName + "s')";
            case "ManyToOne":
                return fieldName + " = models.ForeignKey('" + targetClass + "', on_delete=models.CASCADE)";
            case "OneToOne":
                return fieldName + " = models.OneToOneField('" + targetClass + "', on_delete=models.CASCADE)";
            case "ManyToMany":
                return fieldName + " = models.ManyToManyField('" + targetClass + "', related_name='" + currentClassName.toLowerCase() + "s')";
            default:
                return fieldName + " = models.CharField(max_length=255)";
        }
    }
}