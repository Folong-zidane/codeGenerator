package com.ecommerce.complex.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.ecommerce.complex.enums.UserStatus;

@Entity
@Table(name = "users")
public class User {

    @NotBlank
    @Column
    private String username;

    @NotBlank
    @Email
    @Column
    private String email;

    @Column
    private String passwordHash;

    @Column
    private UserRole role;

    @Column
    private LocalDateTime lastLogin;

    @Column
    private boolean active;

    @Column
    private authenticate(password) boolean;

    @Column
    private updateProfile(profile) void;

    @Column
    private getOrders() List~Order~;

    @Column
    private isActive() boolean;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public authenticate(password) getBoolean() {
        return boolean;
    }

    public void setBoolean(authenticate(password) boolean) {
        this.boolean = boolean;
    }

    public updateProfile(profile) getVoid() {
        return void;
    }

    public void setVoid(updateProfile(profile) void) {
        this.void = void;
    }

    public getOrders() getList~Order~() {
        return List~Order~;
    }

    public void setList~Order~(getOrders() List~Order~) {
        this.List~Order~ = List~Order~;
    }

    public isActive() getBoolean() {
        return boolean;
    }

    public void setBoolean(isActive() boolean) {
        this.boolean = boolean;
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
