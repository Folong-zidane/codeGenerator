package com.analysis.test.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.analysis.test.enums.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private UUID id;

    @Column
    private UUID userId;

    @Column
    private Float total;

    @Column
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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
