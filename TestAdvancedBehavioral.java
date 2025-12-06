import com.basiccode.generator.parser.SimpleClassParser;
import com.basiccode.generator.parser.EnhancedSequenceDiagramParser;
import com.basiccode.generator.model.*;
import com.basiccode.generator.generator.spring.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TestAdvancedBehavioral {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Test de génération comportementale avancée");
            
            // Lire les diagrammes
            String classContent = Files.readString(Paths.get("test-simple.mermaid"));
            String sequenceContent = Files.readString(Paths.get("test-behavioral.mermaid"));
            
            System.out.println("📋 Diagrammes chargés:");
            System.out.println("  - Classes: " + classContent.split("\n").length + " lignes");
            System.out.println("  - Séquences: " + sequenceContent.split("\n").length + " lignes");
            
            // Parser les diagrammes
            SimpleClassParser classParser = new SimpleClassParser();
            Diagram classModel = classParser.parseClassDiagram(classContent);
            
            EnhancedSequenceDiagramParser sequenceParser = new EnhancedSequenceDiagramParser();
            EnhancedSequenceDiagramParser.SequenceDiagram sequenceModel = 
                (EnhancedSequenceDiagramParser.SequenceDiagram) sequenceParser.parse(sequenceContent);
            
            System.out.println("\n📊 Modèles parsés:");
            System.out.println("  - Classes: " + classModel.getClasses().size());
            System.out.println("  - Méthodes de séquence: " + sequenceModel.getMethods().size());
            
            // Afficher les méthodes extraites
            for (EnhancedSequenceDiagramParser.SequenceMethod method : sequenceModel.getMethods()) {
                System.out.println("    🔧 " + method.getTargetClass() + "." + method.getMethodName() + 
                    "(" + method.getFormattedParameters() + ") -> " + method.getReturnType());
            }
            
            // Créer des classes enrichies avec les méthodes de séquence
            List<EnhancedClass> enhancedClasses = new ArrayList<>();
            
            for (ClassModel clazz : classModel.getClasses()) {
                EnhancedClass enhanced = new EnhancedClass(clazz);
                
                // Trouver les méthodes pour cette classe
                List<BusinessMethod> classMethods = new ArrayList<>();
                for (EnhancedSequenceDiagramParser.SequenceMethod seqMethod : sequenceModel.getMethods()) {
                    if (seqMethod.getTargetClass().toLowerCase().contains(clazz.getName().toLowerCase())) {
                        BusinessMethod businessMethod = new BusinessMethod();
                        businessMethod.setName(seqMethod.getMethodName());
                        businessMethod.setReturnType(seqMethod.getReturnType());
                        
                        // Convertir les paramètres
                        List<String> params = new ArrayList<>();
                        for (EnhancedSequenceDiagramParser.MethodParameter param : seqMethod.getParameters()) {
                            params.add(param.getType() + " " + param.getName());
                        }
                        businessMethod.setParameters(params);
                        
                        // Générer la logique métier basique
                        List<String> logic = new ArrayList<>();
                        logic.add("// Méthode extraite du diagramme de séquence");
                        logic.add("log.info(\"Exécution de " + seqMethod.getMethodName() + "\");");
                        if (!"void".equals(seqMethod.getReturnType())) {
                            logic.add("return null; // TODO: Implémenter");
                        }
                        businessMethod.setBusinessLogic(logic);
                        
                        classMethods.add(businessMethod);
                    }
                }
                
                enhanced.setBehavioralMethods(classMethods);
                enhancedClasses.add(enhanced);
            }
            
            System.out.println("\n🔧 Génération de code comportemental avancé:");
            
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
                    (clazz.getBehavioralMethods() != null ? clazz.getBehavioralMethods().size() : 0) + " méthodes comportementales)");
                System.out.println("    - Controller: " + controller.length() + " chars");
                
                // Afficher les méthodes comportementales générées
                if (clazz.getBehavioralMethods() != null && !clazz.getBehavioralMethods().isEmpty()) {
                    System.out.println("    📄 Méthodes comportementales:");
                    for (BusinessMethod method : clazz.getBehavioralMethods()) {
                        System.out.println("      - " + method.getName() + "(" + 
                            String.join(", ", method.getParameters()) + ") -> " + method.getReturnType());
                    }
                }
            }
            
            // Sauvegarder un exemple de service généré
            if (!enhancedClasses.isEmpty()) {
                EnhancedClass firstClass = enhancedClasses.get(0);
                String serviceCode = serviceGen.generateService(firstClass, "com.test.behavioral");
                
                Files.write(Paths.get("generated-service-example.java"), serviceCode.getBytes());
                System.out.println("\n📄 Service exemple sauvegardé dans: generated-service-example.java");
            }
            
            System.out.println("\n🎉 Test de génération comportementale avancée terminé avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}