package com.example.blog.repository;

import com.example.blog.entity.MediaFile;
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
import java.util.UUID;
import com.example.blog.enums.MediaFileStatus;

/**
 * Repository for MediaFile entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface MediaFileRepository extends 
        JpaRepository<MediaFile, UUID>,
        JpaSpecificationExecutor<MediaFile> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all MediaFile by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<MediaFile> findByStatus(MediaFileStatus status);

    /**
     * Find MediaFile by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<MediaFile> findByStatus(MediaFileStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count MediaFile created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM MediaFile e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find MediaFile paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of MediaFile entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM MediaFile e WHERE e.status = 'ACTIVE'")
    Page<MediaFile> findAllActive(Pageable pageable);
    
    Optional<MediaFile> findByHashSha256(String hashSha256);

    /**
     * Find recently created MediaFile entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM MediaFile e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<MediaFile> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if MediaFile exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM MediaFile e WHERE e.id = :id")
    boolean existsById(@Param("id") UUID id);

    /**
     * Find all MediaFile with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM MediaFile e")
    List<MediaFile> findAllWithEagerLoading();

}
