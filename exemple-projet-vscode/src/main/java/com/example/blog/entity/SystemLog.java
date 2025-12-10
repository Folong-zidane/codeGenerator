package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.SystemLogStatus;

@Entity
@Table(name = "systemlogs")
public class SystemLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "entite_id")
    private Integer entiteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SystemLogStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getText() {
        return message;
    }

    public SystemLogStatus getStatus() {
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
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setStatus(SystemLogStatus status) {
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

    public void enregistrer() {
        // TODO: Implement enregistrer logic
    }

    public void nettoyer(String jours) {
        // TODO: Implement nettoyer logic
    }

    public java.util.List<Pattern> analyser() {
        // TODO: Implement analyser logic
        return null;
    }

    public void suspend() {
        if (this.status != SystemLogStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = SystemLogStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != SystemLogStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = SystemLogStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
