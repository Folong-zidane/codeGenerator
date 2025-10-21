package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

public class RepositoryGenerator {
    
    public JavaFile generateRepository(ClassModel model, String basePackage, String language) {
        switch (language.toLowerCase()) {
            case "java":
                return generateJavaRepository(model, basePackage);
            case "python":
                return generatePythonRepository(model, basePackage);
            case "csharp":
                return generateCSharpRepository(model, basePackage);
            default:
                return generateJavaRepository(model, basePackage);
        }
    }
    
    private JavaFile generateJavaRepository(ClassModel model, String basePackage) {
        ClassName entityClass = ClassName.get(basePackage + ".entity", model.getName());
        ClassName jpaRepository = ClassName.get("org.springframework.data.jpa.repository", "JpaRepository");
        
        // Interface Repository
        TypeSpec.Builder repositoryBuilder = TypeSpec.interfaceBuilder(model.getName() + "Repository")
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(ParameterizedTypeName.get(jpaRepository, entityClass, ClassName.get(UUID.class)))
            .addJavadoc("Repository for {@link $T}\n", entityClass)
            .addJavadoc("Generated automatically from UML model\n");
        
        // Add custom query methods based on fields
        for (Field field : model.getFields()) {
            if (field.isUnique() || field.getName().contains("name") || field.getName().contains("code")) {
                repositoryBuilder.addMethod(generateFindByMethod(field, entityClass));
            }
        }
        
        TypeSpec repository = repositoryBuilder.build();
        
        return JavaFile.builder(basePackage + ".repository", repository)
            .addFileComment("Generated Repository")
            .build();
    }
    
    private JavaFile generatePythonRepository(ClassModel model, String basePackage) {
        StringBuilder pythonCode = new StringBuilder();
        pythonCode.append("from sqlalchemy.orm import Session\n");
        pythonCode.append("from typing import Optional, List\n");
        pythonCode.append("from uuid import UUID\n\n");
        pythonCode.append("class ").append(model.getName()).append("Repository:\n");
        pythonCode.append("    def __init__(self, db: Session):\n");
        pythonCode.append("        self.db = db\n\n");
        pythonCode.append("    def find_by_id(self, id: UUID) -> Optional[").append(model.getName()).append("]:\n");
        pythonCode.append("        return self.db.query(").append(model.getName()).append(").filter(").append(model.getName()).append(".id == id).first()\n\n");
        pythonCode.append("    def find_all(self) -> List[").append(model.getName()).append("]:\n");
        pythonCode.append("        return self.db.query(").append(model.getName()).append(").all()\n\n");
        pythonCode.append("    def save(self, entity: ").append(model.getName()).append(") -> ").append(model.getName()).append(":\n");
        pythonCode.append("        self.db.add(entity)\n");
        pythonCode.append("        self.db.commit()\n");
        pythonCode.append("        self.db.refresh(entity)\n");
        pythonCode.append("        return entity\n\n");
        pythonCode.append("    def delete(self, entity: ").append(model.getName()).append("):\n");
        pythonCode.append("        self.db.delete(entity)\n");
        pythonCode.append("        self.db.commit()\n");
        
        TypeSpec dummyClass = TypeSpec.classBuilder("PythonRepositoryCode")
            .addJavadoc(pythonCode.toString())
            .build();
        
        return JavaFile.builder(basePackage, dummyClass).build();
    }
    
    private JavaFile generateCSharpRepository(ClassModel model, String basePackage) {
        StringBuilder csharpCode = new StringBuilder();
        csharpCode.append("using System;\n");
        csharpCode.append("using System.Collections.Generic;\n");
        csharpCode.append("using System.Linq;\n");
        csharpCode.append("using Microsoft.EntityFrameworkCore;\n\n");
        csharpCode.append("namespace ").append(basePackage).append(".Repositories\n{\n");
        csharpCode.append("    public interface I").append(model.getName()).append("Repository\n    {\n");
        csharpCode.append("        Task<").append(model.getName()).append("> GetByIdAsync(Guid id);\n");
        csharpCode.append("        Task<IEnumerable<").append(model.getName()).append(">> GetAllAsync();\n");
        csharpCode.append("        Task<").append(model.getName()).append("> CreateAsync(").append(model.getName()).append(" entity);\n");
        csharpCode.append("        Task<").append(model.getName()).append("> UpdateAsync(").append(model.getName()).append(" entity);\n");
        csharpCode.append("        Task DeleteAsync(Guid id);\n");
        csharpCode.append("    }\n\n");
        csharpCode.append("    public class ").append(model.getName()).append("Repository : I").append(model.getName()).append("Repository\n    {\n");
        csharpCode.append("        private readonly DbContext _context;\n\n");
        csharpCode.append("        public ").append(model.getName()).append("Repository(DbContext context)\n        {\n");
        csharpCode.append("            _context = context;\n        }\n\n");
        csharpCode.append("        public async Task<").append(model.getName()).append("> GetByIdAsync(Guid id)\n        {\n");
        csharpCode.append("            return await _context.Set<").append(model.getName()).append(">().FindAsync(id);\n        }\n");
        csharpCode.append("        // ... other methods\n");
        csharpCode.append("    }\n}");
        
        TypeSpec dummyClass = TypeSpec.classBuilder("CSharpRepositoryCode")
            .addJavadoc(csharpCode.toString())
            .build();
        
        return JavaFile.builder(basePackage, dummyClass).build();
    }
    
    private MethodSpec generateFindByMethod(Field field, ClassName entityClass) {
        String methodName = "findBy" + capitalize(field.getName());
        TypeName fieldType = getJavaType(field.getType());
        
        return MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), entityClass))
            .addParameter(fieldType, field.getName())
            .build();
    }
    
    private TypeName getJavaType(String type) {
        return switch (type) {
            case "String" -> ClassName.get(String.class);
            case "Integer", "int" -> ClassName.get(Integer.class);
            case "Long", "long" -> ClassName.get(Long.class);
            case "Float", "float" -> ClassName.get(Float.class);
            case "Double", "double" -> ClassName.get(Double.class);
            case "Boolean", "boolean" -> ClassName.get(Boolean.class);
            case "UUID" -> ClassName.get(UUID.class);
            default -> ClassName.bestGuess(type);
        };
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}