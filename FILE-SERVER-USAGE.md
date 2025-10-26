# 🚀 Serveur de Fichiers - Génération Directe

## 📡 API Endpoints

### **Génération dans Dossiers Spécifiés**
```
POST /api/v2/generate/files
```

### **Génération Directe**
```
POST /api/v2/generate/direct
```

## 🔧 Frameworks Supportés

| Framework | Description | Commande |
|-----------|-------------|----------|
| `spring_boot` | Java Spring Boot | `"framework": "spring_boot"` |
| `python` | Python FastAPI | `"framework": "python"` |
| `django` | Python Django | `"framework": "django"` |
| `flask` | Python Flask | `"framework": "flask"` |
| `dotnet` | C# .NET | `"framework": "dotnet"` |

## 📋 Exemples d'Utilisation

### **1. Générer Projet Spring Boot**
```bash
curl -X POST "http://localhost:8080/api/v2/generate/files" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example",
    "outputPath": "./my-spring-project",
    "generationType": "complete_project",
    "framework": "spring_boot"
  }'
```

### **2. Générer Projet Python FastAPI**
```bash
curl -X POST "http://localhost:8080/api/v2/generate/files" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example",
    "outputPath": "./my-python-project",
    "generationType": "complete_project",
    "framework": "python"
  }'
```

### **3. Générer Projet Django**
```bash
curl -X POST "http://localhost:8080/api/v2/generate/files" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example",
    "outputPath": "./my-django-project",
    "generationType": "complete_project",
    "framework": "django"
  }'
```

### **4. Générer Projet Flask**
```bash
curl -X POST "http://localhost:8080/api/v2/generate/files" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example",
    "outputPath": "./my-flask-project",
    "generationType": "complete_project",
    "framework": "flask"
  }'
```

### **5. Générer Projet .NET**
```bash
curl -X POST "http://localhost:8080/api/v2/generate/files" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "Com.Example",
    "outputPath": "./my-dotnet-project",
    "generationType": "complete_project",
    "framework": "dotnet"
  }'
```

## 🎯 Test avec PicknDrop

```bash
# Créer la requête avec ton fichier
cat > request.json << 'EOF'
{
  "umlContent": "classDiagram\n    class Colis {\n        +UUID id\n        +String trackingNumber\n        +String status\n        +Float weight\n        +String dimensions\n        +Date createdAt\n        +updateStatus(newStatus, acteur)\n        +calculateFee()\n    }\n    \n    class ColisHistorique {\n        +UUID id\n        +UUID colisId\n        +String ancienStatus\n        +String nouveauStatus\n        +Date dateChangement\n        +UUID acteurId\n        +String commentaire\n    }\n    \n    class Expedition {\n        +UUID id\n        +UUID colisId\n        +UUID prDepartId\n        +UUID prArriveeId\n        +String status\n        +Integer numeroSaut\n        +Date dateDepart\n        +Date dateArrivee\n        +planifierExpedition()\n        +confirmerDepart()\n    }\n    \n    class PointRelais {\n        +UUID id\n        +String name\n        +String address\n        +Float latitude\n        +Float longitude\n        +Integer capacity\n        +Boolean isActive\n        +String contactPhone\n        +calculateDistance(otherPR)\n        +checkCapacity()\n    }\n    \n    class User {\n        +UUID id\n        +String username\n        +String email\n        +String phone\n        +String accountType\n        +Boolean active\n        +Date createdAt\n        +validateCredentials()\n        +updateProfile()\n    }\n    \n    class EventLog {\n        +UUID id\n        +String category\n        +String type\n        +String action\n        +UUID entiteId\n        +String entiteType\n        +UUID acteurId\n        +String acteurType\n        +Date timestamp\n        +String payload\n        +logEvent()\n    }\n    \n    Colis \"1\" --> \"*\" ColisHistorique\n    Colis \"1\" --> \"*\" Expedition\n    Expedition \"*\" --> \"1\" PointRelais : depart\n    Expedition \"*\" --> \"1\" PointRelais : arrivee\n    User \"1\" --> \"*\" Colis : expediteur\n    User \"1\" --> \"*\" Colis : destinataire",
  "packageName": "com.pickndrop",
  "outputPath": "./pickndrop-spring-boot",
  "generationType": "complete_project",
  "framework": "spring_boot"
}
EOF

# Générer le projet Spring Boot
curl -X POST "http://localhost:8080/api/v2/generate/files" \
  -H "Content-Type: application/json" \
  -d @request.json

# Lancer le projet
cd pickndrop-spring-boot
mvn spring-boot:run
```

## 📊 Réponses API

### **Succès**
```json
{
  "success": true,
  "message": "Files generated successfully in /path/to/project",
  "classCount": 6,
  "outputPath": "/absolute/path/to/project"
}
```

### **Erreur**
```json
{
  "success": false,
  "message": "Error: Invalid UML content",
  "classCount": 0,
  "outputPath": null
}
```

## 🚀 Comment ça Marche

1. **L'API reçoit** la requête avec le contenu UML
2. **Parse le diagramme** avec ANTLR4
3. **Génère le code CRUD** avec les générateurs existants
4. **Crée la structure complète** selon le framework
5. **Écrit directement** dans le dossier spécifié
6. **Retourne la confirmation** avec le chemin

## 🎯 Avantages

- ✅ **Écriture directe** dans les dossiers
- ✅ **Projets complets** prêts à lancer
- ✅ **Multi-frameworks** (Java, Python, C#)
- ✅ **Pas de ZIP** à extraire
- ✅ **Structure complète** avec dépendances

## 🔧 Lancement du Serveur

```bash
# Dans le projet basicCode
mvn spring-boot:run

# Le serveur démarre sur http://localhost:8080
# Endpoints disponibles:
# - POST /api/v2/generate/files
# - POST /api/v2/generate/direct
```