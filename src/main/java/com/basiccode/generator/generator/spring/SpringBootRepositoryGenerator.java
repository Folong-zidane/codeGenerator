package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IRepositoryGenerator;
import com.basiccode.generator.model.EnhancedClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Spring Boot Repository Generator with Advanced Patterns
 * Generates repositories with:
 * - JpaSpecificationExecutor for advanced queries
 * - @EntityGraph for optimized lazy loading
 * - Custom @Query methods with pagination
 * - Type-safe query building support
 */
@Component
@Slf4j
public class SpringBootRepositoryGenerator implements IRepositoryGenerator {
    
    @Override
    public String generateRepository(EnhancedClass enhancedClass, String packageName) {
        log.info("Generating advanced repository for {}", enhancedClass.getOriginalClass().getName());
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        // Package declaration
        code.append("package ").append(packageName).append(".repository;\n\n");
        
        // Imports
        generateImports(code, packageName, className);
        
        // Repository interface
        code.append("/**\n");
        code.append(" * Repository for ").append(className).append(" entity\n");
        code.append(" * Supports advanced queries, pagination, and specifications\n");
        code.append(" */\n");
        code.append("@Repository\n");
        code.append("public interface ").append(className).append("Repository extends \n");
        code.append("        JpaRepository<").append(className).append(", Long>,\n");
        code.append("        JpaSpecificationExecutor<").append(className).append("> {\n\n");
        
        // Add common query methods
        generateCommonQueryMethods(code, className, enhancedClass);
        
        // Add custom query methods
        generateCustomQueryMethods(code, className, enhancedClass);
        
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Generates necessary imports for the repository
     */
    private void generateImports(StringBuilder code, String packageName, String className) {
        code.append("import ").append(packageName).append(".entity.").append(className).append(";\n");
        code.append("import org.springframework.data.domain.Page;\n");
        code.append("import org.springframework.data.domain.Pageable;\n");
        code.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
        code.append("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n");
        code.append("import org.springframework.data.jpa.repository.Query;\n");
        code.append("import org.springframework.data.jpa.repository.EntityGraph;\n");
        code.append("import org.springframework.data.repository.query.Param;\n");
        code.append("import org.springframework.stereotype.Repository;\n");
        code.append("import java.time.LocalDateTime;\n");
        code.append("import java.util.List;\n");
        code.append("import java.util.Optional;\n\n");
    }
    
    /**
     * Generates common query methods based on entity attributes
     */
    private void generateCommonQueryMethods(StringBuilder code, String className, EnhancedClass enhancedClass) {
        code.append("    // ========== SIMPLE QUERY METHODS ==========\n\n");
        
        // Find by active status if field exists
        if (hasField(enhancedClass, "isActive")) {
            code.append("    /**\n");
            code.append("     * Find all active ").append(className).append(" entities\n");
            code.append("     * @return List of active entities\n");
            code.append("     */\n");
            code.append("    List<").append(className).append("> findByIsActiveTrue();\n\n");
        }
        
        // Find by name/title if exists
        if (hasField(enhancedClass, "name")) {
            code.append("    /**\n");
            code.append("     * Find ").append(className).append(" by exact name\n");
            code.append("     * @param name the entity name\n");
            code.append("     * @return Optional containing the entity if found\n");
            code.append("     */\n");
            code.append("    Optional<").append(className).append("> findByName(String name);\n\n");
            
            code.append("    /**\n");
            code.append("     * Find ").append(className).append(" by name (case-insensitive, contains)\n");
            code.append("     * @param name the search term\n");
            code.append("     * @return List of matching entities\n");
            code.append("     */\n");
            code.append("    List<").append(className).append("> findByNameContainingIgnoreCase(String name);\n\n");
        }
        
        if (hasField(enhancedClass, "email")) {
            code.append("    /**\n");
            code.append("     * Find ").append(className).append(" by email (unique)\n");
            code.append("     * @param email the email address\n");
            code.append("     * @return Optional containing the entity if found\n");
            code.append("     */\n");
            code.append("    Optional<").append(className).append("> findByEmail(String email);\n\n");
        }
        
        // Add state-based queries if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            
            code.append("    /**\n");
            code.append("     * Find all ").append(className).append(" by status\n");
            code.append("     * @param status the status enum\n");
            code.append("     * @return List of entities with given status\n");
            code.append("     */\n");
            code.append("    List<").append(className).append("> findByStatus(").append(enumName).append(" status);\n\n");
            
            code.append("    /**\n");
            code.append("     * Find ").append(className).append(" by status with pagination\n");
            code.append("     * @param status the status enum\n");
            code.append("     * @param pageable pagination information\n");
            code.append("     * @return Page of entities with given status\n");
            code.append("     */\n");
            code.append("    Page<").append(className).append("> findByStatus(").append(enumName).append(" status, Pageable pageable);\n\n");
        }
    }
    
