import com.basiccode.generator.parser.SimpleClassParser;
import com.basiccode.generator.model.*;
import com.basiccode.generator.generator.spring.SpringBootEntityGenerator;

public class TestMethodGeneration {
    public static void main(String[] args) {
        // Test the method parsing and generation
        SimpleClassParser parser = new SimpleClassParser();
        
        String classDiagram = """
            classDiagram
                class User {
                    +UUID id
                    +String username
                    +String email
                    +authenticate(password) boolean
                    +updateProfile(profile) void
                }
        """;
        
        try {
            Diagram diagram = parser.parseClassDiagram(classDiagram);
            
            if (!diagram.getClasses().isEmpty()) {
                ClassModel classModel = diagram.getClasses().get(0);
                
                System.out.println("=== PARSED CLASS ===");
                System.out.println("Class: " + classModel.getName());
                System.out.println("Attributes: " + classModel.getFields().size());
                System.out.println("Methods: " + (classModel.getMethods() != null ? classModel.getMethods().size() : 0));
                
                if (classModel.getMethods() != null) {
                    for (Method method : classModel.getMethods()) {
                        System.out.println("Method: " + method.getName() + " -> " + method.getReturnType());
                    }
                }
                
                // Test generation
                SpringBootEntityGenerator generator = new SpringBootEntityGenerator();
                EnhancedClass enhancedClass = new EnhancedClass();
                enhancedClass.setOriginalClass(new UmlClass(classModel.getName()));
                
                // Convert fields to attributes
                for (Field field : classModel.getFields()) {
                    UmlAttribute attr = new UmlAttribute();
                    attr.setName(field.getName());
                    attr.setType(field.getType());
                    enhancedClass.getOriginalClass().getAttributes().add(attr);
                }
                
                // Set methods
                enhancedClass.getOriginalClass().setMethods(classModel.getMethods());
                
                String generatedCode = generator.generateEntity(enhancedClass, "com.example.test");
                
                System.out.println("\n=== GENERATED CODE ===");
                System.out.println(generatedCode);
                
            } else {
                System.out.println("No classes found in diagram");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}