package com.basiccode.generator.merger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class IntelligentMerger {
    private static final String GENERATED_MARKER = "// Generated";
    private static final String MANUAL_MARKER = "// Manual";
    
    public MergeResult mergeWithExisting(JavaFile newCode, Path existingFile) throws IOException {
        if (!Files.exists(existingFile)) {
            return MergeResult.newFile(newCode);
        }
        
        // Parse existing file
        JavaParser parser = new JavaParser();
        CompilationUnit existingUnit = parser.parse(existingFile).getResult()
            .orElseThrow(() -> new RuntimeException("Cannot parse existing file: " + existingFile));
        
        // Parse new generated code
        CompilationUnit newUnit = parser.parse(newCode.toString()).getResult()
            .orElseThrow(() -> new RuntimeException("Cannot parse new generated code"));
        
        // Perform intelligent merge
        CompilationUnit mergedUnit = performMerge(existingUnit, newUnit);
        
        return MergeResult.merged(mergedUnit.toString(), analyzeChanges(existingUnit, newUnit));
    }
    
    private CompilationUnit performMerge(CompilationUnit existing, CompilationUnit newCode) {
        CompilationUnit merged = existing.clone();
        
        // Get classes from both units
        ClassOrInterfaceDeclaration existingClass = existing.getClassByName(getClassName(existing))
            .orElseThrow(() -> new RuntimeException("No class found in existing file"));
        ClassOrInterfaceDeclaration newClass = newCode.getClassByName(getClassName(newCode))
            .orElseThrow(() -> new RuntimeException("No class found in new code"));
        ClassOrInterfaceDeclaration mergedClass = merged.getClassByName(getClassName(merged))
            .orElseThrow(() -> new RuntimeException("No class found in merged file"));
        
        // Merge imports
        mergeImports(merged, newCode);
        
        // Merge annotations
        mergeClassAnnotations(mergedClass, newClass);
        
        // Merge fields (only add new ones)
        mergeFields(mergedClass, existingClass, newClass);
        
        // Merge methods (preserve manual, add new generated)
        mergeMethods(mergedClass, existingClass, newClass);
        
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
    
    private void mergeClassAnnotations(ClassOrInterfaceDeclaration merged, ClassOrInterfaceDeclaration newClass) {
        Set<String> existingAnnotations = merged.getAnnotations().stream()
            .map(ann -> ann.getNameAsString())
            .collect(Collectors.toSet());
        
        newClass.getAnnotations().forEach(ann -> {
            if (!existingAnnotations.contains(ann.getNameAsString())) {
                merged.addAnnotation(ann.clone());
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
        
        // Add only new fields from generated code
        newClass.getFields().forEach(newField -> {
            boolean hasNewFields = newField.getVariables().stream()
                .anyMatch(var -> !existingFields.contains(var.getNameAsString()));
            
            if (hasNewFields) {
                // Only add variables that don't exist
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
        
        // Add new generated methods, preserve existing ones
        newClass.getMethods().forEach(newMethod -> {
            String signature = getMethodSignature(newMethod);
            
            if (!existingMethods.containsKey(signature)) {
                // New method - add it
                merged.addMember(newMethod.clone());
            } else {
                // Method exists - check if it's generated or manual
                MethodDeclaration existingMethod = existingMethods.get(signature);
                if (isGeneratedMethod(existingMethod) && !isManualMethod(existingMethod)) {
                    // Replace generated method with new version
                    merged.remove(existingMethod);
                    merged.addMember(newMethod.clone());
                }
                // If manual method, keep existing version
            }
        });
    }
    
    private String getMethodSignature(MethodDeclaration method) {
        return method.getNameAsString() + "(" + 
            method.getParameters().stream()
                .map(p -> p.getType().asString())
                .collect(Collectors.joining(",")) + ")";
    }
    
    private boolean isGeneratedMethod(MethodDeclaration method) {
        return method.getComment().map(Comment::getContent)
            .map(content -> content.contains(GENERATED_MARKER))
            .orElse(false) ||
            method.getAnnotations().stream()
                .anyMatch(ann -> ann.getNameAsString().contains("Generated"));
    }
    
    private boolean isManualMethod(MethodDeclaration method) {
        return method.getComment().map(Comment::getContent)
            .map(content -> content.contains(MANUAL_MARKER))
            .orElse(false);
    }
    
    private String getClassName(CompilationUnit unit) {
        return unit.getTypes().stream()
            .filter(type -> type instanceof ClassOrInterfaceDeclaration)
            .map(type -> type.getNameAsString())
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No class found"));
    }
    
    private ChangeAnalysis analyzeChanges(CompilationUnit existing, CompilationUnit newCode) {
        ChangeAnalysis analysis = new ChangeAnalysis();
        
        ClassOrInterfaceDeclaration existingClass = existing.getClassByName(getClassName(existing)).orElse(null);
        ClassOrInterfaceDeclaration newClass = newCode.getClassByName(getClassName(newCode)).orElse(null);
        
        if (existingClass != null && newClass != null) {
            // Analyze field changes
            Set<String> existingFields = existingClass.getFields().stream()
                .flatMap(f -> f.getVariables().stream())
                .map(v -> v.getNameAsString())
                .collect(Collectors.toSet());
            
            Set<String> newFields = newClass.getFields().stream()
                .flatMap(f -> f.getVariables().stream())
                .map(v -> v.getNameAsString())
                .collect(Collectors.toSet());
            
            newFields.stream()
                .filter(field -> !existingFields.contains(field))
                .forEach(analysis::addNewField);
            
            // Analyze method changes
            Set<String> existingMethods = existingClass.getMethods().stream()
                .map(this::getMethodSignature)
                .collect(Collectors.toSet());
            
            Set<String> newMethods = newClass.getMethods().stream()
                .map(this::getMethodSignature)
                .collect(Collectors.toSet());
            
            newMethods.stream()
                .filter(method -> !existingMethods.contains(method))
                .forEach(analysis::addNewMethod);
        }
        
        return analysis;
    }
    
    public static class MergeResult {
        private final String mergedCode;
        private final boolean isNewFile;
        private final ChangeAnalysis changes;
        
        private MergeResult(String mergedCode, boolean isNewFile, ChangeAnalysis changes) {
            this.mergedCode = mergedCode;
            this.isNewFile = isNewFile;
            this.changes = changes;
        }
        
        public static MergeResult newFile(JavaFile javaFile) {
            return new MergeResult(javaFile.toString(), true, new ChangeAnalysis());
        }
        
        public static MergeResult merged(String mergedCode, ChangeAnalysis changes) {
            return new MergeResult(mergedCode, false, changes);
        }
        
        public String getMergedCode() { return mergedCode; }
        public boolean isNewFile() { return isNewFile; }
        public ChangeAnalysis getChanges() { return changes; }
    }
    
    public static class ChangeAnalysis {
        private final List<String> newFields = new ArrayList<>();
        private final List<String> newMethods = new ArrayList<>();
        
        public void addNewField(String field) { newFields.add(field); }
        public void addNewMethod(String method) { newMethods.add(method); }
        
        public List<String> getNewFields() { return newFields; }
        public List<String> getNewMethods() { return newMethods; }
        
        public boolean hasChanges() { return !newFields.isEmpty() || !newMethods.isEmpty(); }
    }
}