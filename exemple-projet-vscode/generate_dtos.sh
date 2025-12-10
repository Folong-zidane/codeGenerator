#!/bin/bash
cd src/main/java/com/example/blog/dto

# Liste des entités
entities=(
    "Article" "Utilisateur" "Administrateur" "BlocContenu" "Rubrique" "Tag"
    "MediaFile" "MediaVariant" "MediaUsage" "MediaProcessingJob" "MediaLicense"
    "VideoStream" "AudioTrack" "DocumentPreview" "BulkUpload" "Statistiques"
    "VueArticle" "TelechargementMedia" "PartageArticle" "FeaturedItem"
    "HomepageLayout" "BoostRule" "CategoryFilter" "CarouselSlide" "CardPreview"
    "RealTimeUpdate" "UserPreference" "SEOConfig" "Sitemap" "Page" "Menu"
    "Langue" "Traduction" "LangueContenu" "PWAConfig" "AnalyticsEvent" "ABTest"
    "ProduitPremium" "Panier" "Paiement" "Transaction" "AbonnementPayant"
    "Paywall" "Commentaire" "Favori" "AbonnementNewsletter" "Recommandation"
    "SSOConfig" "RateLimit" "WCAGAudit" "CDNConfig" "TicketSupport"
    "MaintenanceTask" "Backup" "SystemLog" "PerformanceMetric" "ContratMaintenance"
    "DraftSession" "EditorState" "OfflineQueue" "RecoveryPoint" "ContentVersion"
    "AuditLog" "Cache" "Archive" "AnalyticsSession" "Region" "ArticleTag"
    "PartageConfig" "LienPartage" "Notification" "NotificationPreference"
    "PushNotification" "EmailNotification" "NotificationTemplate" "DeviceToken"
)

for entity in "${entities[@]}"; do
    # CreateDto
    cat > "${entity}CreateDto.java" << EOFCREATE
package com.example.blog.dto;

import lombok.Data;

@Data
public class ${entity}CreateDto {
    // TODO: Add fields
}
EOFCREATE

    # UpdateDto
    cat > "${entity}UpdateDto.java" << EOFUPDATE
package com.example.blog.dto;

import lombok.Data;

@Data
public class ${entity}UpdateDto {
    // TODO: Add fields
}
EOFUPDATE

    # ReadDto
    cat > "${entity}ReadDto.java" << EOFREAD
package com.example.blog.dto;

import lombok.Data;

@Data
public class ${entity}ReadDto {
    private Integer id;
    // TODO: Add fields
}
EOFREAD

    echo "Created DTOs for $entity"
done

echo "All DTOs created!"
