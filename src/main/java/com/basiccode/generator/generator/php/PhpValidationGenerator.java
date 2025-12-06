package com.basiccode.generator.generator.php;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * PhpValidationGenerator - Generates Laravel form requests and validation
 * 
 * Phase 2 Week 3 - VALIDATION & CONSTRAINTS
 * 
 * Generates:
 * - Form Request classes with validation rules
 * - Custom validation rules
 * - Error messages translation
 * - Constraint decorators in models
 * 
 * Supports:
 * - Required/Optional fields
 * - String constraints (max_length, min_length, regex, email, url)
 * - Numeric constraints (min, max, digits)
 * - Unique constraints
 * - Custom validation
 * 
 * @version 1.0.0
 * @since PHP Phase 2
 */
@Slf4j
@Component
public class PhpValidationGenerator {
    
    /**
     * Generate Form Request class for model
     */
    public String generateFormRequest(PhpModelParser.PhpModelDefinition model, String action) {
        StringBuilder code = new StringBuilder();
        
        String className = model.getName() + capitalize(action) + "Request";
        
        code.append("<?php\n\n");
        code.append("namespace App\\Http\\Requests;\n\n");
        code.append("use Illuminate\\Foundation\\Http\\FormRequest;\n");
        code.append("use Illuminate\\Validation\\Rule;\n\n");
        
        code.append("/**\n");
        code.append(" * ").append(className).append("\n");
        code.append(" * \n");
        code.append(" * Validates ").append(model.getName()).append(" ").append(action).append(" requests\n");
        code.append(" */\n");
        code.append("class ").append(className).append(" extends FormRequest\n");
        code.append("{\n");
        code.append("    /**\n");
        code.append("     * Determine if the user is authorized to make this request\n");
        code.append("     */\n");
        code.append("    public function authorize(): bool\n");
        code.append("    {\n");
        code.append("        return true;\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Get the validation rules that apply to the request\n");
        code.append("     */\n");
        code.append("    public function rules(): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        
        for (PhpModelParser.PhpFieldDefinition field : model.getFields()) {
            code.append(generateFieldValidation(field, action));
        }
        
        code.append("        ];\n");
        code.append("    }\n\n");
        
        code.append("    /**\n");
        code.append("     * Get custom validation error messages\n");
        code.append("     */\n");
        code.append("    public function messages(): array\n");
        code.append("    {\n");
        code.append("        return [\n");
        
        for (PhpModelParser.PhpFieldDefinition field : model.getFields()) {
            if (field.getConstraints() != null && !field.getConstraints().isEmpty()) {
                for (PhpModelParser.PhpConstraintDefinition constraint : field.getConstraints()) {
                    code.append(String.format(
                        "            '%s.%s' => '%s',\n",
                        field.getName(), constraint.getType(), constraint.getErrorMessage()
                    ));
                }
            }
        }
        
        code.append("        ];\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Generate validation rules for a single field
     */
    private String generateFieldValidation(PhpModelParser.PhpFieldDefinition field, String action) {
        StringBuilder rules = new StringBuilder();
        rules.append("            '").append(field.getName()).append("' => '");
        
        // Skip fields like id, timestamps
        if (field.getName().equals("id") || field.getName().contains("_at")) {
            return "";
        }
        
        List<String> rulesList = new ArrayList<>();
        
        // Required unless update action and field has no constraints
        if ("create".equals(action) || (field.getConstraints() != null && 
            field.getConstraints().stream().anyMatch(c -> c.getType().equals("required")))) {
            rulesList.add("required");
        } else if ("update".equals(action)) {
            rulesList.add("sometimes");
        }
        
        // Type-specific rules
        switch (field.getType()) {
            case "string" -> rulesList.add("string|max:255");
            case "text" -> rulesList.add("string");
            case "integer" -> {
                rulesList.add("integer");
                rulesList.add("numeric");
            }
            case "boolean" -> rulesList.add("boolean");
            case "date" -> rulesList.add("date_format:Y-m-d");
            case "timestamp", "dateTime" -> rulesList.add("date_format:Y-m-d H:i:s");
            case "email" -> {
                rulesList.add("email:rfc,dns");
                rulesList.add("unique:".concat(toTableName(field.getName())));
            }
            case "uuid" -> rulesList.add("uuid");
            case "json" -> rulesList.add("json");
            case "decimal" -> rulesList.add("numeric");
        }
        
        // Add nullable if optional
        if (field.isNullable()) {
            rulesList.add("nullable");
        }
        
        // Add constraint-specific rules
        if (field.getConstraints() != null) {
            for (PhpModelParser.PhpConstraintDefinition constraint : field.getConstraints()) {
                switch (constraint.getType()) {
                    case "unique" -> rulesList.add("unique:" + toTableName(field.getName()));
                    case "email" -> rulesList.add("email:rfc,dns");
                    case "url" -> rulesList.add("url");
                    case "regex" -> rulesList.add("regex:/^[a-z0-9]+$/i");
                }
            }
        }
        
        rules.append(String.join("|", rulesList));
        rules.append("',\n");
        
        return rules.toString();
    }
    
    /**
     * Generate validation trait for model (docblock constraints)
     */
    public String generateModelValidationComments(PhpModelParser.PhpModelDefinition model) {
        StringBuilder code = new StringBuilder();
        
        code.append("    /**\n");
        code.append("     * Validation Rules\n");
        code.append("     * \n");
        
        for (PhpModelParser.PhpFieldDefinition field : model.getFields()) {
            code.append("     * @property ").append(field.getType()).append(" $").append(field.getName());
            
            if (field.getConstraints() != null && !field.getConstraints().isEmpty()) {
                code.append(" - ");
                List<String> constraintNames = new ArrayList<>();
                for (PhpModelParser.PhpConstraintDefinition constraint : field.getConstraints()) {
                    constraintNames.add(constraint.getType());
                }
                code.append(String.join(", ", constraintNames));
            }
            code.append("\n");
        }
        
        code.append("     */\n");
        
        return code.toString();
    }
    
    /**
     * Generate custom validation rule class
     */
    public String generateCustomValidationRule(String ruleName, String validationLogic) {
        return String.format("""
            <?php
            
            namespace App\\Rules;
            
            use Closure;
            use Illuminate\\Contracts\\Validation\\ValidationRule;
            
            /**
             * %s - Custom validation rule
             */
            class %s implements ValidationRule
            {
                /**
                 * Run the validation rule.
                 */
                public function validate(string $attribute, mixed $value, Closure $fail): void
                {
                    // %s
                    if (!$this->passes($value)) {
                        $fail('The :attribute field is invalid.');
                    }
                }
                
                private function passes(mixed $value): bool
                {
                    // TODO: Implement validation logic
                    %s
                    return true;
                }
            }
            """,
            ruleName, ruleName, ruleName, validationLogic
        );
    }
    
    /**
     * Generate API validation response trait
     */
    public String generateValidationResponseTrait() {
        return """
            <?php
            
            namespace App\\Traits;
            
            use Illuminate\\Http\\JsonResponse;
            
            /**
             * Trait for consistent validation error responses
             */
            trait ValidatesWithJson
            {
                /**
                 * Prepare a JSON response for an error during HTTP request.
                 */
                protected function failedValidation($validator)
                {
                    throw new \\Illuminate\\Validation\\ValidationException($validator);
                }
                
                /**
                 * Return validation error in JSON format
                 */
                protected function jsonErrorResponse($errors, $statusCode = 422): JsonResponse
                {
                    return response()->json([
                        'success' => false,
                        'message' => 'Validation failed',
                        'errors' => $errors,
                    ], $statusCode);
                }
            }
            """;
    }
    
    /**
     * Generate validation messages translation file
     */
    public String generateValidationMessages() {
        return """
            <?php
            
            return [
                /*
                |--------------------------------------------------------------------------
                | Validation Language Lines
                |--------------------------------------------------------------------------
                |
                | The following language lines contain the default error messages used by
                | the validator class. Some of these rules have multiple versions such
                | as the size rules. Feel free to tweak each of these messages here.
                |
                */
            
                'accepted' => 'The :attribute must be accepted.',
                'accepted_if' => 'The :attribute must be accepted when :other is :value.',
                'active_url' => 'The :attribute is not a valid URL.',
                'after' => 'The :attribute must be a date after :date.',
                'after_or_equal' => 'The :attribute must be a date after or equal to :date.',
                'alpha' => 'The :attribute must only contain letters.',
                'alpha_dash' => 'The :attribute must only contain letters, numbers, dashes and underscores.',
                'alpha_num' => 'The :attribute must only contain letters and numbers.',
                'array' => 'The :attribute must be an array.',
                'before' => 'The :attribute must be a date before :date.',
                'before_or_equal' => 'The :attribute must be a date before or equal to :date.',
                'between' => [
                    'array' => 'The :attribute must have between :min and :max items.',
                    'file' => 'The :attribute must be between :min and :max kilobytes.',
                    'numeric' => 'The :attribute must be between :min and :max.',
                    'string' => 'The :attribute must be between :min and :max characters.',
                ],
                'boolean' => 'The :attribute field must be true or false.',
                'confirmed' => 'The :attribute confirmation does not match.',
                'current_password' => 'The password is incorrect.',
                'date' => 'The :attribute is not a valid date.',
                'date_equals' => 'The :attribute must be a date equal to :date.',
                'date_format' => 'The :attribute does not match the format :format.',
                'declined' => 'The :attribute must be declined.',
                'declined_if' => 'The :attribute must be declined when :other is :value.',
                'different' => 'The :attribute and :other must be different.',
                'digits' => 'The :attribute must be :digits digits.',
                'digits_between' => 'The :attribute must be between :min and :max digits.',
                'dimensions' => 'The :attribute has invalid image dimensions.',
                'distinct' => 'The :attribute field has a duplicate value.',
                'email' => 'The :attribute must be a valid email address.',
                'ends_with' => 'The :attribute must end with one of the following: :values.',
                'exists' => 'The selected :attribute is invalid.',
                'file' => 'The :attribute must be a file.',
                'filled' => 'The :attribute field must have a value.',
                'gt' => [
                    'array' => 'The :attribute must have more than :value items.',
                    'file' => 'The :attribute must be greater than :value kilobytes.',
                    'numeric' => 'The :attribute must be greater than :value.',
                    'string' => 'The :attribute must be greater than :value characters.',
                ],
                'gte' => [
                    'array' => 'The :attribute must have :value items or more.',
                    'file' => 'The :attribute must be greater than or equal to :value kilobytes.',
                    'numeric' => 'The :attribute must be greater than or equal to :value.',
                    'string' => 'The :attribute must be greater than or equal to :value characters.',
                ],
                'image' => 'The :attribute must be an image.',
                'in' => 'The selected :attribute is invalid.',
                'in_array' => 'The :attribute field does not exist in :other.',
                'integer' => 'The :attribute must be an integer.',
                'ip' => 'The :attribute must be a valid IP address.',
                'ipv4' => 'The :attribute must be a valid IPv4 address.',
                'ipv6' => 'The :attribute must be a valid IPv6 address.',
                'json' => 'The :attribute must be a valid JSON string.',
                'lt' => [
                    'array' => 'The :attribute must have less than :value items.',
                    'file' => 'The :attribute must be less than :value kilobytes.',
                    'numeric' => 'The :attribute must be less than :value.',
                    'string' => 'The :attribute must be less than :value characters.',
                ],
                'lte' => [
                    'array' => 'The :attribute must not have more than :value items.',
                    'file' => 'The :attribute must be less than or equal to :value kilobytes.',
                    'numeric' => 'The :attribute must be less than or equal to :value.',
                    'string' => 'The :attribute must be less than or equal to :value characters.',
                ],
                'max' => [
                    'array' => 'The :attribute must not have more than :max items.',
                    'file' => 'The :attribute must not be greater than :max kilobytes.',
                    'numeric' => 'The :attribute must not be greater than :max.',
                    'string' => 'The :attribute must not be greater than :max characters.',
                ],
                'mimes' => 'The :attribute must be a file of type: :values.',
                'mimetypes' => 'The :attribute must be a file of type: :values.',
                'min' => [
                    'array' => 'The :attribute must have at least :min items.',
                    'file' => 'The :attribute must be at least :min kilobytes.',
                    'numeric' => 'The :attribute must be at least :min.',
                    'string' => 'The :attribute must be at least :min characters.',
                ],
                'multiple_of' => 'The :attribute must be a multiple of :value.',
                'not_in' => 'The selected :attribute is invalid.',
                'not_regex' => 'The :attribute format is invalid.',
                'numeric' => 'The :attribute must be a number.',
                'password' => 'The password is incorrect.',
                'present' => 'The :attribute field must be present.',
                'regex' => 'The :attribute format is invalid.',
                'required' => 'The :attribute field is required.',
                'required_if' => 'The :attribute field is required when :other is :value.',
                'required_unless' => 'The :attribute field is required unless :other is in :values.',
                'required_with' => 'The :attribute field is required when :values is present.',
                'required_with_all' => 'The :attribute field is required when :values are present.',
                'required_without' => 'The :attribute field is required when :values is not present.',
                'required_without_all' => 'The :attribute field is required when none of :values are present.',
                'same' => 'The :attribute and :other must match.',
                'size' => [
                    'array' => 'The :attribute must contain :size items.',
                    'file' => 'The :attribute must be :size kilobytes.',
                    'numeric' => 'The :attribute must be :size.',
                    'string' => 'The :attribute must be :size characters.',
                ],
                'starts_with' => 'The :attribute must start with one of the following: :values.',
                'string' => 'The :attribute must be a string.',
                'timezone' => 'The :attribute must be a valid zone.',
                'unique' => 'The :attribute has already been taken.',
                'uploaded' => 'The :attribute failed to upload.',
                'url' => 'The :attribute format is invalid.',
                'uuid' => 'The :attribute must be a valid UUID.',
            ];
            """;
    }
    
    /**
     * Convert field name to table name
     */
    private String toTableName(String fieldName) {
        String name = fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        if (name.endsWith("y")) {
            return name.substring(0, name.length() - 1) + "ies";
        }
        return name + "s";
    }
    
    /**
     * Capitalize first character
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
