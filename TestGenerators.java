import com.basiccode.generator.generator.django.DjangoGeneratorFactory;
import com.basiccode.generator.generator.spring.SpringBootGeneratorFactory;
import com.basiccode.generator.generator.python.PythonGeneratorFactory;
import com.basiccode.generator.generator.csharp.CSharpGeneratorFactory;
import com.basiccode.generator.generator.typescript.TypeScriptGeneratorFactory;
import com.basiccode.generator.generator.php.PhpGeneratorFactory;
import com.basiccode.generator.model.*;
import java.util.Arrays;

public class TestGenerators {
    
    public static void main(String[] args) {
        System.out.println("🚀 Testing All Code Generators");
        System.out.println("=" .repeat(60));
        
        // Create test data
        EnhancedClass testClass = createTestClass();
        String packageName = "com.test.app";
        
        // Test each language
        testLanguage("Java Spring Boot", testClass, packageName, "java");
        testLanguage("Django REST", testClass, packageName, "django");
        testLanguage("Python FastAPI", testClass, packageName, "python");
        testLanguage("C# .NET Core", testClass, packageName, "csharp");
        testLanguage("TypeScript Express", testClass, packageName, "typescript");
        testLanguage("PHP Slim", testClass, packageName, "php");
        
        System.out.println("\n🎉 All generators tested successfully!");
    }
    
