package com.example.blog.repository;

import com.example.blog.entity.Post;
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
 * Repository for Post entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface PostRepository extends 
        JpaRepository<Post, Long>,
        JpaSpecificationExecutor<Post> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Post by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Post> findByStatus(PostStatus status);

    /**
     * Find Post by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Post> findByStatus(PostStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Post created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Post e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Post paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Post entities
     */
    @EntityGraph(attributePaths = {})
    Page<Post> findAllActive(Pageable pageable);

    /**
     * Find recently created Post entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Post e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Post> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Post exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Post e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Post with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Post e")
    List<Post> findAllWithEagerLoading();

}
