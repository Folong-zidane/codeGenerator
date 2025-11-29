package com.basiccode.generator.merge;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class IntelligentMergeStrategy implements IMergeStrategy {
    
    private static final String GENERATED_MARKER = "@Generated";
    
    @Override
    public MergeResult merge(String newContent, Path existingFile) throws Exception {
        if (!Files.exists(existingFile)) {
            return new MergeResult(newContent, true, "New file created");
        }
        
        JavaParser parser = new JavaParser();
        
        // Parse existing and new content
        CompilationUnit existing = parser.parse(existingFile).getResult()
            .orElseThrow(() -> new RuntimeException("Cannot parse existing file"));
        CompilationUnit newCode = parser.parse(newContent).getResult()
            .orElseThrow(() -> new RuntimeException("Cannot parse new content"));
        
        // Perform intelligent merge
        CompilationUnit merged = performIntelligentMerge(existing, newCode);
        
        return new MergeResult(merged.toString(), true, "Intelligently merged");
    }
    
    private CompilationUnit performIntelligentMerge(CompilationUnit existing, CompilationUnit newCode) {
        CompilationUnit merged = existing.clone();
        
        ClassOrInterfaceDeclaration existingClass = getFirstClass(existing);
        ClassOrInterfaceDeclaration newClass = getFirstClass(newCode);
        ClassOrInterfaceDeclaration mergedClass = getFirstClass(merged);
        
        if (existingClass != null && newClass != null && mergedClass != null) {
            // Merge imports
            mergeImports(merged, newCode);
            
            // Merge fields (add new ones only)
            mergeFields(mergedClass, existingClass, newClass);
            
            // Merge methods (preserve manual, update generated)
            mergeMethods(mergedClass, existingClass, newClass);
        }
        
        return merged;
    }
    
    private void mergeImports(CompilationUnit merged, CompilationUnit newCode) {
        Set<String> existingImports = merged.getImports().stream()
            .map(imp -> imp.getNameAsString())
            .collect(Collectors.toSet());
        
        newCode.getImports().forEach(imp -> {
            if (!existingImports.contains(imp.getNameAsString())) {
                merged.addImport(imp.getNameAsString());
            }
        });
    }
    
    private void mergeFields(ClassOrInterfaceDeclaration merged, 
                           ClassOrInterfaceDeclaration existing, 
                           ClassOrInterfaceDeclaration newClass) {
        
        Set<String> existingFields = existing.getFields().stream()
            .flatMap(field -> field.getVariables().stream())
            .map(var -> var.getNameAsString())
            .collect(Collectors.toSet());
        
        // Add new fields only
        newClass.getFields().forEach(newField -> {
            boolean hasNewFields = newField.getVariables().stream()
                .anyMatch(var -> !existingFields.contains(var.getNameAsString()));
            
            if (hasNewFields) {
                FieldDeclaration clonedField = newField.clone();
                clonedField.getVariables().removeIf(var -> 
                    existingFields.contains(var.getNameAsString()));
                
                if (!clonedField.getVariables().isEmpty()) {
                    merged.addMember(clonedField);
                }
            }
        });
    }
    
    private void mergeMethods(ClassOrInterfaceDeclaration merged,
                            ClassOrInterfaceDeclaration existing,
                            ClassOrInterfaceDeclaration newClass) {
        
        Map<String, MethodDeclaration> existingMethods = existing.getMethods().stream()
            .collect(Collectors.toMap(this::getMethodSignature, m -> m));
        
        newClass.getMethods().forEach(newMethod -> {
            String signature = getMethodSignature(newMethod);
            
            if (!existingMethods.containsKey(signature)) {
                // New method - add it
                merged.addMember(newMethod.clone());
            } else {
                // Method exists - replace if generated
                MethodDeclaration existingMethod = existingMethods.get(signature);
                if (isGeneratedMethod(existingMethod)) {
                    merged.remove(existingMethod);
                    merged.addMember(newMethod.clone());
                }
            }
        });
    }
    
    private ClassOrInterfaceDeclaration getFirstClass(CompilationUnit unit) {
        return unit.getTypes().stream()
            .filter(type -> type instanceof ClassOrInterfaceDeclaration)
            .map(type -> (ClassOrInterfaceDeclaration) type)
            .findFirst()
            .orElse(null);
    }
    
    private String getMethodSignature(MethodDeclaration method) {
        return method.getNameAsString() + "(" + 
            method.getParameters().stream()
                .map(p -> p.getType().asString())
                .collect(Collectors.joining(",")) + ")";
    }
    
    private boolean isGeneratedMethod(MethodDeclaration method) {
        return method.getAnnotations().stream()
            .anyMatch(ann -> ann.getNameAsString().contains("Generated"));
    }
    
    @Override
    public boolean canHandle(String fileName) {
        return fileName.endsWith(".java");
    }
    
    @Override
    public String getStrategyName() {
        return "IntelligentJavaMerge";
    }
}