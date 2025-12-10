package com.example.blog.repository;

import com.example.blog.entity.Favori;
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
import com.example.blog.enums.FavoriStatus;

/**
 * Repository for Favori entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface FavoriRepository extends 
        JpaRepository<Favori, Long>,
        JpaSpecificationExecutor<Favori> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Favori by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Favori> findByStatus(FavoriStatus status);

    /**
     * Find Favori by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Favori> findByStatus(FavoriStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Favori created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Favori e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Favori paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Favori entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM Favori e WHERE e.status = 'ACTIVE'")
    Page<Favori> findAllActive(Pageable pageable);

    /**
     * Find recently created Favori entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Favori e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Favori> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Favori exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Favori e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Favori with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Favori e")
    List<Favori> findAllWithEagerLoading();

}
