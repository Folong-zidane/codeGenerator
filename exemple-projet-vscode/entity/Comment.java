package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.CommentStatus;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private UUID id;

    @Column
    private String content;

    @Column
    private UUID postId;

    @Column
    private UUID userId;

    @Column
    private Date createdAt;

    @Column
    private Boolean approved;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentStatus status;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
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
        if (this.status != CommentStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = CommentStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != CommentStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = CommentStatus.ACTIVE;
    }

}
