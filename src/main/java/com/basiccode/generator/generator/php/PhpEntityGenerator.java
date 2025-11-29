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
}