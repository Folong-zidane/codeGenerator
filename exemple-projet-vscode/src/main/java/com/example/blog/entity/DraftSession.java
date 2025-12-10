package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.DraftSessionStatus;

@Entity
@Table(name = "draftsessions")
public class DraftSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "data_json", columnDefinition = "JSON")
    private String dataJson;

    @Column(name = "last_save")
    private LocalDateTime lastSave;

    @Column(name = "synced")
    private Boolean synced;

    @Column(name = "version")
    private Integer version;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DraftSessionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return synced;
    }

    public DraftSessionStatus getStatus() {
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
    public String getDataJson() {
        return dataJson;
    }
    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public LocalDateTime getLastSave() {
        return lastSave;
    }
    public void setLastSave(LocalDateTime lastSave) {
        this.lastSave = lastSave;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public void setStatus(DraftSessionStatus status) {
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
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    public void sauvegarderDelta() {
        // TODO: Implement sauvegarderDelta logic
    }

    public void restaurer() {
        // TODO: Implement restaurer logic
    }

    public void synchroniser() {
        // TODO: Implement synchroniser logic
    }

    public void fusionnerConflits() {
        // TODO: Implement fusionnerConflits logic
    }

    public void suspend() {
        if (this.status != DraftSessionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = DraftSessionStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != DraftSessionStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = DraftSessionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
