package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.OrderItemStatus;

@Entity
@Table(name = "orderitems")
public class OrderItem {

    @Column
    private int quantity;

    @Column
    private BigDecimal unitPrice;

    @Column
    private BigDecimal lineTotal;

    @Column
    private calculateLineTotal() BigDecimal;

    @Column
    private getProduct() Product;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderItemStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    public calculateLineTotal() getBigDecimal() {
        return BigDecimal;
    }

    public void setBigDecimal(calculateLineTotal() BigDecimal) {
        this.BigDecimal = BigDecimal;
    }

    public getProduct() getProduct() {
        return Product;
    }

    public void setProduct(getProduct() Product) {
        this.Product = Product;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
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
        if (this.status != OrderItemStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = OrderItemStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != OrderItemStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = OrderItemStatus.ACTIVE;
    }

}
