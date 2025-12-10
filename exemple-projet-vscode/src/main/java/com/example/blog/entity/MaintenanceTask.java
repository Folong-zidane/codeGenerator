package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.MaintenanceTaskStatus;

@Entity
@Table(name = "maintenancetasks")
public class MaintenanceTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_planifiee")
    private LocalDateTime datePlanifiee;

    @Column(name = "heures_travaillees")
    private Float heuresTravaillees;

    @Column(name = "responsable_id")
    private Integer responsableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MaintenanceTaskStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getText() {
        return description;
    }

    public MaintenanceTaskStatus getStatus() {
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
    public LocalDateTime getDatePlanifiee() {
        return datePlanifiee;
    }
    public void setDatePlanifiee(LocalDateTime datePlanifiee) {
        this.datePlanifiee = datePlanifiee;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Float getHeuresTravaillees() {
        return heuresTravaillees;
    }
    public void setHeuresTravaillees(Float heuresTravaillees) {
        this.heuresTravaillees = heuresTravaillees;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getResponsableId() {
        return responsableId;
    }
    public void setResponsableId(Integer responsableId) {
        this.responsableId = responsableId;
    }
    public void setStatus(MaintenanceTaskStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void planifier() {
        // TODO: Implement planifier logic
    }

    public void executer() {
        // TODO: Implement executer logic
    }

    public void reporter() {
        // TODO: Implement reporter logic
    }

    public void suspend() {
        if (this.status != MaintenanceTaskStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = MaintenanceTaskStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != MaintenanceTaskStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = MaintenanceTaskStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
