import com.basiccode.generator.parser.SimpleClassParser;
import com.basiccode.generator.model.*;
import java.util.ArrayList;
import com.basiccode.generator.generator.spring.SpringBootEntityGenerator;
import com.basiccode.generator.generator.spring.SpringBootControllerGenerator;
import com.basiccode.generator.generator.spring.SpringBootServiceGenerator;
import com.basiccode.generator.generator.spring.SpringBootRepositoryGenerator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestGeneratorSimple {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Test des générateurs BasicCode");
            
            // Lire le diagramme de test
            String mermaidContent = Files.readString(Paths.get("test-simple.mermaid"));
            System.out.println("📋 Diagramme chargé:");
            System.out.println(mermaidContent);
            
            // Parser le diagramme
            SimpleClassParser parser = new SimpleClassParser();
            Diagram diagram = parser.parseClassDiagram(mermaidContent);
            List<ClassModel> classes = diagram.getClasses();
            
            System.out.println("\n📊 Classes parsées: " + classes.size());
            for (ClassModel clazz : classes) {
                System.out.println("  - " + clazz.getName() + " (" + clazz.getFields().size() + " champs)");
            }
            
            // Convertir en EnhancedClass
            List<EnhancedClass> enhancedClasses = new ArrayList<>();
            for (ClassModel clazz : classes) {
                enhancedClasses.add(new EnhancedClass(clazz));
            }
            
            // Test des générateurs
            System.out.println("\n🔧 Test des générateurs:");
            
            // Entity Generator
            SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
            for (EnhancedClass clazz : enhancedClasses) {
                String entityCode = entityGen.generateEntity(clazz, "com.test");
                System.out.println("\n✅ Entity " + clazz.getOriginalClass().getName() + " générée (" + entityCode.length() + " caractères)");
                System.out.println("📄 Aperçu:");
                System.out.println(entityCode.substring(0, Math.min(200, entityCode.length())) + "...");
            }
            
            // Repository Generator
            SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
            for (EnhancedClass clazz : enhancedClasses) {
                String repoCode = repoGen.generateRepository(clazz, "com.test");
                System.out.println("\n✅ Repository " + clazz.getOriginalClass().getName() + " générée (" + repoCode.length() + " caractères)");
            }
            
            // Service Generator
            SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
            for (EnhancedClass clazz : enhancedClasses) {
                String serviceCode = serviceGen.generateService(clazz, "com.test");
                System.out.println("✅ Service " + clazz.getOriginalClass().getName() + " générée (" + serviceCode.length() + " caractères)");
            }
            
            // Controller Generator
            SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
            for (EnhancedClass clazz : enhancedClasses) {
                String controllerCode = controllerGen.generateController(clazz, "com.test");
                System.out.println("✅ Controller " + clazz.getOriginalClass().getName() + " générée (" + controllerCode.length() + " caractères)");
            }
            
            System.out.println("\n🎉 Test terminé avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}