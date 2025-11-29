# 🧹 Nettoyage des Générateurs Legacy

## Problème Identifié
Les générateurs legacy ne respectent pas l'architecture moderne mise en place :
- Code monolithique sans injection de dépendances
- Pas d'utilisation des services StateEnhancer/BehaviorExtractor
- Duplication de logique avec les services modernes

## Générateurs à Supprimer
```
src/main/java/com/basiccode/generator/generator/
├── DjangoProjectGenerator.java     ❌ SUPPRIMER
├── PythonProjectGenerator.java     ❌ SUPPRIMER  
├── CSharpProjectGenerator.java     ❌ SUPPRIMER
├── TypeScriptProjectGenerator.java ❌ SUPPRIMER
└── PhpProjectGenerator.java        ❌ SUPPRIMER
```

## Services Modernes à Conserver
```
src/main/java/com/basiccode/generator/service/
├── TripleDiagramCodeGeneratorService.java ✅ CONSERVER
├── StateEnhancer.java                     ✅ CONSERVER
├── BehaviorExtractor.java                 ✅ CONSERVER
└── CodeGenerationOrchestrator.java        ✅ CONSERVER
```

## Plan d'Action
1. **Supprimer** les générateurs legacy monolithiques
2. **Étendre** les services modernes pour tous les langages
3. **Utiliser** l'architecture SOLID existante
4. **Maintenir** la compatibilité API

## Avantages
- ✅ Code unifié et maintenable
- ✅ Réutilisation des services StateEnhancer/BehaviorExtractor
- ✅ Architecture SOLID respectée
- ✅ Injection de dépendances
- ✅ Tests plus faciles