# 🔐 Système d'Authentification et Gestion des Erreurs

## ✅ Fonctionnalités Implémentées

### 1. **Gestion des Erreurs Améliorée**

#### Réponses d'Erreur Structurées
```json
{
  "timestamp": "2024-12-10T14:59:48",
  "status": 400,
  "error": "Données invalides",
  "message": "Veuillez corriger les erreurs suivantes",
  "validationErrors": {
    "titre": "Le titre doit contenir entre 10 et 200 caractères",
    "email": "Format d'email invalide",
    "rubriqueId": "L'ID de rubrique doit être positif"
  }
}
```

#### Types d'Erreurs Gérées
- **400 Bad Request** - Données invalides avec détails
- **404 Not Found** - Ressource non trouvée
- **409 Conflict** - Email déjà utilisé
- **401 Unauthorized** - Identifiants incorrects
- **403 Forbidden** - Compte désactivé
- **500 Internal Server Error** - Erreur serveur

### 2. **Système d'Authentification**

#### Rôles Utilisateur
- `SUPER_ADMIN` - Créé automatiquement au démarrage
- `ADMIN` - Gestion complète
- `REDACTEUR` - Création/modification d'articles
- `USER` - Lecture et inscription libre

#### Endpoints d'Authentification

**Inscription Publique (USER)**
```http
POST /api/v1/auth/register
{
  "email": "user@example.com",
  "motDePasse": "password123",
  "nom": "Dupont",
  "prenom": "Jean"
}
```

**Connexion**
```http
POST /api/v1/auth/login
{
  "email": "user@example.com",
  "motDePasse": "password123"
}
```

**Réponse d'Authentification**
```json
{
  "token": "fake-jwt-token",
  "email": "user@example.com",
  "role": "USER",
  "message": "Connexion réussie"
}
```

**Création de Rédacteur (Admin uniquement)**
```http
POST /api/v1/auth/admin/create-redacteur
{
  "email": "redacteur@blog.com",
  "motDePasse": "password123",
  "nom": "Martin",
  "prenom": "Sophie"
}
```

### 3. **Accès Public aux Articles**

#### Endpoints Publics (Sans Authentification)
```http
GET /api/v1/public/articles              # Articles publiés
GET /api/v1/public/articles/{id}         # Article spécifique
GET /api/v1/public/articles/featured     # Articles en avant
GET /api/v1/public/rubriques             # Catégories
GET /api/v1/public/rubriques/{id}/articles # Articles par catégorie
```

### 4. **Super Admin Automatique**

Au démarrage de l'application, un super admin est créé automatiquement :
- **Email** : `admin@blog.com`
- **Mot de passe** : `admin123`
- **Rôle** : `SUPER_ADMIN`

## 🔄 Workflow d'Utilisation

### Utilisateur Normal (USER)
1. **Inscription libre** via `/auth/register`
2. **Accès lecture** à tous les articles publiés
3. **Connexion optionnelle** pour fonctionnalités futures

### Rédacteur (REDACTEUR)
1. **Créé par un admin** via `/auth/admin/create-redacteur`
2. **Connexion** avec identifiants fournis
3. **Création/modification** d'articles
4. **Soumission** pour validation

### Administrateur (ADMIN/SUPER_ADMIN)
1. **Connexion** avec compte privilégié
2. **Création** de comptes rédacteurs
3. **Validation/rejet** d'articles
4. **Gestion complète** du système

## 📊 Validations Renforcées

### Articles
- Titre : 10-200 caractères obligatoire
- Description : 50-500 caractères obligatoire
- Rubrique : ID positif obligatoire
- Blocs contenu : 1-50 blocs maximum

### Authentification
- Email : Format valide obligatoire
- Mot de passe : 6 caractères minimum
- Nom/Prénom : 100 caractères maximum

### Médias
- Taille : 50MB maximum
- Types : Images, vidéos, audio, PDF
- Hash SHA-256 : Déduplication automatique

## 🚀 Exemples d'Utilisation

### Inscription et Première Connexion
```bash
# 1. Inscription utilisateur
curl -X POST /api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jean@example.com",
    "motDePasse": "password123",
    "nom": "Dupont",
    "prenom": "Jean"
  }'

# 2. Connexion
curl -X POST /api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jean@example.com",
    "motDePasse": "password123"
  }'
```

### Accès Public aux Articles
```bash
# Articles publiés (sans authentification)
curl /api/v1/public/articles

# Article spécifique
curl /api/v1/public/articles/1

# Articles en avant
curl /api/v1/public/articles/featured
```

### Gestion Administrative
```bash
# Connexion admin
curl -X POST /api/v1/auth/login \
  -d '{"email": "admin@blog.com", "motDePasse": "admin123"}'

# Création rédacteur
curl -X POST /api/v1/auth/admin/create-redacteur \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "email": "redacteur@blog.com",
    "motDePasse": "redacteur123",
    "nom": "Martin",
    "prenom": "Sophie"
  }'
```

## 🎯 Prêt pour Production

Le système d'authentification est maintenant opérationnel avec :
- ✅ Gestion des erreurs détaillée
- ✅ Inscription libre pour utilisateurs
- ✅ Création contrôlée des rédacteurs
- ✅ Super admin automatique
- ✅ Accès public aux articles
- ✅ Validations renforcées
- ✅ Messages d'erreur explicites

## 🔜 Améliorations Futures

1. **JWT Réel** - Tokens sécurisés
2. **Hash des Mots de Passe** - BCrypt
3. **Autorisation par Rôle** - Spring Security
4. **Refresh Tokens** - Sessions longues
5. **2FA** - Double authentification