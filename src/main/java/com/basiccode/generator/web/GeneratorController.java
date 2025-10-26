package com.basiccode.generator.web;

import com.basiccode.generator.enhanced.IncrementalGenerationManager;
import com.basiccode.generator.generator.PythonProjectGenerator;
import com.basiccode.generator.generator.CSharpProjectGenerator;
import com.basiccode.generator.generator.DjangoProjectGenerator;
import com.basiccode.generator.generator.TypeScriptProjectGenerator;
import com.basiccode.generator.generator.PhpProjectGenerator;
import com.basiccode.generator.model.Diagram;
import com.basiccode.generator.parser.DiagramParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api/generate")
@CrossOrigin(origins = "*")
public class GeneratorController {
    
    @PostMapping("/crud")
    public ResponseEntity<byte[]> generateCrud(@RequestBody GenerateRequest request) throws Exception {
        DiagramParser parser = new DiagramParser();
        Diagram diagram = parser.parse(request.umlContent());
        
        Path tempDir = Files.createTempDirectory("generated");
        
        // Générer selon le langage
        switch (request.language().toLowerCase()) {
            case "java" -> generateJavaCrud(diagram, request, tempDir);
            case "python" -> generatePythonCrud(diagram, request, tempDir);
            case "django" -> generateDjangoCrud(diagram, request, tempDir);
            case "csharp" -> generateCSharpCrud(diagram, request, tempDir);
            case "typescript" -> generateTypeScriptCrud(diagram, request, tempDir);
            case "php" -> generatePhpEntitiesOnly(diagram, request, tempDir);
            default -> generateJavaCrud(diagram, request, tempDir);
        }
        
        byte[] zipBytes = createZip(tempDir);
        deleteDirectory(tempDir);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated-" + request.language() + "-code.zip")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipBytes);
    }
    
    private void generateJavaCrud(Diagram diagram, GenerateRequest request, Path tempDir) throws IOException {
        com.basiccode.generator.enhanced.EnhancedEntityGenerator entityGen = new com.basiccode.generator.enhanced.EnhancedEntityGenerator();
        com.basiccode.generator.generator.RepositoryGenerator repoGen = new com.basiccode.generator.generator.RepositoryGenerator();
        com.basiccode.generator.generator.ServiceGenerator serviceGen = new com.basiccode.generator.generator.ServiceGenerator();
        com.basiccode.generator.generator.ControllerGenerator controllerGen = new com.basiccode.generator.generator.ControllerGenerator();
        
        for (com.basiccode.generator.model.ClassModel clazz : diagram.getClasses()) {
            var entityFile = entityGen.generateEntity(clazz, request.packageName(), com.basiccode.generator.config.Framework.SPRING_BOOT);
            var repoFile = repoGen.generateRepository(clazz, request.packageName(), "java");
            var serviceFile = serviceGen.generateService(clazz, request.packageName(), "java");
            var controllerFile = controllerGen.generateController(clazz, request.packageName(), "java");
            
            writeJavaFile(entityFile, tempDir, "entity");
            writeJavaFile(repoFile, tempDir, "repository");
            writeJavaFile(serviceFile, tempDir, "service");
            writeJavaFile(controllerFile, tempDir, "controller");
        }
    }
    
    private void generatePythonCrud(Diagram diagram, GenerateRequest request, Path tempDir) throws IOException {
        com.basiccode.generator.generator.PythonProjectGenerator pythonGen = new com.basiccode.generator.generator.PythonProjectGenerator();
        pythonGen.generateCompleteProject(diagram.getClasses(), request.packageName(), tempDir);
    }
    
    private void generateCSharpCrud(Diagram diagram, GenerateRequest request, Path tempDir) throws IOException {
        com.basiccode.generator.generator.CSharpProjectGenerator csharpGen = new com.basiccode.generator.generator.CSharpProjectGenerator();
        csharpGen.generateCompleteProject(diagram.getClasses(), request.packageName(), tempDir);
    }
    
