package com.example.blog.service;

import com.example.blog.entity.*;
import com.example.blog.repository.*;
import com.example.blog.dto.*;
import com.example.blog.exception.EntityNotFoundException;
import com.example.blog.exception.ValidationException;
import com.example.blog.enums.ArticleStatus;
import com.example.blog.enums.ArticleStatut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository repository;
    private final RubriqueRepository rubriqueRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final TagRepository tagRepository;
    private final BlocContenuRepository blocContenuRepository;
    private final MediaFileRepository mediaFileRepository;
    private final StatistiquesRepository statistiquesRepository;
    private final FeaturedItemRepository featuredItemRepository;

    @Transactional(readOnly = true)
    public List<ArticleReadDto> findAll() {
        return repository.findAll().stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArticleReadDto getById(Integer id) {
        return repository.findById(Long.valueOf(id))
            .map(this::convertToReadDto)
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
    }

    public ArticleReadDto create(ArticleCreateDto createDto) {
        Article article = new Article();
        article.setTitre(createDto.getTitre());
        article.setSlug(generateSlug(createDto.getTitre()));
        article.setDescription(createDto.getDescription());
        article.setStatut(createDto.getStatut() != null ? createDto.getStatut() : "BROUILLON");
        article.setVisible(createDto.getVisible() != null ? createDto.getVisible() : true);
        article.setRegion(createDto.getRegion());
        article.setDateCreation(LocalDateTime.now());
        article.setDateModification(LocalDateTime.now());
        
        if (createDto.getDatePublication() != null) {
            article.setDatePublication(createDto.getDatePublication());
        } else if ("PUBLIE".equals(createDto.getStatut())) {
            article.setDatePublication(LocalDateTime.now());
        }
        
        Rubrique rubrique = rubriqueRepository.findById(Long.valueOf(createDto.getRubriqueId()))
            .orElseThrow(() -> new EntityNotFoundException("Rubrique", Long.valueOf(createDto.getRubriqueId())));
        article.setRubriqueId(rubrique.getId());
        
        if (createDto.getAuteurId() != null) {
            Utilisateur auteur = utilisateurRepository.findById(Long.valueOf(createDto.getAuteurId()))
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur", Long.valueOf(createDto.getAuteurId())));
            article.setAuteurId(auteur.getId());
        }
        
        if (createDto.getImageCouvertureId() != null) {
            article.setImageCouvertureId(createDto.getImageCouvertureId());
        }
        
        Article saved = repository.save(article);
        
        if (createDto.getBlocsContenu() != null && !createDto.getBlocsContenu().isEmpty()) {
            for (BlocContenuDto blocDto : createDto.getBlocsContenu()) {
                BlocContenu bloc = new BlocContenu();
                bloc.setArticleId(saved.getId());
                bloc.setTypeBloc(blocDto.getTypeBloc());
                bloc.setContenuTexte(blocDto.getContenuTexte());
                bloc.setContenuMarkdown(blocDto.getContenuMarkdown());
                bloc.setMediaFileId(blocDto.getMediaFileId());
                bloc.setEmbedUrl(blocDto.getEmbedUrl());
                bloc.setLegende(blocDto.getLegende());
                bloc.setAltText(blocDto.getAltText());
                bloc.setPosition(blocDto.getPosition());
                bloc.setOrdre(blocDto.getOrdre());
                bloc.setCodeLanguage(blocDto.getCodeLanguage());
                bloc.setDateCreation(LocalDateTime.now());
                blocContenuRepository.save(bloc);
            }
        }
        
        Statistiques stats = new Statistiques();
        stats.setArticleId(saved.getId());
        stats.setVues(0);
        stats.setCreatedAt(LocalDateTime.now());
        statistiquesRepository.save(stats);
        
        return convertToReadDto(saved);
    }

    public ArticleReadDto update(Integer id, ArticleUpdateDto updateDto) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        if (updateDto.getTitre() != null) {
            article.setTitre(updateDto.getTitre());
            article.setSlug(generateSlug(updateDto.getTitre()));
        }
        if (updateDto.getDescription() != null) article.setDescription(updateDto.getDescription());
        if (updateDto.getStatut() != null) article.setStatut(updateDto.getStatut());
        if (updateDto.getVisible() != null) article.setVisible(updateDto.getVisible());
        if (updateDto.getRegion() != null) article.setRegion(updateDto.getRegion());
        if (updateDto.getDatePublication() != null) article.setDatePublication(updateDto.getDatePublication());
        if (updateDto.getRubriqueId() != null) article.setRubriqueId(updateDto.getRubriqueId());
        if (updateDto.getImageCouvertureId() != null) article.setImageCouvertureId(updateDto.getImageCouvertureId());
        
        article.setDateModification(LocalDateTime.now());
        
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public void deleteById(Integer id) {
        if (!repository.existsById(Long.valueOf(id))) {
            throw new EntityNotFoundException("Article", Long.valueOf(id));
        }
        repository.deleteById(Long.valueOf(id));
    }

    @Transactional(readOnly = true)
    public Page<ArticleReadDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
            .map(this::convertToReadDto);
    }
    
    private String generateSlug(String titre) {
        return titre.toLowerCase()
            .replaceAll("[àáâãäå]", "a")
            .replaceAll("[èéêë]", "e")
            .replaceAll("[ìíîï]", "i")
            .replaceAll("[òóôõö]", "o")
            .replaceAll("[ùúûü]", "u")
            .replaceAll("[ç]", "c")
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");
    }

    private ArticleReadDto convertToReadDto(Article article) {
        ArticleReadDto dto = new ArticleReadDto();
        dto.setId(article.getId());
        dto.setTitre(article.getTitre());
        dto.setSlug(article.getSlug());
        dto.setDescription(article.getDescription());
        dto.setStatut(article.getStatut());
        dto.setDatePublication(article.getDatePublication());
        dto.setDateCreation(article.getDateCreation());
        dto.setRegion(article.getRegion());
        
        if (article.getRubriqueId() != null) {
            rubriqueRepository.findById(Long.valueOf(article.getRubriqueId()))
                .ifPresent(r -> dto.setRubriqueNom(r.getNom()));
        }
        
        if (article.getAuteurId() != null) {
            utilisateurRepository.findById(Long.valueOf(article.getAuteurId()))
                .ifPresent(u -> dto.setAuteurNom(u.getEmail()));
        }
        
        return dto;
    }

    public ArticleReadDto publishArticle(Integer id) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        article.setStatut("PUBLIE");
        article.setDatePublication(LocalDateTime.now());
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public ArticleReadDto publishAdvanced(Integer id, ArticlePublicationDto publicationDto) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        if (publicationDto.getEnAvantPremiere() != null && publicationDto.getEnAvantPremiere()) {
            article.setStatut("EN_AVANT_PREMIERE");
            if (publicationDto.getDateFinAvantPremiere() != null) {
                // Programmer la publication après l'avant-première
            }
        } else if (publicationDto.getDatePublication() != null) {
            if (publicationDto.getDatePublication().isAfter(LocalDateTime.now())) {
                article.setStatut("PROGRAMME");
            } else {
                article.setStatut("PUBLIE");
            }
            article.setDatePublication(publicationDto.getDatePublication());
        } else {
            article.setStatut("PUBLIE");
            article.setDatePublication(LocalDateTime.now());
        }
        
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public ArticleReadDto scheduleArticle(Integer id, LocalDateTime datePublication) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        article.setStatut("PROGRAMME");
        article.setDatePublication(datePublication);
        
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public ArticleReadDto setPreview(Integer id, LocalDateTime dateFin) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        article.setStatut("EN_AVANT_PREMIERE");
        
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public ArticleReadDto archiveArticle(Integer id) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        article.setStatut("ARCHIVE");
        
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public ArticleReadDto rejectArticle(Integer id, String motif) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        article.setStatut("REJETE");
        
        Article updated = repository.save(article);
        return convertToReadDto(updated);
    }

    public void featureArticle(Integer id, FeaturedArticleDto featuredDto) {
        Article article = repository.findById(Long.valueOf(id))
            .orElseThrow(() -> new EntityNotFoundException("Article", Long.valueOf(id)));
        
        FeaturedItem featured = new FeaturedItem();
        featured.setArticleId(id);
        featured.setPosition(featuredDto.getPosition());
        featured.setDateDebut(featuredDto.getDateDebut() != null ? featuredDto.getDateDebut() : LocalDateTime.now());
        featured.setActif(featuredDto.getActif() != null ? featuredDto.getActif() : true);
        featured.setCreatedAt(LocalDateTime.now());
        
        featuredItemRepository.save(featured);
    }

    @Transactional(readOnly = true)
    public List<ArticleReadDto> getFeaturedArticles(String section) {
        List<FeaturedItem> featured = featuredItemRepository.findByActifTrueOrderByPositionAsc();
        return featured.stream()
            .map(f -> repository.findById(Long.valueOf(f.getArticleId())))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ArticleReadDto> getArticlesByStatus(String status, Pageable pageable) {
        return repository.findByStatut(status, pageable)
            .map(this::convertToReadDto);
    }

    @Transactional(readOnly = true)
    public List<ArticleReadDto> getScheduledArticles() {
        return repository.findByStatut("PROGRAMME").stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ArticleReadDto> getArticlesByRubrique(Integer rubriqueId) {
        return repository.findByRubriqueAndPublished(rubriqueId).stream()
            .map(this::convertToReadDto)
            .collect(Collectors.toList());
    }

}
