package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLAttribute;

/**
 * Générateur de sérialiseurs Django REST Framework ultra-optimisé
 */
public class DjangoSerializerGenerator {
    
    public String generateSerializer(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder serializer = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        String serializerName = className + "Serializer";
        String createSerializerName = className + "CreateSerializer";
        
        // Imports
        serializer.append("from rest_framework import serializers\n");
        serializer.append("from django.core.validators import MinValueValidator, MaxValueValidator\n");
        serializer.append("from django.utils.translation import gettext_lazy as _\n");
        serializer.append("from typing import Dict, Any\n\n");
        
        serializer.append("from .models import ").append(className).append("\n\n");
        
        // Main serializer
        serializer.append("class ").append(serializerName).append("(serializers.ModelSerializer):\n");
        serializer.append("    \"\"\"Comprehensive serializer for ").append(className).append(" with validation\"\"\"\n\n");
        
        // Add computed fields
        serializer.append("    # Computed fields\n");
        serializer.append("    full_name = serializers.SerializerMethodField()\n");
        serializer.append("    age_in_days = serializers.SerializerMethodField()\n");
        serializer.append("    is_recent = serializers.SerializerMethodField()\n\n");
        
        // Meta class
        serializer.append("    class Meta:\n");
        serializer.append("        model = ").append(className).append("\n");
        serializer.append("        fields = '__all__'\n");
        serializer.append("        read_only_fields = ('id', 'created_at', 'updated_at')\n");
        serializer.append("        extra_kwargs = {\n");
        
        // Generate field validations
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            for (UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
                String fieldName = toSnakeCase(attr.getName());
                serializer.append(generateFieldValidation(fieldName, attr.getType()));
            }
        }
        
        serializer.append("        }\n\n");
        
        // Validation methods
        serializer.append("    def validate(self, data: Dict[str, Any]) -> Dict[str, Any]:\n");
        serializer.append("        \"\"\"Cross-field validation\"\"\"\n");
        serializer.append("        # Add business logic validation here\n");
        serializer.append("        return data\n\n");
        
        // Computed field methods
        serializer.append("    def get_full_name(self, obj) -> str:\n");
        serializer.append("        \"\"\"Generate full name if applicable\"\"\"\n");
        serializer.append("        if hasattr(obj, 'first_name') and hasattr(obj, 'last_name'):\n");
        serializer.append("            return f'{obj.first_name} {obj.last_name}'\n");
        serializer.append("        return str(obj)\n\n");
        
        serializer.append("    def get_age_in_days(self, obj) -> int:\n");
        serializer.append("        \"\"\"Calculate age in days since creation\"\"\"\n");
        serializer.append("        from django.utils import timezone\n");
        serializer.append("        return (timezone.now() - obj.created_at).days\n\n");
        
        serializer.append("    def get_is_recent(self, obj) -> bool:\n");
        serializer.append("        \"\"\"Check if object was created recently\"\"\"\n");
        serializer.append("        return self.get_age_in_days(obj) <= 7\n\n");
        
        // Create serializer
        serializer.append("class ").append(createSerializerName).append("(serializers.ModelSerializer):\n");
        serializer.append("    \"\"\"Optimized serializer for ").append(className).append(" creation\"\"\"\n\n");
        
        serializer.append("    class Meta:\n");
        serializer.append("        model = ").append(className).append("\n");
        serializer.append("        exclude = ('id', 'created_at', 'updated_at')\n\n");
        
        serializer.append("    def create(self, validated_data: Dict[str, Any]) -> ").append(className).append(":\n");
        serializer.append("        \"\"\"Create with additional processing\"\"\"\n");
        serializer.append("        # Pre-processing logic here\n");
        serializer.append("        instance = super().create(validated_data)\n");
        serializer.append("        # Post-processing logic here\n");
        serializer.append("        return instance\n");
        
        return serializer.toString();
    }
    
    private String generateFieldValidation(String fieldName, String fieldType) {
        StringBuilder validation = new StringBuilder();
        validation.append("            '").append(fieldName).append("': {\n");
        
        if (fieldType != null) {
            switch (fieldType.toLowerCase()) {
                case "string":
                case "str":
                    if (fieldName.contains("email")) {
                        validation.append("                'validators': [serializers.EmailValidator()],\n");
                    }
                    validation.append("                'min_length': 1,\n");
                    validation.append("                'max_length': 255,\n");
                    break;
                case "integer":
                case "int":
                    validation.append("                'min_value': 0,\n");
                    break;
                case "bigdecimal":
                case "decimal":
                    validation.append("                'min_value': 0,\n");
                    validation.append("                'max_digits': 12,\n");
                    validation.append("                'decimal_places': 2,\n");
                    break;
            }
        }
        
        validation.append("                'error_messages': {\n");
        validation.append("                    'required': _('This field is required.'),\n");
        validation.append("                    'blank': _('This field cannot be blank.'),\n");
        validation.append("                }\n");
        validation.append("            },\n");
        
        return validation.toString();
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    public String getFileExtension() {
        return ".py";
    }
    
    public String getSerializerDirectory() {
        return "serializers";
    }
}