package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.RecoveryPointStatus;

@Entity
@Table(name = "recoverypoints")
public class RecoveryPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "draft_session_id")
    private Integer draftSessionId;

    @Column(name = "snapshot_json", columnDefinition = "JSON")
    private String snapshotJson;

    @Column(name = "label")
    private String label;

    @Column(name = "auto_interval")
    private Boolean autoInterval;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RecoveryPointStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return autoInterval;
    }

    public RecoveryPointStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getAutoInterval() {
        return autoInterval;
    }
    public void setAutoInterval(Boolean autoInterval) {
        this.autoInterval = autoInterval;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Integer getDraftSessionId() {
        return draftSessionId;
    }
    public void setDraftSessionId(Integer draftSessionId) {
        this.draftSessionId = draftSessionId;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getSnapshotJson() {
        return snapshotJson;
    }
    public void setSnapshotJson(String snapshotJson) {
        this.snapshotJson = snapshotJson;
    }
    public void setStatus(RecoveryPointStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void creer() {
        // TODO: Implement creer logic
    }

    public void restaurer() {
        // TODO: Implement restaurer logic
    }

    public String comparer(String pointId) {
        // TODO: Implement comparer logic
        return null;
    }

    public void suspend() {
        if (this.status != RecoveryPointStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = RecoveryPointStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != RecoveryPointStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = RecoveryPointStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
