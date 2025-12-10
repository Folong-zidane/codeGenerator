package com.example.blog.repository;

import com.example.blog.entity.PWAConfig;
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
import com.example.blog.enums.PWAConfigStatus;

/**
 * Repository for PWAConfig entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface PWAConfigRepository extends 
        JpaRepository<PWAConfig, Long>,
        JpaSpecificationExecutor<PWAConfig> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all PWAConfig by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<PWAConfig> findByStatus(PWAConfigStatus status);

    /**
     * Find PWAConfig by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<PWAConfig> findByStatus(PWAConfigStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count PWAConfig created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM PWAConfig e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find PWAConfig paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of PWAConfig entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM PWAConfig e WHERE e.status = 'ACTIVE'")
    Page<PWAConfig> findAllActive(Pageable pageable);

    /**
     * Find recently created PWAConfig entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM PWAConfig e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<PWAConfig> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if PWAConfig exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM PWAConfig e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all PWAConfig with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM PWAConfig e")
    List<PWAConfig> findAllWithEagerLoading();

}
