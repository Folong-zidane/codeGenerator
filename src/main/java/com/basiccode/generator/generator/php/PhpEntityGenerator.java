package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;

public class PhpEntityGenerator implements IEntityGenerator {
    
    @Override
    public String generateEntity(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Models;\n\n");
        code.append("use Illuminate\\Database\\Eloquent\\Factories\\HasFactory;\n");
        code.append("use Illuminate\\Database\\Eloquent\\Model;\n");
        code.append("use Carbon\\Carbon;\n");
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("use ").append(packageName).append("\\Enums\\").append(enumName).append(";\n");
        }
        
        code.append("\nclass ").append(className).append(" extends Model\n");
        code.append("{\n");
        code.append("    use HasFactory;\n\n");
        
        code.append("    protected $table = '").append(className.toLowerCase()).append("s';\n\n");
        
        // Fillable fields
        code.append("    protected $fillable = [\n");
        for (int i = 0; i < enhancedClass.getOriginalClass().getAttributes().size(); i++) {
            UmlAttribute attr = enhancedClass.getOriginalClass().getAttributes().get(i);
            if (!"id".equalsIgnoreCase(attr.getName())) {
                code.append("        '").append(attr.getName()).append("'");
                if (enhancedClass.isStateful() || i < enhancedClass.getOriginalClass().getAttributes().size() - 1) {
                    code.append(",");
                }
                code.append("\n");
            }
        }
        
        if (enhancedClass.isStateful()) {
            code.append("        'status',\n");
        }
        
        code.append("    ];\n\n");
        
