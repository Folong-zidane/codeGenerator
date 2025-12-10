package com.example.blog.repository;

import com.example.blog.entity.RealTimeUpdate;
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
import com.example.blog.enums.RealTimeUpdateStatus;

/**
 * Repository for RealTimeUpdate entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface RealTimeUpdateRepository extends 
        JpaRepository<RealTimeUpdate, Long>,
        JpaSpecificationExecutor<RealTimeUpdate> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all RealTimeUpdate by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<RealTimeUpdate> findByStatus(RealTimeUpdateStatus status);

    /**
     * Find RealTimeUpdate by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<RealTimeUpdate> findByStatus(RealTimeUpdateStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count RealTimeUpdate created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM RealTimeUpdate e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find RealTimeUpdate paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of RealTimeUpdate entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM RealTimeUpdate e WHERE e.status = 'ACTIVE'")
    Page<RealTimeUpdate> findAllActive(Pageable pageable);

    /**
     * Find recently created RealTimeUpdate entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM RealTimeUpdate e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<RealTimeUpdate> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if RealTimeUpdate exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM RealTimeUpdate e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all RealTimeUpdate with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM RealTimeUpdate e")
    List<RealTimeUpdate> findAllWithEagerLoading();

}
