# ✅ Vérification Phase 1 - Extension v1.1.0

## 🎯 Checklist de Vérification

### Installation
```bash
cd /home/folongzidane/Documents/Projet/basicCode/vscode-extension
code --install-extension basiccode-generator-1.1.0.vsix
```

---

## 📋 Tests à Effectuer

### Test 1 : Barre d'État ⭐
**Objectif** : Vérifier que le bouton est visible

**Étapes** :
1. Ouvrir VSCode
2. Regarder en bas à gauche
3. Chercher le bouton "🚀 Generate"

**Résultat attendu** :
```
✅ Bouton visible
✅ Tooltip "Generate project from UML diagrams (Ctrl+Shift+G)"
✅ Cliquable
```

---

### Test 2 : Output Channel ⭐
**Objectif** : Vérifier les logs détaillés

**Étapes** :
1. Ouvrir VSCode
2. View → Output (ou Ctrl+Shift+U)
3. Sélectionner "basicCode Generator" dans le dropdown
4. Cliquer sur "🚀 Generate"

**Résultat attendu** :
```
✅ Channel "basicCode Generator" visible
✅ Logs avec timestamps
✅ Icônes (ℹ️, ⚠️, ❌)
✅ Messages détaillés
```

**Exemple de logs** :
```
[2025-01-15T10:30:15.123Z] ℹ️ Extension basicCode Generator activated
[2025-01-15T10:30:20.456Z] ℹ️ Starting generation...
[2025-01-15T10:30:20.789Z] ℹ️ Starting validation...
```

---

### Test 3 : Messages Contextuels ⭐
**Objectif** : Vérifier les messages avec actions

**Étapes** :
1. Configurer un backend invalide :
   - Ctrl+, (Settings)
   - Chercher "basiccode.backend"
   - Mettre "http://invalid-backend.com"
2. Cliquer sur "🚀 Generate"
3. Attendre le message d'erreur

**Résultat attendu** :
```
✅ Message : "❌ Generation failed: ..."
✅ Boutons : [Retry] [Check Backend] [View Logs]
✅ [Retry] relance la génération
✅ [Check Backend] ouvre le navigateur
✅ [View Logs] ouvre l'Output panel
```

---

### Test 4 : Validation - Pas de Diagrammes ⭐
**Objectif** : Vérifier la validation des diagrammes

**Étapes** :
1. Créer un nouveau dossier vide
2. Ouvrir dans VSCode
3. Cliquer sur "🚀 Generate"

**Résultat attendu** :
```
✅ Message : "⚠️ No diagrams found in src/diagrams/"
✅ Boutons : [Create Sample] [Open Folder] [Cancel]
✅ [Create Sample] crée src/diagrams/class-diagram.mmd
✅ [Open Folder] ouvre le dossier dans l'explorateur
```

---

### Test 5 : Validation - Backend Inaccessible ⭐
**Objectif** : Vérifier la validation du backend

**Étapes** :
1. Créer src/diagrams/ avec un fichier .mmd
2. Configurer backend invalide
3. Cliquer sur "🚀 Generate"

**Résultat attendu** :
```
✅ Message : "❌ Backend unreachable. Check your configuration."
✅ Boutons : [Configure] [Use Production] [Continue Anyway]
✅ [Configure] ouvre les settings
✅ [Use Production] met à jour vers production
✅ [Continue Anyway] continue malgré l'erreur
```

---

### Test 6 : Menu Contextuel - Dossier ⭐
**Objectif** : Vérifier le menu sur dossier diagrams/

**Étapes** :
1. Créer src/diagrams/
2. Clic droit sur le dossier "diagrams"
3. Chercher "basicCode: Generate Project"

**Résultat attendu** :
```
✅ Menu visible
✅ Commande "basicCode: Generate Project"
✅ Cliquable
✅ Lance la génération
```

---

### Test 7 : Menu Contextuel - Fichier ⭐
**Objectif** : Vérifier le menu sur fichier .mmd

**Étapes** :
1. Créer src/diagrams/test.mmd
2. Clic droit sur le fichier
3. Chercher "basicCode: Generate Project"

**Résultat attendu** :
```
✅ Menu visible
✅ Commande "basicCode: Generate Project"
✅ Cliquable
✅ Lance la génération
```

---

