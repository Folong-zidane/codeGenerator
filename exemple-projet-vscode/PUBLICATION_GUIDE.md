# Guide de Publication d'Articles

## 🚀 Fonctionnalités de Publication

### 1. **Statuts d'Articles**
- `BROUILLON` - Article en cours de rédaction
- `EN_ATTENTE` - Soumis pour validation
- `PROGRAMME` - Publication programmée
- `PUBLIE` - Article publié et visible
- `ARCHIVE` - Article archivé
- `REJETE` - Article rejeté avec motif
- `EN_AVANT_PREMIERE` - Avant-première exclusive

### 2. **Publication Simple**
```http
PATCH /api/v1/articles/{id}/publish
```
Publication immédiate avec date actuelle.

### 3. **Publication Avancée**
```http
POST /api/v1/articles/{id}/publish-advanced
Content-Type: application/json

{
  "datePublication": "2024-12-15T10:00:00",
  "enAvantPremiere": true,
  "dateFinAvantPremiere": "2024-12-14T23:59:59",
  "notifierAbonnes": true,
  "publierReseauxSociaux": false
}
```

### 4. **Programmation de Publication**
```http
PATCH /api/v1/articles/{id}/schedule?datePublication=2024-12-15T10:00:00
```

### 5. **Avant-Première**
```http
PATCH /api/v1/articles/{id}/preview?dateFin=2024-12-14T23:59:59
```

## 📋 Gestion des Articles en Avant

### Mise en Avant d'un Article
```http
POST /api/v1/articles/{id}/feature
Content-Type: application/json

{
  "position": 1,
  "dateDebut": "2024-12-10T00:00:00",
  "dateFin": "2024-12-17T23:59:59",
  "section": "UNE",
  "actif": true
}
```

### Sections Disponibles
- **UNE** - Article principal en une
- **SIDEBAR** - Barre latérale
- **CAROUSEL** - Carrousel d'images

### Récupération des Articles en Avant
```http
GET /api/v1/articles/featured?section=UNE
```

## 🗂️ Gestion des Rubriques/Catégories

### Création de Rubrique
```http
POST /api/v1/rubriques
Content-Type: application/json

{
  "nom": "Culture Africaine",
  "description": "Articles sur la culture africaine",
  "slug": "culture-africaine",
  "parentId": null,
  "couleur": "#FF6B35",
  "icone": "fas fa-drum",
  "ordre": 1,
  "visible": true
}
```

### Hiérarchie des Rubriques
```http
GET /api/v1/rubriques/tree
```

**Réponse:**
```json
[
  {
    "id": 1,
    "nom": "Culture",
    "slug": "culture",
    "enfants": [
      {
        "id": 2,
        "nom": "Culture Africaine",
        "slug": "culture-africaine",
        "parentId": 1
      },
      {
        "id": 3,
        "nom": "Culture Européenne", 
        "slug": "culture-europeenne",
        "parentId": 1
      }
    ]
  }
]
```

### Articles par Rubrique
```http
GET /api/v1/rubriques/{id}/articles
```

## 📊 Endpoints de Consultation

### Articles par Statut
```http
GET /api/v1/articles/by-status/PUBLIE?page=0&size=10
GET /api/v1/articles/by-status/PROGRAMME
GET /api/v1/articles/by-status/EN_AVANT_PREMIERE
```

### Articles Programmés
```http
GET /api/v1/articles/scheduled
```

### Articles Publiés
```http
GET /api/v1/articles?statut=PUBLIE&visible=true
```

## ⚡ Publication Automatique

### Scheduler Intégré
Le système vérifie automatiquement toutes les minutes les articles programmés et les publie à l'heure prévue.

### Fonctionnalités Automatiques
- ✅ Publication des articles programmés
- ✅ Fin automatique des avant-premières
- ✅ Nettoyage des contenus expirés
- ✅ Notifications aux abonnés

## 🔄 Workflow de Publication

### 1. Création → Brouillon
```
Article créé → Statut: BROUILLON
```

### 2. Soumission → En Attente
```
BROUILLON → EN_ATTENTE (validation éditoriale)
```

### 3. Validation → Publication
```
EN_ATTENTE → PUBLIE (publication immédiate)
EN_ATTENTE → PROGRAMME (publication différée)
EN_ATTENTE → EN_AVANT_PREMIERE (accès exclusif)
EN_ATTENTE → REJETE (avec motif)
```

### 4. Gestion Post-Publication
```
PUBLIE → ARCHIVE (archivage)
PUBLIE → EN_AVANT_PREMIERE (remise en avant)
```

## 🎯 Cas d'Usage Avancés

### Lancement Exclusif
1. Créer l'article en `BROUILLON`
2. Le mettre en `EN_AVANT_PREMIERE` 
3. Programmer la publication publique
4. Notifier les abonnés premium

### Série d'Articles
1. Créer plusieurs articles
2. Les programmer à intervalles réguliers
3. Les lier via des tags communs
4. Mise en avant du premier article

### Événement Spécial
1. Créer une rubrique dédiée
2. Programmer plusieurs articles
3. Mettre en avant via le carrousel
4. Activation simultanée

## 📈 Métriques et Suivi

Chaque article publié génère automatiquement :
- Statistiques de vues
- Métriques d'engagement
- Historique des modifications
- Logs de publication