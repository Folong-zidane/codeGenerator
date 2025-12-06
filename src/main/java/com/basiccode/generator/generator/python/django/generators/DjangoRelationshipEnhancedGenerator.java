package com.basiccode.generator.generator.python.django.generators;

import com.basiccode.generator.model.ClassModel;
import com.basiccode.generator.model.Field;
import com.basiccode.generator.model.Relationship;
import java.util.*;

/**
 * DjangoRelationshipEnhancedGenerator - Phase 2
 * Advanced relationship handling with cascade options, related_name optimization,
 * select_related/prefetch_related hints, and through model support.
 */
public class DjangoRelationshipEnhancedGenerator {

    private final String moduleName;
    private final String appName;

    public DjangoRelationshipEnhancedGenerator(String moduleName, String appName) {
        this.moduleName = moduleName;
        this.appName = appName;
    }

    public String generateEnhancedRelationshipField(Relationship relationship, ClassModel sourceEntity, ClassModel targetEntity) {
        StringBuilder code = new StringBuilder();
        String fieldName = formatFieldName(relationship.getSourceProperty());
        String targetModel = formatClassName(targetEntity.getName());

        switch (relationship.getType()) {
            case "ONETOMANY":
                code.append(generateForeignKeyField(fieldName, targetModel, relationship));
                break;
            case "MANYTOMANY":
                code.append(generateManyToManyField(fieldName, targetModel, relationship, sourceEntity, targetEntity));
                break;
            case "ONETOONE":
                code.append(generateOneToOneField(fieldName, targetModel, relationship));
                break;
        }

        return code.toString();
    }

    private String generateForeignKeyField(String fieldName, String targetModel, Relationship relationship) {
        StringBuilder code = new StringBuilder();
        String cascade = determineCascadeOption(relationship.getCascadeDelete());
        String relatedName = generateRelatedName(relationship.getTargetProperty());

        code.append("    ").append(fieldName).append(" = models.ForeignKey(\n");
        code.append("        '").append(targetModel).append("',\n");
        code.append("        on_delete=models.").append(cascade).append(",\n");
        code.append("        related_name='").append(relatedName).append("',\n");
        code.append("        db_index=True,\n");
        code.append("        null=").append(isNullable(relationship)).append(",\n");
        code.append("        blank=").append(isNullable(relationship)).append("\n");
        code.append("    )\n");

        return code.toString();
    }

    private String generateManyToManyField(String fieldName, String targetModel, Relationship relationship, ClassModel source, ClassModel target) {
        StringBuilder code = new StringBuilder();
        String relatedName = generateRelatedName(relationship.getTargetProperty());

        code.append("    ").append(fieldName).append(" = models.ManyToManyField(\n");
        code.append("        '").append(targetModel).append("',\n");
        code.append("        related_name='").append(relatedName).append("',\n");
        code.append("        db_table='").append(generateM2MTableName(source, target)).append("'\n");
        code.append("    )\n");

        return code.toString();
    }

    private String generateOneToOneField(String fieldName, String targetModel, Relationship relationship) {
        StringBuilder code = new StringBuilder();
        String cascade = determineCascadeOption(relationship.getCascadeDelete());
        String relatedName = generateRelatedName(relationship.getTargetProperty());

        code.append("    ").append(fieldName).append(" = models.OneToOneField(\n");
        code.append("        '").append(targetModel).append("',\n");
        code.append("        on_delete=models.").append(cascade).append(",\n");
        code.append("        related_name='").append(relatedName).append("',\n");
        code.append("        null=").append(isNullable(relationship)).append(",\n");
        code.append("        blank=").append(isNullable(relationship)).append("\n");
        code.append("    )\n");

        return code.toString();
    }

