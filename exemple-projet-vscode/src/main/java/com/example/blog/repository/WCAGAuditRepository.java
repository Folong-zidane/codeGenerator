package com.example.blog.repository;

import com.example.blog.entity.WCAGAudit;
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
import com.example.blog.enums.WCAGAuditStatus;

/**
 * Repository for WCAGAudit entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface WCAGAuditRepository extends 
        JpaRepository<WCAGAudit, Long>,
        JpaSpecificationExecutor<WCAGAudit> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all WCAGAudit by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<WCAGAudit> findByStatus(WCAGAuditStatus status);

    /**
     * Find WCAGAudit by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<WCAGAudit> findByStatus(WCAGAuditStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count WCAGAudit created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM WCAGAudit e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find WCAGAudit paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of WCAGAudit entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM WCAGAudit e WHERE e.status = 'ACTIVE'")
    Page<WCAGAudit> findAllActive(Pageable pageable);

    /**
     * Find recently created WCAGAudit entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM WCAGAudit e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<WCAGAudit> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if WCAGAudit exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM WCAGAudit e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all WCAGAudit with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM WCAGAudit e")
    List<WCAGAudit> findAllWithEagerLoading();

}
