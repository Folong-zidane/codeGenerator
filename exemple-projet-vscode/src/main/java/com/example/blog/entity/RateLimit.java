package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.RateLimitStatus;

@Entity
@Table(name = "ratelimits")
public class RateLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "requests_count")
    private Integer requestsCount;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RateLimitStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RateLimitStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public LocalDateTime getBlockedUntil() {
        return blockedUntil;
    }
    public void setBlockedUntil(LocalDateTime blockedUntil) {
        this.blockedUntil = blockedUntil;
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
    public String getIpAddress() {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public Integer getRequestsCount() {
        return requestsCount;
    }
    public void setRequestsCount(Integer requestsCount) {
        this.requestsCount = requestsCount;
    }
    public void setStatus(RateLimitStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean verifierLimite() {
        // TODO: Implement verifierLimite logic
        return null;
    }

    public void bloquer() {
        // TODO: Implement bloquer logic
    }

    public void debloquer() {
        // TODO: Implement debloquer logic
    }

    public void suspend() {
        if (this.status != RateLimitStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = RateLimitStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != RateLimitStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = RateLimitStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
