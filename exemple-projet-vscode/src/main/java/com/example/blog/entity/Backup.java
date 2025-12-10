package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.BackupStatus;

@Entity
@Table(name = "backups")
public class Backup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fichier_url")
    private String fichierUrl;

    @Column(name = "taille")
    private Integer taille;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "succes")
    private Boolean succes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BackupStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return succes;
    }

    public BackupStatus getStatus() {
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
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }
    public String getFichierUrl() {
        return fichierUrl;
    }
    public void setFichierUrl(String fichierUrl) {
        this.fichierUrl = fichierUrl;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(BackupStatus status) {
        this.status = status;
    }
    public Boolean getSucces() {
        return succes;
    }
    public void setSucces(Boolean succes) {
        this.succes = succes;
    }
    public Integer getTaille() {
        return taille;
    }
    public void setTaille(Integer taille) {
        this.taille = taille;
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

    public Boolean verifierIntegrite() {
        // TODO: Implement verifierIntegrite logic
        return null;
    }

    public void suspend() {
        if (this.status != BackupStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = BackupStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != BackupStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = BackupStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
