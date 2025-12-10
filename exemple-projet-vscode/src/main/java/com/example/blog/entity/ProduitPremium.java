package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.ProduitPremiumStatus;

@Entity
@Table(name = "produitpremiums")
public class ProduitPremium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "prix_fcfa")
    private Integer prixFcfa;

    @Column(name = "mobile_money_actif")
    private Boolean mobileMoneyActif;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProduitPremiumStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Boolean getBoolean() {
        return mobileMoneyActif;
    }

    public ProduitPremiumStatus getStatus() {
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
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Boolean getMobileMoneyActif() {
        return mobileMoneyActif;
    }
    public void setMobileMoneyActif(Boolean mobileMoneyActif) {
        this.mobileMoneyActif = mobileMoneyActif;
    }
    public Integer getPrixFcfa() {
        return prixFcfa;
    }
    public void setPrixFcfa(Integer prixFcfa) {
        this.prixFcfa = prixFcfa;
    }
    public void setStatus(ProduitPremiumStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean verifierStock() {
        // TODO: Implement verifierStock logic
        return null;
    }

    public void deduireStock() {
        // TODO: Implement deduireStock logic
    }

    public void suspend() {
        if (this.status != ProduitPremiumStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = ProduitPremiumStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != ProduitPremiumStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = ProduitPremiumStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
