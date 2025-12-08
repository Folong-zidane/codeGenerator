package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.UserStatus;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private UUID id;

    @NotBlank
    @Column
    private String username;

    @NotBlank
    @Email
    @Column
    private String email;

    @Column
    private String password;

    @Column
    private UserStatus status;

    @Column
    private Date createdAt;

    @Column
    private List<"*"> "*"s;

    @Column
    private List<"*"> "*"s;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<"*"> get"*"s() {
        return "*"s;
    }

    public void set"*"s(List<"*"> "*"s) {
        this."*"s = "*"s;
    }

    public List<"*"> get"*"s() {
        return "*"s;
    }

    public void set"*"s(List<"*"> "*"s) {
        this."*"s = "*"s;
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
        // TODO: Hash password with BCrypt
        this.updatedAt = java.time.LocalDateTime.now();
    }

    public void suspend() {
        if (this.status != UserStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = UserStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != UserStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = UserStatus.ACTIVE;
    }

}
