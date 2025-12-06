package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.Review;
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
 * Repository for Review entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface ReviewRepository extends 
        JpaRepository<Review, Long>,
        JpaSpecificationExecutor<Review> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Review by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Review> findByStatus(ReviewStatus status);

    /**
     * Find Review by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Review> findByStatus(ReviewStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Review created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Review e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Review paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Review entities
     */
    @EntityGraph(attributePaths = {})
    Page<Review> findAllActive(Pageable pageable);

    /**
     * Find recently created Review entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Review e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Review> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Review exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Review e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Review with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Review e")
    List<Review> findAllWithEagerLoading();

}
