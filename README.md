# 🚀 Générateur UML vers Code - Production Ready

Transformez vos diagrammes UML en applications CRUD complètes et fonctionnelles en quelques secondes.

## 🌟 Fonctionnalités

- **6 langages supportés** : Java, Python (FastAPI/Django), C#, TypeScript, PHP
- **Applications 100% fonctionnelles** : Base de données, API REST, documentation incluses
- **Architecture MVC complète** : Entités, Repositories, Services, Controllers
- **Déploiement immédiat** : Applications prêtes pour la production

## 🌐 API Déployée - Prête à l'Emploi

- **URL Production** : https://codegenerator-cpyh.onrender.com
- **Documentation Live** : https://codegenerator-cpyh.onrender.com/docs
- **Guide d'utilisation** : [API-USAGE-DEPLOYED.md](API-USAGE-DEPLOYED.md)
- **Aucune installation locale requise** ✅

## ⚡ Utilisation Rapide - API Déployée

### 1. Génération Directe avec cURL
```bash
# Générer un projet Java Spring Boot complet
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/crud" \
  -H "Content-Type: application/json" \
  -d '{
    "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n        +String email\n        +validateEmail()\n    }\n    class Product {\n        +UUID id\n        +String name\n        +Float price\n    }\n    User \"1\" --> \"*\" Product",
    "packageName": "com.ecommerce",
    "language": "java"
  }' \
  -o ecommerce-java.zip

# Extraire et démarrer
unzip ecommerce-java.zip
cd ecommerce-java
./start.sh  # Application prête !
```

### 2. Avec Insomnia/Postman
```json
POST https://codegenerator-cpyh.onrender.com/api/generate/crud
Content-Type: application/json

{
  "umlContent": "classDiagram\n    class User {\n        +UUID id\n        +String username\n    }\n    class Order {\n        +UUID id\n        +UUID userId\n        +Float total\n    }\n    User \"1\" --> \"*\" Order",
  "language": "django",
  "packageName": "com.blog"
}
```

## 🎯 Langages Supportés

| Langage | Framework | Status | Exemple |
|---------|-----------|--------|---------|
| **Java** | Spring Boot | ✅ Complet | `./generate-from-mermaid.sh app.mermaid java` |
| **Python** | FastAPI | ✅ Complet | `./generate-from-mermaid.sh app.mermaid python` |
| **Python** | Django REST | ✅ Complet | `./generate-from-mermaid.sh app.mermaid django` |
| **C#** | .NET Core | ✅ Complet | `./generate-from-mermaid.sh app.mermaid csharp` |
| **TypeScript** | Express + TypeORM | ✅ Complet | `./generate-from-mermaid.sh app.mermaid typescript` |
| **PHP** | Slim + Eloquent | ✅ Complet | `./generate-from-mermaid.sh app.mermaid php` |

## 📋 Ce qui est Généré

### Pour Chaque Langage
- ✅ **Entités/Modèles** avec annotations ORM
- ✅ **Repositories** avec CRUD complet
- ✅ **Services** avec logique métier
- ✅ **Controllers** avec endpoints REST
- ✅ **Configuration** base de données
- ✅ **Documentation** API (Swagger)
- ✅ **Point d'entrée** application
- ✅ **Dépendances** et configuration

### Endpoints REST Automatiques
```
GET    /api/users/        # Liste avec pagination
POST   /api/users/        # Création
GET    /api/users/{id}/   # Détail
PUT    /api/users/{id}/   # Modification
DELETE /api/users/{id}/   # Suppression
```

## 🌐 API Live - Aucune Installation

### Accès Direct
- **API Production** : https://codegenerator-cpyh.onrender.com
- **Documentation** : https://codegenerator-cpyh.onrender.com/docs
- **Exemples** : https://codegenerator-cpyh.onrender.com/examples
- **Santé API** : https://codegenerator-cpyh.onrender.com/actuator/health

### Test Rapide
```bash
# Vérifier que l'API fonctionne
curl https://codegenerator-cpyh.onrender.com/actuator/health

# Valider un diagramme UML
curl -X POST https://codegenerator-cpyh.onrender.com/api/generate/validate \
  -H "Content-Type: text/plain" \
  -d "classDiagram\n    class User {\n        +UUID id\n    }"
```

## 📚 Documentation

- **[Documentation Complète](API-DOCUMENTATION-COMPLETE.md)** - Guide complet d'utilisation
- **[Améliorations Django](DJANGO-AMELIORE.md)** - Détails sur le générateur Django
- **[Langages Complétés](LANGAGES-COMPLETES.md)** - Support multi-langages
- **[Corrections Appliquées](CORRECTIONS-APPLIQUEES.md)** - Historique des améliorations

## 🎯 Exemples d'Applications

### E-commerce
```mermaid
classDiagram
    class User {
        +String username
        +String email
    }
    class Product {
        +String name
        +Float price
    }
    class Order {
        +String userId
        +String productId
        +Integer quantity
    }
    User "1" --> "*" Order
    Product "1" --> "*" Order
```

### Blog
```mermaid
classDiagram
    class Author {
        +String name
        +String email
    }
    class Post {
        +String title
        +String content
        +String authorId
    }
    Author "1" --> "*" Post
```

## 🚀 Déploiement

L'application est déployée sur Render et accessible à l'adresse :
**https://codegenerator-cpyh.onrender.com**

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature
3. Commit les changements
4. Push vers la branche
5. Ouvrir une Pull Request

## 📄 Licence

Ce projet est sous licence MIT.