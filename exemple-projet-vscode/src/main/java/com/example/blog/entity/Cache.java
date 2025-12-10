package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CacheStatus;

@Entity
@Table(name = "caches")
public class Cache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cle")
    private String cle;

    @Column(name = "data", columnDefinition = "JSON")
    private String data;

    @Column(name = "expiration")
    private LocalDateTime expiration;

    @Column(name = "hits")
    private Integer hits;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CacheStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CacheStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String get(String cle) {
        // TODO: Implement get logic
        return null;
    }

    public void set(String cle, String data, String ttl) {
        // TODO: Implement set logic
    }
    public String getCle() {
        return cle;
    }
    public void setCle(String cle) {
        this.cle = cle;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public LocalDateTime getExpiration() {
        return expiration;
    }
    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
    public Integer getHits() {
        return hits;
    }
    public void setHits(Integer hits) {
        this.hits = hits;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(CacheStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void invalider(String cle) {
        // TODO: Implement invalider logic
    }

    public void invaliderParTag(String tag) {
        // TODO: Implement invaliderParTag logic
    }

    public void nettoyer() {
        // TODO: Implement nettoyer logic
    }

    public void suspend() {
        if (this.status != CacheStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = CacheStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != CacheStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = CacheStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
