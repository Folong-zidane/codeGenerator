package com.example.blog.repository;

import com.example.blog.entity.User;
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
 * Repository for User entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface UserRepository extends 
        JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find User by email (unique)
     * @param email the email address
     * @return Optional containing the entity if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all User by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Find User by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count User created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM User e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find User paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of User entities
     */
    @EntityGraph(attributePaths = {})
    Page<User> findAllActive(Pageable pageable);

    /**
     * Find recently created User entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM User e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<User> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if User exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM User e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all User with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM User e")
    List<User> findAllWithEagerLoading();

}
