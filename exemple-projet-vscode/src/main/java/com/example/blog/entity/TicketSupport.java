package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.TicketSupportStatus;

@Entity
@Table(name = "ticketsupports")
public class TicketSupport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "resolution_time")
    private Integer resolutionTime;

    @Column(name = "sla_deadline")
    private LocalDateTime slaDeadline;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketSupportStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getText() {
        return description;
    }

    public TicketSupportStatus getStatus() {
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getResolutionTime() {
        return resolutionTime;
    }
    public void setResolutionTime(Integer resolutionTime) {
        this.resolutionTime = resolutionTime;
    }
    public LocalDateTime getSlaDeadline() {
        return slaDeadline;
    }
    public void setSlaDeadline(LocalDateTime slaDeadline) {
        this.slaDeadline = slaDeadline;
    }
    public void setStatus(TicketSupportStatus status) {
        this.status = status;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
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

    public void assigner(String userId) {
        // TODO: Implement assigner logic
    }

    public void resoudre() {
        // TODO: Implement resoudre logic
    }

    public void escalader() {
        // TODO: Implement escalader logic
    }

    public void suspend() {
        if (this.status != TicketSupportStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = TicketSupportStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != TicketSupportStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = TicketSupportStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
