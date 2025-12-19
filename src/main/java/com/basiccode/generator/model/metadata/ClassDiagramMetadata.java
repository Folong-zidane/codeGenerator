package com.basiccode.generator.model.metadata;

public class ClassDiagramMetadata {
    // ID Generation
    private String idGenerationStrategy = "uuid"; // uuid|auto-increment|sequence
    private String idType = "UUID"; // UUID|Long|String|Integer
    private String idColumnName = "id";
    
    // Naming Conventions
    private String tableNamingConvention = "snake_case"; // snake_case|camelCase|PascalCase
    private String columnNamingConvention = "snake_case";
    private String schemaName = "public";
    private boolean tablePrefix = false;
    private String tablePrefixValue = "tbl_";
    
    // Audit & Versioning
    private boolean auditFields = false;
    private String auditStrategy = "created-updated";
    private String createdAtField = "createdAt";
    private String updatedAtField = "updatedAt";
    private String versionField = "version";
    
    // Soft Delete
    private boolean softDelete = false;
    private String softDeleteStrategy = "timestamp";
    private String softDeleteField = "deletedAt";
    
    // Validation
    private String validationFramework = "jakarta-validation";
    private boolean nullSafety = true;
    private boolean constraintAnnotations = true;
    
    // Code Generation
    private boolean useLombok = true;
    private String lombokAnnotations = "@Data";
    private boolean generateBuilders = true;
    private String generateConstructors = "all-args";
    
    // Cache
    private String cacheStrategy = "none";
    private int cacheTtlSeconds = 3600;
    private boolean secondLevelCache = false;
    
    // Relations
    private boolean bidirectionalMapping = true;
    private String cascadeOperations = "none"; // none|persist|merge|remove|all
    private String fetchTypeDefault = "lazy"; // lazy|eager
    private boolean orphanRemoval = false;
    
    // Inheritance
    private String inheritanceStrategy = "single-table"; // single-table|joined|table-per-class
    private String discriminatorColumnName = "dtype";
    private String discriminatorType = "string"; // string|integer|char
    
    // Indices
    private boolean autoIndexForeignKeys = true;
    private boolean autoIndexUniqueConstraints = true;
    private boolean compositeIndices = false;
    private String indexNamingPattern = "idx_{table}_{column}";
    
    public static ClassDiagramMetadata getDefault() {
        return new ClassDiagramMetadata();
    }
    
    // Getters and setters
    public String getIdGenerationStrategy() { return idGenerationStrategy; }
    public void setIdGenerationStrategy(String idGenerationStrategy) { this.idGenerationStrategy = idGenerationStrategy; }
    
    public String getIdType() { return idType; }
    public void setIdType(String idType) { this.idType = idType; }
    
    public String getIdColumnName() { return idColumnName; }
    public void setIdColumnName(String idColumnName) { this.idColumnName = idColumnName; }
    
    public String getTableNamingConvention() { return tableNamingConvention; }
    public void setTableNamingConvention(String tableNamingConvention) { this.tableNamingConvention = tableNamingConvention; }
    
    public String getColumnNamingConvention() { return columnNamingConvention; }
    public void setColumnNamingConvention(String columnNamingConvention) { this.columnNamingConvention = columnNamingConvention; }
    
    public String getSchemaName() { return schemaName; }
    public void setSchemaName(String schemaName) { this.schemaName = schemaName; }
    
    public boolean isTablePrefix() { return tablePrefix; }
    public void setTablePrefix(boolean tablePrefix) { this.tablePrefix = tablePrefix; }
    
    public String getTablePrefixValue() { return tablePrefixValue; }
    public void setTablePrefixValue(String tablePrefixValue) { this.tablePrefixValue = tablePrefixValue; }
    
    public boolean isAuditFields() { return auditFields; }
    public void setAuditFields(boolean auditFields) { this.auditFields = auditFields; }
    
    public String getAuditStrategy() { return auditStrategy; }
    public void setAuditStrategy(String auditStrategy) { this.auditStrategy = auditStrategy; }
    
    public String getCreatedAtField() { return createdAtField; }
    public void setCreatedAtField(String createdAtField) { this.createdAtField = createdAtField; }
    
    public String getUpdatedAtField() { return updatedAtField; }
    public void setUpdatedAtField(String updatedAtField) { this.updatedAtField = updatedAtField; }
    
    public String getVersionField() { return versionField; }
    public void setVersionField(String versionField) { this.versionField = versionField; }
    
