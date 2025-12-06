import com.basiccode.generator.generator.TypeScriptProjectGenerator;
import com.basiccode.generator.model.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class TestTypeScriptGenerator {
    public static void main(String[] args) {
        try {
            // Créer des modèles de test
            List<ClassModel> classes = new ArrayList<>();
            
            // User class
            ClassModel user = new ClassModel("User");
            user.addField(new Field("username", "String", Visibility.PUBLIC));
            user.addField(new Field("email", "String", Visibility.PUBLIC));
            user.addField(new Field("active", "Boolean", Visibility.PUBLIC));
            classes.add(user);
            
            // Product class
            ClassModel product = new ClassModel("Product");
            product.addField(new Field("name", "String", Visibility.PUBLIC));
            product.addField(new Field("price", "Float", Visibility.PUBLIC));
            product.addField(new Field("stock", "Integer", Visibility.PUBLIC));
            classes.add(product);
            
            // Order class
            ClassModel order = new ClassModel("Order");
            order.addField(new Field("userId", "String", Visibility.PUBLIC));
            order.addField(new Field("productId", "String", Visibility.PUBLIC));
            order.addField(new Field("quantity", "Integer", Visibility.PUBLIC));
            order.addField(new Field("total", "Float", Visibility.PUBLIC));
            classes.add(order);
            
            // Générer le projet TypeScript
            TypeScriptProjectGenerator generator = new TypeScriptProjectGenerator();
            Path outputDir = Paths.get("typescript-test-output");
            
            System.out.println("🔧 Génération du projet TypeScript...");
            generator.generateCompleteProject(classes, "com.example", outputDir);
            
            System.out.println("✅ Projet TypeScript généré dans: " + outputDir.toAbsolutePath());
            
            // Analyser les fichiers générés
            analyzeGeneratedFiles(outputDir);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void analyzeGeneratedFiles(Path outputDir) {
        System.out.println("\n📋 ANALYSE DES FICHIERS GÉNÉRÉS:");
        System.out.println("================================");
        
        try {
            java.nio.file.Files.walk(outputDir)
                .filter(java.nio.file.Files::isRegularFile)
                .forEach(file -> {
                    String fileName = file.getFileName().toString();
                    long size = 0;
                    try {
                        size = java.nio.file.Files.size(file);
                    } catch (Exception e) {}
                    
                    System.out.println("📄 " + file.toString().replace(outputDir.toString(), "") + " (" + size + " bytes)");
                });
                
            System.out.println("\n🔍 PROBLÈMES IDENTIFIÉS:");
            System.out.println("========================");
            
            // Vérifier package.json
            Path packageJson = outputDir.resolve("package.json");
            if (java.nio.file.Files.exists(packageJson)) {
                System.out.println("✅ package.json existe");
            } else {
                System.out.println("❌ package.json manquant");
            }
            
            // Vérifier database.ts
            Path databaseTs = outputDir.resolve("src/config/database.ts");
            if (java.nio.file.Files.exists(databaseTs)) {
                String content = java.nio.file.Files.readString(databaseTs);
                if (content.contains("entities: [User]")) {
                    System.out.println("❌ Configuration database hardcodée pour User seulement");
                }
                if (!content.contains("process.env")) {
                    System.out.println("❌ Pas de variables d'environnement dans la configuration DB");
                }
            }
            
            // Vérifier les entités
            Path entitiesDir = outputDir.resolve("src/entities");
            if (java.nio.file.Files.exists(entitiesDir)) {
                long entityCount = java.nio.file.Files.list(entitiesDir).count();
                System.out.println("📊 " + entityCount + " entités générées");
                
                // Vérifier les relations
                java.nio.file.Files.list(entitiesDir)
                    .forEach(entityFile -> {
                        try {
                            String content = java.nio.file.Files.readString(entityFile);
                            if (!content.contains("@OneToMany") && !content.contains("@ManyToOne")) {
                                System.out.println("❌ " + entityFile.getFileName() + " : Pas de relations générées");
                            }
                        } catch (Exception e) {}
                    });
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'analyse: " + e.getMessage());
        }
    }
}