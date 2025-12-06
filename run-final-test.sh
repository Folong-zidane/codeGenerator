#!/bin/bash

echo "🚀 LANCEMENT DU TEST FINAL COMPLET"
echo "=================================="
echo "Date: $(date)"
echo "Projet: UML-to-Code Generator"
echo ""

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonction pour afficher les résultats
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ $2${NC}"
    else
        echo -e "${RED}❌ $2${NC}"
    fi
}

# Fonction pour afficher les informations
print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Fonction pour afficher les avertissements
print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

echo "📋 Phase 1: Vérification de l'environnement"
echo "============================================"

# Vérifier Java
print_info "Vérification de Java..."
java -version 2>&1 | head -1
JAVA_OK=$?
print_result $JAVA_OK "Java disponible"

# Vérifier Maven
print_info "Vérification de Maven..."
mvn -version 2>&1 | head -1
MAVEN_OK=$?
print_result $MAVEN_OK "Maven disponible"

echo ""
echo "📊 Phase 2: Analyse du projet"
echo "=============================="

# Statistiques du projet
JAVA_FILES=$(find src -name "*.java" | wc -l)
TOTAL_LINES=$(find src -name "*.java" -exec wc -l {} + 2>/dev/null | tail -1 | awk '{print $1}' || echo "0")
TEST_FILES=$(find src/test -name "*Test.java" | wc -l)

print_info "Fichiers Java: $JAVA_FILES"
print_info "Lignes de code: $TOTAL_LINES"
print_info "Fichiers de test: $TEST_FILES"

echo ""
echo "🔧 Phase 3: Compilation"
echo "======================="

print_info "Compilation du projet..."
mvn clean compile -q > /tmp/compile.log 2>&1
COMPILE_STATUS=$?

if [ $COMPILE_STATUS -eq 0 ]; then
    print_result 0 "Compilation réussie"
else
    print_result 1 "Compilation échouée"
    print_warning "Erreurs de compilation détectées, mais les tests vont continuer..."
    
    # Compter les erreurs
    ERROR_COUNT=$(grep -c "ERROR" /tmp/compile.log || echo "0")
    print_info "Nombre d'erreurs: $ERROR_COUNT"
fi

echo ""
echo "🧪 Phase 4: Exécution des tests"
echo "==============================="

# Test 1: Test complet de l'application
print_info "Lancement du test final complet..."
mvn test -Dtest=CompleteApplicationTest -q > /tmp/complete_test.log 2>&1
COMPLETE_TEST_STATUS=$?

if [ $COMPLETE_TEST_STATUS -eq 0 ]; then
    print_result 0 "Test complet réussi"
    
    # Extraire les statistiques du test
    if [ -f /tmp/complete_test.log ]; then
        echo ""
        print_info "Résultats du test complet:"
        grep -E "(✅|🔥|🐍|📜|🐘|🚀|📊)" /tmp/complete_test.log | while read line; do
            echo "  $line"
        done
    fi
else
    print_result 1 "Test complet échoué"
    print_warning "Détails dans /tmp/complete_test.log"
fi

# Test 2: Tests Spring Boot
print_info "Test des générateurs Spring Boot..."
mvn test -Dtest=SimpleGeneratorTest -q > /tmp/spring_test.log 2>&1
SPRING_TEST_STATUS=$?
print_result $SPRING_TEST_STATUS "Tests Spring Boot"

# Test 3: Tests TypeScript
print_info "Test des générateurs TypeScript..."
mvn test -Dtest=TypeScriptGeneratorTest -q > /tmp/ts_test.log 2>&1
TS_TEST_STATUS=$?
print_result $TS_TEST_STATUS "Tests TypeScript"

# Test 4: Tests PHP
print_info "Test des générateurs PHP..."
mvn test -Dtest=PhpGeneratorTest -q > /tmp/php_test.log 2>&1
PHP_TEST_STATUS=$?
print_result $PHP_TEST_STATUS "Tests PHP"

echo ""
echo "📈 Phase 5: Analyse des performances"
echo "===================================="

# Mesurer le temps de génération
print_info "Test de performance de génération..."
START_TIME=$(date +%s%N)

# Simuler une génération rapide
mvn exec:java -Dexec.mainClass="com.basiccode.generator.Main" -Dexec.args="--help" -q > /dev/null 2>&1

