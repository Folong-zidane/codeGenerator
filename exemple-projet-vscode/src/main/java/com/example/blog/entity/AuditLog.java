package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.AuditLogStatus;

@Entity
@Table(name = "auditlogs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "entite_type")
    private String entiteType;

    @Column(name = "entite_id")
    private Integer entiteId;

    @Column(name = "valeurs_avant", columnDefinition = "JSON")
    private String valeursAvant;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuditLogStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AuditLogStatus getStatus() {
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
    public Integer getEntiteId() {
        return entiteId;
    }
    public void setEntiteId(Integer entiteId) {
        this.entiteId = entiteId;
    }
    public String getEntiteType() {
        return entiteType;
    }
    public void setEntiteType(String entiteType) {
        this.entiteType = entiteType;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(AuditLogStatus status) {
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
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getValeursAvant() {
        return valeursAvant;
    }
    public void setValeursAvant(String valeursAvant) {
        this.valeursAvant = valeursAvant;
    }

    public void enregistrer() {
        // TODO: Implement enregistrer logic
    }

    public java.io.File exporter() {
        // TODO: Implement exporter logic
        return null;
    }

    public void anonymiser() {
        // TODO: Implement anonymiser logic
    }

    public void suspend() {
        if (this.status != AuditLogStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = AuditLogStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != AuditLogStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = AuditLogStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
