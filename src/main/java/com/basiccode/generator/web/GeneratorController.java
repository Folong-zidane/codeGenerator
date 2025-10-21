package com.basiccode.generator.web;

import com.basiccode.generator.enhanced.IncrementalGenerationManager;
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
        IncrementalGenerationManager manager = new IncrementalGenerationManager();
        manager.generateIncremental(diagram.getClasses(), request.packageName(), tempDir);
        
        byte[] zipBytes = createZip(tempDir);
        deleteDirectory(tempDir);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated-code.zip")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(zipBytes);
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
    
    public record GenerateRequest(String umlContent, String packageName) {}
    public record ValidationResult(boolean valid, String message, int classCount) {}
}