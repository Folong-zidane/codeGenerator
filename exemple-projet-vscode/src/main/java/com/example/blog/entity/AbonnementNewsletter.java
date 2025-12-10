package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.AbonnementNewsletterStatus;

@Entity
@Table(name = "abonnementnewsletters")
public class AbonnementNewsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "preferences_rubriques", columnDefinition = "JSON")
    private String preferencesRubriques;

    @Column(name = "date_inscription")
    private LocalDateTime dateInscription;

    @Column(name = "double_optin")
    private Boolean doubleOptin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AbonnementNewsletterStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return doubleOptin;
    }

    public AbonnementNewsletterStatus getStatus() {
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
    public LocalDateTime getDateInscription() {
        return dateInscription;
    }
    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }
    public Boolean getDoubleOptin() {
        return doubleOptin;
    }
    public void setDoubleOptin(Boolean doubleOptin) {
        this.doubleOptin = doubleOptin;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getPreferencesRubriques() {
        return preferencesRubriques;
    }
    public void setPreferencesRubriques(String preferencesRubriques) {
        this.preferencesRubriques = preferencesRubriques;
    }
    public void setStatus(AbonnementNewsletterStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void confirmer() {
        // TODO: Implement confirmer logic
    }

    public void desabonner() {
        // TODO: Implement desabonner logic
    }

    public void mettreAJourPreferences() {
        // TODO: Implement mettreAJourPreferences logic
    }

    public void suspend() {
        if (this.status != AbonnementNewsletterStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = AbonnementNewsletterStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != AbonnementNewsletterStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = AbonnementNewsletterStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
