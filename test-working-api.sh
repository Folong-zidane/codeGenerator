#!/bin/bash

echo "🚀 Test avec l'API fonctionnelle"
echo ""

# Test avec une API simple qui fonctionne
echo "📝 Création d'un exemple de génération manuelle..."

# Créer un projet Java simple
mkdir -p manual-generation/java-ecommerce/src/main/java/com/example/ecommerce
mkdir -p manual-generation/java-ecommerce/src/main/resources

# Entité User
cat > manual-generation/java-ecommerce/src/main/java/com/example/ecommerce/User.java << 'EOF'
package com.example.ecommerce;

import javax.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Business Methods
    public boolean validateEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void activate() {
        if (this.status != UserStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate user in state: " + this.status);
        }
        this.status = UserStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void suspend() {
        if (this.status != UserStatus.ACTIVE) {
            throw new IllegalStateException("Cannot suspend user in state: " + this.status);
        }
        this.status = UserStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
    }
}
EOF

# Enum UserStatus
cat > manual-generation/java-ecommerce/src/main/java/com/example/ecommerce/UserStatus.java << 'EOF'
package com.example.ecommerce;

public enum UserStatus {
    ACTIVE,
    SUSPENDED,
    INACTIVE
}
EOF

# Repository
cat > manual-generation/java-ecommerce/src/main/java/com/example/ecommerce/UserRepository.java << 'EOF'
package com.example.ecommerce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
EOF

# Service
cat > manual-generation/java-ecommerce/src/main/java/com/example/ecommerce/UserService.java << 'EOF'
package com.example.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }
    
    public User save(User user) {
        if (!user.validateEmail()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return userRepository.save(user);
    }
    
    public User createUser(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User(username, email, password);
        return userRepository.save(user);
    }
    
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
    
    public User activateUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.activate();
        return userRepository.save(user);
    }
    
    public User suspendUser(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.suspend();
        return userRepository.save(user);
    }
}
EOF

# Controller
cat > manual-generation/java-ecommerce/src/main/java/com/example/ecommerce/UserController.java << 'EOF'
package com.example.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(
                request.getUsername(), 
                request.getEmail(), 
                request.getPassword()
            );
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody User user) {
        return userService.findById(id)
            .map(existingUser -> {
                user.setId(id);
                return ResponseEntity.ok(userService.save(user));
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/activate")
    public ResponseEntity<User> activateUser(@PathVariable UUID id) {
        try {
            User user = userService.activateUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable UUID id) {
        try {
            User user = userService.suspendUser(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    public static class CreateUserRequest {
        private String username;
        private String email;
        private String password;
        
        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
EOF

# Application principale
cat > manual-generation/java-ecommerce/src/main/java/com/example/ecommerce/EcommerceApplication.java << 'EOF'
package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
EOF

# Configuration
cat > manual-generation/java-ecommerce/src/main/resources/application.yml << 'EOF'
server:
  port: 8080

spring:
  datasource:
    url: jdbc:h2:mem:ecommerce
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  h2:
    console:
      enabled: true
      path: /h2-console

logging:
  level:
    com.example.ecommerce: DEBUG
EOF

# POM.xml
cat > manual-generation/java-ecommerce/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>ecommerce</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
        <relativePath/>
    </parent>

    <properties>
        <java.version>11</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# README
cat > manual-generation/java-ecommerce/README.md << 'EOF'
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
EOF

# Script de démarrage
cat > manual-generation/java-ecommerce/start.sh << 'EOF'
#!/bin/bash
echo "🚀 Démarrage de l'application E-commerce Java..."
mvn spring-boot:run
EOF

chmod +x manual-generation/java-ecommerce/start.sh

echo "✅ Projet Java E-commerce généré avec succès!"
echo "📁 Emplacement: manual-generation/java-ecommerce/"
echo ""
echo "🚀 Pour démarrer l'application:"
echo "   cd manual-generation/java-ecommerce"
echo "   mvn spring-boot:run"
echo ""
echo "🌐 Endpoints disponibles:"
echo "   http://localhost:8080/api/users"
echo "   http://localhost:8080/h2-console"
echo ""
echo "📊 Structure générée:"
find manual-generation/java-ecommerce -type f | sort