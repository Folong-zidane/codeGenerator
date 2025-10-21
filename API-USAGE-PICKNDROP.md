# 🚀 Utilisation de l'API pour PicknDrop

## 📍 URL de l'API
```
https://codegenerator-cpyh.onrender.com
```

## 🔧 Méthodes d'utilisation

### 1️⃣ Validation UML

```bash
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/validate" \
  -H "Content-Type: application/json" \
  -d "$(cat examples/pickndrop-model.mermaid)"
```

**Réponse attendue:**
```json
{
  "valid": true,
  "message": "Valid UML",
  "classCount": 6
}
```

### 2️⃣ Génération CRUD (ZIP)

```bash
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class Colis {\n        +UUID id\n        +String trackingNumber\n        +String status\n        +Float weight\n        +String dimensions\n        +Date createdAt\n        +updateStatus(newStatus, acteur)\n        +calculateFee()\n    }\n    \n    class ColisHistorique {\n        +UUID id\n        +UUID colisId\n        +String ancienStatus\n        +String nouveauStatus\n        +Date dateChangement\n        +UUID acteurId\n        +String commentaire\n    }\n    \n    class Expedition {\n        +UUID id\n        +UUID colisId\n        +UUID prDepartId\n        +UUID prArriveeId\n        +String status\n        +Integer numeroSaut\n        +Date dateDepart\n        +Date dateArrivee\n        +planifierExpedition()\n        +confirmerDepart()\n    }\n    \n    class PointRelais {\n        +UUID id\n        +String name\n        +String address\n        +Float latitude\n        +Float longitude\n        +Integer capacity\n        +Boolean isActive\n        +String contactPhone\n        +calculateDistance(otherPR)\n        +checkCapacity()\n    }\n    \n    class User {\n        +UUID id\n        +String username\n        +String email\n        +String phone\n        +String accountType\n        +Boolean active\n        +Date createdAt\n        +validateCredentials()\n        +updateProfile()\n    }\n    \n    class EventLog {\n        +UUID id\n        +String category\n        +String type\n        +String action\n        +UUID entiteId\n        +String entiteType\n        +UUID acteurId\n        +String acteurType\n        +Date timestamp\n        +String payload\n        +logEvent()\n    }\n    \n    Colis \"1\" --> \"*\" ColisHistorique\n    Colis \"1\" --> \"*\" Expedition\n    Expedition \"*\" --> \"1\" PointRelais : depart\n    Expedition \"*\" --> \"1\" PointRelais : arrivee\n    User \"1\" --> \"*\" Colis : expediteur\n    User \"1\" --> \"*\" Colis : destinataire",
    "packageName": "com.pickndrop"
  }' \
  --output "pickndrop-generated.zip"
```

### 3️⃣ Avec JavaScript/Fetch

```javascript
// Validation UML
async function validateUML() {
    const umlContent = `classDiagram
    class Colis {
        +UUID id
        +String trackingNumber
        +String status
        +Float weight
        +String dimensions
        +Date createdAt
        +updateStatus(newStatus, acteur)
        +calculateFee()
    }
    // ... reste du diagramme
    `;
    
    const response = await fetch('https://codegenerator-cpyh.onrender.com/api/generate/validate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: umlContent
    });
    
    const result = await response.json();
    console.log('Validation:', result);
}

// Génération CRUD
async function generateCRUD() {
    const request = {
        umlContent: `classDiagram
        class Colis {
            +UUID id
            +String trackingNumber
            +String status
            +Float weight
            +String dimensions
            +Date createdAt
            +updateStatus(newStatus, acteur)
            +calculateFee()
        }
        // ... reste du diagramme
        `,
        packageName: 'com.pickndrop'
    };
    
    const response = await fetch('https://codegenerator-cpyh.onrender.com/api/generate/crud', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });
    
    if (response.ok) {
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'pickndrop-generated.zip';
        a.click();
    }
}
```

### 4️⃣ Avec Python

