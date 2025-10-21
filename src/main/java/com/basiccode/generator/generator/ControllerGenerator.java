package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

public class ControllerGenerator {
    
    public JavaFile generateController(ClassModel model, String basePackage, String language) {
        switch (language.toLowerCase()) {
            case "java":
                return generateJavaController(model, basePackage);
            case "python":
                return generatePythonController(model, basePackage);
            case "csharp":
                return generateCSharpController(model, basePackage);
            default:
                return generateJavaController(model, basePackage);
        }
    }
    
    private JavaFile generateJavaController(ClassModel model, String basePackage) {
        ClassName entityClass = ClassName.get(basePackage + ".entity", model.getName());
        ClassName serviceClass = ClassName.get(basePackage + ".service", model.getName() + "Service");
        ClassName responseEntity = ClassName.get("org.springframework.http", "ResponseEntity");
        
        // Build controller class
        TypeSpec controller = TypeSpec.classBuilder(model.getName() + "Controller")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RestController"))
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "RequestMapping"))
                .addMember("value", "$S", "/api/" + toKebabCase(model.getName()) + "s")
                .build())
            .addField(serviceClass, "service", Modifier.PRIVATE, Modifier.FINAL)
            .addMethod(generateConstructor(serviceClass))
            .addMethod(generateCreateEndpoint(model, entityClass, responseEntity))
            .addMethod(generateGetByIdEndpoint(model, entityClass, responseEntity))
            .addMethod(generateGetAllEndpoint(model, entityClass, responseEntity))
            .addMethod(generateUpdateEndpoint(model, entityClass, responseEntity))
            .addMethod(generateDeleteEndpoint(model, responseEntity))
            .build();
        
        return JavaFile.builder(basePackage + ".controller", controller)
            .addFileComment("Generated REST Controller")
            .build();
    }
    
    private JavaFile generatePythonController(ClassModel model, String basePackage) {
        StringBuilder pythonCode = new StringBuilder();
        pythonCode.append("from fastapi import APIRouter, Depends, HTTPException\n");
        pythonCode.append("from typing import List\n");
        pythonCode.append("from uuid import UUID\n\n");
        pythonCode.append("router = APIRouter(prefix=\"/api/").append(toKebabCase(model.getName())).append("s\", tags=[\"").append(model.getName()).append("\"])\n\n");
        pythonCode.append("@router.post(\"/\")\n");
        pythonCode.append("async def create_").append(model.getName().toLowerCase()).append("(entity: ").append(model.getName()).append("):\n");
        pythonCode.append("    return service.create(entity)\n\n");
        pythonCode.append("@router.get(\"/{id}\")\n");
        pythonCode.append("async def get_").append(model.getName().toLowerCase()).append("(id: UUID):\n");
        pythonCode.append("    entity = service.find_by_id(id)\n");
        pythonCode.append("    if not entity:\n");
        pythonCode.append("        raise HTTPException(status_code=404, detail=\"Not found\")\n");
        pythonCode.append("    return entity\n\n");
        pythonCode.append("@router.get(\"/\")\n");
        pythonCode.append("async def get_all_").append(model.getName().toLowerCase()).append("s() -> List[").append(model.getName()).append("]:\n");
        pythonCode.append("    return service.find_all()\n\n");
        pythonCode.append("@router.put(\"/{id}\")\n");
        pythonCode.append("async def update_").append(model.getName().toLowerCase()).append("(id: UUID, entity: ").append(model.getName()).append("):\n");
        pythonCode.append("    return service.update(entity)\n\n");
        pythonCode.append("@router.delete(\"/{id}\")\n");
        pythonCode.append("async def delete_").append(model.getName().toLowerCase()).append("(id: UUID):\n");
        pythonCode.append("    service.delete(id)\n");
        pythonCode.append("    return {\"message\": \"Deleted successfully\"}\n");
        
        TypeSpec dummyClass = TypeSpec.classBuilder("PythonControllerCode")
            .addJavadoc(pythonCode.toString())
            .build();
        
        return JavaFile.builder(basePackage, dummyClass).build();
    }
    
    private JavaFile generateCSharpController(ClassModel model, String basePackage) {
        StringBuilder csharpCode = new StringBuilder();
        csharpCode.append("using Microsoft.AspNetCore.Mvc;\n");
        csharpCode.append("using System;\n");
        csharpCode.append("using System.Collections.Generic;\n");
        csharpCode.append("using System.Threading.Tasks;\n\n");
        csharpCode.append("namespace ").append(basePackage).append(".Controllers\n{\n");
        csharpCode.append("    [ApiController]\n");
        csharpCode.append("    [Route(\"api/[controller]\")]\n");
        csharpCode.append("    public class ").append(model.getName()).append("Controller : ControllerBase\n    {\n");
        csharpCode.append("        private readonly I").append(model.getName()).append("Service _service;\n\n");
        csharpCode.append("        public ").append(model.getName()).append("Controller(I").append(model.getName()).append("Service service)\n        {\n");
        csharpCode.append("            _service = service;\n        }\n\n");
        csharpCode.append("        [HttpPost]\n");
        csharpCode.append("        public async Task<ActionResult<").append(model.getName()).append(">> Create(").append(model.getName()).append(" entity)\n        {\n");
        csharpCode.append("            var result = await _service.CreateAsync(entity);\n");
        csharpCode.append("            return CreatedAtAction(nameof(GetById), new { id = result.Id }, result);\n        }\n\n");
        csharpCode.append("        [HttpGet(\"{id}\")]\n");
        csharpCode.append("        public async Task<ActionResult<").append(model.getName()).append(">> GetById(Guid id)\n        {\n");
        csharpCode.append("            var entity = await _service.GetByIdAsync(id);\n");
        csharpCode.append("            return entity == null ? NotFound() : Ok(entity);\n        }\n");
        csharpCode.append("        // ... other methods\n");
        csharpCode.append("    }\n}");
        
        TypeSpec dummyClass = TypeSpec.classBuilder("CSharpControllerCode")
            .addJavadoc(csharpCode.toString())
            .build();
        
        return JavaFile.builder(basePackage, dummyClass).build();
    }
    
    private MethodSpec generateConstructor(ClassName serviceClass) {
        return MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(serviceClass, "service")
            .addStatement("this.service = service")
            .build();
    }
    
    private MethodSpec generateCreateEndpoint(ClassModel model, ClassName entityClass, ClassName responseEntity) {
        return MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PostMapping"))
            .addParameter(ParameterSpec.builder(entityClass, "entity")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestBody"))
                .build())
            .returns(ParameterizedTypeName.get(responseEntity, entityClass))
            .addStatement("$T created = service.create(entity)", entityClass)
            .addStatement("return $T.ok(created)", responseEntity)
            .build();
    }
    
    private MethodSpec generateGetByIdEndpoint(ClassModel model, ClassName entityClass, ClassName responseEntity) {
        return MethodSpec.methodBuilder("getById")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "GetMapping"))
                .addMember("value", "$S", "/{id}")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "id")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .returns(ParameterizedTypeName.get(responseEntity, entityClass))
            .addStatement("$T<$T> entity = service.findById(id)", Optional.class, entityClass)
            .addStatement("return entity.map($T::ok).orElse($T.notFound().build())", responseEntity, responseEntity)
            .build();
    }
    
    private MethodSpec generateGetAllEndpoint(ClassModel model, ClassName entityClass, ClassName responseEntity) {
        return MethodSpec.methodBuilder("getAll")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "GetMapping"))
            .returns(ParameterizedTypeName.get(responseEntity, ParameterizedTypeName.get(ClassName.get(List.class), entityClass)))
            .addStatement("$T<$T> entities = service.findAll()", List.class, entityClass)
            .addStatement("return $T.ok(entities)", responseEntity)
            .build();
    }
    
    private MethodSpec generateUpdateEndpoint(ClassModel model, ClassName entityClass, ClassName responseEntity) {
        return MethodSpec.methodBuilder("update")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "PutMapping"))
                .addMember("value", "$S", "/{id}")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "id")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .addParameter(ParameterSpec.builder(entityClass, "entity")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "RequestBody"))
                .build())
            .returns(ParameterizedTypeName.get(responseEntity, entityClass))
            .addStatement("$T updated = service.update(entity)", entityClass)
            .addStatement("return $T.ok(updated)", responseEntity)
            .build();
    }
    
    private MethodSpec generateDeleteEndpoint(ClassModel model, ClassName responseEntity) {
        return MethodSpec.methodBuilder("delete")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(ClassName.get("org.springframework.web.bind.annotation", "DeleteMapping"))
                .addMember("value", "$S", "/{id}")
                .build())
            .addParameter(ParameterSpec.builder(UUID.class, "id")
                .addAnnotation(ClassName.get("org.springframework.web.bind.annotation", "PathVariable"))
                .build())
            .returns(ParameterizedTypeName.get(responseEntity, ClassName.get(Void.class)))
            .addStatement("service.delete(id)")
            .addStatement("return $T.noContent().build()", responseEntity)
            .build();
    }
    
    private String toKebabCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }
}