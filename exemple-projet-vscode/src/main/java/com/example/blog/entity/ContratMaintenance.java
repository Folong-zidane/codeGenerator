package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.ContratMaintenanceStatus;

@Entity
@Table(name = "contratmaintenances")
public class ContratMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "heures_mensuelles")
    private Integer heuresMensuelles;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ContratMaintenanceStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ContratMaintenanceStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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
    public Integer getHeuresMensuelles() {
        return heuresMensuelles;
    }
    public void setHeuresMensuelles(Integer heuresMensuelles) {
        this.heuresMensuelles = heuresMensuelles;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(ContratMaintenanceStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void consommerHeures(String heures) {
        // TODO: Implement consommerHeures logic
    }

    public void renouveler() {
        // TODO: Implement renouveler logic
    }

    public java.io.File genererRapport() {
        // TODO: Implement genererRapport logic
        return null;
    }

    public void suspend() {
        if (this.status != ContratMaintenanceStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = ContratMaintenanceStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != ContratMaintenanceStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = ContratMaintenanceStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
