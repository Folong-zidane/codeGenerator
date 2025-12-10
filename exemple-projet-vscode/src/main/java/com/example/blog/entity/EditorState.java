package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.EditorStateStatus;

@Entity
@Table(name = "editorstates")
public class EditorState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "draft_session_id")
    private Integer draftSessionId;

    @Column(name = "delta_operations", columnDefinition = "JSON")
    private String deltaOperations;

    @Column(name = "version")
    private Integer version;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EditorStateStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public EditorStateStatus getStatus() {
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
    public String getDeltaOperations() {
        return deltaOperations;
    }
    public void setDeltaOperations(String deltaOperations) {
        this.deltaOperations = deltaOperations;
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
    public void setStatus(EditorStateStatus status) {
        this.status = status;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    public void appliquerDelta() {
        // TODO: Implement appliquerDelta logic
    }

    public void annuler() {
        // TODO: Implement annuler logic
    }

    public void refaire() {
        // TODO: Implement refaire logic
    }

    public void suspend() {
        if (this.status != EditorStateStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = EditorStateStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != EditorStateStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = EditorStateStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