    /**
     * Generates custom query methods with @Query annotations
     */
    private void generateCustomQueryMethods(StringBuilder code, String className, EnhancedClass enhancedClass) {
        code.append("    // ========== CUSTOM QUERY METHODS ==========\n\n");
        
        code.append("    /**\n");
        code.append("     * Count ").append(className).append(" created after specified date\n");
        code.append("     * @param date the cutoff date\n");
        code.append("     * @return count of entities created after date\n");
        code.append("     */\n");
        code.append("    @Query(\"SELECT COUNT(e) FROM ").append(className).append(" e WHERE e.createdAt >= :date\")\n");
        code.append("    long countCreatedAfter(@Param(\"date\") LocalDateTime date);\n\n");
        
        code.append("    /**\n");
        code.append("     * Find ").append(className).append(" paginated and sorted\n");
        code.append("     * @param pageable pagination and sorting information\n");
        code.append("     * @return Page of ").append(className).append(" entities\n");
        code.append("     */\n");
        code.append("    @EntityGraph(attributePaths = {})\n");
        code.append("    Page<").append(className).append("> findAllActive(Pageable pageable);\n\n");
        
        code.append("    /**\n");
        code.append("     * Find recently created ").append(className).append(" entities (last N days)\n");
        code.append("     * @param days number of days to look back\n");
        code.append("     * @return List of recently created entities\n");
        code.append("     */\n");
        code.append("    @Query(\"SELECT e FROM ").append(className).append(" e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC\")\n");
        code.append("    List<").append(className).append("> findRecentlyCreated(@Param(\"date\") LocalDateTime date);\n\n");
        
        code.append("    /**\n");
        code.append("     * Check if ").append(className).append(" exists by ID\n");
        code.append("     * @param id the entity ID\n");
        code.append("     * @return true if exists, false otherwise\n");
        code.append("     */\n");
        code.append("    @Query(\"SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ").append(className).append(" e WHERE e.id = :id\")\n");
        code.append("    boolean existsById(@Param(\"id\") Long id);\n\n");
        
        code.append("    /**\n");
        code.append("     * Find all ").append(className).append(" with optional eager loading\n");
        code.append("     * @return List of all entities\n");
        code.append("     */\n");
        code.append("    @EntityGraph(attributePaths = {})\n");
        code.append("    @Query(\"SELECT DISTINCT e FROM ").append(className).append(" e\")\n");
        code.append("    List<").append(className).append("> findAllWithEagerLoading();\n\n");
        
        // Add search method for name field
        if (hasField(enhancedClass, "name")) {
            code.append("    /**\n");
            code.append("     * Search ").append(className).append(" by name pattern\n");
            code.append("     * @param namePattern the search pattern\n");
            code.append("     * @param pageable pagination information\n");
            code.append("     * @return Page of matching entities\n");
            code.append("     */\n");
            code.append("    @Query(\"SELECT e FROM ").append(className).append(" e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :pattern, '%'))\")\n");
            code.append("    Page<").append(className).append("> searchByName(@Param(\"pattern\") String namePattern, Pageable pageable);\n\n");
        }
    }
    
    private boolean hasField(EnhancedClass enhancedClass, String fieldName) {
        return enhancedClass.getOriginalClass().getAttributes().stream()
            .anyMatch(attr -> fieldName.equalsIgnoreCase(attr.getName()));
    }
    
    @Override
    public String getRepositoryDirectory() {
        return "repository";
    }
}