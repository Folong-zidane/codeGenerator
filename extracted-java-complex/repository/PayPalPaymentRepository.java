package com.ecommerce.complex.repository;

import com.ecommerce.complex.entity.PayPalPayment;
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
 * Repository for PayPalPayment entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface PayPalPaymentRepository extends 
        JpaRepository<PayPalPayment, Long>,
        JpaSpecificationExecutor<PayPalPayment> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find PayPalPayment by email (unique)
     * @param email the email address
     * @return Optional containing the entity if found
     */
    Optional<PayPalPayment> findByEmail(String email);

    /**
     * Find all PayPalPayment by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<PayPalPayment> findByStatus(PayPalPaymentStatus status);

    /**
     * Find PayPalPayment by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<PayPalPayment> findByStatus(PayPalPaymentStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count PayPalPayment created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM PayPalPayment e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find PayPalPayment paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of PayPalPayment entities
     */
    @EntityGraph(attributePaths = {})
    Page<PayPalPayment> findAllActive(Pageable pageable);

    /**
     * Find recently created PayPalPayment entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM PayPalPayment e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<PayPalPayment> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if PayPalPayment exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM PayPalPayment e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all PayPalPayment with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM PayPalPayment e")
    List<PayPalPayment> findAllWithEagerLoading();

}
