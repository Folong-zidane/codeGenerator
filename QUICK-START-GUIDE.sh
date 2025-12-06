#!/bin/bash

# Script de démarrage rapide - Vérification de Qualité du Code Généré

cat << 'EOF'

╔════════════════════════════════════════════════════════════════╗
║                                                                ║
║   🧪 VÉRIFICATION DE QUALITÉ - CODE GÉNÉRÉ vs DIAGRAMMES     ║
║                                                                ║
║   Application: Code Generator Multi-Language                 ║
║   Date: 30 Nov 2025                                           ║
║   Status: ✅ SETUP COMPLET                                    ║
║                                                                ║
╚════════════════════════════════════════════════════════════════╝

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
📋 QU'A ÉTÉ CRÉÉ ?
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

✅ 16 Diagrammes UML (Mermaid)
   - 5 langages (Java, Python, C#, TypeScript, PHP)
   - 2 types par langage (Class + Sequence)
   
   Location: diagrams/{langage}/{type}/{nom}.mermaid

✅ API REST pour la Génération
   - Endpoint: POST /api/v1/generate/{language}
   - Support: java, python, csharp, typescript, php
   - Response: Code généré + Métriques de qualité
   
   Fichiers:
   - CodeGenerationController.java
   - CodeGenerationService.java
   - GenerationRequest.java
   - GenerationResponse.java

✅ Collection Postman (5 Requêtes)
   - Java: E-commerce Domain
   - Python: Django Models
   - C#: ASP.NET Entities
   - TypeScript: Node.js API
   - PHP: Laravel Models
   
   Location: postman/code-generation-quality-tests.json

✅ Guides de Vérification
   - QUALITY-VERIFICATION-GUIDE.md (Critères détaillés)
   - POSTMAN-TEST-GUIDE.md (Guide d'utilisation)
   - QUALITY-TEST-REPORT.md (À remplir)
   
   Location: generated-test/

✅ Structure de Test Organisée
   - Dossiers pour chaque langage
   - Diagrammes de référence copiés
   - Rapport de test initialisé
   
   Location: generated-test/{langage}/

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
🚀 COMMENT DÉMARRER
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ÉTAPE 1️⃣: Démarrer l'Application
─────────────────────────────────

   cd /home/folongzidane/Documents/Projet/basicCode
   mvn spring-boot:run

   Alternative (avec JAR):
   mvn clean package -DskipTests
   java -jar target/generator-*.jar


ÉTAPE 2️⃣: Importer la Collection Postman
──────────────────────────────────────────

   1. Ouvrir Postman
   2. File → Import
   3. Sélectionner: postman/code-generation-quality-tests.json
   4. Importer et configurer l'environnement:
      - base_url: http://localhost:8080
      - api_version: v1


ÉTAPE 3️⃣: Exécuter les Requêtes de Test
─────────────────────────────────────────

   Pour chaque langage, dans Postman:
   
   1. Java - E-Commerce Domain
   2. Python - Django Models
   3. C# - ASP.NET Core Entities
   4. TypeScript - Node.js API
   5. PHP - Laravel Models
   
   Chaque requête retourne:
   - Code généré (liste de fichiers)
   - Métriques de qualité (classes, méthodes, complétude)


ÉTAPE 4️⃣: Vérifier la Qualité
───────────────────────────────

   Pour chaque réponse:
   
   a) Ouvrir le fichier: generated-test/QUALITY-VERIFICATION-GUIDE.md
   
   b) Vérifier les critères:
      ✓ Classes du diagramme présentes
      ✓ Propriétés correctes
      ✓ Relations (1:1, 1:N) implémentées
      ✓ Syntaxe valide
      ✓ Conventions du langage
      ✓ Méthodes métier présentes
   
   c) Comparer avec le diagramme:
      - diagrams/java/class/ecommerce-domain.mermaid
      - diagrams/python/class/django-models.mermaid
      - (etc.)
   
   d) Documenter les résultats:
      generated-test/QUALITY-TEST-REPORT.md


