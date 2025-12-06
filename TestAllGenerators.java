import com.basiccode.generator.parser.SimpleClassParser;
import com.basiccode.generator.model.*;
import com.basiccode.generator.generator.spring.*;
import com.basiccode.generator.generator.django.*;
import com.basiccode.generator.generator.csharp.*;
import com.basiccode.generator.generator.typescript.*;
import com.basiccode.generator.generator.php.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class TestAllGenerators {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Test de TOUS les générateurs BasicCode");
            
            // Lire le diagramme de test
            String mermaidContent = Files.readString(Paths.get("test-simple.mermaid"));
            System.out.println("📋 Diagramme chargé: " + mermaidContent.split("\n").length + " lignes");
            
            // Parser le diagramme
            SimpleClassParser parser = new SimpleClassParser();
            Diagram diagram = parser.parseClassDiagram(mermaidContent);
            List<ClassModel> classes = diagram.getClasses();
            
            System.out.println("📊 Classes parsées: " + classes.size());
            for (ClassModel clazz : classes) {
                System.out.println("  - " + clazz.getName() + " (" + clazz.getFields().size() + " champs)");
            }
            
            // Convertir en EnhancedClass
            List<EnhancedClass> enhancedClasses = new ArrayList<>();
            for (ClassModel clazz : classes) {
                enhancedClasses.add(new EnhancedClass(clazz));
            }
            
            System.out.println("\n🔧 Test des générateurs par langage:");
            
            // ===== JAVA SPRING BOOT =====
            System.out.println("\n☕ JAVA SPRING BOOT:");
            testJavaGenerators(enhancedClasses);
            
            // ===== PYTHON DJANGO =====
            System.out.println("\n🐍 PYTHON DJANGO:");
            testDjangoGenerators(enhancedClasses);
            
            // ===== C# ASP.NET =====
            System.out.println("\n🔷 C# ASP.NET:");
            testCSharpGenerators(enhancedClasses);
            
            // ===== TYPESCRIPT NODE.JS =====
            System.out.println("\n📘 TYPESCRIPT NODE.JS:");
            testTypeScriptGenerators(enhancedClasses);
            
            // ===== PHP LARAVEL =====
            System.out.println("\n🐘 PHP LARAVEL:");
            testPhpGenerators(enhancedClasses);
            
            System.out.println("\n🎉 Test de tous les générateurs terminé avec succès!");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testJavaGenerators(List<EnhancedClass> classes) {
        try {
            SpringBootEntityGenerator entityGen = new SpringBootEntityGenerator();
            SpringBootRepositoryGenerator repoGen = new SpringBootRepositoryGenerator();
            SpringBootServiceGenerator serviceGen = new SpringBootServiceGenerator();
            SpringBootControllerGenerator controllerGen = new SpringBootControllerGenerator();
            
            for (EnhancedClass clazz : classes) {
                String entity = entityGen.generateEntity(clazz, "com.test");
                String repo = repoGen.generateRepository(clazz, "com.test");
                String service = serviceGen.generateService(clazz, "com.test");
                String controller = controllerGen.generateController(clazz, "com.test");
                
                System.out.println("  ✅ " + clazz.getOriginalClass().getName() + 
                    " - Entity:" + entity.length() + 
                    ", Repo:" + repo.length() + 
                    ", Service:" + service.length() + 
                    ", Controller:" + controller.length() + " chars");
            }
        } catch (Exception e) {
            System.out.println("  ❌ Java: " + e.getMessage());
        }
    }
    
    private static void testDjangoGenerators(List<EnhancedClass> classes) {
        try {
            DjangoEntityGenerator entityGen = new DjangoEntityGenerator();
            DjangoRepositoryGenerator repoGen = new DjangoRepositoryGenerator();
            DjangoServiceGenerator serviceGen = new DjangoServiceGenerator();
            DjangoControllerGenerator controllerGen = new DjangoControllerGenerator();
            
            for (EnhancedClass clazz : classes) {
                String entity = entityGen.generateEntity(clazz, "test_app");
                String repo = repoGen.generateRepository(clazz, "test_app");
                String service = serviceGen.generateService(clazz, "test_app");
                String controller = controllerGen.generateController(clazz, "test_app");
                
                System.out.println("  ✅ " + clazz.getOriginalClass().getName() + 
                    " - Model:" + entity.length() + 
                    ", Repo:" + repo.length() + 
                    ", Service:" + service.length() + 
                    ", View:" + controller.length() + " chars");
            }
        } catch (Exception e) {
            System.out.println("  ❌ Django: " + e.getMessage());
        }
    }
    
    private static void testCSharpGenerators(List<EnhancedClass> classes) {
        try {
            CSharpEntityGenerator entityGen = new CSharpEntityGenerator();
            CSharpRepositoryGenerator repoGen = new CSharpRepositoryGenerator();
            CSharpServiceGenerator serviceGen = new CSharpServiceGenerator();
            CSharpControllerGenerator controllerGen = new CSharpControllerGenerator();
            
            for (EnhancedClass clazz : classes) {
                String entity = entityGen.generateEntity(clazz, "TestApp");
                String repo = repoGen.generateRepository(clazz, "TestApp");
                String service = serviceGen.generateService(clazz, "TestApp");
                String controller = controllerGen.generateController(clazz, "TestApp");
                
                System.out.println("  ✅ " + clazz.getOriginalClass().getName() + 
                    " - Entity:" + entity.length() + 
                    ", Repo:" + repo.length() + 
                    ", Service:" + service.length() + 
                    ", Controller:" + controller.length() + " chars");
            }
        } catch (Exception e) {
            System.out.println("  ❌ C#: " + e.getMessage());
        }
    }
    
    private static void testTypeScriptGenerators(List<EnhancedClass> classes) {
        try {
            TypeScriptEntityGenerator entityGen = new TypeScriptEntityGenerator();
            TypeScriptRepositoryGenerator repoGen = new TypeScriptRepositoryGenerator();
            TypeScriptServiceGenerator serviceGen = new TypeScriptServiceGenerator();
            TypeScriptControllerGenerator controllerGen = new TypeScriptControllerGenerator();
            
            for (EnhancedClass clazz : classes) {
                String entity = entityGen.generateEntity(clazz, "test-app");
                String repo = repoGen.generateRepository(clazz, "test-app");
                String service = serviceGen.generateService(clazz, "test-app");
                String controller = controllerGen.generateController(clazz, "test-app");
                
                System.out.println("  ✅ " + clazz.getOriginalClass().getName() + 
                    " - Entity:" + entity.length() + 
                    ", Repo:" + repo.length() + 
                    ", Service:" + service.length() + 
                    ", Controller:" + controller.length() + " chars");
            }
        } catch (Exception e) {
            System.out.println("  ❌ TypeScript: " + e.getMessage());
        }
    }
    
    private static void testPhpGenerators(List<EnhancedClass> classes) {
        try {
            PhpEntityGenerator entityGen = new PhpEntityGenerator();
            PhpRepositoryGenerator repoGen = new PhpRepositoryGenerator();
            PhpServiceGenerator serviceGen = new PhpServiceGenerator();
            PhpControllerGenerator controllerGen = new PhpControllerGenerator();
            
            for (EnhancedClass clazz : classes) {
                String entity = entityGen.generateEntity(clazz, "TestApp");
                String repo = repoGen.generateRepository(clazz, "TestApp");
                String service = serviceGen.generateService(clazz, "TestApp");
                String controller = controllerGen.generateController(clazz, "TestApp");
                
                System.out.println("  ✅ " + clazz.getOriginalClass().getName() + 
                    " - Model:" + entity.length() + 
                    ", Repo:" + repo.length() + 
                    ", Service:" + service.length() + 
                    ", Controller:" + controller.length() + " chars");
            }
        } catch (Exception e) {
            System.out.println("  ❌ PHP: " + e.getMessage());
        }
    }
}