#!/bin/bash

echo "🔧 Build Extension VSCode - basicCode Generator"
echo "=============================================="

# Nettoyer complètement
echo "🧹 Nettoyage complet..."
rm -rf node_modules package-lock.json out/ *.vsix

# Installer dépendances
echo "📦 Installation des dépendances..."
npm install --no-package-lock

# Compiler TypeScript
echo "🔨 Compilation TypeScript..."
npm run compile

# Vérifier compilation
if [ ! -f "out/extension.js" ]; then
    echo "❌ Erreur de compilation TypeScript"
    exit 1
fi

echo "✅ Compilation réussie"

# Installer vsce globalement
echo "📥 Installation de @vscode/vsce..."
npm install -g @vscode/vsce

# Générer package
echo "📦 Génération du package VSIX..."
vsce package --no-dependencies

# Vérifier génération
if [ -f "basiccode-generator-1.0.0.vsix" ]; then
    echo "✅ Package généré : basiccode-generator-1.0.0.vsix"
    
    # Installer extension
    echo "🔧 Installation de l'extension..."
    code --install-extension basiccode-generator-1.0.0.vsix
    
    echo ""
    echo "🎉 INSTALLATION TERMINÉE !"
    echo ""
    echo "📋 Prochaines étapes :"
    echo "1. Redémarrer VSCode"
    echo "2. Créer un projet avec src/diagrams/"
    echo "3. Utiliser Ctrl+Shift+G pour générer"
    echo ""
    echo "⚙️ Configuration :"
    echo "Ctrl+, → Rechercher 'basiccode' → Configurer backend/langage"
    
else
    echo "❌ Erreur lors de la génération du package"
    exit 1
fi