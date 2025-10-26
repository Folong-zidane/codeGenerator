import com.basiccode.generator.enhanced.EnhancedEntityGenerator;
import com.basiccode.generator.model.*;
import com.basiccode.generator.config.Framework;
import com.squareup.javapoet.JavaFile;
import java.util.Arrays;

public class TestPhpGeneration {
    public static void main(String[] args) {
        // Create a simple test class
        ClassModel user = new ClassModel("User");
        user.addField(new Field("username", "String", false, false));
        user.addField(new Field("email", "String", false, false));
        
        // Generate PHP entity
        EnhancedEntityGenerator generator = new EnhancedEntityGenerator();
        JavaFile phpFile = generator.generateEntity(user, "com.example", Framework.PHP_LARAVEL);
        
        // Extract PHP code from JavaDoc
        String phpCode = phpFile.typeSpec.javadoc.toString();
        phpCode = phpCode.replace("Generated PHP Entity:\n", "").trim();
        
        System.out.println("=== Generated PHP Code ===");
        System.out.println(phpCode);
    }
}