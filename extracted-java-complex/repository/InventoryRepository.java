package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.Inventory;
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
 * Repository for Inventory entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface InventoryRepository extends 
        JpaRepository<Inventory, Long>,
        JpaSpecificationExecutor<Inventory> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Inventory by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Inventory> findByStatus(InventoryStatus status);

    /**
     * Find Inventory by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Inventory> findByStatus(InventoryStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Inventory created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Inventory e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Inventory paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Inventory entities
     */
    @EntityGraph(attributePaths = {})
    Page<Inventory> findAllActive(Pageable pageable);

    /**
     * Find recently created Inventory entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Inventory e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Inventory> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Inventory exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Inventory e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Inventory with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Inventory e")
    List<Inventory> findAllWithEagerLoading();

}
