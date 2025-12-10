package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.NotificationPreferenceStatus;

@Entity
@Table(name = "notificationpreferences")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email_actif")
    private Boolean emailActif;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationPreferenceStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return emailActif;
    }

    public NotificationPreferenceStatus getStatus() {
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
    public Boolean getEmailActif() {
        return emailActif;
    }
    public void setEmailActif(Boolean emailActif) {
        this.emailActif = emailActif;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(NotificationPreferenceStatus status) {
        this.status = status;
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

    public void mettreAJour() {
        // TODO: Implement mettreAJour logic
    }

    public void desactiverTout() {
        // TODO: Implement desactiverTout logic
    }

    public void suspend() {
        if (this.status != NotificationPreferenceStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = NotificationPreferenceStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != NotificationPreferenceStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = NotificationPreferenceStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
