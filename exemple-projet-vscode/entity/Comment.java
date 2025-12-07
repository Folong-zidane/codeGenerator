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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "approved")
    private Boolean approved = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatus status = CommentStatus.DRAFT;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Business method from class-diagram
    public void approve() {
        this.approved = true;
        this.status = CommentStatus.APPROVED;
    }

    // State transition methods from state-diagram
    public void submit() {
        if (this.status != CommentStatus.DRAFT) {
            throw new IllegalStateException("Cannot submit comment in state: " + this.status);
        }
        this.status = CommentStatus.PENDING_REVIEW;
    }

    public void reject() {
        if (this.status != CommentStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot reject comment in state: " + this.status);
        }
        this.status = CommentStatus.REJECTED;
        this.approved = false;
    }

    public void revise() {
        if (this.status != CommentStatus.REJECTED) {
            throw new IllegalStateException("Cannot revise comment in state: " + this.status);
        }
        this.status = CommentStatus.DRAFT;
    }

    public void publish() {
        if (this.status != CommentStatus.APPROVED) {
            throw new IllegalStateException("Cannot publish comment in state: " + this.status);
        }
        this.status = CommentStatus.PUBLISHED;
    }

    public void archive() {
        if (this.status != CommentStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot archive comment in state: " + this.status);
        }
        this.status = CommentStatus.ARCHIVED;
    }

    public void restore() {
        if (this.status != CommentStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot restore comment in state: " + this.status);
        }
        this.status = CommentStatus.PUBLISHED;
    }
}
