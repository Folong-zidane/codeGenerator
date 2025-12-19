package com.basiccode.generator.generator.php;

import com.basiccode.generator.model.EnhancedClass;

public class PhpRequestGenerator {
    
    public String generateStoreRequest(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Http\\Requests;\n\n");
        code.append("use Illuminate\\Foundation\\Http\\FormRequest;\n\n");
        
        code.append("class Store").append(className).append("Request extends FormRequest\n");
        code.append("{\n");
        
        // Authorize method
        code.append("    /**\n");
        code.append("     * Determine if the user is authorized to make this request.\n");
        code.append("     */\n");
        code.append("    public function authorize(): bool\n");
        code.append("    {\n");
        code.append("        return true;\n");
        code.append("    }\n\n");
        
        // Rules method
        code.append("    /**\n");
        code.append("     * Get the validation rules that apply to the request.\n");
        code.append("     */\n");
        code.append("    public function rules(): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        
        for (com.basiccode.generator.model.UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!"id".equalsIgnoreCase(attr.getName())) {
                String rules = generateValidationRules(attr);
                code.append("            '").append(attr.getName()).append("' => '").append(rules).append("',\n");
            }
        }
        
        code.append("        ];\n");
        code.append("    }\n\n");
        
        // Messages method
        code.append("    /**\n");
        code.append("     * Get custom validation messages.\n");
        code.append("     */\n");
        code.append("    public function messages(): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        code.append("            // Add custom validation messages here\n");
        code.append("        ];\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    public String generateUpdateRequest(EnhancedClass enhancedClass, String packageName) {
        StringBuilder code = new StringBuilder();
        String className = enhancedClass.getOriginalClass().getName();
        
        code.append("<?php\n\n");
        code.append("namespace ").append(packageName).append("\\Http\\Requests;\n\n");
        code.append("use Illuminate\\Foundation\\Http\\FormRequest;\n");
        code.append("use Illuminate\\Validation\\Rule;\n\n");
        
        code.append("class Update").append(className).append("Request extends FormRequest\n");
        code.append("{\n");
        
        // Authorize method
        code.append("    /**\n");
        code.append("     * Determine if the user is authorized to make this request.\n");
        code.append("     */\n");
        code.append("    public function authorize(): bool\n");
        code.append("    {\n");
        code.append("        return true;\n");
        code.append("    }\n\n");
        
        // Rules method
        code.append("    /**\n");
        code.append("     * Get the validation rules that apply to the request.\n");
        code.append("     */\n");
        code.append("    public function rules(): array\n");
        code.append("    {\n");
        code.append("        $id = $this->route('").append(className.toLowerCase()).append("');\n\n");
        code.append("        return [\n");
        
        for (com.basiccode.generator.model.UMLAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!"id".equalsIgnoreCase(attr.getName())) {
                String rules = generateUpdateValidationRules(attr, className);
                code.append("            '").append(attr.getName()).append("' => ").append(rules).append(",\n");
            }
        }
        
        code.append("        ];\n");
        code.append("    }\n\n");
        
        // Messages method
        code.append("    /**\n");
        code.append("     * Get custom validation messages.\n");
        code.append("     */\n");
        code.append("    public function messages(): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        code.append("            // Add custom validation messages here\n");
        code.append("        ];\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateValidationRules(com.basiccode.generator.model.UMLAttribute attr) {
        StringBuilder rules = new StringBuilder("required");
        
        switch (attr.getType().toLowerCase()) {
            case "string":
                rules.append("|string|max:255");
                if (attr.getName().toLowerCase().contains("email")) {
                    rules.append("|email|unique:").append(attr.getName().toLowerCase()).append("s,email");
                }
                break;
            case "integer":
            case "int":
            case "long":
                rules.append("|integer|min:0");
                break;
            case "float":
            case "double":
                rules.append("|numeric|min:0");
                break;
            case "boolean":
                rules.append("|boolean");
                break;
            case "date":
                rules.append("|date");
                break;
            case "datetime":
                rules.append("|date_format:Y-m-d H:i:s");
                break;
            default:
                rules.append("|string");
        }
        
        return rules.toString();
    }
    
    private String generateUpdateValidationRules(com.basiccode.generator.model.UMLAttribute attr, String className) {
        StringBuilder rules = new StringBuilder();
        
        // For updates, most fields are sometimes required
        if (attr.getName().toLowerCase().contains("email")) {
            rules.append("'sometimes|required|email|unique:").append(className.toLowerCase()).append("s,email,' . $id");
        } else {
            rules.append("'sometimes|required");
            
            switch (attr.getType().toLowerCase()) {
                case "string":
                    rules.append("|string|max:255");
                    break;
                case "integer":
                case "int":
                case "long":
                    rules.append("|integer|min:0");
                    break;
                case "float":
                case "double":
                    rules.append("|numeric|min:0");
                    break;
                case "boolean":
                    rules.append("|boolean");
                    break;
                case "date":
                    rules.append("|date");
                    break;
                case "datetime":
                    rules.append("|date_format:Y-m-d H:i:s");
                    break;
                default:
                    rules.append("|string");
            }
            
            rules.append("'");
        }
        
        return rules.toString();
    }
}