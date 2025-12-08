package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.generator.InheritanceAwareEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import com.basiccode.generator.model.Method;
import com.basiccode.generator.model.Parameter;
import java.util.*;
import java.util.UUID;

/**
 * Spring Boot specific entity generator with inheritance support
 */
public class SpringBootEntityGenerator implements InheritanceAwareEntityGenerator {
    
    // Track generated fields to avoid duplications
    private Set<String> generatedFields;
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        // Initialize field tracker for each entity generation
        generatedFields = new HashSet<>();
        
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("package ").append(packageName).append(".entity;\n\n");
        code.append("import javax.persistence.*;\n");
        code.append("import java.time.LocalDateTime;\n");
        code.append("import java.util.UUID;\n");
        code.append("import javax.validation.constraints.*;\n\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("import ").append(packageName).append(".enums.").append(enumName).append(";\n\n");
        }
        
        // Handle inheritance and class type
        if (enhancedClass.getOriginalClass().isInterface()) {
            // Generate interface
            code.append("public interface ").append(className).append(" {\n\n");
            generateInterfaceMethods(code, enhancedClass);
            code.append("}\n");
            return code.toString();
        } else if (enhancedClass.getOriginalClass().isAbstract()) {
            // Generate abstract class
            code.append("@MappedSuperclass\n");
            code.append("public abstract class ").append(className).append(" {\n\n");
        } else {
            // Generate concrete entity
            code.append("@Entity\n");
            code.append("@Table(name = \"").append(pluralize(className.toLowerCase())).append("\")\n");
            
            String superClass = enhancedClass.getOriginalClass().getSuperClass();
            if (superClass != null && !superClass.isEmpty()) {
                code.append("public class ").append(className).append(" extends ").append(superClass).append(" {\n\n");
            } else {
                code.append("public class ").append(className).append(" {\n\n");
            }
        }
        
        // Generate fields (skip inherited fields if has superclass)
        String superClass = enhancedClass.getOriginalClass().getSuperClass();
        boolean hasInheritedFields = superClass != null && !superClass.isEmpty();
        
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            // Skip duplicates
            if (generatedFields.contains(attr.getName())) {
                continue;
            }
            
            // Skip common inherited fields
            if (hasInheritedFields && isInheritedField(attr.getName())) {
                continue;
            }
            
            // Mark field as generated
            generatedFields.add(attr.getName());
            
            if ("id".equals(attr.getName())) {
                code.append("    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            
            // Add validation annotations (more selective)
            if ("String".equals(attr.getType()) && !"id".equals(attr.getName())) {
                // Only add @NotBlank for required fields (not description, notes, etc.)
                if (isRequiredField(attr.getName())) {
                    code.append("    @NotBlank\n");
                }
            }
            if ("email".equals(attr.getName().toLowerCase())) {
                code.append("    @Email\n");
            }
            
            // Generate JPA relationship annotations
            if (isRelationshipField(attr)) {
                generateJpaRelationField(code, attr, packageName);
            } else if (attr.isRelationship()) {
                generateJpaRelationshipAnnotation(code, attr, className);
            } else {
                code.append("    @Column\n");
                code.append("    private ").append(attr.getType()).append(" ").append(attr.getName()).append(";\n\n");
            }
        }
        
        // Add state field if stateful (check for duplicates)
        if (enhancedClass.isStateful() && !generatedFields.contains("status")) {
            generatedFields.add("status");
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("    @Enumerated(EnumType.STRING)\n");
            code.append("    @Column(name = \"status\")\n");
            code.append("    private ").append(enumName).append(" status;\n\n");
        }
        
        // Add audit fields only if not inherited and not already generated
        if (!hasInheritedFields && !generatedFields.contains("createdAt")) {
            generatedFields.add("createdAt");
            generatedFields.add("updatedAt");
            code.append("    @Column(name = \"created_at\")\n");
            code.append("    private LocalDateTime createdAt;\n\n");
            code.append("    @Column(name = \"updated_at\")\n");
            code.append("    private LocalDateTime updatedAt;\n\n");
        }
        
        // Generate getters and setters
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            generateGetterSetter(code, attr.getType(), attr.getName());
        }
        