    public String generateThroughModel(ClassModel source, ClassModel target, List<Field> extraAttributes) {
        StringBuilder code = new StringBuilder();
        String throughName = generateThroughModelName(source, target);

        code.append("class ").append(throughName).append("(models.Model):\n");
        code.append("    ").append(formatFieldName(source.getName().toLowerCase()))
            .append(" = models.ForeignKey('").append(formatClassName(source.getName()))
            .append("', on_delete=models.CASCADE)\n");
        code.append("    ").append(formatFieldName(target.getName().toLowerCase()))
            .append(" = models.ForeignKey('").append(formatClassName(target.getName()))
            .append("', on_delete=models.CASCADE)\n");

        if (extraAttributes != null && !extraAttributes.isEmpty()) {
            for (Field attr : extraAttributes) {
                code.append("    ").append(formatFieldName(attr.getName())).append(" = ")
                    .append(generateDjangoFieldType(attr.getType())).append("\n");
            }
        }

        code.append("    created_at = models.DateTimeField(auto_now_add=True)\n");
        code.append("    updated_at = models.DateTimeField(auto_now=True)\n\n");

        code.append("    class Meta:\n");
        code.append("        db_table = '").append(generateM2MTableName(source, target)).append("'\n");
        code.append("        unique_together = [('")
            .append(formatFieldName(source.getName().toLowerCase())).append("', '")
            .append(formatFieldName(target.getName().toLowerCase())).append("')]\n\n");

        code.append("    def __str__(self):\n");
        code.append("        return f'{self.").append(formatFieldName(source.getName().toLowerCase()))
            .append("} - {self.").append(formatFieldName(target.getName().toLowerCase()))
            .append("}'\n");

        return code.toString();
    }

    public String generateQueryOptimizationHints(List<Relationship> relationships) {
        StringBuilder code = new StringBuilder();
        
        List<String> selectRelated = new ArrayList<>();
        List<String> prefetchRelated = new ArrayList<>();
        
        for (Relationship rel : relationships) {
            if ("ONETOMANY".equals(rel.getType()) || "ONETOONE".equals(rel.getType())) {
                selectRelated.add(formatFieldName(rel.getSourceProperty()));
            } else if ("MANYTOMANY".equals(rel.getType())) {
                prefetchRelated.add(formatFieldName(rel.getSourceProperty()));
            }
        }
        
        if (!selectRelated.isEmpty() || !prefetchRelated.isEmpty()) {
            code.append("    @classmethod\n");
            code.append("    def get_optimized_queryset(cls):\n");
            code.append("        queryset = cls.objects.all()\n");
            
            if (!selectRelated.isEmpty()) {
                code.append("        queryset = queryset.select_related(");
                for (int i = 0; i < selectRelated.size(); i++) {
                    code.append("'").append(selectRelated.get(i)).append("'");
                    if (i < selectRelated.size() - 1) code.append(", ");
                }
                code.append(")\n");
            }
            
            if (!prefetchRelated.isEmpty()) {
                code.append("        queryset = queryset.prefetch_related(");
                for (int i = 0; i < prefetchRelated.size(); i++) {
                    code.append("'").append(prefetchRelated.get(i)).append("'");
                    if (i < prefetchRelated.size() - 1) code.append(", ");
                }
                code.append(")\n");
            }
            
            code.append("        return queryset\n\n");
        }
        
        return code.toString();
    }

    // Utility methods
    
    private String determineCascadeOption(boolean cascadeDelete) {
        return cascadeDelete ? "CASCADE" : "PROTECT";
    }
    
    private String generateRelatedName(String targetProperty) {
        if (targetProperty == null || targetProperty.isEmpty()) {
            return "related_%(class)s_set";
        }
        return formatFieldName(targetProperty);
    }
    
    private String generateThroughModelName(ClassModel source, ClassModel target) {
        return formatClassName(source.getName()) + formatClassName(target.getName());
    }
    
    private String generateM2MTableName(ClassModel source, ClassModel target) {
        String sourceName = source.getName().toLowerCase();
        String targetName = target.getName().toLowerCase();
        return appName + "_" + sourceName + "_" + targetName;
    }
    
    private boolean isNullable(Relationship relationship) {
        return relationship.getSourceMultiplicity() == 0;
    }
    
    private String formatFieldName(String name) {
        if (name == null || name.isEmpty()) {
            return "related_object";
        }
        return name.toLowerCase().replaceAll("[^a-zA-Z0-9_]", "_");
    }
    
    private String formatClassName(String name) {
        if (name == null || name.isEmpty()) {
            return "RelatedModel";
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    
    private String generateDjangoFieldType(String javaType) {
        switch (javaType.toLowerCase()) {
            case "string":
                return "models.CharField(max_length=255)";
            case "int":
            case "integer":
                return "models.IntegerField()";
            case "long":
                return "models.BigIntegerField()";
            case "float":
            case "double":
                return "models.FloatField()";
            case "boolean":
                return "models.BooleanField(default=False)";
            case "date":
                return "models.DateField()";
            case "datetime":
                return "models.DateTimeField()";
            case "decimal":
                return "models.DecimalField(max_digits=10, decimal_places=2)";
            case "text":
                return "models.TextField()";
            case "uuid":
                return "models.UUIDField()";
            default:
                return "models.CharField(max_length=255)";
        }
    }
}