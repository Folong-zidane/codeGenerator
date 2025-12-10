package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.AnalyticsSessionStatus;

@Entity
@Table(name = "analyticssessions")
public class AnalyticsSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "pages_vues", columnDefinition = "JSON")
    private String pagesVues;

    @Column(name = "duree_session")
    private Integer dureeSession;

    @Column(name = "entree_url")
    private String entreeUrl;

    @Column(name = "sortie_url")
    private String sortieUrl;

    @Column(name = "conversions")
    private Integer conversions;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AnalyticsSessionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPagesVues() {
        return pagesVues;
    }

    public Integer getDureeSession() {
        return dureeSession;
    }

    public String getEntreeUrl() {
        return entreeUrl;
    }

    public String getSortieUrl() {
        return sortieUrl;
    }

    public Integer getConversions() {
        return conversions;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public AnalyticsSessionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setConversions(Integer conversions) {
        this.conversions = conversions;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }
    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }
    public void setDureeSession(Integer dureeSession) {
        this.dureeSession = dureeSession;
    }
    public void setEntreeUrl(String entreeUrl) {
        this.entreeUrl = entreeUrl;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    public void setPagesVues(String pagesVues) {
        this.pagesVues = pagesVues;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public void setSortieUrl(String sortieUrl) {
        this.sortieUrl = sortieUrl;
    }
    public void setStatus(AnalyticsSessionStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void enregistrerPageVue() {
        // TODO: Implement enregistrerPageVue logic
    }

    public void terminer() {
        // TODO: Implement terminer logic
    }

    public Float calculerConversion() {
        // TODO: Implement calculerConversion logic
        return 0.0f;
    }

    public void suspend() {
        if (this.status != AnalyticsSessionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = AnalyticsSessionStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != AnalyticsSessionStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = AnalyticsSessionStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
