package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PostStatus;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private UUID id;

    @NotBlank
    @Column
    private String title;

    @Column
    private String content;

    @Column
    private UUID authorId;

    @Column
    private PostStatus status;

    @Column
    private Date publishedAt;

    @Column
    private Integer viewCount;

    @Column
    private List<"*"> "*"s;

    @Column
    private List<"*"> "*"s;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PostStatus status;

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

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
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

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
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
        if (this.status != PostStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = PostStatus.SUSPENDED;
    }

    public void activate() {
        if (this.status != PostStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = PostStatus.ACTIVE;
    }

}
