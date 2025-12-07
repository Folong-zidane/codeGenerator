package com.example.blog.repository;

import com.example.blog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Category entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface CategoryRepository extends 
        JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find Category by exact name
     * @param name the entity name
     * @return Optional containing the entity if found
     */
    Optional<Category> findByName(String name);

    /**
     * Find Category by name (case-insensitive, contains)
     * @param name the search term
     * @return List of matching entities
     */
    List<Category> findByNameContainingIgnoreCase(String name);

    /**
     * Find all Category by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Category> findByStatus(CategoryStatus status);

    /**
     * Find Category by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Category> findByStatus(CategoryStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Category created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Category e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Category paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Category entities
     */
    @EntityGraph(attributePaths = {})
    Page<Category> findAllActive(Pageable pageable);

    /**
     * Find recently created Category entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Category e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Category> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Category exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Category e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Category with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Category e")
    List<Category> findAllWithEagerLoading();

    /**
     * Search Category by name pattern
     * @param namePattern the search pattern
     * @param pageable pagination information
     * @return Page of matching entities
     */
    @Query("SELECT e FROM Category e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    Page<Category> searchByName(@Param("pattern") String namePattern, Pageable pageable);

}