```python
import requests
import json

# URL de l'API
API_URL = "https://codegenerator-cpyh.onrender.com"

# Contenu UML
uml_content = """classDiagram
    class Colis {
        +UUID id
        +String trackingNumber
        +String status
        +Float weight
        +String dimensions
        +Date createdAt
        +updateStatus(newStatus, acteur)
        +calculateFee()
    }
    
    class ColisHistorique {
        +UUID id
        +UUID colisId
        +String ancienStatus
        +String nouveauStatus
        +Date dateChangement
        +UUID acteurId
        +String commentaire
    }
    
    class Expedition {
        +UUID id
        +UUID colisId
        +UUID prDepartId
        +UUID prArriveeId
        +String status
        +Integer numeroSaut
        +Date dateDepart
        +Date dateArrivee
        +planifierExpedition()
        +confirmerDepart()
    }
    
    class PointRelais {
        +UUID id
        +String name
        +String address
        +Float latitude
        +Float longitude
        +Integer capacity
        +Boolean isActive
        +String contactPhone
        +calculateDistance(otherPR)
        +checkCapacity()
    }
    
    class User {
        +UUID id
        +String username
        +String email
        +String phone
        +String accountType
        +Boolean active
        +Date createdAt
        +validateCredentials()
        +updateProfile()
    }
    
    class EventLog {
        +UUID id
        +String category
        +String type
        +String action
        +UUID entiteId
        +String entiteType
        +UUID acteurId
        +String acteurType
        +Date timestamp
        +String payload
        +logEvent()
    }
    
    Colis "1" --> "*" ColisHistorique
    Colis "1" --> "*" Expedition
    Expedition "*" --> "1" PointRelais : depart
    Expedition "*" --> "1" PointRelais : arrivee
    User "1" --> "*" Colis : expediteur
    User "1" --> "*" Colis : destinataire"""

def validate_uml():
    """Valider le diagramme UML"""
    response = requests.post(
        f"{API_URL}/api/generate/validate",
        headers={"Content-Type": "application/json"},
        data=uml_content
    )
    return response.json()

def generate_crud():
    """Générer le code CRUD"""
    payload = {
        "umlContent": uml_content,
        "packageName": "com.pickndrop"
    }
    
    response = requests.post(
        f"{API_URL}/api/generate/crud",
        headers={"Content-Type": "application/json"},
        json=payload
    )
    
    if response.status_code == 200:
        with open("pickndrop-generated.zip", "wb") as f:
            f.write(response.content)
        print("✅ Code généré: pickndrop-generated.zip")
    else:
        print(f"❌ Erreur: {response.status_code}")

# Utilisation
if __name__ == "__main__":
    # Validation
    validation = validate_uml()
    print("Validation:", validation)
    
    # Génération
    generate_crud()
```

## 📦 Structure du projet généré

Le ZIP contient une structure Spring Boot complète :

```
generated/
├── src/main/java/com/pickndrop/
│   ├── entity/
│   │   ├── Colis.java
│   │   ├── ColisHistorique.java
│   │   ├── Expedition.java
│   │   ├── PointRelais.java
│   │   ├── User.java
│   │   └── EventLog.java
│   ├── repository/
│   │   ├── ColisRepository.java
│   │   ├── ColisHistoriqueRepository.java
│   │   ├── ExpeditionRepository.java
│   │   ├── PointRelaisRepository.java
│   │   ├── UserRepository.java
│   │   └── EventLogRepository.java
│   ├── service/
│   │   ├── ColisService.java
│   │   ├── ColisHistoriqueService.java
│   │   ├── ExpeditionService.java
│   │   ├── PointRelaisService.java
│   │   ├── UserService.java
│   │   └── EventLogService.java
│   └── controller/
│       ├── ColisController.java
│       ├── ColisHistoriqueController.java
│       ├── ExpeditionController.java
│       ├── PointRelaisController.java
│       ├── UserController.java
│       └── EventLogController.java
```

## 🚀 Démarrage rapide

1. **Télécharger le code généré**
```bash
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d @request.json \
  --output "pickndrop.zip"
```

2. **Extraire et configurer**
```bash
unzip pickndrop.zip
cd generated/
```

3. **Ajouter les dépendances Spring Boot** (créer pom.xml)

4. **Lancer l'application**
```bash
mvn spring-boot:run
```

## 🔧 Endpoints générés

Chaque entité aura des endpoints REST :

- `GET /api/colis` - Liste tous les colis
- `GET /api/colis/{id}` - Récupère un colis par ID
- `POST /api/colis` - Crée un nouveau colis
- `PUT /api/colis/{id}` - Met à jour un colis
- `DELETE /api/colis/{id}` - Supprime un colis

Et ainsi de suite pour toutes les entités (ColisHistorique, Expedition, PointRelais, User, EventLog).

## 📝 Notes importantes

- L'API génère du code Spring Boot avec JPA
- Les relations UML sont converties en annotations JPA (@OneToMany, @ManyToOne)
- Chaque entité a des champs d'audit (createdAt, updatedAt)
- Les méthodes métier sont générées avec des TODO à implémenter