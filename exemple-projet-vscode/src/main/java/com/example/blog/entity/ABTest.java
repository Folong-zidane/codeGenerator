package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.ABTestStatus;

@Entity
@Table(name = "abtests")
public class ABTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "page_id")
    private Integer pageId;

    @Column(name = "nom_test")
    private String nomTest;

    @Column(name = "variant_a", columnDefinition = "JSON")
    private String variantA;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "participants")
    private Integer participants;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ABTestStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ABTestStatus getStatus() {
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
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNomTest() {
        return nomTest;
    }
    public void setNomTest(String nomTest) {
        this.nomTest = nomTest;
    }
    public Integer getPageId() {
        return pageId;
    }
    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }
    public Integer getParticipants() {
        return participants;
    }
    public void setParticipants(Integer participants) {
        this.participants = participants;
    }
    public void setStatus(ABTestStatus status) {
        this.status = status;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getVariantA() {
        return variantA;
    }
    public void setVariantA(String variantA) {
        this.variantA = variantA;
    }

    public String assignerVariant(String userId) {
        // TODO: Implement assignerVariant logic
        return "";
    }

    public String calculerGagnant() {
        // TODO: Implement calculerGagnant logic
        return "";
    }

    public void suspend() {
        if (this.status != ABTestStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = ABTestStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != ABTestStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = ABTestStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
