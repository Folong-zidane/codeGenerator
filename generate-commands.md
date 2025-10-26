# 🚀 Commandes de Génération par Langage

## Configuration de base
```bash
API_URL="http://localhost:8080"
```

## 1. ☕ Java Spring Boot
```bash
# Modifier le JSON pour Java
jq '.language = "java" | .packageName = "com.example.ecommerce"' request.json > java-request.json

# Générer le projet Java
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./java-request.json)" \
    -o java-spring-boot.zip

echo "✅ Java project generated: java-spring-boot.zip"
```

## 2. 🐍 Python FastAPI
```bash
# Modifier le JSON pour Python
jq '.language = "python" | .packageName = "com.example.ecommerce"' request.json > python-request.json

# Générer le projet Python
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./python-request.json)" \
    -o python-fastapi.zip

echo "✅ Python project generated: python-fastapi.zip"
```

## 3. 🔷 C# .NET Core
```bash
# Modifier le JSON pour C#
jq '.language = "csharp" | .packageName = "Example.Ecommerce"' request.json > csharp-request.json

# Générer le projet C#
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./csharp-request.json)" \
    -o csharp-dotnet.zip

echo "✅ C# project generated: csharp-dotnet.zip"
```

## 4. 🟨 JavaScript Node.js (si supporté)
```bash
# Modifier le JSON pour JavaScript
jq '.language = "javascript" | .packageName = "com.example.ecommerce"' request.json > js-request.json

# Générer le projet JavaScript
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./js-request.json)" \
    -o javascript-express.zip

echo "✅ JavaScript project generated: javascript-express.zip"
```

## 4. 🐍 Python Django
```bash
# Modifier le JSON pour Django
jq '.language = "django" | .packageName = "com.example.ecommerce"' request.json > django-request.json

# Générer le projet Django
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./django-request.json)" \
    -o python-django.zip

echo "✅ Django project generated: python-django.zip"
```

## 5. 🟦 TypeScript Node.js
```bash
# Modifier le JSON pour TypeScript
jq '.language = "typescript" | .packageName = "com.example.ecommerce"' request.json > typescript-request.json

# Générer le projet TypeScript
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./typescript-request.json)" \
    -o nodejs-typescript.zip

echo "✅ TypeScript project generated: nodejs-typescript.zip"
```

## 6. 🐘 PHP Laravel
```bash
# Modifier le JSON pour PHP
jq '.language = "php" | .packageName = "Example\\Ecommerce"' request.json > php-request.json

# Générer le projet PHP
curl -s -X POST "$API_URL/api/generate/crud" \
    -H "Content-Type: application/json" \
    -d "$(cat ./php-request.json)" \
    -o php-laravel.zip

echo "✅ PHP project generated: php-laravel.zip"
```

## 🔧 Commande One-Liner pour tous les langages
```bash
# Générer tous les langages en une seule commande
for lang in java python django csharp typescript php; do
    case $lang in
        java) pkg="com.example.ecommerce" ;;
        python) pkg="com.example.ecommerce" ;;
        django) pkg="com.example.ecommerce" ;;
        csharp) pkg="Example.Ecommerce" ;;
        typescript) pkg="com.example.ecommerce" ;;
        php) pkg="Example\\Ecommerce" ;;
    esac
    
    jq --arg language "$lang" --arg package "$pkg" \
       '.language = $language | .packageName = $package' \
       request.json | \
    curl -s -X POST "http://localhost:8080/api/generate/crud" \
        -H "Content-Type: application/json" \
        -d @- \
        -o "${lang}-project.zip"
    
    echo "✅ $lang project generated: ${lang}-project.zip"
done
```

## 📋 Validation UML avant génération
```bash
# Valider le contenu UML
curl -s -X POST "$API_URL/api/generate/validate" \
    -H "Content-Type: application/json" \
    -d "$(jq -r '.umlContent' request.json)" | \
    jq '.valid, .message, .classCount'
```

## 🔍 Extraction et examen des projets générés
```bash
# Créer les dossiers d'extraction
mkdir -p extracted/{java,python,csharp}

# Extraire chaque projet
unzip -q java-spring-boot.zip -d extracted/java/
unzip -q python-fastapi.zip -d extracted/python/
unzip -q csharp-dotnet.zip -d extracted/csharp/

# Examiner la structure
echo "📁 Java structure:"
find extracted/java -name "*.java" | head -5

echo "📁 Python structure:"
find extracted/python -name "*.py" | head -5

echo "📁 C# structure:"
find extracted/csharp -name "*.cs" | head -5
```

## 🚀 Démarrage rapide
```bash
# 1. Démarrer l'API
mvn spring-boot:run &

# 2. Attendre le démarrage
sleep 10

# 3. Générer tous les projets
./generate-all-languages.sh

# 4. Examiner les résultats
ls -la generated-projects/
```