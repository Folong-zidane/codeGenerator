package com.example.blog.repository;

import com.example.blog.entity.FeaturedItem;
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
import com.example.blog.enums.FeaturedItemStatus;

/**
 * Repository for FeaturedItem entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface FeaturedItemRepository extends 
        JpaRepository<FeaturedItem, Long>,
        JpaSpecificationExecutor<FeaturedItem> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all FeaturedItem by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<FeaturedItem> findByStatus(FeaturedItemStatus status);

    /**
     * Find FeaturedItem by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<FeaturedItem> findByStatus(FeaturedItemStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count FeaturedItem created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM FeaturedItem e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find FeaturedItem paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of FeaturedItem entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM FeaturedItem e WHERE e.status = 'ACTIVE'")
    Page<FeaturedItem> findAllActive(Pageable pageable);

    /**
     * Find recently created FeaturedItem entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM FeaturedItem e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<FeaturedItem> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if FeaturedItem exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM FeaturedItem e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all FeaturedItem with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM FeaturedItem e")
    List<FeaturedItem> findAllWithEagerLoading();

    // ========== FEATURED METHODS ==========
    
    List<FeaturedItem> findByActifTrueOrderByPositionAsc();
    
    @Query("SELECT f FROM FeaturedItem f WHERE f.actif = true AND (f.dateDebut IS NULL OR f.dateDebut <= :now) ORDER BY f.position ASC")
    List<FeaturedItem> findActiveAtDate(@Param("now") LocalDateTime now);
    
    Optional<FeaturedItem> findByArticleIdAndActifTrue(Integer articleId);
    
    @Query("SELECT f FROM FeaturedItem f WHERE f.actif = true AND f.position BETWEEN :minPos AND :maxPos ORDER BY f.position ASC")
    List<FeaturedItem> findByPositionRange(@Param("minPos") Integer minPos, @Param("maxPos") Integer maxPos);

}
