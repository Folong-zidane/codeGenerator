package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

public class ServiceGenerator {
    
    public JavaFile generateService(ClassModel model, String basePackage, String language) {
        switch (language.toLowerCase()) {
            case "java":
                return generateJavaService(model, basePackage);
            case "python":
                return generatePythonService(model, basePackage);
            case "csharp":
                return generateCSharpService(model, basePackage);
            default:
                return generateJavaService(model, basePackage);
        }
    }
    
    private JavaFile generateJavaService(ClassModel model, String basePackage) {
        ClassName entityClass = ClassName.get(basePackage + ".entity", model.getName());
        ClassName repositoryClass = ClassName.get(basePackage + ".repository", model.getName() + "Repository");
        
        // Build service class
        TypeSpec service = TypeSpec.classBuilder(model.getName() + "Service")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.stereotype", "Service"))
            .addAnnotation(ClassName.get("org.springframework.transaction.annotation", "Transactional"))
            .addField(repositoryClass, "repository", Modifier.PRIVATE, Modifier.FINAL)
            .addMethod(generateConstructor(repositoryClass))
            .addMethod(generateCreateMethod(model, entityClass))
            .addMethod(generateFindByIdMethod(model, entityClass))
            .addMethod(generateFindAllMethod(model, entityClass))
            .addMethod(generateUpdateMethod(model, entityClass))
            .addMethod(generateDeleteMethod(model))
            .build();
        
        return JavaFile.builder(basePackage + ".service", service)
            .addFileComment("Generated Service Layer")
            .build();
    }
    
    private JavaFile generatePythonService(ClassModel model, String basePackage) {
        StringBuilder pythonCode = new StringBuilder();
        pythonCode.append("from typing import Optional, List\n");
        pythonCode.append("from uuid import UUID\n\n");
        pythonCode.append("class ").append(model.getName()).append("Service:\n");
        pythonCode.append("    def __init__(self, repository: ").append(model.getName()).append("Repository):\n");
        pythonCode.append("        self.repository = repository\n\n");
        pythonCode.append("    def create(self, entity: ").append(model.getName()).append(") -> ").append(model.getName()).append(":\n");
        pythonCode.append("        return self.repository.save(entity)\n\n");
        pythonCode.append("    def find_by_id(self, id: UUID) -> Optional[").append(model.getName()).append("]:\n");
        pythonCode.append("        return self.repository.find_by_id(id)\n\n");
        pythonCode.append("    def find_all(self) -> List[").append(model.getName()).append("]:\n");
        pythonCode.append("        return self.repository.find_all()\n\n");
        pythonCode.append("    def update(self, entity: ").append(model.getName()).append(") -> ").append(model.getName()).append(":\n");
        pythonCode.append("        return self.repository.save(entity)\n\n");
        pythonCode.append("    def delete(self, id: UUID):\n");
        pythonCode.append("        entity = self.repository.find_by_id(id)\n");
        pythonCode.append("        if entity:\n");
        pythonCode.append("            self.repository.delete(entity)\n");
        
        TypeSpec dummyClass = TypeSpec.classBuilder("PythonServiceCode")
            .addJavadoc(pythonCode.toString())
            .build();
        
        return JavaFile.builder(basePackage, dummyClass).build();
    }
    
    private JavaFile generateCSharpService(ClassModel model, String basePackage) {
        StringBuilder csharpCode = new StringBuilder();
        csharpCode.append("using System;\n");
        csharpCode.append("using System.Collections.Generic;\n");
        csharpCode.append("using System.Threading.Tasks;\n\n");
        csharpCode.append("namespace ").append(basePackage).append(".Services\n{\n");
        csharpCode.append("    public interface I").append(model.getName()).append("Service\n    {\n");
        csharpCode.append("        Task<").append(model.getName()).append("> CreateAsync(").append(model.getName()).append(" entity);\n");
        csharpCode.append("        Task<").append(model.getName()).append("> GetByIdAsync(Guid id);\n");
        csharpCode.append("        Task<IEnumerable<").append(model.getName()).append(">> GetAllAsync();\n");
        csharpCode.append("        Task<").append(model.getName()).append("> UpdateAsync(").append(model.getName()).append(" entity);\n");
        csharpCode.append("        Task DeleteAsync(Guid id);\n");
        csharpCode.append("    }\n\n");
        csharpCode.append("    public class ").append(model.getName()).append("Service : I").append(model.getName()).append("Service\n    {\n");
        csharpCode.append("        private readonly I").append(model.getName()).append("Repository _repository;\n\n");
        csharpCode.append("        public ").append(model.getName()).append("Service(I").append(model.getName()).append("Repository repository)\n        {\n");
        csharpCode.append("            _repository = repository;\n        }\n\n");
        csharpCode.append("        public async Task<").append(model.getName()).append("> CreateAsync(").append(model.getName()).append(" entity)\n        {\n");
        csharpCode.append("            return await _repository.CreateAsync(entity);\n        }\n");
        csharpCode.append("        // ... other methods\n");
        csharpCode.append("    }\n}");
        
        TypeSpec dummyClass = TypeSpec.classBuilder("CSharpServiceCode")
            .addJavadoc(csharpCode.toString())
            .build();
        
        return JavaFile.builder(basePackage, dummyClass).build();
    }
    
    private MethodSpec generateConstructor(ClassName repositoryClass) {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(repositoryClass, "repository")
            .addStatement("this.repository = repository")
            .build();
    }
    
    private MethodSpec generateCreateMethod(ClassModel model, ClassName entityClass) {
        return MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(entityClass, "entity")
            .returns(entityClass)
            .addStatement("return repository.save(entity)")
            .build();
    }
    
    private MethodSpec generateFindByIdMethod(ClassModel model, ClassName entityClass) {
        return MethodSpec.methodBuilder("findById")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "id")
            .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), entityClass))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.transaction.annotation", "Transactional"))
                .addMember("readOnly", "true")
                .build())
            .addStatement("return repository.findById(id)")
            .build();
    }
    
    private MethodSpec generateFindAllMethod(ClassModel model, ClassName entityClass) {
        return MethodSpec.methodBuilder("findAll")
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(ClassName.get(List.class), entityClass))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.transaction.annotation", "Transactional"))
                .addMember("readOnly", "true")
                .build())
            .addStatement("return repository.findAll()")
            .build();
    }
    
    private MethodSpec generateUpdateMethod(ClassModel model, ClassName entityClass) {
        return MethodSpec.methodBuilder("update")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(entityClass, "entity")
            .returns(entityClass)
            .addStatement("return repository.save(entity)")
            .build();
    }
    
    private MethodSpec generateDeleteMethod(ClassModel model) {
        return MethodSpec.methodBuilder("delete")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(UUID.class, "id")
            .addStatement("repository.deleteById(id)")
            .build();
    }
}