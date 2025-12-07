package com.example.blog.repository;

import com.example.blog.entity.Comment;
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
 * Repository for Comment entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface CommentRepository extends 
        JpaRepository<Comment, Long>,
        JpaSpecificationExecutor<Comment> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Comment by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Comment> findByStatus(CommentStatus status);

    /**
     * Find Comment by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Comment> findByStatus(CommentStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Comment created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Comment e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Comment paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Comment entities
     */
    @EntityGraph(attributePaths = {})
    Page<Comment> findAllActive(Pageable pageable);

    /**
     * Find recently created Comment entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Comment e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Comment> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Comment exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Comment e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Comment with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Comment e")
    List<Comment> findAllWithEagerLoading();

}
