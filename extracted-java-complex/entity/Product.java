package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.ProductStatus;

@Entity
@Table(name = "products")
public class Product {

    @NotBlank
    @Column
    private String name;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @Column
    private int quantity;

    @Column
    private String sku;

    @Column
    private ProductCategory category;

    @Column
    private List~Review~ reviews;

    @Column
    private decreaseStock(amount) void;

    @Column
    private increaseStock(amount) void;

    @Column
    private getAverageRating() Double;

    @Column
    private isInStock() boolean;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public List~Review~ getReviews() {
        return reviews;
    }

    public void setReviews(List~Review~ reviews) {
        this.reviews = reviews;
    }

    public decreaseStock(amount) getVoid() {
        return void;
    }

    public void setVoid(decreaseStock(amount) void) {
        this.void = void;
    }

    public increaseStock(amount) getVoid() {
        return void;
    }

    public void setVoid(increaseStock(amount) void) {
        this.void = void;
    }

    public getAverageRating() getDouble() {
        return Double;
    }

    public void setDouble(getAverageRating() Double) {
        this.Double = Double;
    }

    public isInStock() getBoolean() {
        return boolean;
    }

    public void setBoolean(isInStock() boolean) {
        this.boolean = boolean;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
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
        if (this.status != ProductStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = ProductStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != ProductStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = ProductStatus.ACTIVE;
    }

}
