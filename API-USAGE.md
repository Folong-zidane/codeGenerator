# 🚀 Comment Utiliser l'API UML Code Generator

## 📡 URL de Déploiement
```
https://your-app-name.onrender.com
```

## 🔧 Endpoints Disponibles

### 1. **Générer du Code CRUD**
```http
POST /api/generate/crud
Content-Type: application/json

{
  "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n    }",
  "packageName": "com.example"
}
```

**Réponse** : Fichier ZIP contenant le code généré

### 2. **Valider un Diagramme UML**
```http
POST /api/generate/validate
Content-Type: text/plain

classDiagram
    class User {
        +UUID id
        +String username
        +String email
    }
```

**Réponse** :
```json
{
  "valid": true,
  "message": "Valid UML",
  "classCount": 1
}
```

## 💻 Utilisation dans votre Code

### **JavaScript/TypeScript**
```javascript
// Générer du code
async function generateCode(umlContent, packageName) {
  const response = await fetch('https://your-app.onrender.com/api/generate/crud', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      umlContent: umlContent,
      packageName: packageName
    })
  });
  
  if (response.ok) {
    const blob = await response.blob();
    // Télécharger le fichier ZIP
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'generated-code.zip';
    a.click();
  }
}

// Valider UML
async function validateUML(umlContent) {
  const response = await fetch('https://your-app.onrender.com/api/generate/validate', {
    method: 'POST',
    headers: {
      'Content-Type': 'text/plain',
    },
    body: umlContent
  });
  
  const result = await response.json();
  console.log('Validation:', result);
  return result.valid;
}
```

### **Python**
```python
import requests
import io
import zipfile

def generate_code(uml_content, package_name):
    url = "https://your-app.onrender.com/api/generate/crud"
    payload = {
        "umlContent": uml_content,
        "packageName": package_name
    }
    
    response = requests.post(url, json=payload)
    
    if response.status_code == 200:
        # Sauvegarder le ZIP
        with open("generated-code.zip", "wb") as f:
            f.write(response.content)
        
        # Ou extraire directement
        with zipfile.ZipFile(io.BytesIO(response.content)) as zip_file:
            zip_file.extractall("./generated")
        
        return True
    return False

def validate_uml(uml_content):
    url = "https://your-app.onrender.com/api/generate/validate"
    response = requests.post(url, data=uml_content, 
                           headers={'Content-Type': 'text/plain'})
    return response.json()
```

### **Java**
```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UMLGenerator {
    private static final String BASE_URL = "https://your-app.onrender.com";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    
    public byte[] generateCode(String umlContent, String packageName) throws Exception {
        var request = Map.of(
            "umlContent", umlContent,
            "packageName", packageName
        );
        
        var httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/generate/crud"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(request)))
            .build();
        
        var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
        return response.body();
    }
    
    public ValidationResult validateUML(String umlContent) throws Exception {
        var httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/generate/validate"))
            .header("Content-Type", "text/plain")
            .POST(HttpRequest.BodyPublishers.ofString(umlContent))
            .build();
        
        var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), ValidationResult.class);
    }
}
```

### **cURL**
```bash
# Générer du code
curl -X POST https://your-app.onrender.com/api/generate/crud \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }",
    "packageName": "com.example"
  }' \
  --output generated-code.zip

# Valider UML
curl -X POST https://your-app.onrender.com/api/generate/validate \
  -H "Content-Type: text/plain" \
  -d "classDiagram
    class User {
        +UUID id
        +String username
    }"
```

## 📋 Exemple Complet d'Utilisation

### **Frontend React**
```jsx
import React, { useState } from 'react';

function UMLGenerator() {
  const [umlContent, setUmlContent] = useState('');
  const [packageName, setPackageName] = useState('com.example');
  const [isValid, setIsValid] = useState(null);

  const validateUML = async () => {
    try {
      const response = await fetch('https://your-app.onrender.com/api/generate/validate', {
        method: 'POST',
        headers: { 'Content-Type': 'text/plain' },
        body: umlContent
      });
      const result = await response.json();
      setIsValid(result.valid);
    } catch (error) {
      setIsValid(false);
    }
  };

  const generateCode = async () => {
    try {
      const response = await fetch('https://your-app.onrender.com/api/generate/crud', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ umlContent, packageName })
      });
      
      if (response.ok) {
        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'generated-code.zip';
        a.click();
      }
    } catch (error) {
      console.error('Generation failed:', error);
    }
  };

  return (
    <div>
      <textarea 
        value={umlContent}
        onChange={(e) => setUmlContent(e.target.value)}
        placeholder="Entrez votre diagramme UML Mermaid..."
      />
      <input 
        value={packageName}
        onChange={(e) => setPackageName(e.target.value)}
        placeholder="Package name"
      />
      <button onClick={validateUML}>Valider UML</button>
      <button onClick={generateCode} disabled={!isValid}>Générer Code</button>
      {isValid !== null && (
        <p>{isValid ? '✅ UML Valide' : '❌ UML Invalide'}</p>
      )}
    </div>
  );
}
```

## 🔧 Déploiement sur Render

1. **Push vers GitHub**
2. **Connecter à Render**
3. **Utiliser le fichier `render.yaml`**
4. **L'API sera disponible sur votre URL Render**

## 📊 Structure du Code Généré

Le ZIP contient :
```
src/main/java/com/example/
├── entity/
│   ├── User.java
│   └── Product.java
├── repository/
│   ├── UserRepository.java
│   └── ProductRepository.java
├── service/
│   ├── UserService.java
│   └── ProductService.java
└── controller/
    ├── UserController.java
    └── ProductController.java
```

Chaque fichier contient :
- **Annotations JPA complètes**
- **Validation Bean Validation**
- **Documentation Swagger**
- **Champs d'audit automatiques**
- **Méthodes CRUD optimisées**