    private static void testLanguage(String languageName, EnhancedClass testClass, String packageName, String language) {
        System.out.println("\n📋 Testing: " + languageName);
        System.out.println("-" .repeat(40));
        
        try {
            long startTime = System.currentTimeMillis();
            
            switch (language) {
                case "java":
                    testJavaGeneration(testClass, packageName);
                    break;
                case "django":
                    testDjangoGeneration(testClass, packageName);
                    break;
                case "python":
                    testPythonGeneration(testClass, packageName);
                    break;
                case "csharp":
                    testCSharpGeneration(testClass, packageName);
                    break;
                case "typescript":
                    testTypeScriptGeneration(testClass, packageName);
                    break;
                case "php":
                    testPhpGeneration(testClass, packageName);
                    break;
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("⏱️  Generation time: " + (endTime - startTime) + "ms");
            System.out.println("✅ Status: SUCCESS");
            
        } catch (Exception e) {
            System.out.println("❌ Status: FAILED - " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testJavaGeneration(EnhancedClass testClass, String packageName) {
        var factory = new SpringBootGeneratorFactory();
        
        String entity = factory.createEntityGenerator().generateEntity(testClass, packageName);
        String service = factory.createServiceGenerator().generateService(testClass, packageName);
        String repository = factory.createRepositoryGenerator().generateRepository(testClass, packageName);
        String controller = factory.createControllerGenerator().generateController(testClass, packageName);
        
        assert entity.contains("@Entity") : "Java entity missing @Entity annotation";
        assert service.contains("@Service") : "Java service missing @Service annotation";
        assert repository.contains("@Repository") : "Java repository missing @Repository annotation";
        assert controller.contains("@RestController") : "Java controller missing @RestController annotation";
        
        System.out.println("📄 Generated: Entity, Service, Repository, Controller");
    }
    
    private static void testDjangoGeneration(EnhancedClass testClass, String packageName) {
        var factory = new DjangoGeneratorFactory();
        
        String entity = factory.createEntityGenerator().generateEntity(testClass, packageName);
        String service = factory.createServiceGenerator().generateService(testClass, packageName);
        String repository = factory.createRepositoryGenerator().generateRepository(testClass, packageName);
        String controller = factory.createControllerGenerator().generateController(testClass, packageName);
        
        assert entity.contains("models.Model") : "Django model missing models.Model inheritance";
        assert service.contains("ViewSet") : "Django service missing ViewSet";
        assert repository.contains("Serializer") : "Django repository missing Serializer";
        assert controller.contains("router") : "Django controller missing router";
        
        System.out.println("📄 Generated: Model, ViewSet, Serializer, URLs");
    }
    
    private static void testPythonGeneration(EnhancedClass testClass, String packageName) {
        var factory = new PythonGeneratorFactory();
        
        String entity = factory.createEntityGenerator().generateEntity(testClass, packageName);
        String service = factory.createServiceGenerator().generateService(testClass, packageName);
        String repository = factory.createRepositoryGenerator().generateRepository(testClass, packageName);
        String controller = factory.createControllerGenerator().generateController(testClass, packageName);
        
        assert entity.contains("BaseModel") : "Python entity missing BaseModel";
        assert service.contains("class") : "Python service missing class definition";
        assert repository.contains("class") : "Python repository missing class definition";
        assert controller.contains("@router") : "Python controller missing @router decorator";
        
        System.out.println("📄 Generated: Pydantic Model, Service, Repository, FastAPI Router");
    }
    
    private static void testCSharpGeneration(EnhancedClass testClass, String packageName) {
        var factory = new CSharpGeneratorFactory();
        
        String entity = factory.createEntityGenerator().generateEntity(testClass, packageName);
        String service = factory.createServiceGenerator().generateService(testClass, packageName);
        String repository = factory.createRepositoryGenerator().generateRepository(testClass, packageName);
        String controller = factory.createControllerGenerator().generateController(testClass, packageName);
        
        assert entity.contains("public class") : "C# entity missing public class";
        assert service.contains("interface") : "C# service missing interface";
        assert repository.contains("interface") : "C# repository missing interface";
        assert controller.contains("[ApiController]") : "C# controller missing [ApiController] attribute";
        
        System.out.println("📄 Generated: Entity, Service Interface/Implementation, Repository, Controller");
    }
    
    private static void testTypeScriptGeneration(EnhancedClass testClass, String packageName) {
        var factory = new TypeScriptGeneratorFactory();
        
        String entity = factory.createEntityGenerator().generateEntity(testClass, packageName);
        String service = factory.createServiceGenerator().generateService(testClass, packageName);
        String repository = factory.createRepositoryGenerator().generateRepository(testClass, packageName);
        String controller = factory.createControllerGenerator().generateController(testClass, packageName);
        
        assert entity.contains("export") : "TypeScript entity missing export";
        assert service.contains("export class") : "TypeScript service missing export class";
        assert repository.contains("export class") : "TypeScript repository missing export class";
        assert controller.contains("export class") : "TypeScript controller missing export class";
        
        System.out.println("📄 Generated: Interface, Service, Repository, Express Controller");
    }
    
    private static void testPhpGeneration(EnhancedClass testClass, String packageName) {
        var factory = new PhpGeneratorFactory();
        
        String entity = factory.createEntityGenerator().generateEntity(testClass, packageName);
        String service = factory.createServiceGenerator().generateService(testClass, packageName);
        String repository = factory.createRepositoryGenerator().generateRepository(testClass, packageName);
        String controller = factory.createControllerGenerator().generateController(testClass, packageName);
        
        assert entity.contains("class") : "PHP entity missing class";
        assert service.contains("class") : "PHP service missing class";
        assert repository.contains("class") : "PHP repository missing class";
        assert controller.contains("class") : "PHP controller missing class";
        
        System.out.println("📄 Generated: Entity, Service, Repository, Slim Controller");
    }
    
    private static EnhancedClass createTestClass() {
        // Create test UML class
        UmlClass umlClass = new UmlClass("User");
        
        // Add attributes manually
        UmlAttribute id = new UmlAttribute("id", "UUID");
        UmlAttribute username = new UmlAttribute("username", "String");
        UmlAttribute email = new UmlAttribute("email", "String");
        UmlAttribute status = new UmlAttribute("status", "UserStatus");
        
        umlClass.setAttributes(Arrays.asList(id, username, email, status));
        
        // Create enhanced class
        EnhancedClass enhanced = new EnhancedClass(umlClass);
        enhanced.setStateful(true);
        
        // Add behavioral method
        BusinessMethod method = new BusinessMethod();
        method.setName("validateUser");
        method.setReturnType("boolean");
        method.setBusinessLogic(Arrays.asList("Check email format", "Validate username uniqueness"));
        
        enhanced.setBehavioralMethods(Arrays.asList(method));
        
        return enhanced;
    }
}