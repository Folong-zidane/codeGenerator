package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.SSOConfigStatus;

@Entity
@Table(name = "ssoconfigs")
public class SSOConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "scopes", columnDefinition = "JSON")
    private String scopes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SSOConfigStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return active;
    }

    public SSOConfigStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
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
    public String getScopes() {
        return scopes;
    }
    public void setScopes(String scopes) {
        this.scopes = scopes;
    }
    public void setStatus(SSOConfigStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Utilisateur authentifier(String token) {
        // TODO: Implement authentifier logic
        return null;
    }

    public void synchroniser() {
        // TODO: Implement synchroniser logic
    }

    public void suspend() {
        if (this.status != SSOConfigStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = SSOConfigStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != SSOConfigStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = SSOConfigStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
