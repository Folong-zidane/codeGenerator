package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.MediaUsageStatus;

@Entity
@Table(name = "mediausages")
public class MediaUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "media_file_id")
    private Integer mediaFileId;

    @Column(name = "position")
    private Integer position;

    @Column(name = "date_usage")
    private LocalDateTime dateUsage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MediaUsageStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MediaUsageStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateUsage() {
        return dateUsage;
    }
    public void setDateUsage(LocalDateTime dateUsage) {
        this.dateUsage = dateUsage;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getMediaFileId() {
        return mediaFileId;
    }
    public void setMediaFileId(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }
    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }
    public void setStatus(MediaUsageStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void enregistrerUsage() {
        // TODO: Implement enregistrerUsage logic
    }

    public Integer compterUtilisations() {
        // TODO: Implement compterUtilisations logic
        return 0;
    }

    public void suspend() {
        if (this.status != MediaUsageStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = MediaUsageStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != MediaUsageStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = MediaUsageStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
