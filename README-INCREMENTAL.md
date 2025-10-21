# 🔄 Système de Génération Incrémentale - RÉSOLU

## ✅ Problème Principal Résolu

**AVANT** : La régénération écrasait les modifications manuelles
**MAINTENANT** : Le système préserve intelligemment le code manuel et ajoute seulement les nouveautés

## 🛠️ Technologies Utilisées

### 1. **JavaParser** - Analyse de Code
```xml
<dependency>
    <groupId>com.github.javaparser</groupId>
    <artifactId>javaparser-core</artifactId>
    <version>3.25.8</version>
</dependency>
```

### 2. **Swagger/OpenAPI** - Documentation API
```xml
<dependency>
    <groupId>io.swagger.core.v3</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>2.2.20</version>
</dependency>
```

### 3. **Hibernate Validator** - Validation ORM
```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>
```

## 🔍 Comment ça Fonctionne

### 1. **Détection Intelligente**
```java
// IntelligentMerger.java
CompilationUnit existingCode = parser.parse(existingFile);
CompilationUnit newCode = parser.parse(generatedCode);

// Analyse :
// - Champs existants vs nouveaux
// - Méthodes manuelles vs générées
// - Annotations à préserver
```

### 2. **Stratégie de Merge**
```java
// Préserve les méthodes manuelles
if (isManualMethod(existingMethod)) {
    // Garde la version manuelle
} else if (isGeneratedMethod(existingMethod)) {
    // Remplace par nouvelle version générée
}

// Ajoute seulement les nouveaux champs
if (!existingFields.contains(newField.getName())) {
    mergedClass.addField(newField);
}
```

### 3. **Backup Automatique**
```java
// Sauvegarde avant modification
Path backup = createBackup(originalFile);
// → User_20241201_143022.java.bak
```

## 📊 Exemple Concret

### Scénario : Modification de User

#### 1. **Génération Initiale**
```bash
java -jar uml-generator.jar diagram.mermaid -o output --incremental
```

Génère :
```java
@Entity
@Table(name = "user")
@Schema(description = "User entity")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false)
    private UUID id;
    
    @Column(name = "username", nullable = false, length = 255)
    @NotBlank(message = "username cannot be blank")
    @Schema(description = "Name", required = true)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true, length = 255)
    @Email(message = "Invalid email format")
    @Schema(description = "Email address", required = true)
    private String email;
    
    // Audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    
    @Version
    private Long version;
    
    // Constructors, getters, setters...
}
```

#### 2. **Développeur Ajoute Code Manuel**
```java
// AJOUT MANUEL
public boolean isValidEmail() {
    return email != null && email.contains("@") && email.length() > 5;
}

// MODIFICATION MANUELLE du getter
public String getUsername() {
    return username != null ? username.trim().toUpperCase() : null;
}
```

#### 3. **Modification du Diagramme UML**
```mermaid
class User {
    +UUID id
    +String username
    +String email
    +String phone     // ← NOUVEAU
    +Boolean active   // ← NOUVEAU
}
```

#### 4. **Régénération Incrémentale**
```bash
java -jar uml-generator.jar diagram.mermaid -o output --incremental
```

**Résultat** :
```
💾 Backup created: User_20241201_143022.java.bak
🔄 Merged User - Added: 2 fields, 4 methods
```

#### 5. **Code Final Mergé**
```java
@Entity
@Table(name = "user")
@Schema(description = "User entity")
public class User {
    // Champs existants préservés
    @Id private UUID id;
    private String username;
    private String email;
    
    // NOUVEAUX CHAMPS ajoutés automatiquement
    @Column(name = "phone", nullable = true, length = 255)
    @Schema(description = "Name")
    private String phone;
    
    @Column(name = "active", nullable = true)
    @Schema(description = "active (Boolean)")
    private Boolean active;
    
    // Champs d'audit préservés
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;
    
    // MÉTHODE MANUELLE préservée
    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.length() > 5;
    }
    
    // GETTER MANUEL préservé
    public String getUsername() {
        return username != null ? username.trim().toUpperCase() : null;
    }
    
    // NOUVEAUX GETTERS/SETTERS générés
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
```

## 📈 Améliorations ORM et Swagger

### **Mapping ORM Avancé**
- **Index automatiques** sur created_at, updated_at
- **Contraintes de validation** (@Email, @NotBlank, @Min)
- **Types optimisés** (precision/scale pour decimaux)
- **Optimistic locking** avec @Version
- **Audit automatique** avec @CreationTimestamp/@UpdateTimestamp

### **Documentation Swagger Complète**
- **@Schema** sur classes et champs
- **Descriptions intelligentes** basées sur les noms
- **Exemples automatiques** pour les types
- **AccessMode READ_ONLY** pour les champs d'audit
- **Validation requirements** (required = true/false)

## 🎯 Commandes Disponibles

### Génération Standard
```bash
java -jar uml-generator.jar diagram.mermaid -o output
```

### Génération Incrémentale (Recommandée)
```bash
java -jar uml-generator.jar diagram.mermaid -o output --incremental
```

### Génération Dynamique
```bash
java -jar uml-generator.jar generate-dynamic diagram.mermaid -o output
```

## 📊 Rapport de Génération

```
📊 GENERATION SUMMARY
====================
✅ Created: 2    (nouveaux fichiers)
🔄 Merged: 3     (fichiers mergés)
⏭️  Skipped: 1    (aucun changement)
📁 Total: 6

🔍 MERGE DETAILS:
  User:
    + Fields: phone, active
    + Methods: getPhone(), setPhone(), getActive(), setActive()
  Order:
    + Fields: deliveryAddress
    + Methods: getDeliveryAddress(), setDeliveryAddress()
```

## ✅ Avantages du Système

1. **🔒 Préservation Totale** - Aucune perte de code manuel
2. **🚀 Ajout Intelligent** - Seules les nouveautés sont ajoutées  
3. **💾 Backup Automatique** - Sécurité garantie
4. **📊 Visibilité Complète** - Rapport détaillé des changements
5. **⚡ Performance** - Génère seulement ce qui a changé
6. **🏗️ ORM Production-Ready** - Mapping avancé avec validation
7. **📚 Documentation Auto** - Swagger intégré

Le système est maintenant **production-ready** pour des projets d'entreprise !