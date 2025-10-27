#!/bin/bash

echo "🚀 Déploiement du Générateur UML vers Code"
echo "=========================================="

# Vérifier que Maven est installé
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven n'est pas installé"
    exit 1
fi

# Nettoyer et compiler
echo "🧹 Nettoyage et compilation..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation"
    exit 1
fi

# Créer le JAR
echo "📦 Création du JAR..."
mvn package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la création du JAR"
    exit 1
fi

# Vérifier que le JAR existe
JAR_FILE=$(find target -name "*.jar" -not -name "*sources*" | head -1)

if [ -z "$JAR_FILE" ]; then
    echo "❌ JAR non trouvé"
    exit 1
fi

echo "✅ JAR créé: $JAR_FILE"

# Tester l'application localement
echo "🧪 Test de l'application..."
java -jar "$JAR_FILE" --server.port=8081 &
APP_PID=$!

# Attendre que l'application démarre
sleep 15

# Tester l'endpoint de santé
if curl -s http://localhost:8081/actuator/health > /dev/null; then
    echo "✅ Application fonctionne correctement"
else
    echo "❌ Application ne répond pas"
    kill $APP_PID 2>/dev/null
    exit 1
fi

# Arrêter l'application de test
kill $APP_PID 2>/dev/null

echo ""
echo "🎉 Déploiement prêt !"
echo "📦 JAR: $JAR_FILE"
echo "🌐 API Production: https://codegenerator-cpyh.onrender.com"
echo ""
echo "📋 Pour déployer sur Render:"
echo "   1. Push vers GitHub"
echo "   2. Render détectera automatiquement les changements"
echo "   3. L'application sera redéployée"
echo ""
echo "🧪 Pour tester localement:"
echo "   java -jar $JAR_FILE"
echo "   API: http://localhost:8080"