import com.basiccode.generator.parser.SimpleClassParser;
import com.basiccode.generator.service.BehaviorExtractor;
import com.basiccode.generator.service.StateEnhancer;
import com.basiccode.generator.model.*;
import com.basiccode.generator.generator.spring.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.ArrayList;

public class TestSimpleBehavioral {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Test de génération comportementale simplifiée");
            
            // Lire les diagrammes
            String classContent = Files.readString(Paths.get("test-simple.mermaid"));
            String sequenceContent = Files.readString(Paths.get("test-behavioral.mermaid"));
            
            System.out.println("📋 Diagrammes chargés:");
            System.out.println("  - Classes: " + classContent.split("\n").length + " lignes");
            System.out.println("  - Séquences: " + sequenceContent.split("\n").length + " lignes");
            
            // Parser les diagrammes
            SimpleClassParser parser = new SimpleClassParser();
            Diagram classModel = parser.parseClassDiagram(classContent);
            SequenceDiagram sequenceModel = parser.parseSequenceDiagram(sequenceContent);
            
            System.out.println("\n📊 Modèles parsés:");
            System.out.println("  - Classes: " + classModel.getClasses().size());
            System.out.println("  - Messages de séquence: " + (sequenceModel.getMessages() != null ? sequenceModel.getMessages().size() : 0));
            
            // Extraire les comportements
            BehaviorExtractor behaviorExtractor = new BehaviorExtractor();
            Map<String, List<BusinessMethod>> businessLogic = behaviorExtractor.extractBusinessLogic(sequenceModel);
            
            System.out.println("  - Classes avec comportements: " + businessLogic.size());
            for (Map.Entry<String, List<BusinessMethod>> entry : businessLogic.entrySet()) {
                System.out.println("    🔧 " + entry.getKey() + ": " + entry.getValue().size() + " méthodes");
                for (BusinessMethod method : entry.getValue()) {
                    System.out.println("      - " + method.getName() + "() -> " + method.getReturnType());
                }
            }
            
            // Améliorer les classes avec les comportements
            StateEnhancer stateEnhancer = new StateEnhancer();
            List<EnhancedClass> enhancedClasses = new ArrayList<>();
            
            for (ClassModel clazz : classModel.getClasses()) {
                EnhancedClass enhanced = new EnhancedClass(clazz);
                
                // Ajouter les comportements pertinents
                String className = clazz.getName();
                List<BusinessMethod> classBehaviors = new ArrayList<>();
                
                // Chercher les comportements pour cette classe
                for (Map.Entry<String, List<BusinessMethod>> entry : businessLogic.entrySet()) {
                    String participantName = entry.getKey();
                    if (participantName.toLowerCase().contains(className.toLowerCase())) {
                        classBehaviors.addAll(entry.getValue());
                    }
                }
                
                enhanced.setBehavioralMethods(classBehaviors);
                enhancedClasses.add(enhanced);
            }
            
            System.out.println("\n🔧 Génération de code comportemental:");
            
            // Générer le code avec comportements
            SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
            SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
            SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
            
            for (EnhancedClass clazz : enhancedClasses) {
                String className = clazz.getOriginalClass().getName();
                
                String entity = entityGen.generateEntity(clazz, "com.test.behavioral");
                String service = serviceGen.generateService(clazz, "com.test.behavioral");
                String controller = controllerGen.generateController(clazz, "com.test.behavioral");
                
                System.out.println("  ✅ " + className + ":");
                System.out.println("    - Entity: " + entity.length() + " chars");
                System.out.println("    - Service: " + service.length() + " chars (" + 
                    (clazz.getBehavioralMethods() != null ? clazz.getBehavioralMethods().size() : 0) + " comportements)");
                System.out.println("    - Controller: " + controller.length() + " chars");
                
                // Afficher un aperçu du service avec comportements
                if (clazz.getBehavioralMethods() != null && !clazz.getBehavioralMethods().isEmpty()) {
                    System.out.println("    📄 Aperçu service avec comportements:");
                    String[] lines = service.split("\n");
                    for (int i = 0; i < Math.min(10, lines.length); i++) {
                        if (lines[i].trim().contains("public") && lines[i].trim().contains("(")) {
                            System.out.println("      " + lines[i].trim());
                        }
                    }
                }
            }
            
            System.out.println("\n🎉 Test de génération comportementale terminé avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}