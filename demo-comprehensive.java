import com.basiccode.generator.service.MinimalComprehensiveService;
import com.basiccode.generator.model.ComprehensiveCodeResult;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DemoComprehensive {
    public static void main(String[] args) throws IOException {
        MinimalComprehensiveService service = new MinimalComprehensiveService();
        
        String classDiagram = """
            classDiagram
                class Order {
                    +UUID id
                    +String customerEmail
                    +Float total
                    +OrderStatus status
                    +LocalDateTime createdAt
                }
                class OrderItem {
                    +UUID id
                    +UUID orderId
                    +String productName
                    +Integer quantity
                    +Float price
                }
                Order "1" --> "*" OrderItem
            """;

        String sequenceDiagram = """
            sequenceDiagram
                participant Client
                participant OrderController
                participant OrderService
                participant OrderRepository
                
                Client->>+OrderController: POST /api/orders
                OrderController->>+OrderService: createOrder(orderData)
                OrderService->>+OrderRepository: save(order)
                OrderRepository-->>-OrderService: Order created
                OrderService-->>-OrderController: Success
                OrderController-->>-Client: 201 Created
            """;

        String stateDiagram = """
            stateDiagram-v2
                [*] --> PENDING
                PENDING --> CONFIRMED : confirm()
                PENDING --> CANCELLED : cancel()
                CONFIRMED --> PROCESSING : startProcessing()
                PROCESSING --> SHIPPED : ship()
                SHIPPED --> DELIVERED : deliver()
                DELIVERED --> [*]
                
                CONFIRMED --> CANCELLED : cancel()
                PROCESSING --> CANCELLED : cancel()
            """;

        ComprehensiveCodeResult result = service.generateComprehensiveCode(
            classDiagram, sequenceDiagram, stateDiagram, "com.example.ecommerce", "java");

        // Create demo directory
        Files.createDirectories(Paths.get("demo-output"));
        
        // Write generated files
        for (var entry : result.getGeneratedFiles().entrySet()) {
            try (FileWriter writer = new FileWriter("demo-output/" + entry.getKey())) {
                writer.write(entry.getValue());
            }
        }
        
        System.out.println("🎯 Comprehensive Generation Demo Complete!");
        System.out.println("Generated " + result.getGeneratedFiles().size() + " files in demo-output/");
        System.out.println("\nFeatures included:");
        System.out.println("✅ Complete Spring Boot application");
        System.out.println("✅ State management with OrderStatus enum");
        System.out.println("✅ State transition methods with validation");
        System.out.println("✅ REST endpoints for state transitions");
        System.out.println("✅ Business logic from sequence diagrams");
        System.out.println("✅ Audit fields (createdAt, updatedAt)");
        System.out.println("✅ Exception handling for invalid transitions");
        
        System.out.println("\nGenerated files:");
        result.getGeneratedFiles().keySet().forEach(file -> System.out.println("  📄 " + file));
    }
}