        // Add state getters/setters if stateful
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            generateGetterSetter(code, enumName, "status");
        }
        
        // Add audit field getters/setters only if not inherited
        if (!hasInheritedFields) {
            generateGetterSetter(code, "LocalDateTime", "createdAt");
            generateGetterSetter(code, "LocalDateTime", "updatedAt");
        }
        
        // Add business methods from diagram
        generateBusinessMethods(code, enhancedClass, className);
        
        // Add state transition methods if stateful
        if (enhancedClass.isStateful()) {
            generateStateTransitionMethods(code, enhancedClass);
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    @Override
    public String generateStateEnum(EnhancedClass enhancedClass, String packageName) {
        if (!enhancedClass.isStateful()) return "";
        
        StringBuilder code = new StringBuilder();
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        code.append("package ").append(packageName).append(".enums;\n\n");
        code.append("public enum ").append(enumName).append(" {\n");
        
        if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
            for (int i = 0; i < enhancedClass.getStateEnum().getValues().size(); i++) {
                var value = enhancedClass.getStateEnum().getValues().get(i);
                code.append("    ").append(value.getName());
                if (i < enhancedClass.getStateEnum().getValues().size() - 1) {
                    code.append(",");
                }
                code.append("\n");
            }
        } else {
            code.append("    ACTIVE,\n");
            code.append("    INACTIVE,\n");
            code.append("    SUSPENDED\n");
        }
        
        code.append("}\n");
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
    
    @Override
    public String getEntityDirectory() {
        return "entity";
    }
    
    private void generateGetterSetter(StringBuilder code, String type, String fieldName) {
        String capitalizedName = capitalize(fieldName);
        
        // Getter
        code.append("    public ").append(type).append(" get").append(capitalizedName).append("() {\n");
        code.append("        return ").append(fieldName).append(";\n");
        code.append("    }\n\n");
        
        // Setter
        code.append("    public void set").append(capitalizedName).append("(").append(type).append(" ").append(fieldName).append(") {\n");
        code.append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        code.append("    }\n\n");
    }
    
    private void generateStateTransitionMethods(StringBuilder code, EnhancedClass enhancedClass) {
        // Generate methods from state diagram if available
        if (enhancedClass.getStateTransitionMethods() != null && !enhancedClass.getStateTransitionMethods().isEmpty()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : enhancedClass.getOriginalClass().getName() + "Status";
            
            for (com.basiccode.generator.model.StateTransitionMethod transitionMethod : enhancedClass.getStateTransitionMethods()) {
                String methodName = transitionMethod.getName();
                var transitions = transitionMethod.getTransitions();
                
                code.append("    public void ").append(methodName).append("() {\n");
                
                if (transitions != null && transitions.size() == 1) {
                    var transition = transitions.get(0);
                    code.append("        if (this.status != ").append(enumName).append(".").append(transition.getFromState()).append(") {\n");
                    code.append("            throw new IllegalStateException(\"Cannot ").append(methodName).append(" from state: \" + this.status);\n");
                    code.append("        }\n");
                    code.append("        this.status = ").append(enumName).append(".").append(transition.getToState()).append(";\n");
                } else if (transitions != null && transitions.size() > 1) {
                    code.append("        switch (this.status) {\n");
                    for (var transition : transitions) {
                        code.append("            case ").append(transition.getFromState()).append(":\n");
                        code.append("                this.status = ").append(enumName).append(".").append(transition.getToState()).append(";\n");
                        code.append("                break;\n");
                    }
                    code.append("            default:\n");
                    code.append("                throw new IllegalStateException(\"Cannot ").append(methodName).append(" from state: \" + this.status);\n");
                    code.append("        }\n");
                }
                
                code.append("        this.updatedAt = LocalDateTime.now();\n");
                code.append("    }\n\n");
            }
        } else {
            // Fallback to default methods if no state diagram
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : enhancedClass.getOriginalClass().getName() + "Status";
            
            code.append("    public void suspend() {\n");
            code.append("        if (this.status != ").append(enumName).append(".ACTIVE) {\n");
            code.append("            throw new IllegalStateException(\"Cannot suspend from state: \" + this.status);\n");
            code.append("        }\n");
            code.append("        this.status = ").append(enumName).append(".SUSPENDED;\n");
            code.append("        this.updatedAt = LocalDateTime.now();\n");
            code.append("    }\n\n");
            
            code.append("    public void activate() {\n");
            code.append("        if (this.status != ").append(enumName).append(".SUSPENDED) {\n");
            code.append("            throw new IllegalStateException(\"Cannot activate from state: \" + this.status);\n");
            code.append("        }\n");
            code.append("        this.status = ").append(enumName).append(".ACTIVE;\n");
            code.append("        this.updatedAt = LocalDateTime.now();\n");
            code.append("    }\n\n");
        }
    }
    
    private void generateBusinessMethods(StringBuilder code, EnhancedClass enhancedClass, String className) {
        Set<String> existingMethods = new HashSet<>();
        
        // Generate methods from UML diagram first
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (Method method : enhancedClass.getOriginalClass().getMethods()) {
                generateBusinessMethod(code, method, className);
                existingMethods.add(method.getName().toLowerCase());
            }
        }
        
        // Generate common business methods based on class name (avoid duplicates)
        if ("User".equals(className) && !existingMethods.contains("validateEmail")) {
            generateUserBusinessMethods(code, enhancedClass);
        } else if ("Product".equals(className) && !existingMethods.contains("updateStock")) {
            generateProductBusinessMethods(code, enhancedClass);
        } else if ("Order".equals(className) && !existingMethods.contains("calculateTotal")) {
            generateOrderBusinessMethods(code, enhancedClass);
        }
    }
    
    private void generateBusinessMethod(StringBuilder code, Method method, String className) {
        String returnType = method.getReturnType() != null ? method.getReturnType() : "void";
        
        code.append("    public ").append(returnType).append(" ").append(method.getName()).append("(");
        
        // Add parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                Parameter param = method.getParameters().get(i);
                String paramType = param.getType() != null ? param.getType() : "String";
                code.append(paramType).append(" ").append(param.getName());
                if (i < method.getParameters().size() - 1) {
                    code.append(", ");
                }
            }
        }
        
        code.append(") {\n");
        
        // Generate method body based on method name and return type
        generateMethodBody(code, method, className, returnType);
        
        code.append("    }\n\n");
    }
    
    private void generateMethodBody(StringBuilder code, Method method, String className, String returnType) {
        String methodName = method.getName();
        
        // Generate specific implementations based on method name
        switch (methodName.toLowerCase()) {
            case "authenticate":
                code.append("        if (password == null || password.isEmpty()) {\n");
                code.append("            return false;\n");
                code.append("        }\n");
                code.append("        // TODO: Implement password verification logic\n");
                code.append("        return true;\n");
                break;
                
            case "updateprofile":
                code.append("        if (profile == null) {\n");
                code.append("            throw new IllegalArgumentException(\"Profile cannot be null\");\n");
                code.append("        }\n");
                code.append("        // TODO: Update user profile fields\n");
                code.append("        this.updatedAt = java.time.LocalDateTime.now();\n");
                break;
                
            case "calculatetotal":
                code.append("        // TODO: Calculate order total\n");
                if ("void".equals(returnType)) {
                    code.append("        this.updatedAt = java.time.LocalDateTime.now();\n");
                } else {
                    code.append("        return 0.0;\n");
                }
                break;
                
            case "decreasestock":
            case "increasestock":
                code.append("        // TODO: Update stock quantity\n");
                code.append("        this.updatedAt = java.time.LocalDateTime.now();\n");
                break;
                
            default:
                code.append("        // TODO: Implement ").append(methodName).append(" logic\n");
                if (!"void".equals(returnType)) {
                    if ("boolean".equals(returnType)) {
                        code.append("        return false;\n");
                    } else if ("String".equals(returnType)) {
                        code.append("        return \"\";\n");
                    } else if (returnType.contains("int") || returnType.contains("Integer")) {
                        code.append("        return 0;\n");
                    } else if (returnType.contains("double") || returnType.contains("Double") || returnType.contains("Float")) {
                        code.append("        return 0.0;\n");
                    } else {
                        code.append("        return null;\n");
                    }
                }
                break;
        }
    }
    
    private void generateUserBusinessMethods(StringBuilder code, EnhancedClass enhancedClass) {
        // Only generate validateEmail if email field exists
        if (hasField(enhancedClass, "email")) {
            code.append("    public boolean validateEmail() {\n");
            code.append("        if (this.email == null || this.email.isBlank()) {\n");
            code.append("            throw new IllegalArgumentException(\"Email cannot be empty\");\n");
            code.append("        }\n");
            code.append("        String emailRegex = \"^[A-Za-z0-9+_.-]+@(.+)$\";\n");
            code.append("        return this.email.matches(emailRegex);\n");
            code.append("    }\n\n");
        }
        
        // changePassword() method
        code.append("    public void changePassword(String newPassword) {\n");
        code.append("        if (newPassword == null || newPassword.length() < 8) {\n");
        code.append("            throw new IllegalArgumentException(\"Password must be at least 8 characters\");\n");
        code.append("        }\n");
        code.append("        // TODO: Hash password with BCrypt\n");
        code.append("        this.updatedAt = java.time.LocalDateTime.now();\n");
        code.append("    }\n\n");
    }
    
    private void generateProductBusinessMethods(StringBuilder code, EnhancedClass enhancedClass) {
        // Only generate updateStock if stock field exists
        if (hasField(enhancedClass, "stock")) {
            code.append("    public void updateStock(Integer newQuantity) {\n");
            code.append("        if (newQuantity == null || newQuantity < 0) {\n");
            code.append("            throw new IllegalArgumentException(\"Stock cannot be negative\");\n");
            code.append("        }\n");
            code.append("        this.stock = newQuantity;\n");
            code.append("        this.updatedAt = java.time.LocalDateTime.now();\n");
            code.append("    }\n\n");
        }
    }
    
    private void generateOrderBusinessMethods(StringBuilder code, EnhancedClass enhancedClass) {
        // calculateTotal() method
        code.append("    public Float calculateTotal(java.util.List<Product> products) {\n");
        code.append("        if (products == null || products.isEmpty()) {\n");
        code.append("            throw new IllegalArgumentException(\"Cannot calculate total: no products\");\n");
        code.append("        }\n");
        code.append("        Float total = products.stream()\n");
        code.append("            .map(Product::getPrice)\n");
        code.append("            .reduce(0F, Float::sum);\n");
        code.append("        this.total = total;\n");
        code.append("        return total;\n");
        code.append("    }\n\n");
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private boolean isRequiredField(String fieldName) {
        // Define which fields should be required
        Set<String> requiredFields = Set.of("name", "title", "email", "username", "firstName", "lastName");
        return requiredFields.contains(fieldName.toLowerCase());
    }
    
    private boolean hasField(EnhancedClass enhancedClass, String fieldName) {
        return enhancedClass.getOriginalClass().getAttributes().stream()
            .anyMatch(attr -> fieldName.equalsIgnoreCase(attr.getName()));
    }
    
    private boolean isInheritedField(String fieldName) {
        // Common fields that are typically inherited
        Set<String> inheritedFields = Set.of("id", "createdAt", "updatedAt", "version");
        return inheritedFields.contains(fieldName);
    }
    
    private boolean isRelationshipField(UmlAttribute attr) {
        // Detect UUID fields with _id suffix as relationships
        return (attr.getType().equals("UUID") && attr.getName().endsWith("_id")) ||
               (attr.getType().equals("UUID") && attr.getName().endsWith("Id"));
    }
    
    private void generateJpaRelationField(StringBuilder code, UmlAttribute attr, String packageName) {
        String fieldName = attr.getName();
        String entityName;
        
        // Handle both userId and user_id formats
        if (fieldName.endsWith("_id")) {
            entityName = fieldName.substring(0, fieldName.length() - 3);
        } else if (fieldName.endsWith("Id")) {
            entityName = fieldName.substring(0, fieldName.length() - 2);
        } else {
            return; // Not a relationship field
        }
        
        String targetClass = toPascalCase(entityName);
        
        // Generate JPA relationship
        code.append("    @ManyToOne(fetch = FetchType.LAZY)\n");
        code.append("    @JoinColumn(name = \"").append(fieldName).append("\")\n");
        code.append("    private ").append(targetClass).append(" ").append(entityName).append(";\n\n");
    }
    
    private String toPascalCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) return snakeCase;
        
        String[] parts = snakeCase.split("_");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                result.append(capitalize(part));
            }
        }
        return result.toString();
    }
    
    private String pluralize(String word) {
        if (word == null || word.isEmpty()) return word;
        
        word = word.toLowerCase();
        
        // English pluralization rules
        if (word.endsWith("y") && !isVowel(word.charAt(word.length() - 2))) {
            return word.substring(0, word.length() - 1) + "ies"; // category -> categories
        } else if (word.endsWith("s") || word.endsWith("x") || word.endsWith("z") || 
                   word.endsWith("ch") || word.endsWith("sh")) {
            return word + "es"; // class -> classes
        } else if (word.endsWith("f")) {
            return word.substring(0, word.length() - 1) + "ves"; // leaf -> leaves
        } else if (word.endsWith("fe")) {
            return word.substring(0, word.length() - 2) + "ves"; // knife -> knives
        } else {
            return word + "s"; // user -> users
        }
    }
    
    private boolean isVowel(char c) {
        return "aeiou".indexOf(Character.toLowerCase(c)) >= 0;
    }
    
    @Override
    public String generateInheritanceDeclaration(EnhancedClass enhancedClass, String className) {
        if (enhancedClass.getOriginalClass().isInterface()) {
            return "public interface " + className;
        } else if (enhancedClass.getOriginalClass().isAbstract()) {
            return "@MappedSuperclass\npublic abstract class " + className;
        } else {
            String superClass = enhancedClass.getOriginalClass().getSuperClass();
            if (superClass != null && !superClass.isEmpty()) {
                return "@Entity\n@Table(name = \"" + className.toLowerCase() + "s\")\npublic class " + className + " extends " + superClass;
            } else {
                return "@Entity\n@Table(name = \"" + className.toLowerCase() + "s\")\npublic class " + className;
            }
        }
    }
    
    private void generateInterfaceMethods(StringBuilder code, EnhancedClass enhancedClass) {
        // Generate interface method signatures
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (Method method : enhancedClass.getOriginalClass().getMethods()) {
                String returnType = method.getReturnType() != null ? method.getReturnType() : "void";
                code.append("    ").append(returnType).append(" ").append(method.getName()).append("(");
                
                if (method.getParameters() != null && !method.getParameters().isEmpty()) {
                    for (int i = 0; i < method.getParameters().size(); i++) {
                        Parameter param = method.getParameters().get(i);
                        String paramType = param.getType() != null ? param.getType() : "String";
                        code.append(paramType).append(" ").append(param.getName());
                        if (i < method.getParameters().size() - 1) {
                            code.append(", ");
                        }
                    }
                }
                
                code.append(");\n\n");
            }
        }
    }
    
    private void generateJpaRelationshipAnnotation(StringBuilder code, UmlAttribute attr, String currentClassName) {
        String relationshipType = attr.getRelationshipType();
        String targetClass = attr.getTargetClass();
        
        switch (relationshipType) {
            case "OneToMany":
                code.append("    @OneToMany(mappedBy = \"").append(currentClassName.toLowerCase()).append("\", cascade = CascadeType.ALL, fetch = FetchType.LAZY)\n");
                break;
            case "ManyToOne":
                code.append("    @ManyToOne(fetch = FetchType.LAZY)\n");
                code.append("    @JoinColumn(name = \"").append(targetClass.toLowerCase()).append("_id\")\n");
                break;
            case "OneToOne":
                code.append("    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)\n");
                code.append("    @JoinColumn(name = \"").append(targetClass.toLowerCase()).append("_id\")\n");
                break;
            case "ManyToMany":
                code.append("    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)\n");
                code.append("    @JoinTable(\n");
                code.append("        name = \"").append(currentClassName.toLowerCase()).append("_").append(targetClass.toLowerCase()).append("\",\n");
                code.append("        joinColumns = @JoinColumn(name = \"").append(currentClassName.toLowerCase()).append("_id\"),\n");
                code.append("        inverseJoinColumns = @JoinColumn(name = \"").append(targetClass.toLowerCase()).append("_id\")\n");
                code.append("    )\n");
                break;
        }
    }
}