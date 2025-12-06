package com.basiccode.generator.generator.django;

import com.basiccode.generator.parser.DjangoModelParser.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ✅ Django Model Generator
 * 
 * Generates Django models.py files with:
 * - Model classes with proper field definitions
 * - Validation methods (clean_*, validate_*)
 * - Meta class with ordering, verbose_name, unique_together
 * - String representation (__str__ method)
 * - Custom managers for complex queries
 * - Timestamps (created_at, updated_at)
 * - Django signals and receivers
 * - Abstract base models
 * 
 * Output: Production-ready Django model code
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DjangoModelGenerator {
    
    /**
     * Generate models.py from parsed Django models
     */
    public String generateModels(DjangoModels models) {
        log.info("🐍 Generating Django models.py");
        
        StringBuilder code = new StringBuilder();
        
        // Imports
        code.append(generateImports(models));
        code.append("\n\n");
        
        // Base model
        code.append(generateBaseModel());
        code.append("\n\n");
        
        // Custom managers (if needed)
        code.append(generateCustomManagers(models));
        code.append("\n\n");
        
        // Model classes
        for (DjangoModel model : models.getModelsList()) {
            code.append(generateModelClass(model));
            code.append("\n\n");
        }
        
        // Signals
        code.append(generateSignals(models));
        
        return code.toString();
    }
    
    private String generateImports(DjangoModels models) {
        StringBuilder imports = new StringBuilder();
        
        imports.append("from django.db import models\n");
        imports.append("from django.core.validators import (\n");
        imports.append("    EmailValidator, URLValidator, MinValueValidator,\n");
        imports.append("    MaxValueValidator, RegexValidator, FileExtensionValidator\n");
        imports.append(")\n");
        imports.append("from django.utils.translation import gettext_lazy as _\n");
        imports.append("from django.utils import timezone\n");
        imports.append("from django.db.models.signals import post_save, pre_save\n");
        imports.append("from django.dispatch import receiver\n");
        imports.append("from django.core.exceptions import ValidationError\n");
        imports.append("import uuid\n");
        imports.append("import json\n");
        
        // Check if any field uses specific validators
        boolean hasFile = models.getModelsList().stream()
            .flatMap(m -> m.getFields().stream())
            .anyMatch(f -> f.getFieldType().contains("File"));
        
        if (hasFile) {
            imports.append("from django.core.files.storage import default_storage\n");
        }
        
        return imports.toString();
    }
    
    private String generateBaseModel() {
        return "class BaseModel(models.Model):\n" +
               "    \"\"\"Base model with common fields\"\"\"\n" +
               "    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)\n" +
               "    created_at = models.DateTimeField(auto_now_add=True, verbose_name=_('Created at'))\n" +
               "    updated_at = models.DateTimeField(auto_now=True, verbose_name=_('Updated at'))\n" +
               "    \n" +
               "    class Meta:\n" +
               "        abstract = True\n" +
               "        ordering = ['-created_at']\n" +
               "    \n" +
               "    def __repr__(self):\n" +
               "        return f\"<{self.__class__.__name__}: {self.id}>\"\n";
    }
    
    private String generateCustomManagers(DjangoModels models) {
        StringBuilder managers = new StringBuilder();
        
        managers.append("class ActiveManager(models.Manager):\n");
        managers.append("    \"\"\"Manager for active objects only\"\"\"\n");
        managers.append("    def get_queryset(self):\n");
        managers.append("        return super().get_queryset().filter(is_active=True)\n\n");
        
        managers.append("class TimestampManager(models.Manager):\n");
        managers.append("    \"\"\"Manager with timestamp utilities\"\"\"\n");
        managers.append("    def recent(self, days=7):\n");
        managers.append("        from datetime import timedelta\n");
        managers.append("        cutoff = timezone.now() - timedelta(days=days)\n");
        managers.append("        return self.filter(created_at__gte=cutoff)\n\n");
        
        managers.append("    def today(self):\n");
        managers.append("        return self.filter(created_at__date=timezone.now().date())\n\n");
        
        return managers.toString();
    }
    
    private String generateModelClass(DjangoModel model) {
        StringBuilder modelCode = new StringBuilder();
        
        // Class definition
        String baseClass = model.isAbstract() ? "models.Model" : "BaseModel";
        modelCode.append("class ").append(model.getName()).append("(").append(baseClass).append("):\n");
        
        // Docstring
        if (model.getDocstring() != null) {
            modelCode.append("    \"\"\"").append(model.getDocstring()).append("\"\"\"\n");
        }
        
        // Fields
        if (!model.getFields().isEmpty()) {
            for (DjangoField field : model.getFields()) {
                modelCode.append("    ").append(field.toPythonCode()).append("\n");
            }
        }
        
        // Add timestamp fields if not present
        if (model.getFields().stream().noneMatch(f -> "created_at".equals(f.getName()))) {
            if (!model.isAbstract()) {
                modelCode.append("    created_at = models.DateTimeField(auto_now_add=True, verbose_name=_('Created at'))\n");
                modelCode.append("    updated_at = models.DateTimeField(auto_now=True, verbose_name=_('Updated at'))\n");
            }
        }
        
        // Custom managers
        modelCode.append("    objects = models.Manager()\n");
        if (model.getFields().stream().anyMatch(f -> "is_active".equals(f.getName()))) {
            modelCode.append("    active = ActiveManager()\n");
        }
        
        // Meta class
        modelCode.append("\n    class Meta:\n");
        modelCode.append("        verbose_name = _('").append(formatVerboseName(model.getName())).append("')\n");
        modelCode.append("        verbose_name_plural = _('").append(formatVerboseName(model.getName()) + "s").append("')\n");
        modelCode.append("        ordering = ['-created_at']\n");
        
        if (!model.getUniqueTogether().isEmpty()) {
            modelCode.append("        unique_together = [('").append(
                String.join("', '", model.getUniqueTogether())
            ).append("')]\n");
        }
        
        // String representation
        modelCode.append("\n    def __str__(self):\n");
        String strField = model.getFields().stream()
            .filter(f -> "name".equals(f.getName()) || "title".equals(f.getName()) || "email".equals(f.getName()))
            .findFirst()
            .map(DjangoField::getName)
            .orElse("id");
        modelCode.append("        return str(self.").append(strField).append(")\n");
        
        // Validators
        if (!model.getValidators().isEmpty()) {
            for (DjangoMethod validator : model.getValidators()) {
                modelCode.append("\n    ").append(validator.toPythonSignature()).append("\n");
                modelCode.append("        \"\"\"Validate ").append(validator.getName()).append("\"\"\"\n");
                modelCode.append("        raise ValidationError('Validation not implemented')\n");
            }
        }
        
        // Custom methods
        if (!model.getMethods().isEmpty()) {
            for (DjangoMethod method : model.getMethods()) {
                modelCode.append("\n    ").append(method.toPythonSignature()).append("\n");
                if (method.getDocstring() != null) {
                    modelCode.append("        \"\"\"").append(method.getDocstring()).append("\"\"\"\n");
                }
                modelCode.append("        pass\n");
            }
        }
        
        return modelCode.toString();
    }
    
    private String generateSignals(DjangoModels models) {
        StringBuilder signals = new StringBuilder();
        
        signals.append("# Signal handlers\n\n");
        
        for (DjangoModel model : models.getModelsList()) {
            String modelName = model.getName();
            String modelLower = modelName.substring(0, 1).toLowerCase() + modelName.substring(1);
            
            signals.append("@receiver(post_save, sender=").append(modelName).append(")\n");
            signals.append("def ").append(modelLower).append("_post_save(sender, instance, created, **kwargs):\n");
            signals.append("    \"\"\"Handle ").append(modelName).append(" post-save signal\"\"\"\n");
            signals.append("    if created:\n");
            signals.append("        # Log creation\n");
            signals.append("        import logging\n");
            signals.append("        logger = logging.getLogger(__name__)\n");
            signals.append("        logger.info(f'{sender.__name__} created: {instance.id}')\n\n");
        }
        
        return signals.toString();
    }
    
    private String formatVerboseName(String className) {
        // Convert CamelCase to "Camel case"
        return className
            .replaceAll("([a-z])([A-Z])", "$1 $2")
            .toLowerCase();
    }
    
    /**
     * Generate serializers.py for Django REST Framework
     */
    public String generateSerializers(DjangoModels models, String appName) {
        log.info("🐍 Generating Django serializers.py");
        
        StringBuilder code = new StringBuilder();
        
        code.append("from rest_framework import serializers\n");
        code.append("from apps.").append(appName).append(".models import (\n");
        
        // Import all models
        List<String> modelNames = models.getModelsList().stream()
            .map(DjangoModel::getName)
            .collect(Collectors.toList());
        
        for (int i = 0; i < modelNames.size(); i++) {
            code.append("    ").append(modelNames.get(i));
            if (i < modelNames.size() - 1) code.append(",");
            code.append("\n");
        }
        
        code.append(")\n\n");
        
        // Generate serializers for each model
        for (DjangoModel model : models.getModelsList()) {
            code.append(generateSerializer(model));
            code.append("\n\n");
        }
        
        return code.toString();
    }
    
    private String generateSerializer(DjangoModel model) {
        String modelName = model.getName();
        String serializerName = modelName + "Serializer";
        
        StringBuilder code = new StringBuilder();
        
        code.append("class ").append(serializerName).append("(serializers.ModelSerializer):\n");
        code.append("    \"\"\"Serializer for ").append(modelName).append("\"\"\"\n");
        
        // Add nested serializers for foreign keys
        for (DjangoField field : model.getFields()) {
            if ("ForeignKey".equals(field.getFieldType()) || "OneToOneField".equals(field.getFieldType())) {
                String targetModel = field.getTarget();
                code.append("    ").append(field.getName()).append(" = ")
                    .append(targetModel).append("Serializer(read_only=True)\n");
            }
        }
        
        code.append("\n    class Meta:\n");
        code.append("        model = ").append(modelName).append("\n");
        code.append("        fields = [\n");
        
        List<String> fieldNames = model.getFields().stream()
            .map(DjangoField::getName)
            .collect(Collectors.toList());
        
        fieldNames.add(0, "id");
        fieldNames.add("created_at");
        fieldNames.add("updated_at");
        
        for (String fieldName : fieldNames) {
            code.append("            '").append(fieldName).append("',\n");
        }
        
        code.append("        ]\n");
        code.append("        read_only_fields = ['id', 'created_at', 'updated_at']\n");
        
        return code.toString();
    }
    
    /**
     * Generate viewsets.py for Django REST Framework
     */
    public String generateViewSets(DjangoModels models, String appName) {
        log.info("🐍 Generating Django viewsets.py");
        
        StringBuilder code = new StringBuilder();
        
        code.append("from rest_framework import viewsets, status\n");
        code.append("from rest_framework.decorators import action\n");
        code.append("from rest_framework.response import Response\n");
        code.append("from rest_framework.permissions import IsAuthenticated\n");
        code.append("from django_filters.rest_framework import DjangoFilterBackend\n");
        code.append("from rest_framework.filters import SearchFilter, OrderingFilter\n");
        code.append("from apps.").append(appName).append(".models import (\n");
        
        // Import all models
        for (DjangoModel model : models.getModelsList()) {
            code.append("    ").append(model.getName()).append(",\n");
        }
        
        code.append(")\n");
        code.append("from .serializers import (\n");
        
        // Import all serializers
        for (DjangoModel model : models.getModelsList()) {
            code.append("    ").append(model.getName()).append("Serializer,\n");
        }
        
        code.append(")\n\n");
        
        // Generate viewsets for each model
        for (DjangoModel model : models.getModelsList()) {
            code.append(generateViewSet(model));
            code.append("\n\n");
        }
        
        return code.toString();
    }
    
    private String generateViewSet(DjangoModel model) {
        String modelName = model.getName();
        String viewsetName = modelName + "ViewSet";
        String serializerName = modelName + "Serializer";
        
        StringBuilder code = new StringBuilder();
        
        code.append("class ").append(viewsetName).append("(viewsets.ModelViewSet):\n");
        code.append("    \"\"\"ViewSet for ").append(modelName).append("\"\"\"\n");
        code.append("    queryset = ").append(modelName).append(".objects.all()\n");
        code.append("    serializer_class = ").append(serializerName).append("\n");
        code.append("    permission_classes = [IsAuthenticated]\n");
        code.append("    filter_backends = [DjangoFilterBackend, SearchFilter, OrderingFilter]\n");
        code.append("    filterset_fields = [");
        
        // Filter fields
        List<String> filterableFields = model.getFields().stream()
            .filter(f -> !f.getFieldType().contains("Text"))
            .map(DjangoField::getName)
            .limit(5)
            .collect(Collectors.toList());
        
        for (int i = 0; i < filterableFields.size(); i++) {
            code.append("'").append(filterableFields.get(i)).append("'");
            if (i < filterableFields.size() - 1) code.append(", ");
        }
        
        code.append("]\n");
        code.append("    search_fields = [");
        
        // Search fields
        List<String> searchFields = model.getFields().stream()
            .filter(f -> f.getFieldType().contains("Char") || f.getFieldType().contains("Text"))
            .map(DjangoField::getName)
            .limit(3)
            .collect(Collectors.toList());
        
        for (int i = 0; i < searchFields.size(); i++) {
            code.append("'").append(searchFields.get(i)).append("'");
            if (i < searchFields.size() - 1) code.append(", ");
        }
        
        code.append("]\n");
        code.append("    ordering_fields = ['created_at', 'updated_at']\n");
        code.append("    ordering = ['-created_at']\n");
        
        return code.toString();
    }
}
