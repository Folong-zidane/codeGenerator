package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.SitemapStatus;

@Entity
@Table(name = "sitemaps")
public class Sitemap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_generated")
    private LocalDateTime lastGenerated;

    @Column(name = "url")
    private String url;

    @Column(name = "priority")
    private Float priority;

    @Column(name = "total_urls")
    private Integer totalUrls;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SitemapStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SitemapStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getLastGenerated() {
        return lastGenerated;
    }
    public void setLastGenerated(LocalDateTime lastGenerated) {
        this.lastGenerated = lastGenerated;
    }
    public Float getPriority() {
        return priority;
    }
    public void setPriority(Float priority) {
        this.priority = priority;
    }
    public void setStatus(SitemapStatus status) {
        this.status = status;
    }
    public Integer getTotalUrls() {
        return totalUrls;
    }
    public void setTotalUrls(Integer totalUrls) {
        this.totalUrls = totalUrls;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public void genererSitemap() {
        // TODO: Implement genererSitemap logic
    }

    public void soumettreGoogleSearchConsole() {
        // TODO: Implement soumettreGoogleSearchConsole logic
    }

    public void suspend() {
        if (this.status != SitemapStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = SitemapStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != SitemapStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = SitemapStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
