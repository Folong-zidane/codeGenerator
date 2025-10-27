# 📋 Exemples d'Utilisation

## 🎯 Exemples de Diagrammes UML

### 1. E-commerce Simple
```mermaid
classDiagram
    class User {
        +String username
        +String email
        +String password
        +Boolean active
    }
    
    class Product {
        +String name
        +String description
        +Float price
        +Integer stock
        +Boolean available
    }
    
    class Order {
        +String userId
        +Float totalAmount
        +String status
        +String orderDate
    }
    
    class OrderItem {
        +String orderId
        +String productId
        +Integer quantity
        +Float unitPrice
    }
    
    User "1" --> "*" Order
    Order "1" --> "*" OrderItem
    Product "1" --> "*" OrderItem
```

**Génération :**
```bash
./generate-from-mermaid.sh ecommerce.mermaid django com.ecommerce ./ecommerce-app
```

### 2. Système de Blog
```mermaid
classDiagram
    class Author {
        +String name
        +String email
        +String bio
        +Boolean active
    }
    
    class Category {
        +String name
        +String description
        +String slug
    }
    
    class Post {
        +String title
        +String content
        +String authorId
        +String categoryId
        +Boolean published
        +String publishDate
    }
    
    class Comment {
        +String postId
        +String authorName
        +String content
        +String commentDate
        +Boolean approved
    }
    
    Author "1" --> "*" Post
    Category "1" --> "*" Post
    Post "1" --> "*" Comment
```

**Génération :**
```bash
./generate-from-mermaid.sh blog.mermaid java com.blog ./blog-app
```

### 3. Gestion d'École
```mermaid
classDiagram
    class Student {
        +String firstName
        +String lastName
        +String email
        +String studentNumber
        +Integer age
    }
    
    class Teacher {
        +String firstName
        +String lastName
        +String email
        +String department
        +String specialization
    }
    
    class Course {
        +String name
        +String code
        +String teacherId
        +Integer credits
        +String semester
    }
    
    class Enrollment {
        +String studentId
        +String courseId
        +Float grade
        +String enrollmentDate
        +String status
    }
    
    Teacher "1" --> "*" Course
    Student "*" --> "*" Course : Enrollment
    Student "1" --> "*" Enrollment
    Course "1" --> "*" Enrollment
```

**Génération :**
```bash
./generate-from-mermaid.sh school.mermaid typescript com.school ./school-app
```

### 4. Système de Réservation d'Hôtel
```mermaid
classDiagram
    class Customer {
        +String firstName
        +String lastName
        +String email
        +String phone
        +String address
    }
    
    class Room {
        +String number
        +String type
        +Float pricePerNight
        +Integer capacity
        +Boolean available
    }
    
    class Reservation {
        +String customerId
        +String roomId
        +String checkInDate
        +String checkOutDate
        +Float totalAmount
        +String status
    }
    
    class Payment {
        +String reservationId
        +Float amount
        +String paymentDate
        +String paymentMethod
        +String status
    }
    
    Customer "1" --> "*" Reservation
    Room "1" --> "*" Reservation
    Reservation "1" --> "*" Payment
```

**Génération :**
```bash
./generate-from-mermaid.sh hotel.mermaid csharp Com.Hotel ./hotel-app
```

### 5. Système de Gestion de Tâches
```mermaid
classDiagram
    class User {
        +String username
        +String email
        +String role
        +Boolean active
    }
    
    class Project {
        +String name
        +String description
        +String ownerId
        +String startDate
        +String endDate
        +String status
    }
    
    class Task {
        +String title
        +String description
        +String projectId
        +String assignedTo
        +String priority
        +String status
        +String dueDate
    }
    
    class Comment {
        +String taskId
        +String userId
        +String content
        +String commentDate
    }
    
    User "1" --> "*" Project : owns
    Project "1" --> "*" Task
    User "*" --> "*" Task : assigned
    Task "1" --> "*" Comment
    User "1" --> "*" Comment
```

**Génération :**
```bash
./generate-from-mermaid.sh tasks.mermaid php com.tasks ./tasks-app
```

## 🚀 Commandes de Test Rapide

### Test de Tous les Langages
```bash
# Créer un diagramme simple
cat > simple.mermaid << 'EOF'
classDiagram
    class User {
        +String name
        +String email
    }
EOF

# Tester tous les langages
./generate-from-mermaid.sh simple.mermaid java com.test ./test-java
./generate-from-mermaid.sh simple.mermaid python com.test ./test-python
./generate-from-mermaid.sh simple.mermaid django com.test ./test-django
./generate-from-mermaid.sh simple.mermaid csharp Com.Test ./test-csharp
./generate-from-mermaid.sh simple.mermaid typescript com.test ./test-typescript
./generate-from-mermaid.sh simple.mermaid php com.test ./test-php
```

### Validation UML
```bash
# Valider un diagramme avant génération
curl -X POST "https://codegenerator-cpyh.onrender.com/api/generate/validate" \
  -H "Content-Type: application/json" \
  -d "$(cat simple.mermaid | jq -Rs .)"
```

## 📊 Résultats Attendus

Chaque génération produit une application complète avec :
- ✅ Base de données configurée
- ✅ API REST fonctionnelle
- ✅ Documentation Swagger
- ✅ Authentification (Django)
- ✅ Interface admin (Django)
- ✅ Tests de base
- ✅ Configuration de déploiement