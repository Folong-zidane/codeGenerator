package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.ArchiveStatus;

@Entity
@Table(name = "archives")
public class Archive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "version_num")
    private Integer versionNum;

    @Column(name = "contenu_archive", columnDefinition = "JSON")
    private String contenuArchive;

    @Column(name = "date_archive")
    private LocalDateTime dateArchive;

    @Column(name = "raison")
    private String raison;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArchiveStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ArchiveStatus getStatus() {
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
    public String getContenuArchive() {
        return contenuArchive;
    }
    public void setContenuArchive(String contenuArchive) {
        this.contenuArchive = contenuArchive;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateArchive() {
        return dateArchive;
    }
    public void setDateArchive(LocalDateTime dateArchive) {
        this.dateArchive = dateArchive;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getRaison() {
        return raison;
    }
    public void setRaison(String raison) {
        this.raison = raison;
    }
    public void setStatus(ArchiveStatus status) {
        this.status = status;
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

    public void archiver() {
        // TODO: Implement archiver logic
    }

    public void restaurer() {
        // TODO: Implement restaurer logic
    }

    public void suspend() {
        if (this.status != ArchiveStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = ArchiveStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != ArchiveStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = ArchiveStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
