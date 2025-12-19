package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

import java.util.List;

/**
 * Interface pour les générateurs d'entités metadata-aware
 */
public interface IMetadataAwareEntityGenerator {
    
    /**
     * Génère une entité avec métadonnées
     */
    String generateEntityWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata);
    
    /**
     * Génère plusieurs entités
     */
    String generateEntities(List<EnhancedClass> classes, String packageName, Object metadata);
    
    /**
     * Génère une entité simple
     */
    String generateEntity(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Génère un enum d'état
     */
    String generateStateEnum(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Génère un enum d'état avec métadonnées
     */
    String generateStateEnumWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata);
    
    /**
     * Extension de fichier
     */
    String getFileExtension();
    
    /**
     * Répertoire des entités
     */
    String getEntityDirectory();
    
    /**
     * Répertoire des entités avec métadonnées
     */
    String getEntityDirectoryWithMetadata(Object metadata);
}