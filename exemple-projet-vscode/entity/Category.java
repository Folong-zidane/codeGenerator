package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import javax.validation.constraints.*;
import com.example.blog.enums.CategoryStatus;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CategoryStatus status = CategoryStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "categories")
    private List<Post> posts;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    // State transition methods from state-diagram
    public void submit() {
        if (this.status != CategoryStatus.DRAFT) {
            throw new IllegalStateException("Cannot submit category in state: " + this.status);
        }
        this.status = CategoryStatus.PENDING_REVIEW;
    }

    public void approve() {
        if (this.status != CategoryStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot approve category in state: " + this.status);
        }
        this.status = CategoryStatus.APPROVED;
    }

    public void reject() {
        if (this.status != CategoryStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot reject category in state: " + this.status);
        }
        this.status = CategoryStatus.REJECTED;
    }

    public void revise() {
        if (this.status != CategoryStatus.REJECTED) {
            throw new IllegalStateException("Cannot revise category in state: " + this.status);
        }
        this.status = CategoryStatus.DRAFT;
    }

    public void publish() {
        if (this.status != CategoryStatus.APPROVED) {
            throw new IllegalStateException("Cannot publish category in state: " + this.status);
        }
        this.status = CategoryStatus.PUBLISHED;
    }

    public void archive() {
        if (this.status != CategoryStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot archive category in state: " + this.status);
        }
        this.status = CategoryStatus.ARCHIVED;
    }

    public void restore() {
        if (this.status != CategoryStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot restore category in state: " + this.status);
        }
        this.status = CategoryStatus.PUBLISHED;
    }
}
