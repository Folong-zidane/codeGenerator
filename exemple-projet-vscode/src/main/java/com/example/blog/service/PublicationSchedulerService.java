package com.example.blog.service;

import com.example.blog.entity.Article;
import com.example.blog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicationSchedulerService {

    private final ArticleRepository articleRepository;

    @Scheduled(fixedRate = 60000) // Vérifie toutes les minutes
    @Transactional
    public void publishScheduledArticles() {
        List<Article> scheduledArticles = articleRepository.findScheduledForPublication(LocalDateTime.now());
        
        for (Article article : scheduledArticles) {
            log.info("Publication automatique de l'article: {}", article.getTitre());
            article.setStatut("PUBLIE");
            articleRepository.save(article);
        }
        
        if (!scheduledArticles.isEmpty()) {
            log.info("Publié {} articles programmés", scheduledArticles.size());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?") // Tous les jours à 1h du matin
    @Transactional
    public void cleanupExpiredPreviews() {
        // Logique pour nettoyer les avant-premières expirées
        log.info("Nettoyage des avant-premières expirées");
    }
}