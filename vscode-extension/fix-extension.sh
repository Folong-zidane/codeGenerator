#!/bin/bash

# 🔧 Script de Réparation de l'Extension VSCode basicCode
# Ce script diagnostique et répare les problèmes courants

set -e

echo "🔍 Diagnostic de l'extension VSCode basicCode..."
echo ""

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Fonction de log
log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_info() {
    echo "ℹ️  $1"
}

# 1. Vérifier Node.js et npm
echo "📦 Vérification de l'environnement..."
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    log_success "Node.js installé : $NODE_VERSION"
else
    log_error "Node.js n'est pas installé"
    exit 1
fi

if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm --version)
    log_success "npm installé : $NPM_VERSION"
else
    log_error "npm n'est pas installé"
    exit 1
fi

# 2. Vérifier VSCode
echo ""
echo "🔍 Vérification de VSCode..."
if command -v code &> /dev/null; then
    VSCODE_VERSION=$(code --version | head -n 1)
    log_success "VSCode installé : $VSCODE_VERSION"
else
    log_error "VSCode n'est pas installé ou 'code' n'est pas dans le PATH"
    exit 1
fi

# 3. Vérifier l'extension actuelle
echo ""
echo "🔍 Vérification de l'extension actuelle..."
if code --list-extensions | grep -q "basiccode-generator"; then
    log_warning "Extension déjà installée, désinstallation..."
    code --uninstall-extension basiccode-generator || true
    sleep 2
else
    log_info "Aucune extension existante trouvée"
fi

# 4. Nettoyer les fichiers
echo ""
echo "🧹 Nettoyage des fichiers..."
rm -rf node_modules/
log_success "node_modules/ supprimé"

rm -rf out/
log_success "out/ supprimé"

rm -f *.vsix
log_success "Anciens packages .vsix supprimés"

rm -f package-lock.json
log_success "package-lock.json supprimé"

# 5. Installer les dépendances
echo ""
echo "📦 Installation des dépendances..."
npm install
log_success "Dépendances installées"

# 6. Compiler TypeScript
echo ""
echo "🔨 Compilation TypeScript..."
npm run compile
if [ -f "out/extension.js" ]; then
    log_success "Compilation réussie"
else
    log_error "Échec de la compilation"
    exit 1
fi

# 7. Créer le package VSIX
echo ""
echo "📦 Création du package VSIX..."
npx vsce package
VSIX_FILE=$(ls -t *.vsix | head -n 1)
if [ -f "$VSIX_FILE" ]; then
    log_success "Package créé : $VSIX_FILE"
else
    log_error "Échec de la création du package"
    exit 1
fi

# 8. Installer l'extension
echo ""
echo "🔧 Installation de l'extension..."
code --install-extension "$VSIX_FILE"
sleep 2

# 9. Vérifier l'installation
echo ""
echo "✅ Vérification de l'installation..."
if code --list-extensions | grep -q "basiccode-generator"; then
    log_success "Extension installée avec succès !"
else
    log_error "L'extension n'a pas été installée correctement"
    exit 1
fi

# 10. Afficher les informations
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🎉 Extension VSCode basicCode installée avec succès !"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📋 Prochaines étapes :"
echo ""
echo "1. Redémarrer VSCode"
echo "   → Fermer toutes les fenêtres VSCode"
echo "   → Rouvrir VSCode"
echo ""
echo "2. Vérifier la commande"
echo "   → Ctrl+Shift+P"
echo "   → Taper 'basicCode'"
echo "   → Vous devriez voir 'basicCode: Generate Project'"
echo ""
echo "3. Configurer le backend"
echo "   → Ctrl+,"
echo "   → Chercher 'basiccode'"
echo "   → Définir 'basiccode.backend' = 'https://codegenerator-cpyh.onrender.com'"
echo ""
echo "4. Créer votre structure de projet"
echo "   → mkdir -p src/diagrams"
echo "   → Ajouter vos fichiers .mmd"
echo ""
echo "5. Générer votre projet"
echo "   → Ctrl+Shift+G (raccourci)"
echo "   → ou Ctrl+Shift+P → 'basicCode: Generate Project'"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📚 Documentation complète : ../GUIDE-UTILISATION-EXTENSION.md"
echo ""
echo "🔧 En cas de problème :"
echo "   → Vérifier les logs : Help → Toggle Developer Tools → Console"
echo "   → Relancer ce script : ./fix-extension.sh"
echo "   → Mode debug : Ouvrir ce dossier dans VSCode et appuyer sur F5"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
