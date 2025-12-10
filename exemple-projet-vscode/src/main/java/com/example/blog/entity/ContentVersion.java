package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.ContentVersionStatus;

@Entity
@Table(name = "contentversions")
public class ContentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "version_num")
    private Integer versionNum;

    @Column(name = "changes_diff", columnDefinition = "JSON")
    private String changesDiff;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "restore_point")
    private Boolean restorePoint;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "taille_diff_kb")
    private Float tailleDiffKb;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ContentVersionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return restorePoint;
    }

    public ContentVersionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Integer getArticleId() {
        return articleId;
    }
    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
    public String getChangesDiff() {
        return changesDiff;
    }
    public void setChangesDiff(String changesDiff) {
        this.changesDiff = changesDiff;
    }
    public String getCommentaire() {
        return commentaire;
    }
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
    public Boolean getRestorePoint() {
        return restorePoint;
    }
    public void setRestorePoint(Boolean restorePoint) {
        this.restorePoint = restorePoint;
    }
    public void setStatus(ContentVersionStatus status) {
        this.status = status;
    }
    public Float getTailleDiffKb() {
        return tailleDiffKb;
    }
    public void setTailleDiffKb(Float tailleDiffKb) {
        this.tailleDiffKb = tailleDiffKb;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Integer getVersionNum() {
        return versionNum;
    }
    public void setVersionNum(Integer versionNum) {
        this.versionNum = versionNum;
    }

    public void creerVersion() {
        // TODO: Implement creerVersion logic
    }

    public void restaurerVersion() {
        // TODO: Implement restaurerVersion logic
    }

    public String comparer(String versionId) {
        // TODO: Implement comparer logic
        return null;
    }

    public void suspend() {
        if (this.status != ContentVersionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = ContentVersionStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != ContentVersionStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = ContentVersionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
