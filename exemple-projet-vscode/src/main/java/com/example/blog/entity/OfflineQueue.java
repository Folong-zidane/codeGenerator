package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.OfflineQueueStatus;

@Entity
@Table(name = "offlinequeues")
public class OfflineQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "payload", columnDefinition = "JSON")
    private String payload;

    @Column(name = "queued_at")
    private LocalDateTime queuedAt;

    @Column(name = "synced")
    private Boolean synced;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OfflineQueueStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return synced;
    }

    public OfflineQueueStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getActionType() {
        return actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
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
    public String getPayload() {
        return payload;
    }
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public LocalDateTime getQueuedAt() {
        return queuedAt;
    }
    public void setQueuedAt(LocalDateTime queuedAt) {
        this.queuedAt = queuedAt;
    }
    public Integer getRetryCount() {
        return retryCount;
    }
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
    public void setStatus(OfflineQueueStatus status) {
        this.status = status;
    }
    public Boolean getSynced() {
        return synced;
    }
    public void setSynced(Boolean synced) {
        this.synced = synced;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void ajouter() {
        // TODO: Implement ajouter logic
    }

    public void synchroniser() {
        // TODO: Implement synchroniser logic
    }

    public void retry() {
        // TODO: Implement retry logic
    }

    public void suspend() {
        if (this.status != OfflineQueueStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = OfflineQueueStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != OfflineQueueStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = OfflineQueueStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
