package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.PaymentStatus;

@Entity
@Table(name = "payments")
public class Payment {

    @Column
    private processPayment() boolean;

    @Column
    private refund() boolean;

    @Column
    private getStatus() PaymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public getStatus() getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(getStatus() PaymentStatus) {
        this.PaymentStatus = PaymentStatus;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
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
        if (this.status != PaymentStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = PaymentStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != PaymentStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = PaymentStatus.ACTIVE;
    }

}
