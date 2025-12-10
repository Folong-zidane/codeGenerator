package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.TelechargementMediaStatus;

@Entity
@Table(name = "telechargementmedias")
public class TelechargementMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "media_id")
    private Integer mediaId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "date_telechargement")
    private LocalDateTime dateTelechargement;

    @Column(name = "success")
    private Boolean success;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TelechargementMediaStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return success;
    }

    public TelechargementMediaStatus getStatus() {
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
    public LocalDateTime getDateTelechargement() {
        return dateTelechargement;
    }
    public void setDateTelechargement(LocalDateTime dateTelechargement) {
        this.dateTelechargement = dateTelechargement;
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
    public Integer getMediaId() {
        return mediaId;
    }
    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }
    public void setStatus(TelechargementMediaStatus status) {
        this.status = status;
    }
    public Boolean getSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void enregistrerTelechargement() {
        // TODO: Implement enregistrerTelechargement logic
    }

    public void suspend() {
        if (this.status != TelechargementMediaStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = TelechargementMediaStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != TelechargementMediaStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = TelechargementMediaStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
