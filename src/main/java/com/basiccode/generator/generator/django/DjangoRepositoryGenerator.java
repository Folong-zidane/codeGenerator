package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;

public class DjangoRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();
        
        code.append("from rest_framework import serializers\n");
        code.append("from .models import ").append(className).append("\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("from .enums import ").append(className).append("Status\n\n");
        }
        
        code.append("class ").append(className).append("Serializer(serializers.ModelSerializer):\n");
        code.append("    class Meta:\n");
        code.append("        model = ").append(className).append("\n");
        code.append("        fields = '__all__'\n");
        code.append("        read_only_fields = ('id',)\n\n");
        
        code.append("    def validate(self, data):\n");
        code.append("        # Custom validation logic\n");
        code.append("        return data\n\n");
        
        if (enhancedClass.isStateful()) {
            code.append("    def validate_status(self, value):\n");
            code.append("        if value not in [choice[0] for choice in ").append(className).append("Status.choices]:\n");
            code.append("            raise serializers.ValidationError('Invalid status')\n");
            code.append("        return value\n");
        }
        
        return code.toString();
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "serializers";
    }
}