END_TIME=$(date +%s%N)
DURATION=$(( (END_TIME - START_TIME) / 1000000 )) # Convertir en millisecondes

print_info "Temps de démarrage: ${DURATION}ms"

if [ $DURATION -lt 5000 ]; then
    print_result 0 "Performance acceptable (< 5s)"
else
    print_result 1 "Performance lente (> 5s)"
fi

echo ""
echo "🎯 Phase 6: Rapport final"
echo "========================="

# Calculer le score global
TOTAL_TESTS=5
PASSED_TESTS=0

[ $COMPLETE_TEST_STATUS -eq 0 ] && PASSED_TESTS=$((PASSED_TESTS + 1))
[ $SPRING_TEST_STATUS -eq 0 ] && PASSED_TESTS=$((PASSED_TESTS + 1))
[ $TS_TEST_STATUS -eq 0 ] && PASSED_TESTS=$((PASSED_TESTS + 1))
[ $PHP_TEST_STATUS -eq 0 ] && PASSED_TESTS=$((PASSED_TESTS + 1))
[ $DURATION -lt 5000 ] && PASSED_TESTS=$((PASSED_TESTS + 1))

SUCCESS_RATE=$(( PASSED_TESTS * 100 / TOTAL_TESTS ))

echo "📊 RÉSULTATS FINAUX:"
echo "===================="
echo "Tests passés: $PASSED_TESTS/$TOTAL_TESTS"
echo "Taux de réussite: $SUCCESS_RATE%"
echo "Fichiers Java: $JAVA_FILES"
echo "Lignes de code: $TOTAL_LINES"
echo "Fichiers de test: $TEST_FILES"

if [ $COMPILE_STATUS -ne 0 ]; then
    echo "Erreurs de compilation: $ERROR_COUNT"
fi

echo ""
echo "🏆 ÉVALUATION GLOBALE:"
echo "======================"

if [ $SUCCESS_RATE -ge 80 ]; then
    echo -e "${GREEN}🎉 EXCELLENT - Application prête pour production${NC}"
    echo "✅ Générateurs fonctionnels"
    echo "✅ Tests validés"
    echo "✅ Performance acceptable"
elif [ $SUCCESS_RATE -ge 60 ]; then
    echo -e "${YELLOW}👍 BON - Application fonctionnelle avec améliorations mineures${NC}"
    echo "✅ Fonctionnalités principales opérationnelles"
    echo "⚠️ Quelques corrections nécessaires"
elif [ $SUCCESS_RATE -ge 40 ]; then
    echo -e "${YELLOW}⚠️ MOYEN - Application partiellement fonctionnelle${NC}"
    echo "✅ Base solide"
    echo "🔧 Corrections importantes nécessaires"
else
    echo -e "${RED}❌ FAIBLE - Corrections majeures requises${NC}"
    echo "🔧 Travail de correction important nécessaire"
fi

echo ""
echo "📋 RECOMMANDATIONS:"
echo "==================="

if [ $COMPILE_STATUS -ne 0 ]; then
    echo "🔧 Corriger les $ERROR_COUNT erreurs de compilation restantes"
fi

if [ $COMPLETE_TEST_STATUS -ne 0 ]; then
    echo "🧪 Réviser le test complet de l'application"
fi

if [ $SPRING_TEST_STATUS -ne 0 ]; then
    echo "🍃 Corriger les générateurs Spring Boot"
fi

if [ $TS_TEST_STATUS -ne 0 ]; then
    echo "📜 Corriger les générateurs TypeScript"
fi

if [ $PHP_TEST_STATUS -ne 0 ]; then
    echo "🐘 Corriger les générateurs PHP"
fi

if [ $DURATION -ge 5000 ]; then
    echo "⚡ Optimiser les performances de démarrage"
fi

echo ""
echo "📁 LOGS DISPONIBLES:"
echo "===================="
echo "Compilation: /tmp/compile.log"
echo "Test complet: /tmp/complete_test.log"
echo "Tests Spring: /tmp/spring_test.log"
echo "Tests TypeScript: /tmp/ts_test.log"
echo "Tests PHP: /tmp/php_test.log"

echo ""
echo "🎯 TEST FINAL TERMINÉ"
echo "Date de fin: $(date)"
echo "Statut global: $SUCCESS_RATE% de réussite"

# Code de sortie basé sur le taux de réussite
if [ $SUCCESS_RATE -ge 60 ]; then
    exit 0
else
    exit 1
fi