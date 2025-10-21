# 🚀 Déploiement sur Render

## 📋 Étapes de Déploiement

### 1. **Préparer le Repository**
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/username/uml-code-generator.git
git push -u origin main
```

### 2. **Déployer sur Render**
1. Aller sur [render.com](https://render.com)
2. Connecter votre compte GitHub
3. Créer un nouveau "Web Service"
4. Sélectionner votre repository
5. Render détectera automatiquement le `render.yaml`

### 3. **Configuration Automatique**
Le fichier `render.yaml` configure :
- **Type** : Web Service Docker
- **Plan** : Free (gratuit)
- **Port** : 8080
- **Build** : Dockerfile

### 4. **URL d'Accès**
Votre API sera disponible sur :
```
https://your-app-name.onrender.com
```

## 🔧 Utilisation de l'API

### **Endpoint Principal**
```http
POST /api/generate/crud
Content-Type: application/json

{
  "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n    }",
  "packageName": "com.example"
}
```

### **Réponse**
- Fichier ZIP contenant le code Spring Boot généré
- Entités JPA avec annotations complètes
- Repositories, Services, Controllers
- Documentation Swagger intégrée

## 💡 Exemples d'Intégration

### **Dans une App React**
```javascript
const generateCode = async (umlDiagram) => {
  const response = await fetch('https://your-app.onrender.com/api/generate/crud', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      umlContent: umlDiagram,
      packageName: 'com.myproject'
    })
  });
  
  const blob = await response.blob();
  // Télécharger automatiquement
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'generated-spring-boot.zip';
  a.click();
};
```

### **Dans un Backend Node.js**
```javascript
const axios = require('axios');
const fs = require('fs');

async function generateSpringBootCode(umlContent) {
  try {
    const response = await axios.post('https://your-app.onrender.com/api/generate/crud', {
      umlContent: umlContent,
      packageName: 'com.generated'
    }, {
      responseType: 'arraybuffer'
    });
    
    fs.writeFileSync('generated-code.zip', response.data);
    console.log('Code généré avec succès !');
  } catch (error) {
    console.error('Erreur:', error.message);
  }
}
```

## 🎯 Avantages du Déploiement

1. **Gratuit** - Plan free de Render
2. **Automatique** - Build et deploy automatiques
3. **Scalable** - Peut gérer plusieurs requêtes
4. **HTTPS** - SSL automatique
5. **Monitoring** - Logs et métriques intégrés

## 📊 Monitoring

Render fournit :
- **Logs en temps réel**
- **Métriques de performance**
- **Alertes automatiques**
- **Redémarrage automatique**

Votre générateur de code UML est maintenant **production-ready** ! 🎉