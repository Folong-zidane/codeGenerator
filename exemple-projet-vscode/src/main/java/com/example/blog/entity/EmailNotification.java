package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.EmailNotificationStatus;

@Entity
@Table(name = "emailnotifications")
public class EmailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "notification_id")
    private Integer notificationId;

    @Column(name = "destinataire")
    private String destinataire;

    @Column(name = "corps_html", columnDefinition = "TEXT")
    private String corpsHtml;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EmailNotificationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public EmailNotificationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public String getCorpsHtml() {
        return corpsHtml;
    }
    public void setCorpsHtml(String corpsHtml) {
        this.corpsHtml = corpsHtml;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }
    public void setDateEnvoi(LocalDateTime dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
    public String getDestinataire() {
        return destinataire;
    }
    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getNotificationId() {
        return notificationId;
    }
    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }
    public void setStatus(EmailNotificationStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void envoyer() {
        // TODO: Implement envoyer logic
    }

    public void trackerOuverture() {
        // TODO: Implement trackerOuverture logic
    }

    public void suspend() {
        if (this.status != EmailNotificationStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = EmailNotificationStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != EmailNotificationStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = EmailNotificationStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
