package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.PayPalPaymentStatus;

@Entity
@Table(name = "paypalpayments")
public class PayPalPayment {

    @NotBlank
    @Email
    @Column
    private String email;

    @Column
    private String transactionId;

    @Column
    private processPayment() boolean;

    @Column
    private refund() boolean;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PayPalPaymentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public processPayment() getBoolean() {
        return boolean;
    }

    public void setBoolean(processPayment() boolean) {
        this.boolean = boolean;
    }

    public refund() getBoolean() {
        return boolean;
    }

    public void setBoolean(refund() boolean) {
        this.boolean = boolean;
    }

    public PayPalPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PayPalPaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void suspend() {
        if (this.status != PayPalPaymentStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = PayPalPaymentStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != PayPalPaymentStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = PayPalPaymentStatus.ACTIVE;
    }

}
