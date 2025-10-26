# 🔧 Guide d'Intégration - Scripts dans ZIP

## 📋 Objectif

Intégrer automatiquement les scripts de développement continu dans chaque ZIP généré par l'API.

## 🏗️ Modification du Générateur Java

### 1. Ajouter les Scripts au Projet

```java
// Dans GeneratorController.java ou service approprié
@Service
public class ZipEnhancementService {
    
    public void addDevelopmentScripts(ZipOutputStream zipOut, GenerationRequest request) {
        try {
            // Ajouter update-project.sh
            addFileToZip(zipOut, "update-project.sh", 
                generateUpdateScript("sh", request));
            
            // Ajouter update-project.bat  
            addFileToZip(zipOut, "update-project.bat", 
                generateUpdateScript("bat", request));
            
            // Ajouter README.md personnalisé
            addFileToZip(zipOut, "README.md", 
                generateReadme(request));
            
            // Ajouter configuration projet
            addFileToZip(zipOut, ".project-config", 
                generateProjectConfig(request));
            
            // Ajouter scripts de démarrage
            addFileToZip(zipOut, "start.sh", 
                generateStartScript("sh", request.getLanguage()));
            addFileToZip(zipOut, "start.bat", 
                generateStartScript("bat", request.getLanguage()));
                
        } catch (IOException e) {
            log.error("Erreur lors de l'ajout des scripts", e);
        }
    }
    
    private String generateUpdateScript(String type, GenerationRequest request) {
        // Lire le template depuis resources/templates/
        String template = loadTemplate("update-project." + type);
        
        // Remplacer les variables
        return template
            .replace("{{API_URL}}", getApiUrl())
            .replace("{{LANGUAGE}}", request.getLanguage())
            .replace("{{PACKAGE_NAME}}", request.getPackageName());
    }
    
    private String generateReadme(GenerationRequest request) {
        String template = loadTemplate("README-PROJECT.md");
        
        return template
            .replace("{{PROJECT_NAME}}", extractProjectName(request))
            .replace("{{LANGUAGE}}", request.getLanguage())
            .replace("{{PACKAGE_NAME}}", request.getPackageName())
            .replace("{{CREATED_DATE}}", LocalDateTime.now().toString());
    }
    
    private String generateProjectConfig(GenerationRequest request) {
        return String.format(
            "PROJECT_NAME=%s\n" +
            "LANGUAGE=%s\n" +
            "PACKAGE_NAME=%s\n" +
            "CREATED_DATE=%s\n",
            extractProjectName(request),
            request.getLanguage(),
            request.getPackageName(),
            LocalDateTime.now()
        );
    }
}
```

### 2. Modifier le Controller Principal

```java
// Dans GeneratorController.java
@PostMapping("/api/generate/crud")
public ResponseEntity<byte[]> generateCrud(@RequestBody GenerationRequest request) {
    try {
        // Génération normale du code
        GenerationResult result = generatorService.generate(request);
        
        // Créer le ZIP avec le code généré
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);
        
        // Ajouter le code généré
        addGeneratedCodeToZip(zipOut, result);
        
        // ✨ NOUVEAU: Ajouter les scripts de développement
        zipEnhancementService.addDevelopmentScripts(zipOut, request);
        
        zipOut.close();
        
        return ResponseEntity.ok()
            .header("Content-Type", "application/zip")
            .header("Content-Disposition", 
                "attachment; filename=" + generateFileName(request))
            .body(baos.toByteArray());
            
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}
```

### 3. Templates dans resources/templates/

#### resources/templates/update-project.sh
```bash
#!/bin/bash
# Script généré automatiquement
API_URL="{{API_URL}}"
LANGUAGE="{{LANGUAGE}}"
PACKAGE_NAME="{{PACKAGE_NAME}}"

# [Contenu du script update-project.sh]
```

#### resources/templates/update-project.bat
```batch
@echo off
REM Script généré automatiquement
set API_URL={{API_URL}}
set LANGUAGE={{LANGUAGE}}
set PACKAGE_NAME={{PACKAGE_NAME}}

REM [Contenu du script update-project.bat]
```

#### resources/templates/README-PROJECT.md
```markdown
# 🚀 Projet {{LANGUAGE}} - {{PROJECT_NAME}}

Généré le: {{CREATED_DATE}}
Package: {{PACKAGE_NAME}}

## Démarrage
```bash
./start.sh    # Linux/macOS
start.bat     # Windows
```

## Mise à jour continue
```bash
./update-project.sh model.mermaid
```

[Reste de la documentation...]
```

## 🔄 Workflow Complet

### 1. Génération Initiale
```bash
# L'utilisateur génère son premier projet
curl -X POST "http://localhost:8080/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d @request.json \
  -o my-project.zip

# Extraction
unzip my-project.zip
cd my-project/

# Le ZIP contient maintenant:
# ├── src/                 # Code généré
# ├── update-project.sh    # Script de mise à jour
# ├── update-project.bat   # Version Windows
# ├── start.sh            # Script de démarrage
# ├── start.bat           # Version Windows
# ├── .project-config     # Configuration
# └── README.md           # Documentation
```

### 2. Développement Continu
```bash
# L'utilisateur modifie son diagramme
vim updated-model.mermaid

# Mise à jour automatique
./update-project.sh updated-model.mermaid

# Le script:
# 1. Sauvegarde les modifications utilisateur
# 2. Appelle l'API avec le nouveau diagramme
# 3. Fusionne intelligemment les changements
# 4. Préserve le code personnalisé
```

## 📁 Structure du ZIP Généré

```
generated-project.zip
├── src/main/java/com/example/    # Code généré
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── controller/
├── update-project.sh             # ✨ NOUVEAU
├── update-project.bat            # ✨ NOUVEAU  
├── start.sh                      # ✨ NOUVEAU
├── start.bat                     # ✨ NOUVEAU
├── .project-config               # ✨ NOUVEAU
├── README.md                     # ✨ NOUVEAU
├── pom.xml                       # Configuration Maven
└── model.mermaid                 # Diagramme exemple
```

## 🎯 Avantages

✅ **Expérience utilisateur fluide** - Tout est inclus dans le ZIP  
✅ **Développement continu** - Mise à jour sans perte de code  
✅ **Multi-plateforme** - Scripts Linux/macOS et Windows  
✅ **Documentation intégrée** - README personnalisé  
✅ **Configuration automatique** - Pas de setup manuel  

## 🔧 Implémentation

### Étapes d'intégration:

1. **Ajouter ZipEnhancementService** au projet Java
2. **Créer les templates** dans resources/templates/
3. **Modifier GeneratorController** pour inclure les scripts
4. **Tester** la génération avec les nouveaux scripts
5. **Déployer** la nouvelle version

### Test de validation:
```bash
# Générer un projet test
curl -X POST "http://localhost:8080/api/generate/crud" \
  -d '{"language":"java","packageName":"com.test"}' \
  -o test.zip

# Vérifier le contenu
unzip -l test.zip | grep -E "\.(sh|bat|md|config)$"

# Doit afficher:
# update-project.sh
# update-project.bat  
# start.sh
# start.bat
# README.md
# .project-config
```

Cette intégration transforme le générateur en **véritable outil de développement continu** ! 🚀