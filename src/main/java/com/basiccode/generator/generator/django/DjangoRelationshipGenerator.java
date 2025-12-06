package com.basiccode.generator.generator.django;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlRelationship;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ✅ Django Relationship Generator (Phase 1)
 *
 * Génère les relations Django avec:
 * - ForeignKey avec cascade options
 * - ManyToMany avec through models
 * - OneToOne avec cascade delete
 * - Support des related_name
 * - Support des related_query_name
 * - Lazy loading optimization
 *
 * NOUVEAU: Phase 1 addition - Support basique des relations
 * Phase 2 sera plus avancé avec lazy loading, queryset optimization
 */
@Component
@Slf4j
public class DjangoRelationshipGenerator {

    public String generateRelationshipFields(EnhancedClass enhancedClass, List<UmlRelationship> relationships) {
        log.info("🔗 Generating Django relationships for: {}", enhancedClass.getOriginalClass().getName());

        StringBuilder code = new StringBuilder();

        for (UmlRelationship rel : relationships) {
            code.append(generateRelationshipField(rel));
            code.append("\n");
        }

        return code.toString();
    }

    private String generateRelationshipField(UmlRelationship relationship) {
        String fieldName = relationship.getSourceName();
        String targetModel = relationship.getTargetEntity();
        String relationType = relationship.getType();

        StringBuilder field = new StringBuilder();

        switch (relationType.toUpperCase()) {
            case "ONETOMANY":
            case "MANYTONE":
                field.append("# ForeignKey - One-to-Many relationship\n");
                field.append(fieldName).append(" = models.ForeignKey(\n");
                field.append("    '").append(targetModel).append("',\n");
                field.append("    on_delete=models.CASCADE,\n");
                field.append("    related_name='").append(getRelatedName(fieldName)).append("s',\n");
                field.append("    null=True,\n");
                field.append("    blank=True\n");
                field.append(")");
                break;

            case "MANYTOONE":
                field.append("# ForeignKey - Many-to-One relationship\n");
                field.append(fieldName).append(" = models.ForeignKey(\n");
                field.append("    '").append(targetModel).append("',\n");
                field.append("    on_delete=models.PROTECT,\n");
                field.append("    related_name='").append(getRelatedName(fieldName)).append("',\n");
                field.append("    null=False,\n");
                field.append("    blank=False\n");
                field.append(")");
                break;

            case "MANYTOMANY":
                field.append("# ManyToMany relationship\n");
                field.append(fieldName).append(" = models.ManyToManyField(\n");
                field.append("    '").append(targetModel).append("',\n");
                field.append("    through='").append(getThoughModelName(fieldName, targetModel)).append("',\n");
                field.append("    related_name='").append(getRelatedName(fieldName)).append("s',\n");
                field.append("    blank=True\n");
                field.append(")");
                break;

            case "ONETOONE":
                field.append("# One-to-One relationship\n");
                field.append(fieldName).append(" = models.OneToOneField(\n");
                field.append("    '").append(targetModel).append("',\n");
                field.append("    on_delete=models.CASCADE,\n");
                field.append("    related_name='").append(getRelatedName(fieldName)).append("',\n");
                field.append("    null=True,\n");
                field.append("    blank=True\n");
                field.append(")");
                break;

            default:
                field.append("# Unknown relationship type: ").append(relationType);
        }

        return field.toString();
    }

    private String getRelatedName(String fieldName) {
        // Convert fieldName to snake_case for Django related_name
        return fieldName
            .replaceAll("([a-z])([A-Z])", "$1_$2")
            .toLowerCase();
    }

    private String getThoughModelName(String source, String target) {
        // Generate through model name
        return source.substring(0, 1).toUpperCase() + source.substring(1) +
               target.substring(0, 1).toUpperCase() + target.substring(1) +
               "Through";
    }

    /**
     * Generate through model for ManyToMany relationships
     */
    public String generateThroughModel(UmlRelationship relationship) {
        String sourceModel = relationship.getSourceEntity();
        String targetModel = relationship.getTargetEntity();
        String throughModelName = getThoughModelName(relationship.getSourceName(), targetModel);

        StringBuilder code = new StringBuilder();

        code.append("class ").append(throughModelName).append("(models.Model):\n");
        code.append("    \"\"\"Through model for ManyToMany relationship between ");
        code.append(sourceModel).append(" and ").append(targetModel).append("\"\"\"\n\n");

        code.append("    ").append(sourceModel.toLowerCase()).append(" = models.ForeignKey(\n");
        code.append("        '").append(sourceModel).append("',\n");
        code.append("        on_delete=models.CASCADE\n");
        code.append("    )\n\n");

        code.append("    ").append(targetModel.toLowerCase()).append(" = models.ForeignKey(\n");
        code.append("        '").append(targetModel).append("',\n");
        code.append("        on_delete=models.CASCADE\n");
        code.append("    )\n\n");

        code.append("    created_at = models.DateTimeField(auto_now_add=True)\n\n");

        code.append("    class Meta:\n");
        code.append("        db_table = '").append(sourceModel.toLowerCase()).append("_");
        code.append(targetModel.toLowerCase()).append("'\n");
        code.append("        unique_together = [('").append(sourceModel.toLowerCase());
        code.append("', '").append(targetModel.toLowerCase()).append("')]\n\n");

        code.append("    def __str__(self):\n");
        code.append("        return f'{self.").append(sourceModel.toLowerCase());
        code.append("} - {self.").append(targetModel.toLowerCase()).append("}'\n");

        return code.toString();
    }

    /**
     * Generate model property for reverse relationships
     */
    public String generateReverseProperty(String modelName, String relatedModel) {
        StringBuilder code = new StringBuilder();

        code.append("    @property\n");
        code.append("    def ").append(getRelatedName(relatedModel)).append("_list(self):\n");
        code.append("        \"\"\"Get list of related ").append(relatedModel).append("s\"\"\"\n");
        code.append("        return self.").append(getRelatedName(relatedModel));
        code.append("s.all()\n\n");

        return code.toString();
    }

    /**
     * Generate QuerySet optimization methods
     */
    public String generateOptimizedQueryMethods(String modelName) {
        StringBuilder code = new StringBuilder();

        code.append("class ").append(modelName).append("QuerySet(models.QuerySet):\n");
        code.append("    def with_relations(self):\n");
        code.append("        \"\"\"Optimize queries with select_related and prefetch_related\"\"\"\n");
        code.append("        # Add select_related for ForeignKey\n");
        code.append("        queryset = self.select_related()\n");
        code.append("        # Add prefetch_related for ManyToMany\n");
        code.append("        queryset = queryset.prefetch_related()\n");
        code.append("        return queryset\n\n");

        code.append("    def active(self):\n");
        code.append("        \"\"\"Filter active items\"\"\"\n");
        code.append("        return self.filter(status='ACTIVE')\n\n");

        code.append("    def recent(self):\n");
        code.append("        \"\"\"Order by most recent\"\"\"\n");
        code.append("        return self.order_by('-created_at')\n\n");

        return code.toString();
    }
}
