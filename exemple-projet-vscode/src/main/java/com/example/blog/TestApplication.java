package com.example.blog;

import com.example.blog.entity.*;
import com.example.blog.enums.*;
import java.time.LocalDateTime;

public class TestApplication {
    public static void main(String[] args) {
        System.out.println("=== TEST DES ENTITES ===\n");
        
        // Test Article
        Article article = new Article();
        article.setId(1);
        article.setTitre("Mon premier article");
        article.setDescription("Description de l'article");
        article.setDatePublication(LocalDateTime.now());
        article.setStatus(ArticleStatus.ACTIVE);
        
        System.out.println("Article cree:");
        System.out.println("  ID: " + article.getId());
        System.out.println("  Titre: " + article.getTitre());
        System.out.println("  Status: " + article.getStatus());
        
        // Test Utilisateur
        Utilisateur user = new Utilisateur();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setDateCreation(LocalDateTime.now());
        user.setStatus(UtilisateurStatus.ACTIVE);
        
        System.out.println("\nUtilisateur cree:");
        System.out.println("  ID: " + user.getId());
        System.out.println("  Email: " + user.getEmail());
        System.out.println("  Status: " + user.getStatus());
        
        // Test Favori
        Favori favori = new Favori();
        favori.setId(1);
        favori.setUserId(user.getId());
        favori.setArticleId(article.getId());
        favori.setDateAjout(LocalDateTime.now());
        favori.setStatus(FavoriStatus.ACTIVE);
        
        System.out.println("\nFavori cree:");
        System.out.println("  ID: " + favori.getId());
        System.out.println("  User ID: " + favori.getUserId());
        System.out.println("  Article ID: " + favori.getArticleId());
        System.out.println("  Status: " + favori.getStatus());
        
        // Test transitions d'etat
        System.out.println("\n=== TEST TRANSITIONS D'ETAT ===");
        favori.suspend();
        System.out.println("Favori suspendu: " + favori.getStatus());
        
        favori.activate();
        System.out.println("Favori active: " + favori.getStatus());
        
        System.out.println("\n=== TOUS LES TESTS PASSES ===");
    }
}
