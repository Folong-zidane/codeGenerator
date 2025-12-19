package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLClass;

import java.util.List;

/**
 * Interface pour les générateurs de services metadata-aware
 */
public interface IMetadataAwareServiceGenerator {
    
    /**
     * Génère un service avec métadonnées
     */
    String generateServiceWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata);
    
    /**
     * Génère plusieurs services
     */
    String generateServices(List<UMLClass> classes, String packageName, Object metadata);
    
    /**
     * Génère un service simple
     */
    String generateService(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Extension de fichier
     */
    String getFileExtension();
    
    /**
     * Répertoire des services
     */
    String getServiceDirectory();
    
    /**
     * Répertoire des services avec métadonnées
     */
    String getServiceDirectoryWithMetadata(Object metadata);
}