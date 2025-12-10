# Flux de Création d'Articles avec Médias

## 1. Structure de la Requête de Création

### Étape 1: Upload des médias (images/vidéos)
```http
POST /api/v1/media/upload
Content-Type: multipart/form-data

{
  "file": [fichier binaire],
  "legende": "Description de l'image",
  "altText": "Texte alternatif pour l'accessibilité"
}
```

**Réponse:**
```json
{
  "id": 123,
  "nomOriginal": "photo-culture.jpg",
  "typeMedia": "IMAGE",
  "urlAcces": "/uploads/abc123def456.jpg",
  "hashSha256": "abc123def456...",
  "dateCreation": "2024-12-10T01:45:00"
}
```

### Étape 2: Création de l'article avec références aux médias
```http
POST /api/v1/articles
Content-Type: application/json

{
  "titre": "Découverte de la Culture Marocaine",
  "description": "Un voyage fascinant à travers les traditions et coutumes du Maroc",
  "rubriqueId": 5,
  "auteurId": 12,
  "imageCouvertureId": 123,
  "blocsContenu": [
    {
      "typeBloc": "TEXTE",
      "contenuMarkdown": "# Introduction\n\nLe Maroc est un pays riche en traditions...",
      "ordre": 1
    },
    {
      "typeBloc": "IMAGE",
      "mediaFileId": 123,
      "legende": "Marché traditionnel de Marrakech",
      "altText": "Vue d'ensemble d'un souk coloré",
      "position": "center",
      "ordre": 2
    },
    {
      "typeBloc": "TEXTE",
      "contenuMarkdown": "## Les souks traditionnels\n\nLes marchés marocains...",
      "ordre": 3
    },
    {
      "typeBloc": "VIDEO",
      "mediaFileId": 124,
      "legende": "Artisan travaillant le cuir",
      "position": "center",
      "ordre": 4
    },
    {
      "typeBloc": "GALERIE",
      "mediaFileId": 125,
      "legende": "Collection d'artisanat local",
      "ordre": 5
    }
  ],
  "tagIds": [1, 5, 8],
  "statut": "BROUILLON",
  "visible": true,
  "region": "AFRIQUE_NORD"
}
```

## 2. Flux Complet avec Frontend

### Étape A: Interface d'upload
```javascript
// 1. Upload des fichiers un par un
const uploadMedia = async (file, legende, altText) => {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('legende', legende);
  formData.append('altText', altText);
  
  const response = await fetch('/api/v1/media/upload', {
    method: 'POST',
    body: formData
  });
  
  return response.json(); // Retourne {id, urlAcces, ...}
};
```

### Étape B: Construction de l'article
```javascript
// 2. Construire l'article avec les IDs des médias
const createArticle = async (articleData) => {
  const response = await fetch('/api/v1/articles', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(articleData)
  });
  
  return response.json();
};

// 3. Exemple d'utilisation complète
const handleArticleCreation = async () => {
  // Upload image de couverture
  const coverImage = await uploadMedia(
    coverFile, 
    "Image de couverture", 
    "Paysage marocain"
  );
  
  // Upload autres médias
  const galleryImages = await Promise.all(
    galleryFiles.map(file => uploadMedia(file, file.name, ""))
  );
  
  // Créer l'article
  const article = await createArticle({
    titre: "Mon Article",
    description: "Description...",
    rubriqueId: 1,
    imageCouvertureId: coverImage.id,
    blocsContenu: [
      {
        typeBloc: "TEXTE",
        contenuMarkdown: "# Introduction...",
        ordre: 1
      },
      {
        typeBloc: "IMAGE",
        mediaFileId: galleryImages[0].id,
        legende: "Première image",
        position: "center",
        ordre: 2
      }
    ]
  });
};
```

## 3. Types de Blocs Supportés

### TEXTE
```json
{
  "typeBloc": "TEXTE",
  "contenuMarkdown": "## Titre\n\nContenu en **markdown**",
  "ordre": 1
}
```

### IMAGE
```json
{
  "typeBloc": "IMAGE",
  "mediaFileId": 123,
  "legende": "Description de l'image",
  "altText": "Texte alternatif",
  "position": "center|left|right",
  "ordre": 2
}
```

### VIDEO
```json
{
  "typeBloc": "VIDEO",
  "mediaFileId": 124,
  "legende": "Description de la vidéo",
  "ordre": 3
}
```

### GALERIE
```json
{
  "typeBloc": "GALERIE",
  "mediaFileId": 125, // ID du premier média de la galerie
  "legende": "Collection d'images",
  "ordre": 4
}
```

### AUDIO
```json
{
  "typeBloc": "AUDIO",
  "mediaFileId": 126,
  "legende": "Enregistrement audio",
  "ordre": 5
}
```

### PDF
```json
{
  "typeBloc": "PDF",
  "mediaFileId": 127,
  "legende": "Document PDF",
  "ordre": 6
}
```

## 4. Gestion des Erreurs

### Validation des médias
- Taille max: 50MB
- Types supportés: images (JPEG, PNG, GIF, WebP), vidéos (MP4, AVI, MOV), audio (MP3, WAV, OGG), documents (PDF, DOC)
- Détection de doublons par hash SHA-256

### Validation de l'article
- Titre: 10-200 caractères
- Description: 50-500 caractères
- Au moins un bloc de contenu
- Rubrique existante
- Médias référencés doivent exister

## 5. Optimisations

### Variantes d'images automatiques
Lors de l'upload d'une image, le système génère automatiquement:
- Thumbnail: 150px de largeur
- Medium: 400px de largeur  
- Large: 800px de largeur

### Déduplication
Les fichiers identiques (même hash SHA-256) ne sont stockés qu'une seule fois.

### URLs d'accès
Chaque média a une URL publique accessible: `/uploads/{hash}.{extension}`