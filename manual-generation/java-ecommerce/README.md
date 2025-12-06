# E-commerce Java Application

Application générée à partir d'un diagramme UML avec gestion d'états.

## Fonctionnalités

- ✅ Entité User avec gestion d'états (ACTIVE, SUSPENDED, INACTIVE)
- ✅ Repository JPA avec requêtes personnalisées
- ✅ Service avec logique métier et validation
- ✅ Controller REST avec endpoints CRUD
- ✅ Gestion des transitions d'état (activate/suspend)
- ✅ Base de données H2 en mémoire
- ✅ Configuration Spring Boot complète

## Démarrage

```bash
mvn spring-boot:run
```

## Endpoints

- GET /api/users - Liste tous les utilisateurs
- GET /api/users/{id} - Détail d'un utilisateur
- POST /api/users - Créer un utilisateur
- PUT /api/users/{id} - Modifier un utilisateur
- DELETE /api/users/{id} - Supprimer un utilisateur
- POST /api/users/{id}/activate - Activer un utilisateur
- POST /api/users/{id}/suspend - Suspendre un utilisateur

## Console H2

http://localhost:8080/h2-console
