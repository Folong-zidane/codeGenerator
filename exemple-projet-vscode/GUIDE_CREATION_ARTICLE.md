# Guide de Création d'Articles - Plateforme Interculturelle Africaine

## ✅ Application Démarrée avec Succès

L'application est maintenant accessible sur: **http://localhost:8080**

## 📚 Documentation Swagger

Accédez à la documentation interactive Swagger UI:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs JSON**: http://localhost:8080/v2/api-docs

## 🎯 Flow Complet de Création d'Article

### 1. Prérequis - Créer une Rubrique

```bash
POST http://localhost:8080/api/v1/rubriques
Content-Type: application/json

{
  "nom": "Culture",
  "description": "Articles sur la culture africaine",
  "slug": "culture",
  "visible": true
}
```

### 2. Créer un Article avec Blocs de Contenu

```bash
POST http://localhost:8080/api/v1/articles
Content-Type: application/json

{
  "titre": "Festival Gnaoua 2025 - Patrimoine Musical Marocain",
  "description": "Découvrez l'héritage musical ancestral du Festival Gnaoua d'Essaouira, célébration unique de la culture afro-marocaine qui attire des milliers de visiteurs chaque année.",
  "rubriqueId": 1,
  "statut": "BROUILLON",
  "visible": true,
  "region": "Afrique du Nord",
  "blocsContenu": [
    {
      "typeBloc": "TEXTE",
      "contenuTexte": "Le Festival Gnaoua et Musiques du Monde d'Essaouira est l'un des événements culturels les plus importants d'Afrique.",
      "ordre": 1
    },
    {
      "typeBloc": "IMAGE",
      "legende": "Musiciens Gnaoua en performance",
      "altText": "Groupe de musiciens Gnaoua traditionnels",
      "position": "center",
      "ordre": 2
    },
    {
      "typeBloc": "VIDEO",
      "embedUrl": "https://youtube.com/watch?v=example",
      "legende": "Documentaire Festival Gnaoua 2024",
      "ordre": 3
    },
    {
      "typeBloc": "CITATION",
      "contenuTexte": "La musique Gnaoua est l'âme de l'Afrique qui bat au cœur du Maroc",
      "ordre": 4
    }
  ],
  "tagIds": []
}
```

### 3. Réponse Attendue

```json
{
  "id": 1,
  "titre": "Festival Gnaoua 2025 - Patrimoine Musical Marocain",
  "slug": "festival-gnaoua-2025-patrimoine-musical-marocain",
  "description": "Découvrez l'héritage musical ancestral...",
  "rubriqueNom": "Culture",
  "statut": "BROUILLON",
  "dateCreation": "2025-12-10T00:32:00",
  "region": "Afrique du Nord",
  "vues": null,
  "telechargements": null,
  "partages": null
}
```

### 4. Publier l'Article

```bash
PATCH http://localhost:8080/api/v1/articles/1/publish
```

### 5. Récupérer l'Article

```bash
GET http://localhost:8080/api/v1/articles/1
```

### 6. Lister Tous les Articles

```bash
GET http://localhost:8080/api/v1/articles?page=0&size=20
```

## 📋 Types de Blocs Disponibles

| Type | Description | Champs Requis |
|------|-------------|---------------|
| **TEXTE** | Contenu textuel riche | `contenuTexte` ou `contenuMarkdown` |
| **IMAGE** | Image avec légende | `mediaFileId`, `legende`, `altText`, `position` |
| **GALERIE** | Carousel d'images | `mediaFileId` (multiple) |
| **VIDEO** | Vidéo embed ou upload | `embedUrl` ou `mediaFileId` |
| **PDF** | Document PDF | `mediaFileId` |
| **AUDIO** | Fichier audio | `mediaFileId` |
| **CODE** | Code avec coloration syntaxique | `contenuTexte`, `codeLanguage` |
| **CITATION** | Citation stylisée | `contenuTexte` |

## 🌍 Régions Disponibles

- Cameroun
- Afrique Ouest
- Afrique Est
- Afrique du Nord
- Afrique Australe
- Afrique Centrale

## 📊 Statuts d'Article

- **BROUILLON**: Article en cours de rédaction
- **PUBLIE**: Article publié et visible
- **ARCHIVE**: Article archivé

## 🔧 Endpoints Disponibles

### Articles
- `GET /api/v1/articles` - Liste paginée
- `GET /api/v1/articles/all` - Liste complète
- `GET /api/v1/articles/{id}` - Détails article
- `POST /api/v1/articles` - Créer article
- `PUT /api/v1/articles/{id}` - Modifier article
- `DELETE /api/v1/articles/{id}` - Supprimer article
- `PATCH /api/v1/articles/{id}/publish` - Publier article

### Rubriques
- `GET /api/v1/rubriques` - Liste rubriques
- `POST /api/v1/rubriques` - Créer rubrique

### Tags
- `GET /api/v1/tags` - Liste tags
- `POST /api/v1/tags` - Créer tag

## 🎨 Exemple Complet avec Swagger

1. Ouvrez http://localhost:8080/swagger-ui.html
2. Naviguez vers **Articles**
3. Cliquez sur `POST /api/v1/articles`
4. Cliquez sur **Try it out**
5. Collez le JSON d'exemple ci-dessus
6. Cliquez sur **Execute**
7. Vérifiez la réponse 201 Created

## 🗄️ Base de Données H2

Console H2: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:blogdb`
- Username: `sa`
- Password: (vide)

## ✨ Fonctionnalités Implémentées

✅ Création d'articles avec blocs modulaires  
✅ Support multi-types de contenu (texte, image, vidéo, PDF, audio, code, citation)  
✅ Génération automatique de slug  
✅ Gestion des statuts (brouillon, publié, archivé)  
✅ Association rubrique obligatoire  
✅ Support régions africaines  
✅ Statistiques automatiques (vues, téléchargements, partages)  
✅ Documentation Swagger interactive  
✅ Validation des données  
✅ Gestion d'erreurs (404, 400)  

## 🚀 Prochaines Étapes

- Upload de médias (images, vidéos, PDF)
- Système de tags
- Recherche full-text
- Filtres avancés
- Système de commentaires
- Analytics détaillés
