package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.model.EnhancedClass;
import com.basiccode.generator.model.UmlAttribute;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CSharpValidationGenerator - Generate FluentValidation validators and Data Annotations
 * Creates complete validation infrastructure with async support
 * 
 * Phase 2 Week 3 - C# VALIDATION
 * 
 * Generates:
 * - FluentValidation validators
 * - Data Annotations (Required, StringLength, Range, etc.)
 * - Custom validation rules
 * - Validation error messages
 * - Create/Update DTOs with validation
 * 
 * @version 1.0.0
 * @since C# Phase 2
 */
public class CSharpValidationGenerator {

    private final String projectName;

    public CSharpValidationGenerator(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Generate FluentValidation validator class
     */
    public String generateFluentValidator(EnhancedClass enhancedClass, String packageName) {
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();

        // Header
        code.append("using FluentValidation;\n");
        code.append("using ").append(packageName).append(".DTOs;\n");
        code.append("using ").append(packageName).append(".Resources;\n\n");

        code.append("namespace ").append(packageName).append(".Validators\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// FluentValidation validator for ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("Validator : AbstractValidator<").append(className).append("CreateDto>\n");
        code.append("    {\n");
        code.append("        public ").append(className).append("Validator()\n");
        code.append("        {\n");

        // Add validation rules for each field
        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            code.append(generateFieldValidation(attr));
        }

        code.append("        }\n");
        code.append("    }\n\n");

        // Update validator
        code.append("    /// <summary>\n");
        code.append("    /// FluentValidation validator for ").append(className).append(" updates\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("UpdateValidator : AbstractValidator<").append(className).append("UpdateDto>\n");
        code.append("    {\n");
        code.append("        public ").append(className).append("UpdateValidator()\n");
        code.append("        {\n");

        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!attr.getName().equalsIgnoreCase("id")) {
                code.append(generateFieldValidation(attr));
            }
        }

        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate field validation rule
     */
    private String generateFieldValidation(UmlAttribute attr) {
        StringBuilder code = new StringBuilder();
        String fieldName = capitalizeFirst(attr.getName());

        code.append("            RuleFor(x => x.").append(fieldName).append(")\n");

        // Required validation
        if (!attr.isNullable()) {
            code.append("                .NotEmpty().WithMessage(\"").append(fieldName).append(" is required\")\n");
            code.append("                .NotNull().WithMessage(\"").append(fieldName).append(" cannot be null\")\n");
        } else {
            code.append("                .Empty().When(x => x.").append(fieldName).append(" == null).WithMessage(\"").append(fieldName).append(" is optional\")\n");
        }

        // String length validation
        if (attr.getType().equalsIgnoreCase("string")) {
            code.append("                .MaximumLength(255).WithMessage(\"").append(fieldName).append(" cannot exceed 255 characters\")\n");
            code.append("                .MinimumLength(1).WithMessage(\"").append(fieldName).append(" must have at least 1 character\")\n");
        }

        // Email validation
        if (attr.getName().toLowerCase().contains("email")) {
            code.append("                .EmailAddress().WithMessage(\"").append(fieldName).append(" must be a valid email address\")\n");
        }

        // Phone validation
        if (attr.getName().toLowerCase().contains("phone")) {
            code.append("                .Matches(@\"^[+]?[0-9]{1,3}[.-]?[0-9]{1,14}$\").WithMessage(\"").append(fieldName).append(" must be a valid phone number\")\n");
        }

        // URL validation
        if (attr.getName().toLowerCase().contains("url") || attr.getName().toLowerCase().contains("website")) {
            code.append("                .Must(x => Uri.TryCreate(x, UriKind.Absolute, out _)).WithMessage(\"").append(fieldName).append(" must be a valid URL\")\n");
        }

        code.append("                .Cascade(CascadeMode.Stop);\n\n");

        return code.toString();
    }

    /**
     * Generate Create DTO with Data Annotations
     */
    public String generateCreateDto(EnhancedClass enhancedClass, String packageName) {
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();

        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        code.append("namespace ").append(packageName).append(".DTOs\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Create DTO for ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("CreateDto\n");
        code.append("    {\n");

        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!attr.getName().equalsIgnoreCase("id")) {
                code.append(generateDtoProperty(attr));
            }
        }

        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate Update DTO
     */
    public String generateUpdateDto(EnhancedClass enhancedClass, String packageName) {
        String className = enhancedClass.getOriginalClass().getName();
        StringBuilder code = new StringBuilder();

        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        code.append("namespace ").append(packageName).append(".DTOs\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Update DTO for ").append(className).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(className).append("UpdateDto\n");
        code.append("    {\n");
        code.append("        [Required]\n");
        code.append("        public Guid Id { get; set; }\n\n");

        for (UmlAttribute attr : enhancedClass.getOriginalClass().getAttributes()) {
            if (!attr.getName().equalsIgnoreCase("id") && !attr.getName().equalsIgnoreCase("createdat")) {
                code.append(generateDtoProperty(attr));
            }
        }

        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate DTO property with annotations
     */
    private String generateDtoProperty(UmlAttribute attr) {
        StringBuilder code = new StringBuilder();
        String fieldName = capitalizeFirst(attr.getName());
        String csharpType = mapToCSharpType(attr.getType());

        // Add data annotations
        if (!attr.isNullable()) {
            code.append("        [Required(ErrorMessage = \"").append(fieldName).append(" is required\")]\n");
        }

        if ("string".equals(csharpType)) {
            code.append("        [StringLength(255, MinimumLength = 1, ErrorMessage = \"")
                .append(fieldName).append(" must be between 1 and 255 characters\")]\n");
        }

        if (attr.getName().toLowerCase().contains("email")) {
            code.append("        [EmailAddress(ErrorMessage = \"Invalid email address\")]\n");
        }

        if (attr.getName().toLowerCase().contains("phone")) {
            code.append("        [Phone(ErrorMessage = \"Invalid phone number\")]\n");
        }

        if (attr.getName().toLowerCase().contains("url") || attr.getName().toLowerCase().contains("website")) {
            code.append("        [Url(ErrorMessage = \"Invalid URL\")]\n");
        }

        // Property declaration
        code.append("        public ").append(csharpType);
        if (attr.isNullable() && !isValueType(csharpType)) {
            code.append("?");
        }
        code.append(" ").append(fieldName).append(" { get; set; }\n\n");

        return code.toString();
    }

    /**
     * Generate custom validation rule
     */
    public String generateCustomValidationRule(String ruleName) {
        StringBuilder code = new StringBuilder();

        code.append("using FluentValidation.Validators;\n\n");
        code.append("namespace ").append(projectName).append(".Validators.Rules\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Custom validation rule: ").append(ruleName).append("\n");
        code.append("    /// </summary>\n");
        code.append("    public class ").append(ruleName).append("Validator : PropertyValidator<object, string>\n");
        code.append("    {\n");
        code.append("        public override string Name => \"").append(ruleName).append("\";\n\n");

        code.append("        public override bool IsValid(ValidationContext<object> context, string value)\n");
        code.append("        {\n");
        code.append("            if (string.IsNullOrEmpty(value))\n");
        code.append("                return true;\n\n");

        code.append("            // Add custom validation logic here\n");
        code.append("            return true;\n");
        code.append("        }\n\n");

        code.append("        protected override string GetDefaultMessageTemplate(string errorCode)\n");
        code.append("        {\n");
        code.append("            return \"{PropertyName} does not meet ").append(ruleName).append(" requirements\";\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Generate validation middleware
     */
    public String generateValidationMiddleware() {
        StringBuilder code = new StringBuilder();

        code.append("using FluentValidation;\n");
        code.append("using System.ComponentModel.DataAnnotations;\n\n");
        code.append("namespace ").append(projectName).append(".Middleware\n");
        code.append("{\n");
        code.append("    /// <summary>\n");
        code.append("    /// Validation error handling middleware\n");
        code.append("    /// </summary>\n");
        code.append("    public class ValidationMiddleware\n");
        code.append("    {\n");
        code.append("        private readonly RequestDelegate _next;\n");
        code.append("        private readonly ILogger<ValidationMiddleware> _logger;\n\n");

        code.append("        public ValidationMiddleware(RequestDelegate next, ILogger<ValidationMiddleware> logger)\n");
        code.append("        {\n");
        code.append("            _next = next;\n");
        code.append("            _logger = logger;\n");
        code.append("        }\n\n");

        code.append("        public async Task InvokeAsync(HttpContext context)\n");
        code.append("        {\n");
        code.append("            try\n");
        code.append("            {\n");
        code.append("                await _next(context);\n");
        code.append("            }\n");
        code.append("            catch (ValidationException ex)\n");
        code.append("            {\n");
        code.append("                _logger.LogError(ex, \"Validation error\");\n");
        code.append("                context.Response.StatusCode = StatusCodes.Status400BadRequest;\n");
        code.append("                await context.Response.WriteAsJsonAsync(new\n");
        code.append("                {\n");
        code.append("                    error = \"Validation failed\",\n");
        code.append("                    details = ex.Errors.Select(e => new\n");
        code.append("                    {\n");
        code.append("                        field = e.PropertyName,\n");
        code.append("                        message = e.ErrorMessage\n");
        code.append("                    })\n");
        code.append("                });\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");

        return code.toString();
    }

    /**
     * Map UML type to C# type
     */
    private String mapToCSharpType(String umlType) {
        return switch (umlType.toLowerCase()) {
            case "string", "text", "email", "phone" -> "string";
            case "int", "integer" -> "int";
            case "long", "bigint" -> "long";
            case "decimal", "float", "double" -> "decimal";
            case "boolean", "bool" -> "bool";
            case "date", "datetime", "timestamp" -> "DateTime";
            case "uuid", "guid" -> "Guid";
            default -> "string";
        };
    }

    /**
     * Check if type is value type
     */
    private boolean isValueType(String csharpType) {
        return csharpType.matches("^(int|long|short|decimal|float|double|byte|bool|DateTime|DateTimeOffset|TimeSpan|Guid)$");
    }

    /**
     * Capitalize first letter
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
