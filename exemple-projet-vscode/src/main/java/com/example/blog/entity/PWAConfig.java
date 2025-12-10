package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PWAConfigStatus;

@Entity
@Table(name = "pwaconfigs")
public class PWAConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "manifest_json", columnDefinition = "JSON")
    private String manifestJson;

    @Column(name = "service_worker_version")
    private String serviceWorkerVersion;

    @Column(name = "push_notifications")
    private Boolean pushNotifications;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PWAConfigStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return pushNotifications;
    }

    public PWAConfigStatus getStatus() {
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getManifestJson() {
        return manifestJson;
    }
    public void setManifestJson(String manifestJson) {
        this.manifestJson = manifestJson;
    }
    public Boolean getPushNotifications() {
        return pushNotifications;
    }
    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }
    public String getServiceWorkerVersion() {
        return serviceWorkerVersion;
    }
    public void setServiceWorkerVersion(String serviceWorkerVersion) {
        this.serviceWorkerVersion = serviceWorkerVersion;
    }
    public void setStatus(PWAConfigStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String genererManifest() {
        // TODO: Implement genererManifest logic
        return null;
    }

    public void mettreAJourServiceWorker() {
        // TODO: Implement mettreAJourServiceWorker logic
    }

    public void suspend() {
        if (this.status != PWAConfigStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = PWAConfigStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != PWAConfigStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = PWAConfigStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
