package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.Payment;
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
 * Repository for Payment entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface PaymentRepository extends 
        JpaRepository<Payment, Long>,
        JpaSpecificationExecutor<Payment> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Payment by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Payment> findByStatus(PaymentStatus status);

    /**
     * Find Payment by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Payment created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Payment e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Payment paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Payment entities
     */
    @EntityGraph(attributePaths = {})
    Page<Payment> findAllActive(Pageable pageable);

    /**
     * Find recently created Payment entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Payment e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Payment> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Payment exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Payment e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Payment with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Payment e")
    List<Payment> findAllWithEagerLoading();

}