ÉTAPE 5️⃣: Analyser les Résultats
─────────────────────────────────

   Métriques à vérifier:
   
   ✓ classCount ≥ nombre attendu
   ✓ methodCount ≥ 10
   ✓ codeCompleteness ≥ 80%
   ✓ violations = 0
   
   Exemple de réponse valide:
   
   {
     "status": "SUCCESS",
     "generationId": "abc-123",
     "qualityMetrics": {
       "classCount": 11,
       "methodCount": 45,
       "codeCompleteness": 100.0,
       "violations": 0
     }
   }

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
📊 TABLEAU DE VÉRIFICATION
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Langage    | Diagramme Classe     | Diagramme Seq    | Status
-----------|----------------------|------------------|----------
Java       | ecommerce-domain     | order-creation   | À tester
Python     | django-models        | django-order     | À tester
C#         | aspnet-entities      | aspnet-workflow  | À tester
TypeScript | react-components     | nodejs-order     | À tester
PHP        | laravel-models       | laravel-process  | À tester

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
📁 FICHIERS CLÉS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Diagrammes de Référence:
├── diagrams/java/class/ecommerce-domain.mermaid
├── diagrams/python/class/django-models.mermaid
├── diagrams/csharp/class/aspnet-entities.mermaid
├── diagrams/typescript/class/react-components.mermaid
└── diagrams/php/class/laravel-models.mermaid

Guides de Test:
├── generated-test/README.md (Vue d'ensemble)
├── generated-test/QUALITY-VERIFICATION-GUIDE.md (Critères)
├── generated-test/POSTMAN-TEST-GUIDE.md (Utilisation)
└── generated-test/QUALITY-TEST-REPORT.md (À remplir)

API REST:
├── src/main/java/.../controller/CodeGenerationController.java
├── src/main/java/.../service/CodeGenerationService.java
├── src/main/java/.../dto/GenerationRequest.java
└── src/main/java/.../dto/GenerationResponse.java

Collection Postman:
└── postman/code-generation-quality-tests.json

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
🎯 OBJECTIFS DE TEST
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

POUR CHAQUE LANGAGE:

1. ✓ Vérifier que le code généré contient TOUTES les classes
       du diagramme UML

2. ✓ Vérifier que chaque classe a TOUS les attributs
       définis dans le diagramme

3. ✓ Vérifier que les relations (1:1, 1:N) sont
       correctement implémentées

4. ✓ Vérifier que le code est syntaxiquement valide

5. ✓ Vérifier que les conventions de nommage du langage
       sont respectées

6. ✓ Vérifier que les méthodes métier essentielles
       sont présentes (getters, setters, business logic)

7. ✓ Vérifier que le code est documenté (commentaires,
       javadoc, etc.)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
💡 CONSEILS
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

1. Gardez le fichier QUALITY-VERIFICATION-GUIDE.md ouvert
   pendant les tests pour référence rapide

2. Utilisez un éditeur pour visualiser les diagrammes
   (VS Code, Mermaid Live, etc.)

3. Comparez le code généré ligne par ligne avec les
   critères dans le guide

4. Documentez TOUT dans QUALITY-TEST-REPORT.md
   (classes manquantes, erreurs, remarques)

5. Si une erreur est trouvée, notez:
   - Le langage
   - La classe affectée
   - Le problème exact
   - Une suggestion de correction

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
✨ RÉSUMÉ
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Vous avez maintenant un setup complet pour:

✅ Générer du code pour 5 langages
✅ Comparer le code généré avec les diagrammes UML
✅ Vérifier la qualité du code généré
✅ Documenter les résultats des tests

Prochaines étapes:

1. Démarrer l'application Spring Boot
2. Importer la collection Postman
3. Exécuter les 5 requêtes de test
4. Vérifier la qualité du code généré
5. Documenter les résultats

📚 Pour plus d'informations, consultez:
   - generated-test/README.md
   - generated-test/POSTMAN-TEST-GUIDE.md
   - generated-test/QUALITY-VERIFICATION-GUIDE.md

Bon test ! 🚀

EOF
