package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.MediaLicenseStatus;

@Entity
@Table(name = "medialicenses")
public class MediaLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "media_file_id")
    private Integer mediaFileId;

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "usage_restrictions", columnDefinition = "TEXT")
    private String usageRestrictions;

    @Column(name = "date_expiration")
    private LocalDateTime dateExpiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MediaLicenseStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MediaLicenseStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getAuteur() {
        return auteur;
    }
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }
    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMediaFileId() {
        return mediaFileId;
    }
    public void setMediaFileId(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }
    public void setStatus(MediaLicenseStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUsageRestrictions() {
        return usageRestrictions;
    }
    public void setUsageRestrictions(String usageRestrictions) {
        this.usageRestrictions = usageRestrictions;
    }

    public Boolean verifierDroits() {
        // TODO: Implement verifierDroits logic
        return null;
    }

    public void suspend() {
        if (this.status != MediaLicenseStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = MediaLicenseStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != MediaLicenseStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = MediaLicenseStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
