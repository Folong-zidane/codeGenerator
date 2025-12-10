package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.NotificationTemplateStatus;

@Entity
@Table(name = "notificationtemplates")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "langue_id")
    private Integer langueId;

    @Column(name = "sujet_template")
    private String sujetTemplate;

    @Column(name = "corps_template", columnDefinition = "TEXT")
    private String corpsTemplate;

    @Column(name = "variables", columnDefinition = "JSON")
    private String variables;

    @Column(name = "actif")
    private Boolean actif;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationTemplateStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    public Boolean getBoolean() {
        return actif;
    }

    public NotificationTemplateStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public Boolean getActif() {
        return actif;
    }
    public void setActif(Boolean actif) {
        this.actif = actif;
    }
    public String getCorpsTemplate() {
        return corpsTemplate;
    }
    public void setCorpsTemplate(String corpsTemplate) {
        this.corpsTemplate = corpsTemplate;
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
    public Integer getLangueId() {
        return langueId;
    }
    public void setLangueId(Integer langueId) {
        this.langueId = langueId;
    }
    public void setStatus(NotificationTemplateStatus status) {
        this.status = status;
    }
    public String getSujetTemplate() {
        return sujetTemplate;
    }
    public void setSujetTemplate(String sujetTemplate) {
        this.sujetTemplate = sujetTemplate;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getVariables() {
        return variables;
    }
    public void setVariables(String variables) {
        this.variables = variables;
    }

    public String render(String data) {
        // TODO: Implement render logic
        return "";
    }

    public String previsualiser() {
        // TODO: Implement previsualiser logic
        return "";
    }

    public void suspend() {
        if (this.status != NotificationTemplateStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = NotificationTemplateStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != NotificationTemplateStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = NotificationTemplateStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