### Test 8 : Création Projet Exemple ⭐
**Objectif** : Vérifier la création automatique

**Étapes** :
1. Dossier vide
2. Cliquer sur "🚀 Generate"
3. Cliquer sur [Create Sample]
4. Vérifier src/diagrams/class-diagram.mmd

**Résultat attendu** :
```
✅ Dossier src/diagrams/ créé
✅ Fichier class-diagram.mmd créé
✅ Contenu valide (User, Post)
✅ Message : "✅ Sample project created!"
✅ Bouton [Generate Now]
```

---

### Test 9 : Génération Complète ⭐
**Objectif** : Vérifier la génération end-to-end

**Étapes** :
1. Créer src/diagrams/class-diagram.mmd avec contenu valide
2. Configurer backend production
3. Cliquer sur "🚀 Generate"
4. Attendre la fin

**Résultat attendu** :
```
✅ Validation réussie
✅ Progress bar visible
✅ Logs détaillés dans Output
✅ Fichiers générés
✅ Message : "✅ Project generated successfully!"
```

---

### Test 10 : Logs Détaillés ⭐
**Objectif** : Vérifier tous les logs

**Étapes** :
1. Ouvrir Output panel
2. Sélectionner "basicCode Generator"
3. Faire une génération complète
4. Vérifier les logs

**Résultat attendu** :
```
✅ Extension activated
✅ Starting generation
✅ Starting validation
✅ Scanning diagrams
✅ Found X diagram(s)
✅ Checking backend connectivity
✅ Backend is reachable
✅ Diagrams summary
✅ Initiating generation
✅ Generation ID
✅ Waiting for completion
✅ Processing... (X files)
✅ Generation completed
✅ Downloading
✅ Downloaded X bytes
✅ Extracted X files
✅ Cleaning up
✅ Generation completed successfully
```

---

## 📊 Résumé des Tests

| Test | Fonctionnalité | Status |
|------|----------------|--------|
| 1 | Barre d'état | ⬜ À tester |
| 2 | Output channel | ⬜ À tester |
| 3 | Messages contextuels | ⬜ À tester |
| 4 | Validation diagrammes | ⬜ À tester |
| 5 | Validation backend | ⬜ À tester |
| 6 | Menu contextuel dossier | ⬜ À tester |
| 7 | Menu contextuel fichier | ⬜ À tester |
| 8 | Création exemple | ⬜ À tester |
| 9 | Génération complète | ⬜ À tester |
| 10 | Logs détaillés | ⬜ À tester |

---

## 🐛 Problèmes Potentiels

### Problème 1 : Extension ne se charge pas
**Solution** :
```bash
# Désinstaller
code --uninstall-extension basiccode-generator

# Réinstaller
code --install-extension basiccode-generator-1.1.0.vsix

# Redémarrer VSCode
```

### Problème 2 : Bouton pas visible
**Solution** :
- Vérifier que l'extension est activée
- Redémarrer VSCode
- Vérifier les logs : Help → Toggle Developer Tools → Console

### Problème 3 : Output channel vide
**Solution** :
- Vérifier que "basicCode Generator" est sélectionné
- Faire une action (cliquer sur Generate)
- Vérifier que l'extension est activée

### Problème 4 : Menu contextuel absent
**Solution** :
- Vérifier que le dossier s'appelle exactement "diagrams"
- Vérifier que le fichier a l'extension ".mmd"
- Redémarrer VSCode

---

## ✅ Validation Finale

Une fois tous les tests passés :

```
✅ Barre d'état visible et fonctionnelle
✅ Output channel avec logs détaillés
✅ Messages contextuels avec actions
✅ Validation automatique fonctionnelle
✅ Menu contextuel accessible
✅ Création projet exemple OK
✅ Génération complète réussie
✅ Logs complets et clairs

🎉 Phase 1 validée avec succès !
```

---

## 📞 Support

**Fichier d'installation** : `basiccode-generator-1.1.0.vsix`

**Commandes utiles** :
```bash
# Installer
code --install-extension basiccode-generator-1.1.0.vsix

# Vérifier installation
code --list-extensions | grep basiccode

# Désinstaller
code --uninstall-extension basiccode-generator

# Voir les logs
# Help → Toggle Developer Tools → Console
```

---

**Bonne vérification ! 🎯**
