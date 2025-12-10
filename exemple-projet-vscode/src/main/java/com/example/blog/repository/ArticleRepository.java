package com.example.blog.repository;

import com.example.blog.entity.Article;
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
import com.example.blog.enums.ArticleStatus;

/**
 * Repository for Article entity
 * Supports advanced queries, pagination, and specifications
 */
@Repository
public interface ArticleRepository extends 
        JpaRepository<Article, Long>,
        JpaSpecificationExecutor<Article> {

    // ========== SIMPLE QUERY METHODS ==========

    /**
     * Find all Article by status
     * @param status the status enum
     * @return List of entities with given status
     */
    List<Article> findByStatus(ArticleStatus status);

    /**
     * Find Article by status with pagination
     * @param status the status enum
     * @param pageable pagination information
     * @return Page of entities with given status
     */
    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    // ========== CUSTOM QUERY METHODS ==========

    /**
     * Count Article created after specified date
     * @param date the cutoff date
     * @return count of entities created after date
     */
    @Query("SELECT COUNT(e) FROM Article e WHERE e.createdAt >= :date")
    long countCreatedAfter(@Param("date") LocalDateTime date);

    /**
     * Find Article paginated and sorted
     * @param pageable pagination and sorting information
     * @return Page of Article entities
     */
    @EntityGraph(attributePaths = {})

    @Query("SELECT e FROM Article e WHERE e.status = 'ACTIVE'")
    Page<Article> findAllActive(Pageable pageable);

    /**
     * Find recently created Article entities (last N days)
     * @param days number of days to look back
     * @return List of recently created entities
     */
    @Query("SELECT e FROM Article e WHERE e.createdAt >= :date ORDER BY e.createdAt DESC")
    List<Article> findRecentlyCreated(@Param("date") LocalDateTime date);

    /**
     * Check if Article exists by ID
     * @param id the entity ID
     * @return true if exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Article e WHERE e.id = :id")
    boolean existsById(@Param("id") Long id);

    /**
     * Find all Article with optional eager loading
     * @return List of all entities
     */
    @EntityGraph(attributePaths = {})
    @Query("SELECT DISTINCT e FROM Article e")
    List<Article> findAllWithEagerLoading();

    // ========== PUBLICATION METHODS ==========
    
    List<Article> findByStatut(String statut);
    
    Page<Article> findByStatut(String statut, Pageable pageable);
    
    @Query("SELECT a FROM Article a WHERE a.statut = 'PROGRAMME' AND a.datePublication <= :now")
    List<Article> findScheduledForPublication(@Param("now") LocalDateTime now);
    
    @Query("SELECT a FROM Article a WHERE a.statut = 'PUBLIE' AND a.visible = true ORDER BY a.datePublication DESC")
    List<Article> findPublishedArticles();
    
    @Query("SELECT a FROM Article a WHERE a.statut = 'EN_AVANT_PREMIERE' AND a.visible = true")
    List<Article> findPreviewArticles();
    
    @Query("SELECT a FROM Article a WHERE a.rubriqueId = :rubriqueId AND a.statut = 'PUBLIE' ORDER BY a.datePublication DESC")
    List<Article> findByRubriqueAndPublished(@Param("rubriqueId") Integer rubriqueId);

}
