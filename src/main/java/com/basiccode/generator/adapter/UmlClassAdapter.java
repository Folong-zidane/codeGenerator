package com.basiccode.generator.adapter;

import com.basiccode.generator.model.UMLClass;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;

/**
 * Adaptateur pour enrichir les classes UML avec les métadonnées
 */
@Component
public class UmlClassAdapter {
    
    /**
     * Enrichit une UMLClass avec des métadonnées
     */
    public UMLClass enrichWithMetadata(UMLClass umlClass, String stereotype, String module) {
        // Ajouter le stéréotype
        if (stereotype != null && !umlClass.hasStereotype(stereotype)) {
            umlClass.addStereotype(stereotype);
        }
        
        // Ajouter le module comme métadonnée
        if (module != null) {
            umlClass.addMetadata("module", module);
        }
        
        return umlClass;
    }
    
    /**
     * Détecte automatiquement le stéréotype
     */
    public String detectStereotype(UMLClass umlClass) {
        String className = umlClass.getName();
        
        if (className.endsWith("Service")) return "Service";
        if (className.endsWith("Controller")) return "Controller";
        if (className.endsWith("Repository")) return "Repository";
        
        return "Entity"; // Par défaut
    }
    
    /**
     * Détecte automatiquement le module
     */
    public String detectModule(UMLClass umlClass) {
        return umlClass.getName().toLowerCase();
    }
    
    /**
     * Enrichit une liste de classes
     */
    public List<UMLClass> enrichAll(List<UMLClass> classes) {
        List<UMLClass> enriched = new ArrayList<>();
        
        for (UMLClass umlClass : classes) {
            String stereotype = detectStereotype(umlClass);
            String module = detectModule(umlClass);
            enriched.add(enrichWithMetadata(umlClass, stereotype, module));
        }
        
        return enriched;
    }
}