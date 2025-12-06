package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.Entity;
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
 * Repository for Entity entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface EntityRepository extends 
        JpaRepository<Entity, Long>,
        JpaSpecificationExecutor<Entity> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Entity by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Entity> findByStatus(EntityStatus status);

    /**
     * Find Entity by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Entity> findByStatus(EntityStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Entity created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Entity e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Entity paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Entity entities
     */
    @EntityGraph(attributePaths = {})
    Page<Entity> findAllActive(Pageable pageable);

    /**
     * Find recently created Entity entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Entity e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Entity> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Entity exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Entity e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Entity with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Entity e")
    List<Entity> findAllWithEagerLoading();

}
