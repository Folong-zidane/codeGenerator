#!/bin/bash

# 🚀 Script d'installation automatique - Extension VSCode basicCode Generator

echo "🔧 Installation Extension VSCode basicCode Generator"
echo "=================================================="

# Vérifier Node.js
if ! command -v node &> /dev/null; then
    echo "❌ Node.js non trouvé. Installez Node.js d'abord."
    exit 1
fi

# Vérifier VSCode
if ! command -v code &> /dev/null; then
    echo "❌ VSCode CLI non trouvé. Installez VSCode d'abord."
    exit 1
fi

echo "✅ Prérequis OK"

# Aller dans le dossier extension
cd "$(dirname "$0")"

echo "📦 Installation des dépendances..."
npm install

echo "🔨 Compilation TypeScript..."
npm run compile

# Installer vsce si nécessaire
if ! command -v vsce &> /dev/null; then
    echo "📥 Installation de vsce..."
    npm install -g vsce
fi

echo "📦 Génération du package VSIX..."
vsce package --out basiccode-generator.vsix

echo "🔧 Installation de l'extension..."
code --install-extension basiccode-generator.vsix

echo ""
echo "🎉 INSTALLATION TERMINÉE !"
echo ""
echo "📋 Prochaines étapes :"
echo "1. Redémarrer VSCode"
echo "2. Configurer l'extension :"
echo "   - Ctrl+, → Rechercher 'basiccode'"
echo "   - Définir backend URL, langage, package"
echo "3. Créer un projet avec src/diagrams/"
echo "4. Utiliser Ctrl+Shift+G pour générer"
echo ""
echo "🔗 Backend par défaut : https://codegenerator-cpyh.onrender.com"
echo "📚 Documentation : README.md"