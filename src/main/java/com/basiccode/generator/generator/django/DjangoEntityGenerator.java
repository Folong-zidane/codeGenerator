package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLAttribute;
import java.util.Map;

/**
 * Générateur d'entités Django fonctionnel
 */
public class DjangoEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        return generateEntity(enhancedClass, packageName, null);
    }
    
    public String generateEntity(EnhancedClass enhancedClass, String packageName, Map<String, String> metadata) {
        if (enhancedClass == null || enhancedClass.getOriginalClass() == null) {
            throw new IllegalArgumentException("EnhancedClass and originalClass cannot be null");
        }
        
        StringBuilder model = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Imports based on metadata
        model.append(generateImportsFromMetadata(metadata));
        
        // Class definition
        model.append("class ").append(className).append("(models.Model):\n");
        
        // ID field
        model.append("    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)\n");
        
        // Generate fields from attributes with metadata
        if (enhancedClass.getOriginalClass().getAttributes() != null) {
            for (UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
                if (!"id".equals(attr.getName())) {
                    model.append(generateDjangoField(attr, metadata));
                }
            }
        }
        
        // Timestamps
        model.append("    created_at = models.DateTimeField(auto_now_add=True)\n");
        model.append("    updated_at = models.DateTimeField(auto_now=True)\n\n");
        
        // Meta class with metadata
        model.append(generateMetaClassFromMetadata(className, metadata, enhancedClass));
        
        // String representation
        model.append("    def __str__(self):\n");
        model.append("        return f'").append(className).append(" {self.id}'\n\n");
        
        // Save method
        model.append("    def save(self, *args, **kwargs):\n");
        model.append("        super().save(*args, **kwargs)\n");
        
        return model.toString();
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        if (enhancedClass == null) {
            return "";
        }
        
        StringBuilder enumCode = new StringBuilder();
        enumCode.append("from django.db import models\n\n");
        
        String className = enhancedClass.getOriginalClass() != null ? 
            enhancedClass.getOriginalClass().getName() : "State";
        
        enumCode.append("class ").append(className).append("Status(models.TextChoices):\n");
        enumCode.append("    ACTIVE = 'ACTIVE', 'Active'\n");
        enumCode.append("    INACTIVE = 'INACTIVE', 'Inactive'\n");
        enumCode.append("    PENDING = 'PENDING', 'Pending'\n");
        enumCode.append("    COMPLETED = 'COMPLETED', 'Completed'\n");
        
        return enumCode.toString();
    }
    
    private String generateDjangoField(UMLAttribute attr) {
        return generateDjangoField(attr, null);
    }
    
    private String generateDjangoField(UMLAttribute attr, Map<String, String> metadata) {
        StringBuilder field = new StringBuilder();
        
        // Skip auto-timestamp fields to avoid duplication
        if (isAutoTimestamp(attr.getName(), metadata)) {
            return "";
        }
        
        // Apply naming convention from metadata
        String fieldName = applyNamingConvention(attr.getName(), metadata);
        field.append("    ").append(fieldName).append(" = ");
        
        String fieldType = mapToDjangoFieldAdvanced(attr, metadata);
        field.append(fieldType);
        
        field.append("\n");
        return field.toString();
    }
    
    private boolean isAutoTimestamp(String fieldName) {
        return isAutoTimestamp(fieldName, null);
    }
    
    private boolean isAutoTimestamp(String fieldName, Map<String, String> metadata) {
        if (metadata != null) {
            String createdAtField = metadata.getOrDefault("created-at-field", "createdAt");
            String updatedAtField = metadata.getOrDefault("updated-at-field", "updatedAt");
            return fieldName.equals(createdAtField) || fieldName.equals(updatedAtField);
        }
        
        String lowerName = fieldName.toLowerCase();
        return lowerName.equals("createdat") || lowerName.equals("updatedat") ||
               lowerName.equals("created_at") || lowerName.equals("updated_at");
    }
    
    private String applyNamingConvention(String fieldName, Map<String, String> metadata) {
        if (metadata == null) {
            return toSnakeCase(fieldName);
        }
        
        String convention = metadata.getOrDefault("column-naming-convention", "snake_case");
        switch (convention) {
            case "snake_case":
                return toSnakeCase(fieldName);
            case "camelCase":
                return fieldName;
            case "PascalCase":
                return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            default:
                return toSnakeCase(fieldName);
        }
    }
    
    private String toSnakeCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
    
    private String mapToDjangoFieldAdvanced(UMLAttribute attr) {
        return mapToDjangoFieldAdvanced(attr, null);
    }
    
    private String mapToDjangoFieldAdvanced(UMLAttribute attr, Map<String, String> metadata) {
        String umlType = attr.getType();
        String fieldName = attr.getName().toLowerCase();
        
        if (umlType == null) {
            return "models.CharField(max_length=255, blank=False, null=False)";
        }
        
        switch (umlType.toLowerCase()) {
            case "string":
            case "str":
                if (fieldName.contains("email")) {
                    return "models.EmailField(unique=True, db_index=True)";
                } else if (fieldName.contains("name") || fieldName.contains("title")) {
                    return "models.CharField(max_length=255, blank=False, null=False, db_index=True)";
                } else if (fieldName.contains("slug")) {
                    return "models.SlugField(max_length=255, unique=True, db_index=True)";
                }
                return "models.CharField(max_length=255, blank=False, null=False)";
            case "integer":
            case "int":
                if (fieldName.contains("price") || fieldName.contains("amount")) {
                    return "models.PositiveIntegerField(validators=[MinValueValidator(0)])";
                } else if (fieldName.contains("stock") || fieldName.contains("quantity")) {
                    return "models.PositiveIntegerField(default=0, validators=[MinValueValidator(0)])";
                }
                return "models.IntegerField()";
            case "long":
                return "models.BigIntegerField()";
            case "float":
            case "double":
                return "models.FloatField(validators=[MinValueValidator(0.0)])";
            case "boolean":
            case "bool":
                if (fieldName.contains("active") || fieldName.contains("enabled")) {
                    return "models.BooleanField(default=True, db_index=True)";
                }
                return "models.BooleanField(default=False)";
            case "bigdecimal":
            case "decimal":
                return "models.DecimalField(max_digits=12, decimal_places=2, validators=[MinValueValidator(0)])";
            case "localdatetime":
            case "datetime":
                return "models.DateTimeField(db_index=True)";
            case "localdate":
            case "date":
                return "models.DateField(db_index=True)";
            case "text":
                return "models.TextField(blank=True, null=True)";
            case "email":
                return "models.EmailField(unique=True, db_index=True)";
            case "url":
                return "models.URLField(blank=True, null=True)";
            case "uuid":
                return "models.UUIDField(default=uuid.uuid4, editable=False, unique=True)";
            default:
                return "models.CharField(max_length=255, blank=False, null=False)";
        }
    }
    
    private String generateImportsFromMetadata(Map<String, String> metadata) {
        StringBuilder imports = new StringBuilder();
        imports.append("from django.db import models\n");
        
        // Add imports based on metadata
        if (metadata != null) {
            String validationFramework = metadata.getOrDefault("validation-framework", "django");
            if (validationFramework.contains("pydantic")) {
                imports.append("from pydantic import BaseModel, validator\n");
            }
            
            String cacheStrategy = metadata.getOrDefault("cache-strategy", "none");
            if (!"none".equals(cacheStrategy)) {
                imports.append("from django.core.cache import cache\n");
            }
            
            boolean auditFields = Boolean.parseBoolean(metadata.getOrDefault("audit-fields", "true"));
            if (auditFields) {
                imports.append("from django.contrib.auth.models import User\n");
            }
        }
        
        imports.append("from django.core.validators import MinValueValidator, MaxValueValidator\n");
        imports.append("from django.utils.translation import gettext_lazy as _\n");
        imports.append("import uuid\n\n");
        
        return imports.toString();
    }
    
    private String generateMetaClassFromMetadata(String className, Map<String, String> metadata, EnhancedClass enhancedClass) {
        StringBuilder meta = new StringBuilder();
        meta.append("    class Meta:\n");
        
        // Table name from metadata or entity metadata
        String tableName = className.toLowerCase();
        if (metadata != null) {
            String tableNaming = metadata.getOrDefault("table-naming-convention", "snake_case");
            if ("snake_case".equals(tableNaming)) {
                tableName = toSnakeCase(className);
            }
            
            boolean tablePrefix = Boolean.parseBoolean(metadata.getOrDefault("table-prefix", "false"));
            if (tablePrefix) {
                String prefix = metadata.getOrDefault("table-prefix-value", "tbl_");
                tableName = prefix + tableName;
            }
        }
        
        // Check entity-specific metadata from UMLClass
        if (enhancedClass.getOriginalClass().getMetadata() != null && enhancedClass.getOriginalClass().getMetadata().containsKey("tableName")) {
            tableName = enhancedClass.getOriginalClass().getMetadata().get("tableName");
        }
        
        meta.append("        db_table = '").append(tableName).append("'\n");
        meta.append("        ordering = ['-created_at']\n");
        meta.append("        verbose_name = _('").append(className).append("')\n");
        meta.append("        verbose_name_plural = _('").append(className).append("s')\n");
        
        // Indexes from metadata
        meta.append("        indexes = [\n");
        meta.append("            models.Index(fields=['created_at']),\n");
        meta.append("            models.Index(fields=['updated_at']),\n");
        
        if (metadata != null) {
            boolean autoIndexForeignKeys = Boolean.parseBoolean(metadata.getOrDefault("auto-index-foreign-keys", "true"));
            if (autoIndexForeignKeys) {
                meta.append("            # Auto-generated foreign key indexes\n");
            }
        }
        
        meta.append("        ]\n\n");
        
        return meta.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".py";
    }
    
    @Override
    public String getEntityDirectory() {
        return "models";
    }
}