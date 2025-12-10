package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.DocumentPreviewStatus;

@Entity
@Table(name = "documentpreviews")
public class DocumentPreview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "media_file_id")
    private Integer mediaFileId;

    @Column(name = "page_number")
    private Integer pageNumber;

    @Column(name = "preview_url")
    private String previewUrl;

    @Column(name = "ocr_text", columnDefinition = "TEXT")
    private String ocrText;

    @Column(name = "searchable")
    private Boolean searchable;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentPreviewStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    public Boolean getBoolean() {
        return searchable;
    }

    public DocumentPreviewStatus getStatus() {
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
    public Integer getMediaFileId() {
        return mediaFileId;
    }
    public void setMediaFileId(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }
    public String getOcrText() {
        return ocrText;
    }
    public void setOcrText(String ocrText) {
        this.ocrText = ocrText;
    }
    public Integer getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public String getPreviewUrl() {
        return previewUrl;
    }
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
    public Boolean getSearchable() {
        return searchable;
    }
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }
    public void setStatus(DocumentPreviewStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void extraireTexte() {
        // TODO: Implement extraireTexte logic
    }

    public void genererPreview() {
        // TODO: Implement genererPreview logic
    }

    public void suspend() {
        if (this.status != DocumentPreviewStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = DocumentPreviewStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != DocumentPreviewStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = DocumentPreviewStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
