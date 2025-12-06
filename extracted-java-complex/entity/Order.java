package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

    @Column
    private String orderNumber;

    @Column
    private OrderStatus status;

    @Column
    private LocalDateTime orderDate;

    @Column
    private BigDecimal totalAmount;

    @Column
    private String shippingAddress;

    @Column
    private calculateTotal() BigDecimal;

    @Column
    private updateStatus(status) void;

    @Column
    private getOrderItems() List~OrderItem~;

    @Column
    private cancel() boolean;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public calculateTotal() getBigDecimal() {
        return BigDecimal;
    }

    public void setBigDecimal(calculateTotal() BigDecimal) {
        this.BigDecimal = BigDecimal;
    }

    public updateStatus(status) getVoid() {
        return void;
    }

    public void setVoid(updateStatus(status) void) {
        this.void = void;
    }

    public getOrderItems() getList~OrderItem~() {
        return List~OrderItem~;
    }

    public void setList~OrderItem~(getOrderItems() List~OrderItem~) {
        this.List~OrderItem~ = List~OrderItem~;
    }

    public cancel() getBoolean() {
        return boolean;
    }

    public void setBoolean(cancel() boolean) {
        this.boolean = boolean;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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

    public Float calculateTotal(java.util.List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate total: no products");
        }
        Float total = products.stream()
            .map(Product::getPrice)
            .reduce(0F, Float::sum);
        this.total = total;
        return total;
    }

    public void suspend() {
        if (this.status != OrderStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = OrderStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != OrderStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = OrderStatus.ACTIVE;
    }

}