    private void writePythonFile(com.squareup.javapoet.JavaFile javaFile, Path dir, String fileName) throws IOException {
        Files.createDirectories(dir);
        // Extraire le code Python du JavaDoc
        String pythonCode = javaFile.typeSpec.javadoc.toString();
        Files.writeString(dir.resolve(fileName), pythonCode);
    }
    
    private void writeJavaFile(com.squareup.javapoet.JavaFile javaFile, Path tempDir, String layer) throws IOException {
        String packagePath = javaFile.packageName.replace(".", "/");
        Path packageDir = tempDir.resolve("src/main/java").resolve(packagePath);
        Files.createDirectories(packageDir);
        
        String fileName = javaFile.typeSpec.name + ".java";
        Files.writeString(packageDir.resolve(fileName), javaFile.toString());
    }
    
    private void generateDjangoCrud(Diagram diagram, GenerateRequest request, Path tempDir) throws IOException {
        DjangoProjectGenerator djangoGen = new DjangoProjectGenerator();
        djangoGen.generateCompleteProject(diagram.getClasses(), request.packageName(), tempDir);
    }
    
    private void generateTypeScriptCrud(Diagram diagram, GenerateRequest request, Path tempDir) throws IOException {
        TypeScriptProjectGenerator tsGen = new TypeScriptProjectGenerator();
        tsGen.generateCompleteProject(diagram.getClasses(), request.packageName(), tempDir);
    }
    
    private void generatePhpEntitiesOnly(Diagram diagram, GenerateRequest request, Path tempDir) throws IOException {
        PhpProjectGenerator phpGen = new PhpProjectGenerator();
        phpGen.generateCompleteProject(diagram.getClasses(), request.packageName(), tempDir);
    }
    
    private void writeTypeScriptFile(com.squareup.javapoet.JavaFile javaFile, Path dir, String fileName) throws IOException {
        Files.createDirectories(dir);
        String tsCode = javaFile.typeSpec.javadoc.toString();
        Files.writeString(dir.resolve(fileName), tsCode);
    }
    
    private void writePhpFile(com.squareup.javapoet.JavaFile javaFile, Path dir, String fileName) throws IOException {
        Files.createDirectories(dir);
        String phpCode = javaFile.typeSpec.javadoc.toString();
        // Remove JavaDoc formatting
        phpCode = phpCode.replace("Generated PHP Entity:\n", "").trim();
        Files.writeString(dir.resolve(fileName), phpCode);
    }
    
    private void writeCSharpFile(com.squareup.javapoet.JavaFile javaFile, Path dir, String fileName) throws IOException {
        Files.createDirectories(dir);
        String csharpCode = javaFile.typeSpec.javadoc.toString();
        Files.writeString(dir.resolve(fileName), csharpCode);
    }
    
    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateUml(@RequestBody String umlContent) {
        try {
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(umlContent);
            return ResponseEntity.ok(new ValidationResult(true, "Valid UML", diagram.getClasses().size()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ValidationResult(false, e.getMessage(), 0));
        }
    }
    
    private byte[] createZip(Path sourceDir) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            Files.walk(sourceDir)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String relativePath = sourceDir.relativize(file).toString();
                        ZipEntry entry = new ZipEntry(relativePath);
                        zos.putNextEntry(entry);
                        Files.copy(file, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        }
        return baos.toByteArray();
    }
    
    private void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
            .sorted((a, b) -> b.compareTo(a))
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {}
            });
    }
    
    public record GenerateRequest(String umlContent, String packageName, String language, String projectType) {
        public GenerateRequest {
            // Valeurs par défaut
            if (language == null || language.isEmpty()) {
                language = "java";
            }
            if (projectType == null || projectType.isEmpty()) {
                projectType = "crud-only";
            }
        }
    }
    public record ValidationResult(boolean valid, String message, int classCount) {}
}