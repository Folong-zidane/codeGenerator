package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CategoryStatus;

@Entity
@Table(name = "categorys")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private UUID id;

    @NotBlank
    @Column
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CategoryStatus status;

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

    public CategoryStatus getStatus() {
        return status;
    }

    public void setStatus(CategoryStatus status) {
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
        if (this.status != CategoryStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = CategoryStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != CategoryStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = CategoryStatus.ACTIVE;
    }

}
