package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import javax.validation.constraints.*;
import com.example.blog.enums.PostStatus;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PostStatus status = PostStatus.DRAFT;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
        name = "post_categories",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    // Business methods from class-diagram
    public void publish() {
        if (this.status != PostStatus.APPROVED) {
            throw new IllegalStateException("Cannot publish post in state: " + this.status);
        }
        this.status = PostStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    public void incrementViews() {
        this.viewCount++;
    }

    // State transition methods from state-diagram
    public void submit() {
        if (this.status != PostStatus.DRAFT) {
            throw new IllegalStateException("Cannot submit post in state: " + this.status);
        }
        this.status = PostStatus.PENDING_REVIEW;
    }

    public void approve() {
        if (this.status != PostStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot approve post in state: " + this.status);
        }
        this.status = PostStatus.APPROVED;
    }

    public void reject() {
        if (this.status != PostStatus.PENDING_REVIEW) {
            throw new IllegalStateException("Cannot reject post in state: " + this.status);
        }
        this.status = PostStatus.REJECTED;
    }

    public void revise() {
        if (this.status != PostStatus.REJECTED) {
            throw new IllegalStateException("Cannot revise post in state: " + this.status);
        }
        this.status = PostStatus.DRAFT;
    }

    public void archive() {
        if (this.status != PostStatus.PUBLISHED) {
            throw new IllegalStateException("Cannot archive post in state: " + this.status);
        }
        this.status = PostStatus.ARCHIVED;
    }

    public void restore() {
        if (this.status != PostStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot restore post in state: " + this.status);
        }
        this.status = PostStatus.PUBLISHED;
    }
}
