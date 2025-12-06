import com.basiccode.generator.service.TripleDiagramCodeGeneratorService;
import com.basiccode.generator.model.ComprehensiveCodeResult;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestBehavioralGeneration {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Test de la génération comportementale");
            
            // Lire les diagrammes
            String classContent = Files.readString(Paths.get("test-simple.mermaid"));
            String sequenceContent = Files.readString(Paths.get("test-behavioral.mermaid"));
            
            System.out.println("📋 Diagrammes chargés:");
            System.out.println("  - Classes: " + classContent.split("\n").length + " lignes");
            System.out.println("  - Séquences: " + sequenceContent.split("\n").length + " lignes");
            
            // Créer le service de génération
            TripleDiagramCodeGeneratorService service = new TripleDiagramCodeGeneratorService();
            
            // Test génération Java
            System.out.println("\n☕ Test génération Java comportementale:");
            ComprehensiveCodeResult javaResult = service.generateComprehensiveCode(
                classContent, 
                sequenceContent, 
                "", // Pas de diagramme d'état pour ce test
                "com.test.behavioral", 
                "java"
            );
            
            System.out.println("✅ Génération Java réussie:");
            System.out.println("  - Fichiers générés: " + javaResult.getFiles().size());
            for (String filename : javaResult.getFiles().keySet()) {
                System.out.println("    📄 " + filename + " (" + javaResult.getFiles().get(filename).length() + " chars)");
            }
            
            // Test génération Python
            System.out.println("\n🐍 Test génération Python comportementale:");
            ComprehensiveCodeResult pythonResult = service.generateComprehensiveCode(
                classContent, 
                sequenceContent, 
                "", 
                "test_behavioral", 
                "python"
            );
            
            System.out.println("✅ Génération Python réussie:");
            System.out.println("  - Fichiers générés: " + pythonResult.getFiles().size());
            for (String filename : pythonResult.getFiles().keySet()) {
                System.out.println("    📄 " + filename + " (" + pythonResult.getFiles().get(filename).length() + " chars)");
            }
            
            // Test génération C#
            System.out.println("\n🔷 Test génération C# comportementale:");
            ComprehensiveCodeResult csharpResult = service.generateComprehensiveCode(
                classContent, 
                sequenceContent, 
                "", 
                "TestBehavioral", 
                "csharp"
            );
            
            System.out.println("✅ Génération C# réussie:");
            System.out.println("  - Fichiers générés: " + csharpResult.getFiles().size());
            for (String filename : csharpResult.getFiles().keySet()) {
                System.out.println("    📄 " + filename + " (" + csharpResult.getFiles().get(filename).length() + " chars)");
            }
            
            System.out.println("\n🎉 Test de génération comportementale terminé avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}