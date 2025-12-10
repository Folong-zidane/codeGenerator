package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PaiementStatus;

@Entity
@Table(name = "paiements")
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "panier_id")
    private Integer panierId;

    @Column(name = "montant")
    private Integer montant;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaiementStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public PaiementStatus getStatus() {
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
    public LocalDateTime getDatePaiement() {
        return datePaiement;
    }
    public void setDatePaiement(LocalDateTime datePaiement) {
        this.datePaiement = datePaiement;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMontant() {
        return montant;
    }
    public void setMontant(Integer montant) {
        this.montant = montant;
    }
    public Integer getPanierId() {
        return panierId;
    }
    public void setPanierId(Integer panierId) {
        this.panierId = panierId;
    }
    public void setStatus(PaiementStatus status) {
        this.status = status;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String initierPaiement() {
        // TODO: Implement initierPaiement logic
        return "";
    }

    public void verifierStatut() {
        // TODO: Implement verifierStatut logic
    }

    public void rembourser() {
        // TODO: Implement rembourser logic
    }

    public void suspend() {
        if (this.status != PaiementStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = PaiementStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != PaiementStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = PaiementStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
