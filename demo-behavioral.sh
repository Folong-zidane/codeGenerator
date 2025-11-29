#!/bin/bash

echo "🎯 Démonstration de la Génération Comportementale"
echo "================================================="
echo ""

# Vérifier si l'application est démarrée
echo "🔍 Vérification de l'API..."
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; then
    echo "✅ API accessible sur http://localhost:8080"
else
    echo "❌ API non accessible. Démarrez l'application avec: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "📋 Génération d'un projet avec diagrammes de classes ET de séquence..."
echo ""

# Utiliser l'exemple de requête
curl -X POST "http://localhost:8080/api/behavioral/generate" \
  -H "Content-Type: application/json" \
  -d @example-behavioral-request.json \
  -o user-management-behavioral.zip

if [ -f "user-management-behavioral.zip" ]; then
    echo "✅ Projet généré avec succès : user-management-behavioral.zip"
    echo ""
    echo "📁 Contenu du projet généré :"
    unzip -l user-management-behavioral.zip
    
    echo ""
    echo "📂 Extraction du projet..."
    unzip -q user-management-behavioral.zip -d user-management-behavioral/
    
    echo ""
    echo "🔍 Structure du projet :"
    find user-management-behavioral/ -type f -name "*.java" -o -name "*.md" -o -name "*.xml" | head -20
    
    echo ""
    echo "📖 Aperçu du code généré :"
    echo "=========================="
    
    if [ -f "user-management-behavioral/UserService.java" ]; then
        echo "🔧 UserService.java (extrait) :"
        head -30 user-management-behavioral/UserService.java
    fi
    
    echo ""
    if [ -f "user-management-behavioral/WORKFLOWS.md" ]; then
        echo "📋 Documentation des workflows :"
        head -20 user-management-behavioral/WORKFLOWS.md
    fi
    
else
    echo "❌ Échec de la génération du projet"
    exit 1
fi

echo ""
echo "🎉 Démonstration terminée !"
echo ""
echo "🚀 Pour tester d'autres exemples :"
echo "   ./test-behavioral-generation.sh"
echo ""
echo "📚 Documentation complète :"
echo "   cat BEHAVIORAL-GENERATION.md"
echo ""
echo "🌐 API en ligne :"
echo "   https://codegenerator-cpyh.onrender.com/api/behavioral/example"