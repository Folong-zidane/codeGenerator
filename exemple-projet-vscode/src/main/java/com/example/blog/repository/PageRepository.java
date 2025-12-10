package com.example.blog.repository;

import com.example.blog.entity.Page;
import org.springframework.data.domain.Pageable;
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
import com.example.blog.enums.PageStatus;

/**
 * Repository for Page entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface PageRepository extends 
        JpaRepository<Page, Long>,
        JpaSpecificationExecutor<Page> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Page by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Page> findByStatus(PageStatus status);

    /**
     * Find Page by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    org.springframework.data.domain.Page<Page> findByStatus(PageStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Page created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Page e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Page paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Page entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT e FROM Page e WHERE e.status = 'ACTIVE'")
    org.springframework.data.domain.Page<Page> findAllActive(Pageable pageable);

    /**
     * Find recently created Page entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Page e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Page> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Page exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Page e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Page with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Page e")
    List<Page> findAllWithEagerLoading();

}
