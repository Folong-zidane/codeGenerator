package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.AbonnementPayantStatus;

@Entity
@Table(name = "abonnementpayants")
public class AbonnementPayant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "prix")
    private Integer prix;

    @Column(name = "auto_renouvellement")
    private Boolean autoRenouvellement;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AbonnementPayantStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return autoRenouvellement;
    }

    public AbonnementPayantStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getAutoRenouvellement() {
        return autoRenouvellement;
    }
    public void setAutoRenouvellement(Boolean autoRenouvellement) {
        this.autoRenouvellement = autoRenouvellement;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPrix() {
        return prix;
    }
    public void setPrix(Integer prix) {
        this.prix = prix;
    }
    public void setStatus(AbonnementPayantStatus status) {
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

    public void renouveler() {
        // TODO: Implement renouveler logic
    }

    public void annuler() {
        // TODO: Implement annuler logic
    }

    public void suspendre() {
        // TODO: Implement suspendre logic
    }

    public void suspend() {
        if (this.status != AbonnementPayantStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = AbonnementPayantStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != AbonnementPayantStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = AbonnementPayantStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
