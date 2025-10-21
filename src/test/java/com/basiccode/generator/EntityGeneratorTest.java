package com.basiccode.generator;

import com.basiccode.generator.generator.EntityGenerator;
import com.basiccode.generator.model.*;
import com.squareup.javapoet.JavaFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityGeneratorTest {
    
    private EntityGenerator generator;
    
    @BeforeEach
    void setup() {
        generator = new EntityGenerator();
    }
    
    @Test
    @DisplayName("Should generate Java entity with all annotations")
    void shouldGenerateJavaEntity() {
        // Given
        ClassModel model = createTestModel();
        
        // When
        JavaFile javaFile = generator.generateEntity(model, "com.test", "java");
        
        // Then
        String generatedCode = javaFile.toString();
        
        assertThat(generatedCode)
            .contains("@Entity")
            .contains("@Table(name = \"user\")")
            .contains("private UUID id")
            .contains("private String username")
            .contains("private String email")
            .contains("@Id")
            .contains("@GeneratedValue")
            .contains("public String getUsername()")
            .contains("public void setUsername(String username)");
    }
    
    @Test
    @DisplayName("Should generate Python entity code")
    void shouldGeneratePythonEntity() {
        // Given
        ClassModel model = createTestModel();
        
        // When
        JavaFile javaFile = generator.generateEntity(model, "com.test", "python");
        
        // Then
        String generatedCode = javaFile.toString();
        
        assertThat(generatedCode)
            .contains("from sqlalchemy")
            .contains("class User(Base)")
            .contains("__tablename__ = 'user'")
            .contains("username = Column(String)")
            .contains("email = Column(String)");
    }
    
    @Test
    @DisplayName("Should generate C# entity code")
    void shouldGenerateCSharpEntity() {
        // Given
        ClassModel model = createTestModel();
        
        // When
        JavaFile javaFile = generator.generateEntity(model, "com.test", "csharp");
        
        // Then
        String generatedCode = javaFile.toString();
        
        assertThat(generatedCode)
            .contains("using System")
            .contains("public class User")
            .contains("[Table(\"user\")]")
            .contains("public string Username { get; set; }")
            .contains("public string Email { get; set; }");
    }
    
    private ClassModel createTestModel() {
        ClassModel model = new ClassModel();
        model.setName("User");
        model.setPackageName("com.test");
        
        // Add fields
        Field usernameField = new Field();
        usernameField.setName("username");
        usernameField.setType("String");
        usernameField.setUnique(true);
        usernameField.setNullable(false);
        model.addField(usernameField);
        
        Field emailField = new Field();
        emailField.setName("email");
        emailField.setType("String");
        emailField.setNullable(false);
        model.addField(emailField);
        
        // Add methods
        Method validateMethod = new Method();
        validateMethod.setName("validateEmail");
        validateMethod.setReturnType("void");
        model.addMethod(validateMethod);
        
        Method changePasswordMethod = new Method();
        changePasswordMethod.setName("changePassword");
        changePasswordMethod.setReturnType("void");
        Parameter param = new Parameter();
        param.setName("newPassword");
        param.setType("String");
        changePasswordMethod.getParameters().add(param);
        model.addMethod(changePasswordMethod);
        
        return model;
    }
}