package com.basiccode.generator.enhanced;

import com.basiccode.generator.model.*;
import java.util.List;

public class OpenAPIGenerator {
    
    public String generateSwaggerSpec(List<ClassModel> models, String basePackage) {
        StringBuilder spec = new StringBuilder();
        
        spec.append("openapi: 3.0.0\n");
        spec.append("info:\n");
        spec.append("  title: Generated API\n");
        spec.append("  description: Auto-generated REST API from UML diagram\n");
        spec.append("  version: 1.0.0\n");
        spec.append("  contact:\n");
        spec.append("    name: Generated API Support\n");
        spec.append("servers:\n");
        spec.append("  - url: http://localhost:8080\n");
        spec.append("    description: Development server\n\n");
        
        spec.append("paths:\n");
        for (ClassModel model : models) {
            spec.append(generatePaths(model));
        }
        
        spec.append("\ncomponents:\n");
        spec.append("  schemas:\n");
        for (ClassModel model : models) {
            spec.append(generateSchema(model));
        }
        
        spec.append("  securitySchemes:\n");
        spec.append("    bearerAuth:\n");
        spec.append("      type: http\n");
        spec.append("      scheme: bearer\n");
        spec.append("      bearerFormat: JWT\n");
        
        return spec.toString();
    }
    
    private String generatePaths(ClassModel model) {
        String entityName = model.getName().toLowerCase();
        StringBuilder paths = new StringBuilder();
        
        // GET /api/{entities}
        paths.append("  /api/").append(entityName).append("s:\n");
        paths.append("    get:\n");
        paths.append("      tags: [").append(model.getName()).append("]\n");
        paths.append("      summary: Get all ").append(entityName).append("s\n");
        paths.append("      responses:\n");
        paths.append("        '200':\n");
        paths.append("          description: List of ").append(entityName).append("s\n");
        paths.append("          content:\n");
        paths.append("            application/json:\n");
        paths.append("              schema:\n");
        paths.append("                type: array\n");
        paths.append("                items:\n");
        paths.append("                  $ref: '#/components/schemas/").append(model.getName()).append("'\n");
        
        // POST /api/{entities}
        paths.append("    post:\n");
        paths.append("      tags: [").append(model.getName()).append("]\n");
        paths.append("      summary: Create new ").append(entityName).append("\n");
        paths.append("      requestBody:\n");
        paths.append("        required: true\n");
        paths.append("        content:\n");
        paths.append("          application/json:\n");
        paths.append("            schema:\n");
        paths.append("              $ref: '#/components/schemas/").append(model.getName()).append("'\n");
        paths.append("      responses:\n");
        paths.append("        '201':\n");
        paths.append("          description: Created ").append(entityName).append("\n");
        paths.append("          content:\n");
        paths.append("            application/json:\n");
        paths.append("              schema:\n");
        paths.append("                $ref: '#/components/schemas/").append(model.getName()).append("'\n");
        
        // GET /api/{entities}/{id}
        paths.append("  /api/").append(entityName).append("s/{id}:\n");
        paths.append("    get:\n");
        paths.append("      tags: [").append(model.getName()).append("]\n");
        paths.append("      summary: Get ").append(entityName).append(" by ID\n");
        paths.append("      parameters:\n");
        paths.append("        - name: id\n");
        paths.append("          in: path\n");
        paths.append("          required: true\n");
        paths.append("          schema:\n");
        paths.append("            type: string\n");
        paths.append("            format: uuid\n");
        paths.append("      responses:\n");
        paths.append("        '200':\n");
        paths.append("          description: ").append(model.getName()).append(" found\n");
        paths.append("          content:\n");
        paths.append("            application/json:\n");
        paths.append("              schema:\n");
        paths.append("                $ref: '#/components/schemas/").append(model.getName()).append("'\n");
        paths.append("        '404':\n");
        paths.append("          description: ").append(model.getName()).append(" not found\n");
        
        // PUT /api/{entities}/{id}
        paths.append("    put:\n");
        paths.append("      tags: [").append(model.getName()).append("]\n");
        paths.append("      summary: Update ").append(entityName).append("\n");
        paths.append("      parameters:\n");
        paths.append("        - name: id\n");
        paths.append("          in: path\n");
        paths.append("          required: true\n");
        paths.append("          schema:\n");
        paths.append("            type: string\n");
        paths.append("            format: uuid\n");
        paths.append("      requestBody:\n");
        paths.append("        required: true\n");
        paths.append("        content:\n");
        paths.append("          application/json:\n");
        paths.append("            schema:\n");
        paths.append("              $ref: '#/components/schemas/").append(model.getName()).append("'\n");
        paths.append("      responses:\n");
        paths.append("        '200':\n");
        paths.append("          description: Updated ").append(entityName).append("\n");
        
        // DELETE /api/{entities}/{id}
        paths.append("    delete:\n");
        paths.append("      tags: [").append(model.getName()).append("]\n");
        paths.append("      summary: Delete ").append(entityName).append("\n");
        paths.append("      parameters:\n");
        paths.append("        - name: id\n");
        paths.append("          in: path\n");
        paths.append("          required: true\n");
        paths.append("          schema:\n");
        paths.append("            type: string\n");
        paths.append("            format: uuid\n");
        paths.append("      responses:\n");
        paths.append("        '204':\n");
        paths.append("          description: ").append(model.getName()).append(" deleted\n");
        
        return paths.toString();
    }
    
    private String generateSchema(ClassModel model) {
        StringBuilder schema = new StringBuilder();
        
        schema.append("    ").append(model.getName()).append(":\n");
        schema.append("      type: object\n");
        schema.append("      properties:\n");
        schema.append("        id:\n");
        schema.append("          type: string\n");
        schema.append("          format: uuid\n");
        schema.append("          readOnly: true\n");
        
        for (Field field : model.getFields()) {
            schema.append("        ").append(field.getName()).append(":\n");
            schema.append("          type: ").append(getOpenAPIType(field.getType())).append("\n");
            
            if (field.getType().equals("String") && field.getMaxSize() != null) {
                schema.append("          maxLength: ").append(field.getMaxSize()).append("\n");
            }
            if (field.getType().equals("String") && field.getMinSize() != null) {
                schema.append("          minLength: ").append(field.getMinSize()).append("\n");
            }
            if (!field.isNullable()) {
                schema.append("          nullable: false\n");
            }
            if (field.hasAnnotation("Email")) {
                schema.append("          format: email\n");
            }
        }
        
        schema.append("        createdAt:\n");
        schema.append("          type: string\n");
        schema.append("          format: date-time\n");
        schema.append("          readOnly: true\n");
        schema.append("        updatedAt:\n");
        schema.append("          type: string\n");
        schema.append("          format: date-time\n");
        schema.append("          readOnly: true\n");
        
        // Required fields
        List<String> requiredFields = model.getFields().stream()
            .filter(f -> !f.isNullable())
            .map(Field::getName)
            .toList();
        
        if (!requiredFields.isEmpty()) {
            schema.append("      required:\n");
            for (String field : requiredFields) {
                schema.append("        - ").append(field).append("\n");
            }
        }
        
        return schema.toString();
    }
    
    private String getOpenAPIType(String javaType) {
        return switch (javaType) {
            case "String" -> "string";
            case "Integer", "int", "Long", "long" -> "integer";
            case "Float", "float", "Double", "double", "BigDecimal" -> "number";
            case "Boolean", "boolean" -> "boolean";
            case "UUID" -> "string";
            case "Instant", "Date", "LocalDateTime", "LocalDate" -> "string";
            default -> "string";
        };
    }
}