package com.example.blog.repository;

import com.example.blog.entity.ABTest;
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
import com.example.blog.enums.ABTestStatus;

/**
 * Repository for ABTest entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface ABTestRepository extends 
        JpaRepository<ABTest, Long>,
        JpaSpecificationExecutor<ABTest> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all ABTest by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<ABTest> findByStatus(ABTestStatus status);

    /**
     * Find ABTest by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<ABTest> findByStatus(ABTestStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count ABTest created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM ABTest e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find ABTest paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of ABTest entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM ABTest e WHERE e.status = 'ACTIVE'")
    Page<ABTest> findAllActive(Pageable pageable);

    /**
     * Find recently created ABTest entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM ABTest e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<ABTest> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if ABTest exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM ABTest e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all ABTest with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM ABTest e")
    List<ABTest> findAllWithEagerLoading();

}
