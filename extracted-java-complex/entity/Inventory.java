package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.InventoryStatus;

@Entity
@Table(name = "inventorys")
public class Inventory {

    @Column
    private Map~String, Product~ products;

    @Column
    private addProduct(product) void;

    @Column
    private removeProduct(productId) void;

    @Column
    private getProduct(productId) Product;

    @Column
    private updateStock(productId, quantity) void;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventoryStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Map~String, getProduct~ products() {
        return Product~ products;
    }

    public void setProduct~ products(Map~String, Product~ products) {
        this.Product~ products = Product~ products;
    }

    public addProduct(product) getVoid() {
        return void;
    }

    public void setVoid(addProduct(product) void) {
        this.void = void;
    }

    public removeProduct(productId) getVoid() {
        return void;
    }

    public void setVoid(removeProduct(productId) void) {
        this.void = void;
    }

    public getProduct(productId) getProduct() {
        return Product;
    }

    public void setProduct(getProduct(productId) Product) {
        this.Product = Product;
    }

    public updateStock(productId, getQuantity) void() {
        return quantity) void;
    }

    public void setQuantity) void(updateStock(productId, quantity) void) {
        this.quantity) void = quantity) void;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
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
        if (this.status != InventoryStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = InventoryStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != InventoryStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = InventoryStatus.ACTIVE;
    }

}
