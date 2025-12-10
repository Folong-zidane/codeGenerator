package com.example.blog.repository;

import com.example.blog.entity.DraftSession;
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
import com.example.blog.enums.DraftSessionStatus;

/**
 * Repository for DraftSession entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface DraftSessionRepository extends 
        JpaRepository<DraftSession, Long>,
        JpaSpecificationExecutor<DraftSession> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all DraftSession by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<DraftSession> findByStatus(DraftSessionStatus status);

    /**
     * Find DraftSession by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<DraftSession> findByStatus(DraftSessionStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count DraftSession created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM DraftSession e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find DraftSession paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of DraftSession entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM DraftSession e WHERE e.status = 'ACTIVE'")
    Page<DraftSession> findAllActive(Pageable pageable);

    /**
     * Find recently created DraftSession entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM DraftSession e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<DraftSession> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if DraftSession exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM DraftSession e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all DraftSession with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM DraftSession e")
    List<DraftSession> findAllWithEagerLoading();

}
