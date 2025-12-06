#!/bin/bash

# 🔧 Script de correction et build de l'extension VSCode

echo "🧹 Nettoyage des dépendances..."
rm -rf node_modules package-lock.json

echo "📦 Réinstallation propre..."
npm install

echo "🔨 Compilation TypeScript..."
npm run compile

echo "📦 Génération du package avec la nouvelle version de vsce..."
npx @vscode/vsce package --out basiccode-generator.vsix

echo "✅ Package généré : basiccode-generator.vsix"
echo ""
echo "🔧 Pour installer :"
echo "code --install-extension basiccode-generator.vsix"