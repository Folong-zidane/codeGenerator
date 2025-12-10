package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CDNConfigStatus;

@Entity
@Table(name = "cdnconfigs")
public class CDNConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cache_rules", columnDefinition = "JSON")
    private String cacheRules;

    @Column(name = "edge_location")
    private String edgeLocation;

    @Column(name = "ttl_secondes")
    private Integer ttlSecondes;

    @Column(name = "purge_on_update")
    private Boolean purgeOnUpdate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CDNConfigStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return purgeOnUpdate;
    }

    public CDNConfigStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getCacheRules() {
        return cacheRules;
    }
    public void setCacheRules(String cacheRules) {
        this.cacheRules = cacheRules;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getEdgeLocation() {
        return edgeLocation;
    }
    public void setEdgeLocation(String edgeLocation) {
        this.edgeLocation = edgeLocation;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getPurgeOnUpdate() {
        return purgeOnUpdate;
    }
    public void setPurgeOnUpdate(Boolean purgeOnUpdate) {
        this.purgeOnUpdate = purgeOnUpdate;
    }
    public void setStatus(CDNConfigStatus status) {
        this.status = status;
    }
    public Integer getTtlSecondes() {
        return ttlSecondes;
    }
    public void setTtlSecondes(Integer ttlSecondes) {
        this.ttlSecondes = ttlSecondes;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void purgerCache() {
        // TODO: Implement purgerCache logic
    }

    public void configurerRegles() {
        // TODO: Implement configurerRegles logic
    }

    public void suspend() {
        if (this.status != CDNConfigStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = CDNConfigStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != CDNConfigStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = CDNConfigStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
