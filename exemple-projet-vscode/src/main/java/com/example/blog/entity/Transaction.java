package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.TransactionStatus;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "montant")
    private Integer montant;

    @Column(name = "devise")
    private String devise;

    @Column(name = "date_transaction")
    private LocalDateTime dateTransaction;

    @Column(name = "callback_data", columnDefinition = "JSON")
    private String callbackData;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getCallbackData() {
        return callbackData;
    }
    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }
    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }
    public String getDevise() {
        return devise;
    }
    public void setDevise(String devise) {
        this.devise = devise;
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
    public void setStatus(TransactionStatus status) {
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

    public void verifierStatut() {
        // TODO: Implement verifierStatut logic
    }

    public void rembourser() {
        // TODO: Implement rembourser logic
    }

    public java.io.File genererRecu() {
        // TODO: Implement genererRecu logic
        return null;
    }

    public void suspend() {
        if (this.status != TransactionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = TransactionStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != TransactionStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = TransactionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
