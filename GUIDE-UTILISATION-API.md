# 🚀 Guide d'Utilisation de l'API PicknDrop

## ✅ Résultat de la Génération

L'API a généré avec succès un projet Spring Boot complet pour PicknDrop !

### 📊 Statistiques
- **6 entités** générées : Colis, ColisHistorique, Expedition, PointRelais, User, EventLog
- **24 fichiers** au total (6 entités × 4 couches)
- **Taille du ZIP** : 17KB
- **Package** : `com.pickndrop`

### 🏗️ Structure Générée

```
pickndrop-project/
└── src/main/java/com/pickndrop/
    ├── entity/          # 6 entités JPA
    │   ├── Colis.java
    │   ├── ColisHistorique.java
    │   ├── EventLog.java
    │   ├── Expedition.java
    │   ├── PointRelais.java
    │   └── User.java
    ├── repository/      # 6 repositories Spring Data
    │   ├── ColisRepository.java
    │   ├── ColisHistoriqueRepository.java
    │   ├── EventLogRepository.java
    │   ├── ExpeditionRepository.java
    │   ├── PointRelaisRepository.java
    │   └── UserRepository.java
    ├── service/         # 6 services métier
    │   ├── ColisService.java
    │   ├── ColisHistoriqueService.java
    │   ├── EventLogService.java
    │   ├── ExpeditionService.java
    │   ├── PointRelaisService.java
    │   └── UserService.java
    └── controller/      # 6 contrôleurs REST
        ├── ColisController.java
        ├── ColisHistoriqueController.java
        ├── EventLogController.java
        ├── ExpeditionController.java
        ├── PointRelaisController.java
        └── UserController.java
```

## 🔧 Commandes Utilisées

### 1️⃣ Validation UML
```bash
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/validate" \
  -H "Content-Type: application/json" \
  -d "$(cat examples/pickndrop-model.mermaid)"
```

**Résultat :** ✅ Valid UML, 6 classes détectées

### 2️⃣ Génération CRUD
```bash
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d @pickndrop-request.json \
  --output "pickndrop-spring-boot.zip"
```

**Résultat :** ✅ ZIP de 17KB généré avec succès

### 3️⃣ Extraction
```bash
unzip -o pickndrop-spring-boot.zip -d pickndrop-project/
```

## 📝 Caractéristiques du Code Généré

### 🏛️ Entités JPA
- **Annotations complètes** : `@Entity`, `@Table`, `@Column`
- **Swagger documentation** : `@Schema` pour l'API
- **Champs d'audit** : `createdAt`, `updatedAt`, `version`
- **UUID comme clé primaire**
- **Méthodes equals/hashCode**

### 🗄️ Repositories
- **Spring Data JPA** : Extension de `JpaRepository`
- **Méthodes CRUD automatiques**
- **Queries personnalisées** (findBy...)

### ⚙️ Services
- **Couche métier transactionnelle**
- **Méthodes CRUD complètes** : create, findById, findAll, update, delete
- **Gestion des Optional**

### 🌐 Controllers REST
- **Endpoints RESTful complets**
- **Mapping automatique** : GET, POST, PUT, DELETE
- **Gestion des réponses HTTP**
- **Validation des paramètres**

## 🚀 Prochaines Étapes

### 1️⃣ Compléter le Projet Spring Boot

Créer le `pom.xml` :
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.pickndrop</groupId>
    <artifactId>pickndrop-api</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.2</version>
        <relativePath/>
    </parent>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2️⃣ Créer l'Application Principal

`src/main/java/com/pickndrop/PicknDropApplication.java` :
```java
package com.pickndrop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PicknDropApplication {
    public static void main(String[] args) {
        SpringApplication.run(PicknDropApplication.class, args);
    }
}
```

### 3️⃣ Configuration

`src/main/resources/application.yml` :
```yaml
server:
  port: 8080

spring:
  application:
    name: pickndrop-api
  datasource:
    url: jdbc:h2:mem:pickndrop
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### 4️⃣ Lancer l'Application

```bash
cd pickndrop-project/
mvn spring-boot:run
```

### 5️⃣ Tester les Endpoints

L'application sera disponible sur :
- **API** : http://localhost:8080
- **Swagger UI** : http://localhost:8080/swagger-ui.html
- **H2 Console** : http://localhost:8080/h2-console

**Endpoints générés :**
- `GET /api/coliss` - Liste tous les colis
- `POST /api/coliss` - Créer un colis
- `GET /api/coliss/{id}` - Récupérer un colis
- `PUT /api/coliss/{id}` - Modifier un colis
- `DELETE /api/coliss/{id}` - Supprimer un colis

Et ainsi de suite pour toutes les entités.

## 🎯 Avantages de cette Approche

1. **Rapidité** : Code généré en quelques secondes
2. **Cohérence** : Architecture MVC respectée
3. **Standards** : Annotations JPA et Spring Boot
4. **Documentation** : Swagger intégré
5. **Prêt à l'emploi** : Endpoints REST fonctionnels
6. **Extensible** : Code modifiable et personnalisable

## 🔄 Génération Incrémentale

Pour ajouter de nouvelles entités ou modifier les existantes :
1. Modifier le diagramme Mermaid
2. Relancer l'API
3. Le système préservera vos modifications manuelles

## 📞 Support

L'API est déployée et fonctionnelle sur Render :
- **URL** : https://codegenerator-cpyh.onrender.com
- **Status** : ✅ Opérationnelle
- **Performance** : Génération en ~3 secondes