        // Casts
        code.append("    protected $casts = [\n");
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            String phpType = mapCastType(attr.getType());
            if (!phpType.equals("string")) {
                code.append("        '").append(attr.getName()).append("' => '").append(phpType).append("',\n");
            }
        }
        
        if (enhancedClass.isStateful()) {
            String enumName = enhancedClass.getStateEnum() != null 
                ? enhancedClass.getStateEnum().getName() 
                : className + "Status";
            code.append("        'status' => ").append(enumName).append("::class,\n");
        }
        
        code.append("        'created_at' => 'datetime',\n");
        code.append("        'updated_at' => 'datetime',\n");
        code.append("    ];\n\n");
        
        // Add relationships
        generatePhpRelationships(code, enhancedClass, className);
        
        // Add business methods from UML diagram
        generateBusinessMethods(code, enhancedClass, className);
        
        // State transition methods if stateful
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
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Enums;\n\n");
        code.append("enum ").append(enumName).append(": string\n");
        code.append("{\n");
        
        if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
            for (var value : enhancedClass.getStateEnum().getValues()) {
                code.append("    case ").append(value.getName()).append(" = '").append(value.getName()).append("';\n");
            }
        } else {
            code.append("    case ACTIVE = 'ACTIVE';\n");
            code.append("    case INACTIVE = 'INACTIVE';\n");
            code.append("    case SUSPENDED = 'SUSPENDED';\n");
        }
        
        code.append("\n    public function label(): string\n");
        code.append("    {\n");
        code.append("        return match($this) {\n");
        
        if (enhancedClass.getStateEnum() != null && enhancedClass.getStateEnum().getValues() != null) {
            for (var value : enhancedClass.getStateEnum().getValues()) {
                code.append("            self::").append(value.getName()).append(" => '").append(capitalize(value.getName().toLowerCase())).append("',\n");
            }
        } else {
            code.append("            self::ACTIVE => 'Active',\n");
            code.append("            self::INACTIVE => 'Inactive',\n");
            code.append("            self::SUSPENDED => 'Suspended',\n");
        }
        
        code.append("        };\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return ".php";
    }
    
    @Override
    public String getEntityDirectory() {
        return "Models";
    }
    
    private String mapCastType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "long", "integer", "int" -> "integer";
            case "float", "double" -> "decimal:2";
            case "boolean" -> "boolean";
            case "date", "datetime" -> "datetime";
            default -> "string";
        };
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
    private void generateStateTransitionMethods(StringBuilder code, EnhancedClass enhancedClass) {
        String enumName = enhancedClass.getStateEnum() != null 
            ? enhancedClass.getStateEnum().getName() 
            : enhancedClass.getOriginalClass().getName() + "Status";
        
        // Suspend method
        code.append("    public function suspend(): void\n");
        code.append("    {\n");
        code.append("        if ($this->status !== ").append(enumName).append("::ACTIVE) {\n");
        code.append("            throw new \\InvalidArgumentException('Cannot suspend entity in state: ' . $this->status->value);\n");
        code.append("        }\n");
        code.append("        \n");
        code.append("        $this->status = ").append(enumName).append("::SUSPENDED;\n");
        code.append("        $this->updated_at = Carbon::now();\n");
        code.append("        $this->save();\n");
        code.append("    }\n\n");
        
        // Activate method
        code.append("    public function activate(): void\n");
        code.append("    {\n");
        code.append("        if ($this->status !== ").append(enumName).append("::SUSPENDED) {\n");
        code.append("            throw new \\InvalidArgumentException('Cannot activate entity in state: ' . $this->status->value);\n");
        code.append("        }\n");
        code.append("        \n");
        code.append("        $this->status = ").append(enumName).append("::ACTIVE;\n");
        code.append("        $this->updated_at = Carbon::now();\n");
        code.append("        $this->save();\n");
        code.append("    }\n\n");
    }
    
    private void generateBusinessMethods(StringBuilder code, EnhancedClass enhancedClass, String className) {
        // Generate methods from UML diagram
        if (enhancedClass.getOriginalClass().getMethods() != null) {
            for (var method : enhancedClass.getOriginalClass().getMethods()) {
                generateBusinessMethod(code, method, className);
            }
        }
    }
    
    private void generateBusinessMethod(StringBuilder code, com.basiccode.generator.model.Method method, String className) {
        String returnType = method.getReturnType() != null ? mapPhpReturnType(method.getReturnType()) : "void";
        
        code.append("    public function ").append(method.getName()).append("(");
        
        // Add parameters
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            for (int i = 0; i < method.getParameters().size(); i++) {
                var param = method.getParameters().get(i);
                String paramType = param.getType() != null ? mapPhpType(param.getType()) : "string";
                code.append(paramType).append(" $").append(param.getName());
                if (i < method.getParameters().size() - 1) {
                    code.append(", ");
                }
            }
        }
        
        code.append("): ").append(returnType).append("\n");
        code.append("    {\n");
        
        // Generate method body based on method name
        generatePhpMethodBody(code, method, className, returnType);
        
        code.append("    }\n\n");
    }
    
    private String mapPhpType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "long", "integer", "int" -> "int";
            case "float", "double" -> "float";
            case "boolean" -> "bool";
            default -> "string";
        };
    }
    
    private String mapPhpReturnType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "long", "integer", "int" -> "int";
            case "float", "double" -> "float";
            case "boolean" -> "bool";
            case "void" -> "void";
            default -> "mixed";
        };
    }
    
    private void generatePhpMethodBody(StringBuilder code, com.basiccode.generator.model.Method method, String className, String returnType) {
        String methodName = method.getName().toLowerCase();
        
        switch (methodName) {
            case "authenticate":
                code.append("        if (empty($password)) {\n");
                code.append("            return false;\n");
                code.append("        }\n");
                code.append("        // TODO: Implement password verification logic\n");
                code.append("        return true;\n");
                break;
                
            case "updateprofile":
                code.append("        if (empty($profile)) {\n");
                code.append("            throw new \\InvalidArgumentException('Profile cannot be empty');\n");
                code.append("        }\n");
                code.append("        // TODO: Update user profile fields\n");
                code.append("        $this->updated_at = Carbon::now();\n");
                code.append("        $this->save();\n");
                break;
                
            case "calculatetotal":
                code.append("        // TODO: Calculate order total\n");
                if (!"void".equals(returnType)) {
                    code.append("        return 0;\n");
                }
                break;
                
            default:
                code.append("        // TODO: Implement ").append(methodName).append(" logic\n");
                if (!"void".equals(returnType)) {
                    if ("bool".equals(returnType)) {
                        code.append("        return false;\n");
                    } else if ("string".equals(returnType)) {
                        code.append("        return '';\n");
                    } else if ("int".equals(returnType) || "float".equals(returnType)) {
                        code.append("        return 0;\n");
                    } else {
                        code.append("        return null;\n");
                    }
                }
                break;
        }
    }
    
    private void generatePhpRelationships(StringBuilder code, EnhancedClass enhancedClass, String className) {
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (attr.isRelationship()) {
                generatePhpRelationship(code, attr, className);
            }
        }
    }
    
    private void generatePhpRelationship(StringBuilder code, UmlAttribute attr, String currentClassName) {
        String relationshipType = attr.getRelationshipType();
        String targetClass = attr.getTargetClass();
        String methodName = attr.getName();
        
        switch (relationshipType) {
            case "OneToMany":
                code.append("\n    public function ").append(methodName).append("()\n");
                code.append("    {\n");
                code.append("        return $this->hasMany(").append(targetClass).append("::class);\n");
                code.append("    }\n");
                break;
            case "ManyToOne":
                code.append("\n    public function ").append(methodName).append("()\n");
                code.append("    {\n");
                code.append("        return $this->belongsTo(").append(targetClass).append("::class);\n");
                code.append("    }\n");
                break;
            case "OneToOne":
                code.append("\n    public function ").append(methodName).append("()\n");
                code.append("    {\n");
                code.append("        return $this->hasOne(").append(targetClass).append("::class);\n");
                code.append("    }\n");
                break;
            case "ManyToMany":
                code.append("\n    public function ").append(methodName).append("()\n");
                code.append("    {\n");
                code.append("        return $this->belongsToMany(").append(targetClass).append("::class);\n");
                code.append("    }\n");
                break;
        }
    }
}