package com.example.blog.repository;

import com.example.blog.entity.EmailNotification;
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
import com.example.blog.enums.EmailNotificationStatus;

/**
 * Repository for EmailNotification entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface EmailNotificationRepository extends 
        JpaRepository<EmailNotification, Long>,
        JpaSpecificationExecutor<EmailNotification> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all EmailNotification by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<EmailNotification> findByStatus(EmailNotificationStatus status);

    /**
     * Find EmailNotification by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<EmailNotification> findByStatus(EmailNotificationStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count EmailNotification created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM EmailNotification e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find EmailNotification paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of EmailNotification entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM EmailNotification e WHERE e.status = 'ACTIVE'")
    Page<EmailNotification> findAllActive(Pageable pageable);

    /**
     * Find recently created EmailNotification entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM EmailNotification e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<EmailNotification> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if EmailNotification exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EmailNotification e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all EmailNotification with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM EmailNotification e")
    List<EmailNotification> findAllWithEagerLoading();

}
