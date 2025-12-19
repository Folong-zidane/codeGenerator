package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;

import java.util.List;

/**
 * Interface pour les générateurs de repositories metadata-aware
 */
public interface IMetadataAwareRepositoryGenerator {
    
    /**
     * Génère un repository avec métadonnées
     */
    String generateRepositoryWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata);
    
    /**
     * Génère plusieurs repositories
     */
    String generateRepositories(List<EnhancedClass> classes, String packageName, Object metadata);
    
    /**
     * Génère un repository simple
     */
    String generateRepository(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Extension de fichier
     */
    String getFileExtension();
    
    /**
     * Répertoire des repositories
     */
    String getRepositoryDirectory();
    
    /**
     * Répertoire des repositories avec métadonnées
     */
    String getRepositoryDirectoryWithMetadata(Object metadata);
}