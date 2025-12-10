package com.example.blog.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.*;

import com.example.blog.enums.MediaFileStatus;

@Entity
@Table(name = "mediafiles")
public class MediaFile {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "type_mime")
    private String typeMime;

    @Column(name = "fichier_url")
    private String fichierUrl;

    @Column(name = "taille_octets")
    private Integer tailleOctets;

    @Column(name = "hash_sha256")
    private String hashSha256;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "auteur_id")
    private Integer auteurId;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "legende")
    private String legende;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "duree")
    private Integer duree;

    @Column(name = "nom_original")
    private String nomOriginal;

    @Column(name = "type_media")
    private String typeMedia;

    @Column(name = "taille_fichier")
    private Long tailleFichier;

    @Column(name = "chemin_fichier")
    private String cheminFichier;

    @Column(name = "url_acces")
    private String urlAcces;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MediaFileStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getTypeMime() {
        return typeMime;
    }

    public String getFichierUrl() {
        return fichierUrl;
    }

    public Integer getTailleOctets() {
        return tailleOctets;
    }

    public String getHashSha256() {
        return hashSha256;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public Integer getAuteurId() {
        return auteurId;
    }

    public String getAltText() {
        return altText;
    }

    public String getLegende() {
        return legende;
    }

    public String getDimensions() {
        return dimensions;
    }

    public Integer getDuree() {
        return duree;
    }

    public MediaFileStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setAltText(String altText) {
        this.altText = altText;
    }
    public void setAuteurId(Integer auteurId) {
        this.auteurId = auteurId;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    public void setFichierUrl(String fichierUrl) {
        this.fichierUrl = fichierUrl;
    }
    public void setHashSha256(String hashSha256) {
        this.hashSha256 = hashSha256;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setLegende(String legende) {
        this.legende = legende;
    }
    public void setStatus(MediaFileStatus status) {
        this.status = status;
    }
    public void setTailleOctets(Integer tailleOctets) {
        this.tailleOctets = tailleOctets;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public void setTypeMime(String typeMime) {
        this.typeMime = typeMime;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
    public String getNomOriginal() {
        return nomOriginal;
    }
    public void setNomOriginal(String nomOriginal) {
        this.nomOriginal = nomOriginal;
    }
    public String getTypeMedia() {
        return typeMedia;
    }
    public void setTypeMedia(String typeMedia) {
        this.typeMedia = typeMedia;
    }
    public Long getTailleFichier() {
        return tailleFichier;
    }
    public void setTailleFichier(Long tailleFichier) {
        this.tailleFichier = tailleFichier;
    }
    public String getCheminFichier() {
        return cheminFichier;
    }
    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }
    public String getUrlAcces() {
        return urlAcces;
    }
    public void setUrlAcces(String urlAcces) {
        this.urlAcces = urlAcces;
    }
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String uploadToS3() {
        // TODO: Implement uploadToS3 logic
        return "";
    }

    public String genererHash() {
        // TODO: Implement genererHash logic
        return "";
    }

    public Boolean verifierDuplication() {
        // TODO: Implement verifierDuplication logic
        return null;
    }

    public void supprimerAvecVariants() {
        // TODO: Implement supprimerAvecVariants logic
    }

    public void suspend() {
        if (this.status != MediaFileStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend from state: " + this.status);
        }
        this.status = MediaFileStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.status != MediaFileStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate from state: " + this.status);
        }
        this.status = MediaFileStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

}
