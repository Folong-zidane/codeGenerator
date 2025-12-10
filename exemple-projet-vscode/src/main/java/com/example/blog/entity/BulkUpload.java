package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.BulkUploadStatus;

@Entity
@Table(name = "bulkuploads")
public class BulkUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_files")
    private Integer totalFiles;

    @Column(name = "zip_url")
    private String zipUrl;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BulkUploadStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public BulkUploadStatus getStatus() {
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
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setStatus(BulkUploadStatus status) {
        this.status = status;
    }
    public Integer getTotalFiles() {
        return totalFiles;
    }
    public void setTotalFiles(Integer totalFiles) {
        this.totalFiles = totalFiles;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getZipUrl() {
        return zipUrl;
    }
    public void setZipUrl(String zipUrl) {
        this.zipUrl = zipUrl;
    }

    public void processZip() {
        // TODO: Implement processZip logic
    }

    public java.io.File genererRapport() {
        // TODO: Implement genererRapport logic
        return null;
    }

    public void suspend() {
        if (this.status != BulkUploadStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = BulkUploadStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != BulkUploadStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = BulkUploadStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