    public boolean isSoftDelete() { return softDelete; }
    public void setSoftDelete(boolean softDelete) { this.softDelete = softDelete; }
    
    public String getSoftDeleteStrategy() { return softDeleteStrategy; }
    public void setSoftDeleteStrategy(String softDeleteStrategy) { this.softDeleteStrategy = softDeleteStrategy; }
    
    public String getSoftDeleteField() { return softDeleteField; }
    public void setSoftDeleteField(String softDeleteField) { this.softDeleteField = softDeleteField; }
    
    public String getValidationFramework() { return validationFramework; }
    public void setValidationFramework(String validationFramework) { this.validationFramework = validationFramework; }
    
    public boolean isNullSafety() { return nullSafety; }
    public void setNullSafety(boolean nullSafety) { this.nullSafety = nullSafety; }
    
    public boolean isConstraintAnnotations() { return constraintAnnotations; }
    public void setConstraintAnnotations(boolean constraintAnnotations) { this.constraintAnnotations = constraintAnnotations; }
    
    public boolean isUseLombok() { return useLombok; }
    public void setUseLombok(boolean useLombok) { this.useLombok = useLombok; }
    
    public String getLombokAnnotations() { return lombokAnnotations; }
    public void setLombokAnnotations(String lombokAnnotations) { this.lombokAnnotations = lombokAnnotations; }
    
    public boolean isGenerateBuilders() { return generateBuilders; }
    public void setGenerateBuilders(boolean generateBuilders) { this.generateBuilders = generateBuilders; }
    
    public String getGenerateConstructors() { return generateConstructors; }
    public void setGenerateConstructors(String generateConstructors) { this.generateConstructors = generateConstructors; }
    
    public String getCacheStrategy() { return cacheStrategy; }
    public void setCacheStrategy(String cacheStrategy) { this.cacheStrategy = cacheStrategy; }
    
    public int getCacheTtlSeconds() { return cacheTtlSeconds; }
    public void setCacheTtlSeconds(int cacheTtlSeconds) { this.cacheTtlSeconds = cacheTtlSeconds; }
    
    public boolean isSecondLevelCache() { return secondLevelCache; }
    public void setSecondLevelCache(boolean secondLevelCache) { this.secondLevelCache = secondLevelCache; }
    
    // Relations getters/setters
    public boolean isBidirectionalMapping() { return bidirectionalMapping; }
    public void setBidirectionalMapping(boolean bidirectionalMapping) { this.bidirectionalMapping = bidirectionalMapping; }
    
    public String getCascadeOperations() { return cascadeOperations; }
    public void setCascadeOperations(String cascadeOperations) { this.cascadeOperations = cascadeOperations; }
    
    public String getFetchTypeDefault() { return fetchTypeDefault; }
    public void setFetchTypeDefault(String fetchTypeDefault) { this.fetchTypeDefault = fetchTypeDefault; }
    
    public boolean isOrphanRemoval() { return orphanRemoval; }
    public void setOrphanRemoval(boolean orphanRemoval) { this.orphanRemoval = orphanRemoval; }
    
    // Inheritance getters/setters
    public String getInheritanceStrategy() { return inheritanceStrategy; }
    public void setInheritanceStrategy(String inheritanceStrategy) { this.inheritanceStrategy = inheritanceStrategy; }
    
    public String getDiscriminatorColumnName() { return discriminatorColumnName; }
    public void setDiscriminatorColumnName(String discriminatorColumnName) { this.discriminatorColumnName = discriminatorColumnName; }
    
    public String getDiscriminatorType() { return discriminatorType; }
    public void setDiscriminatorType(String discriminatorType) { this.discriminatorType = discriminatorType; }
    
    // Indices getters/setters
    public boolean isAutoIndexForeignKeys() { return autoIndexForeignKeys; }
    public void setAutoIndexForeignKeys(boolean autoIndexForeignKeys) { this.autoIndexForeignKeys = autoIndexForeignKeys; }
    
    public boolean isAutoIndexUniqueConstraints() { return autoIndexUniqueConstraints; }
    public void setAutoIndexUniqueConstraints(boolean autoIndexUniqueConstraints) { this.autoIndexUniqueConstraints = autoIndexUniqueConstraints; }
    
    public boolean isCompositeIndices() { return compositeIndices; }
    public void setCompositeIndices(boolean compositeIndices) { this.compositeIndices = compositeIndices; }
    
    public String getIndexNamingPattern() { return indexNamingPattern; }
    public void setIndexNamingPattern(String indexNamingPattern) { this.indexNamingPattern = indexNamingPattern; }
}