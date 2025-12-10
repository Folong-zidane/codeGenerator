#!/bin/bash
echo "=== COMPILATION DES ENTITES (TEST SIMPLE) ==="

# Créer le répertoire de sortie
mkdir -p target/test-classes

# Compiler les enums
echo "Compilation des enums..."
javac -d target/test-classes src/main/java/com/example/blog/enums/*.java

# Créer des POJOs simples pour test (sans annotations JPA)
echo "Creation des POJOs de test..."

cat > target/test-classes/SimpleArticle.java << 'EOF'
import java.time.LocalDateTime;

public class SimpleArticle {
    private Integer id;
    private String titre;
    private String description;
    private LocalDateTime datePublication;
    private String status;
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
EOF

cat > target/test-classes/TestSimple.java << 'EOF'
import java.time.LocalDateTime;

public class TestSimple {
    public static void main(String[] args) {
        System.out.println("=== TEST SIMPLE DES ENTITES ===\n");
        
        SimpleArticle article = new SimpleArticle();
        article.setId(1);
        article.setTitre("Mon premier article");
        article.setDescription("Description test");
        article.setDatePublication(LocalDateTime.now());
        article.setStatus("ACTIVE");
        
        System.out.println("Article cree:");
        System.out.println("  ID: " + article.getId());
        System.out.println("  Titre: " + article.getTitre());
        System.out.println("  Description: " + article.getDescription());
        System.out.println("  Status: " + article.getStatus());
        
        System.out.println("\n=== TEST REUSSI ===");
    }
}
EOF

# Compiler et exécuter
cd target/test-classes
javac SimpleArticle.java TestSimple.java
java TestSimple

echo ""
echo "=== COMPILATION TERMINEE ==="
