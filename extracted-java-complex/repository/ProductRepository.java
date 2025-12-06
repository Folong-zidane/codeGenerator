package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.Product;
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
 * Repository for Product entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface ProductRepository extends 
        JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find Product by exact name
     * @param name the entity name
     * @return Optional containing the entity if found
     */
    Optional<Product> findByName(String name);

    /**
     * Find Product by name (case-insensitive, contains)
     * @param name the search term
     * @return List of matching entities
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Find all Product by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Product> findByStatus(ProductStatus status);

    /**
     * Find Product by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Product created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Product e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Product paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Product entities
     */
    @EntityGraph(attributePaths = {})
    Page<Product> findAllActive(Pageable pageable);

    /**
     * Find recently created Product entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Product e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Product> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Product exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Product e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Product with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Product e")
    List<Product> findAllWithEagerLoading();

    /**
     * Search Product by name pattern
     * @param namePattern the search pattern
     * @param pageable pagination information
     * @return Page of matching entities
     */
    @Query("SELECT e FROM Product e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    Page<Product> searchByName(@Param("pattern") String namePattern, Pageable pageable);

}
