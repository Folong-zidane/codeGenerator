package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import javax.validation.constraints.*;
import com.example.blog.enums.UserStatus;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    // Business methods from class-diagram
    public boolean validateEmail() {
        if (this.email == null || this.email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return this.email.matches(emailRegex);
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        this.password = newPassword; // TODO: Hash with BCrypt
        this.updatedAt = LocalDateTime.now();
    }

    // State transition methods from state-diagram
    public void submit() {
        if (this.status != UserStatus.DRAFT) {
            throw new IllegalStateException("Cannot submit user in state: " + this.status);
        }
        this.status = UserStatus.PENDING_REVIEW;
    }

    public void approve() {
        if (this.status != UserStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot approve user in state: " + this.status);
        }
        this.status = UserStatus.APPROVED;
    }

    public void reject() {
        if (this.status != UserStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot reject user in state: " + this.status);
        }
        this.status = UserStatus.REJECTED;
    }

    public void revise() {
        if (this.status != UserStatus.REJECTED) {
            throw new IllegalStateException("Cannot revise user in state: " + this.status);
        }
        this.status = UserStatus.DRAFT;
    }

    public void publish() {
        if (this.status != UserStatus.APPROVED) {
            throw new IllegalStateException("Cannot publish user in state: " + this.status);
        }
        this.status = UserStatus.PUBLISHED;
    }

    public void archive() {
        if (this.status != UserStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot archive user in state: " + this.status);
        }
        this.status = UserStatus.ARCHIVED;
    }

    public void restore() {
        if (this.status != UserStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot restore user in state: " + this.status);
        }
        this.status = UserStatus.PUBLISHED;
    }
}
