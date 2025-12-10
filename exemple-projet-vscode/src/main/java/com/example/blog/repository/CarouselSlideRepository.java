package com.example.blog.repository;

import com.example.blog.entity.CarouselSlide;
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
import com.example.blog.enums.CarouselSlideStatus;

/**
 * Repository for CarouselSlide entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface CarouselSlideRepository extends 
        JpaRepository<CarouselSlide, Long>,
        JpaSpecificationExecutor<CarouselSlide> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all CarouselSlide by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<CarouselSlide> findByStatus(CarouselSlideStatus status);

    /**
     * Find CarouselSlide by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<CarouselSlide> findByStatus(CarouselSlideStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count CarouselSlide created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM CarouselSlide e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find CarouselSlide paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of CarouselSlide entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM CarouselSlide e WHERE e.status = 'ACTIVE'")
    Page<CarouselSlide> findAllActive(Pageable pageable);

    /**
     * Find recently created CarouselSlide entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM CarouselSlide e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<CarouselSlide> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if CarouselSlide exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM CarouselSlide e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all CarouselSlide with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM CarouselSlide e")
    List<CarouselSlide> findAllWithEagerLoading();

}
