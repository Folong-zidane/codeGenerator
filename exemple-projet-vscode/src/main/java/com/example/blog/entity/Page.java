package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.PageStatus;

@Entity
@Table(name = "pages")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "meta_seo_id")
    private Integer metaSeoId;

    @Column(name = "layout_json", columnDefinition = "JSON")
    private String layoutJson;

    @Column(name = "contenu_html", columnDefinition = "TEXT")
    private String contenuHtml;

    @Column(name = "visible")
    private Boolean visible;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PageStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;




    public PageStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getContenuHtml() {
        return contenuHtml;
    }
    public void setContenuHtml(String contenuHtml) {
        this.contenuHtml = contenuHtml;
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
    public String getLayoutJson() {
        return layoutJson;
    }
    public void setLayoutJson(String layoutJson) {
        this.layoutJson = layoutJson;
    }
    public Integer getMetaSeoId() {
        return metaSeoId;
    }
    public void setMetaSeoId(Integer metaSeoId) {
        this.metaSeoId = metaSeoId;
    }
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = slug;
    }
    public void setStatus(PageStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public Boolean getVisible() {
        return visible;
    }
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String render() {
        // TODO: Implement render logic
        return null;
    }

    public String previsualiser() {
        // TODO: Implement previsualiser logic
        return null;
    }

    public void suspend() {
        if (this.status != PageStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = PageStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != PageStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = PageStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
