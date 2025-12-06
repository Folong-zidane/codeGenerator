package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.CreditCardPaymentStatus;

@Entity
@Table(name = "creditcardpayments")
public class CreditCardPayment {

    @Column
    private String cardNumber;

    @Column
    private String expiryDate;

    @Column
    private String cvv;

    @Column
    private processPayment() boolean;

    @Column
    private refund() boolean;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CreditCardPaymentStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

    public CreditCardPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(CreditCardPaymentStatus status) {
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
        if (this.status != CreditCardPaymentStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = CreditCardPaymentStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != CreditCardPaymentStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = CreditCardPaymentStatus.ACTIVE;
    }

}
