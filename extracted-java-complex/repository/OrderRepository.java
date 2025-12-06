package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.Order;
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
 * Repository for Order entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface OrderRepository extends 
        JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Order by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Find Order by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Order created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Order e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Order paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Order entities
     */
    @EntityGraph(attributePaths = {})
    Page<Order> findAllActive(Pageable pageable);

    /**
     * Find recently created Order entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Order e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Order> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Order exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Order e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Order with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Order e")
    List<Order> findAllWithEagerLoading();

}
