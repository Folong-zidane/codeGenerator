package com.basiccode.generator.generator;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UMLClass;

import java.util.List;

/**
 * Interface pour les générateurs de controllers metadata-aware
 */
public interface IMetadataAwareControllerGenerator {
    
    /**
     * Génère un controller avec métadonnées
     */
    String generateControllerWithMetadata(EnhancedClass enhancedClass, String packageName, Object metadata);
    
    /**
     * Génère plusieurs controllers
     */
    String generateControllers(List<UMLClass> classes, String packageName, Object metadata);
    
    /**
     * Génère un controller simple
     */
    String generateController(EnhancedClass enhancedClass, String packageName);
    
    /**
     * Extension de fichier
     */
    String getFileExtension();
    
    /**
     * Répertoire des controllers
     */
    String getControllerDirectory();
    
    /**
     * Répertoire des controllers avec métadonnées
     */
    String getControllerDirectoryWithMetadata(Object metadata);
}