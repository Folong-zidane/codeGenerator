# Generated Comprehensive Application

Package: com.ecommerce.platform
Language: java
Generated with comprehensive UML diagrams support

## Features
- **Metadata-Aware Generation**: Intelligent code generation based on UML metadata
- **Entities from class diagrams**: JPA entities with proper annotations
- **Services from sequence diagrams**: Business logic with transaction support
- **State controllers from state diagrams**: REST controllers with validation
- **Complete Spring Boot Application**: Ready-to-run application with configuration

## New Metadata-Aware Components

### Core Generators
- `MetadataAwareSpringBootEntityGenerator`: Generates JPA entities with metadata-driven features
- `MetadataAwareSpringBootServiceGenerator`: Creates services with business logic and validation
- `MetadataAwareSpringBootRepositoryGenerator`: Generates repositories with custom queries
- `MetadataAwareSpringBootControllerGenerator`: Creates REST controllers with proper endpoints

### Orchestration
- `MetadataAwareGenerationOrchestrator`: Coordinates complete application generation
- `MetadataAwareGenerationController`: REST API for generation requests

### Key Features
- **Automatic CRUD Operations**: Complete CRUD for all entities
- **Validation Support**: Built-in validation based on metadata
- **Audit Fields**: Automatic created/updated timestamps
- **Custom Queries**: Repository methods based on entity attributes
- **Business Endpoints**: Additional REST endpoints for business operations
- **Configuration Files**: Complete Spring Boot configuration (application.yml, pom.xml)

## API Endpoints

### Generate Complete Application
```
POST /api/metadata-aware/generate
Content-Type: application/json

{
  "diagramContent": "classDiagram...",
  "packageName": "com.ecommerce.platform"
}
```

### Generate Application ZIP
```
POST /api/metadata-aware/generate/zip
Content-Type: application/json

{
  "diagramContent": "classDiagram...",
  "packageName": "com.ecommerce.platform",
  "projectName": "ecommerce-app"
}
```

### Validate Diagram
```
POST /api/metadata-aware/validate
Content-Type: application/json

{
  "diagramContent": "classDiagram..."
}
```

## Example Usage

```bash
# Compile and run the test
cd /home/folongzidane/Documents/Projet/basicCode
mvn compile exec:java -Dexec.mainClass="com.basiccode.generator.TestMetadataAwareGeneration"
```

```java
// Or run directly
java -cp target/classes com.basiccode.generator.TestMetadataAwareGeneration
```

## Generated Application Structure
```
com.ecommerce.platform/
├── entity/
│   ├── User.java
│   ├── Product.java
│   └── Order.java
├── repository/
│   ├── UserRepository.java
│   ├── ProductRepository.java
│   └── OrderRepository.java
├── service/
│   ├── UserService.java
│   ├── ProductService.java
│   └── OrderService.java
├── controller/
│   ├── UserController.java
│   ├── ProductController.java
│   └── OrderController.java
├── Application.java
├── application.yml
└── pom.xml
```
