package com.example.blog.repository;

import com.example.blog.entity.Recommandation;
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
import com.example.blog.enums.RecommandationStatus;

/**
 * Repository for Recommandation entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface RecommandationRepository extends 
        JpaRepository<Recommandation, Long>,
        JpaSpecificationExecutor<Recommandation> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Recommandation by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Recommandation> findByStatus(RecommandationStatus status);

    /**
     * Find Recommandation by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Recommandation> findByStatus(RecommandationStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Recommandation created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Recommandation e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Recommandation paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Recommandation entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM Recommandation e WHERE e.status = 'ACTIVE'")
    Page<Recommandation> findAllActive(Pageable pageable);

    /**
     * Find recently created Recommandation entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Recommandation e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Recommandation> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Recommandation exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Recommandation e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Recommandation with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Recommandation e")
    List<Recommandation> findAllWithEagerLoading();

}
