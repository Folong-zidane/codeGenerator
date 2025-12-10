package com.example.blog.repository;

import com.example.blog.entity.SystemLog;
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
import com.example.blog.enums.SystemLogStatus;

/**
 * Repository for SystemLog entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface SystemLogRepository extends 
        JpaRepository<SystemLog, Long>,
        JpaSpecificationExecutor<SystemLog> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all SystemLog by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<SystemLog> findByStatus(SystemLogStatus status);

    /**
     * Find SystemLog by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<SystemLog> findByStatus(SystemLogStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count SystemLog created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM SystemLog e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find SystemLog paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of SystemLog entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM SystemLog e WHERE e.status = 'ACTIVE'")
    Page<SystemLog> findAllActive(Pageable pageable);

    /**
     * Find recently created SystemLog entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM SystemLog e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<SystemLog> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if SystemLog exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM SystemLog e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all SystemLog with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM SystemLog e")
    List<SystemLog> findAllWithEagerLoading();

}
