package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.WCAGAuditStatus;

@Entity
@Table(name = "wcagaudits")
public class WCAGAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "score_aa")
    private Integer scoreAa;

    @Column(name = "issues_json", columnDefinition = "JSON")
    private String issuesJson;

    @Column(name = "last_audit")
    private LocalDateTime lastAudit;

    @Column(name = "auto_fix_available")
    private Boolean autoFixAvailable;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WCAGAuditStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return autoFixAvailable;
    }

    public WCAGAuditStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getAutoFixAvailable() {
        return autoFixAvailable;
    }
    public void setAutoFixAvailable(Boolean autoFixAvailable) {
        this.autoFixAvailable = autoFixAvailable;
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
    public String getIssuesJson() {
        return issuesJson;
    }
    public void setIssuesJson(String issuesJson) {
        this.issuesJson = issuesJson;
    }
    public LocalDateTime getLastAudit() {
        return lastAudit;
    }
    public void setLastAudit(LocalDateTime lastAudit) {
        this.lastAudit = lastAudit;
    }
    public Integer getPageId() {
        return pageId;
    }
    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }
    public Integer getScoreAa() {
        return scoreAa;
    }
    public void setScoreAa(Integer scoreAa) {
        this.scoreAa = scoreAa;
    }
    public void setStatus(WCAGAuditStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void auditer() {
        // TODO: Implement auditer logic
    }

    public java.io.File genererRapport() {
        // TODO: Implement genererRapport logic
        return null;
    }

    public void appliquerCorrections() {
        // TODO: Implement appliquerCorrections logic
    }

    public void suspend() {
        if (this.status != WCAGAuditStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = WCAGAuditStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != WCAGAuditStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = WCAGAuditStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
