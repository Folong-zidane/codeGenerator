package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.MediaVariantStatus;

@Entity
@Table(name = "mediavariants")
public class MediaVariant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "media_file_id")
    private UUID mediaFileId;

    @Column(name = "width")
    private Integer width;

    @Column(name = "url_s3")
    private String urlS3;

    @Column(name = "size_kb")
    private Float sizeKb;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MediaVariantStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MediaVariantStatus getStatus() {
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
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getMediaFileId() {
        return mediaFileId;
    }
    public void setMediaFileId(UUID mediaFileId) {
        this.mediaFileId = mediaFileId;
    }
    public Float getSizeKb() {
        return sizeKb;
    }
    public void setSizeKb(Float sizeKb) {
        this.sizeKb = sizeKb;
    }
    public void setStatus(MediaVariantStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUrlS3() {
        return urlS3;
    }
    public void setUrlS3(String urlS3) {
        this.urlS3 = urlS3;
    }
    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) {
        this.width = width;
    }

    public void genererVariant() {
        // TODO: Implement genererVariant logic
    }

    public void optimiserPourMobile() {
        // TODO: Implement optimiserPourMobile logic
    }

    public void suspend() {
        if (this.status != MediaVariantStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = MediaVariantStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != MediaVariantStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = MediaVariantStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
