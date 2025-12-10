# 🚀 GUIDE DE DÉMARRAGE - Blog Application

## ✅ État Actuel du Projet

- **76 entités** corrigées et prêtes
- **Structure Maven** créée
- **Configuration Spring Boot** en place
- **Base de données H2** configurée (en mémoire)

---

## 📋 Prérequis

- Java 11 ou supérieur
- Maven 3.6+
- Connexion Internet (pour télécharger les dépendances)

---

## 🔧 Structure du Projet

```
exemple-projet-vscode/
├── pom.xml                          # Configuration Maven
├── src/
│   ├── main/
│   │   ├── java/com/example/blog/
│   │   │   ├── BlogApplication.java      # Classe principale
│   │   │   ├── entity/                   # 76 entités JPA
│   │   │   ├── enums/                    # Enums de statut
│   │   │   ├── repository/               # Repositories Spring Data
│   │   │   ├── service/                  # Services métier
│   │   │   └── controller/               # Controllers REST
│   │   └── resources/
│   │       └── application.properties    # Configuration
│   └── test/
│       └── java/                         # Tests unitaires
└── target/                               # Fichiers compilés
```

---

## 🚀 Démarrage Rapide

### 1. Compiler le Projet

```bash
cd /home/folongzidane/Documents/Projet/basicCode/exemple-projet-vscode
mvn clean compile
```

### 2. Lancer l'Application

```bash
mvn spring-boot:run
```

### 3. Accéder à l'Application

- **API REST**: http://localhost:8080
- **Console H2**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:blogdb`
  - Username: `sa`
  - Password: (vide)

---

## 🧪 Tests Disponibles

### Test Simple (Sans Spring Boot)

```bash
./compile_test.sh
```

Ce test vérifie que les POJOs fonctionnent correctement.

### Test avec Spring Boot

Une fois Maven configuré:

```bash
mvn test
```

---

## 📡 Endpoints API (Exemples)

### Articles

```bash
# Créer un article
curl -X POST http://localhost:8080/api/articles \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Mon article",
    "description": "Description",
    "status": "ACTIVE"
  }'

# Lister les articles
curl http://localhost:8080/api/articles

# Obtenir un article
curl http://localhost:8080/api/articles/1
```

### Utilisateurs

```bash
# Créer un utilisateur
curl -X POST http://localhost:8080/api/utilisateurs \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "motDePasse": "password123",
    "status": "ACTIVE"
  }'

# Lister les utilisateurs
curl http://localhost:8080/api/utilisateurs
```

### Favoris

```bash
# Ajouter un favori
curl -X POST http://localhost:8080/api/favoris \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "articleId": 1,
    "status": "ACTIVE"
  }'

# Lister les favoris
curl http://localhost:8080/api/favoris
```

---

## 🔍 Vérification de la Compilation

### Vérifier les Enums

```bash
ls -la src/main/java/com/example/blog/enums/
```

Devrait afficher 76 fichiers d'enums.

### Vérifier les Entités

```bash
ls -la src/main/java/com/example/blog/entity/
```

Devrait afficher 76 fichiers d'entités.

### Compiler une Entité Spécifique

```bash
javac -cp "$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" \
  src/main/java/com/example/blog/entity/Article.java
```

---

## 🐛 Dépannage

### Problème: Maven ne télécharge pas les dépendances

**Solution**:
```bash
# Nettoyer le cache Maven
rm -rf ~/.m2/repository

# Réessayer
mvn clean install -U
```

### Problème: Port 8080 déjà utilisé

**Solution**: Modifier le port dans `application.properties`:
```properties
server.port=8081
```

### Problème: Erreurs de compilation

**Solution**: Vérifier la version de Java:
```bash
java -version
mvn -version
```

---

## 📊 Prochaines Étapes

### 1. Implémenter les Repositories

Les repositories sont déjà créés mais vides. Exemple:

```java
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByStatus(ArticleStatus status);
    List<Article> findByAuteurId(Integer auteurId);
}
```

### 2. Implémenter les Services

```java
@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    
    public Article creerArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setStatus(ArticleStatus.ACTIVE);
        return articleRepository.save(article);
    }
}
```

### 3. Implémenter les Controllers

```java
@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    
    @PostMapping
    public ResponseEntity<Article> creer(@RequestBody Article article) {
        return ResponseEntity.ok(articleService.creerArticle(article));
    }
}
```

### 4. Ajouter les Tests

```java
@SpringBootTest
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    
    @Test
    public void testCreerArticle() {
        Article article = new Article();
        article.setTitre("Test");
        Article saved = articleService.creerArticle(article);
        assertNotNull(saved.getId());
    }
}
```

---

## 📚 Documentation Technique

### Technologies Utilisées

- **Spring Boot 2.7.18**: Framework principal
- **Spring Data JPA**: Accès aux données
- **Hibernate**: ORM
- **H2 Database**: Base de données en mémoire
- **Maven**: Gestion des dépendances

### Conventions de Code

- **Entités**: CamelCase (Article, Utilisateur)
- **Champs**: camelCase (dateCreation, auteurId)
- **Colonnes DB**: snake_case (date_creation, auteur_id)
- **Enums**: UPPER_CASE (ACTIVE, SUSPENDED)

---

## ✅ Checklist de Démarrage

- [ ] Java 11+ installé
- [ ] Maven installé
- [ ] Dépendances téléchargées (`mvn clean install`)
- [ ] Compilation réussie (`mvn compile`)
- [ ] Application démarre (`mvn spring-boot:run`)
- [ ] Console H2 accessible
- [ ] Premier endpoint testé

---

## 🆘 Support

En cas de problème:

1. Vérifier les logs dans la console
2. Consulter `RAPPORT_CORRECTION.md` pour les détails des entités
3. Vérifier la configuration dans `application.properties`
4. Tester avec le script `compile_test.sh`

---

**Bon développement ! 🎉**
