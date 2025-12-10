# Guide de Déploiement - Blog Application

## ✅ Corrections Effectuées

### 1. **IDs en UUID** 
- ✅ MediaFile et MediaVariant utilisent maintenant des UUID au lieu d'Integer
- ✅ Tous les services, contrôleurs et repositories mis à jour
- ✅ EntityNotFoundException accepte maintenant Object au lieu de Long

### 2. **Swagger/OpenAPI Fonctionnel**
- ✅ Remplacement de Springfox par SpringDoc OpenAPI 3
- ✅ Configuration SwaggerConfig mise à jour
- ✅ Annotations des contrôleurs converties vers OpenAPI 3
- ✅ URLs d'accès : 
  - Swagger UI : `http://localhost:8081/swagger-ui/index.html`
  - API Docs : `http://localhost:8081/api-docs`

### 3. **Configuration PostgreSQL pour Render**
- ✅ Profil de production créé : `application-prod.properties`
- ✅ Variables d'environnement configurées
- ✅ Fichier `render.yaml` pour le déploiement automatique

## 🚀 Démarrage Local

```bash
# Port 8081 pour éviter les conflits
mvn spring-boot:run

# Accès Swagger UI
http://localhost:8081/swagger-ui/index.html

# Console H2 (développement)
http://localhost:8081/h2-console
```

## 🌐 Déploiement sur Render

### Configuration Base de Données
```
URL: jdbc:postgresql://dpg-d4sclfk9c44c73ejfom0-a.virginia-postgres.render.com:5432/folongzidane
Username: folongzidane
Password: p0rQM1eHDTwj3oGUKcDVez78f1YbZfbi
Port: 5432
```

### Variables d'Environnement Render
```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://dpg-d4sclfk9c44c73ejfom0-a.virginia-postgres.render.com:5432/folongzidane
DB_USERNAME=folongzidane
DB_PASSWORD=p0rQM1eHDTwj3oGUKcDVez78f1YbZfbi
```

### Commandes de Build
```bash
# Build
mvn clean package -DskipTests

# Start
java -jar target/blog-application-1.0.0.jar --spring.profiles.active=prod
```

## 📝 Structure de Création d'Articles avec Médias

### Étape 1: Upload Média
```http
POST /api/v1/media/upload
Content-Type: multipart/form-data

{
  "file": [fichier binaire],
  "legende": "Description",
  "altText": "Texte alternatif"
}
```

**Réponse avec UUID:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "nomOriginal": "photo.jpg",
  "typeMedia": "IMAGE",
  "urlAcces": "/uploads/abc123def456.jpg",
  "hashSha256": "abc123def456...",
  "dateCreation": "2024-12-10T13:51:00"
}
```

### Étape 2: Création Article
```http
POST /api/v1/articles
Content-Type: application/json

{
  "titre": "Mon Article",
  "description": "Description de l'article",
  "rubriqueId": 1,
  "imageCouvertureId": "550e8400-e29b-41d4-a716-446655440000",
  "blocsContenu": [
    {
      "typeBloc": "TEXTE",
      "contenuMarkdown": "# Introduction\n\nContenu...",
      "ordre": 1
    },
    {
      "typeBloc": "IMAGE",
      "mediaFileId": "550e8400-e29b-41d4-a716-446655440000",
      "legende": "Image descriptive",
      "position": "center",
      "ordre": 2
    }
  ]
}
```

## 🔧 Types de Blocs Supportés

- **TEXTE** : Contenu Markdown
- **IMAGE** : Référence UUID vers MediaFile
- **VIDEO** : Référence UUID vers MediaFile vidéo
- **GALERIE** : Collection d'images
- **AUDIO** : Fichiers audio
- **PDF** : Documents PDF

## 🛡️ Sécurité et Validation

### Upload de Fichiers
- Taille max : 50MB
- Types supportés : JPEG, PNG, GIF, WebP, MP4, AVI, MOV, MP3, WAV, OGG, PDF
- Déduplication par hash SHA-256
- Génération automatique de variantes d'images (150px, 400px, 800px)

### Validation Articles
- Titre : 10-200 caractères
- Description : 50-500 caractères
- Au moins un bloc de contenu requis
- Vérification existence des médias référencés

## 📊 Endpoints Principaux

### Médias
- `POST /api/v1/media/upload` - Upload fichier
- `GET /api/v1/media/{uuid}` - Détails média
- `GET /api/v1/media/hash/{hash}` - Vérification existence

### Articles
- `GET /api/v1/articles` - Liste paginée
- `POST /api/v1/articles` - Création
- `GET /api/v1/articles/{id}` - Détails
- `PUT /api/v1/articles/{id}` - Mise à jour
- `PATCH /api/v1/articles/{id}/publish` - Publication

## 🎯 Prêt pour Production

L'application est maintenant prête pour :
- ✅ Déploiement sur Render avec PostgreSQL
- ✅ Gestion complète des médias avec UUID
- ✅ Documentation API via Swagger
- ✅ Création d'articles modulaires
- ✅ Déduplication automatique des fichiers
- ✅ Génération de variantes d'images