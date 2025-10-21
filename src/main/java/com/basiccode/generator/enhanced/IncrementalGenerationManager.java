package com.basiccode.generator.enhanced;

import com.basiccode.generator.merger.IntelligentMerger;
import com.basiccode.generator.model.ClassModel;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IncrementalGenerationManager {
    private final IntelligentMerger merger = new IntelligentMerger();
    private final List<GenerationReport> reports = new ArrayList<>();
    
    public GenerationResult generateIncremental(List<ClassModel> classes, String basePackage, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        
        EnhancedEntityGenerator entityGen = new EnhancedEntityGenerator();
        
        for (ClassModel classModel : classes) {
            generateEntityIncremental(classModel, basePackage, outputDir, entityGen);
        }
        
        return new GenerationResult(reports);
    }
    
    private void generateEntityIncremental(ClassModel classModel, String basePackage, Path outputDir, EnhancedEntityGenerator generator) throws IOException {
        String entityName = classModel.getName();
        Path entityPath = getEntityPath(outputDir, basePackage, entityName);
        
        // Generate new code
        JavaFile newCode = generator.generateEnhancedEntity(classModel, basePackage);
        
        GenerationReport report = new GenerationReport(entityName, "Entity");
        
        if (Files.exists(entityPath)) {
            // Create backup
            createBackup(entityPath);
            
            // Merge with existing
            IntelligentMerger.MergeResult result = merger.mergeWithExisting(newCode, entityPath);
            
            if (result.getChanges().hasChanges()) {
                // Write merged code
                Files.writeString(entityPath, result.getMergedCode());
                report.setMerged(true);
                report.addChanges(result.getChanges().getNewFields(), result.getChanges().getNewMethods());
                System.out.println("🔄 Merged " + entityName + " - Added: " + 
                    result.getChanges().getNewFields().size() + " fields, " + 
                    result.getChanges().getNewMethods().size() + " methods");
            } else {
                report.setSkipped(true);
                System.out.println("⏭️  Skipped " + entityName + " - No changes detected");
            }
        } else {
            // New file
            Files.createDirectories(entityPath.getParent());
            newCode.writeTo(outputDir);
            report.setCreated(true);
            System.out.println("✅ Created " + entityName);
        }
        
        reports.add(report);
    }
    
    private Path getEntityPath(Path outputDir, String basePackage, String entityName) {
        String packagePath = basePackage.replace('.', '/');
        return outputDir.resolve("src/main/java")
            .resolve(packagePath)
            .resolve("entity")
            .resolve(entityName + ".java");
    }
    
    private void createBackup(Path originalFile) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backupDir = originalFile.getParent().resolve(".backups");
        Files.createDirectories(backupDir);
        
        String fileName = originalFile.getFileName().toString();
        String backupName = fileName.replace(".java", "_" + timestamp + ".java.bak");
        Path backupPath = backupDir.resolve(backupName);
        
        Files.copy(originalFile, backupPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("💾 Backup created: " + backupPath.getFileName());
    }
    
    public static class GenerationResult {
        private final List<GenerationReport> reports;
        
        public GenerationResult(List<GenerationReport> reports) {
            this.reports = reports;
        }
        
        public void printSummary() {
            long created = reports.stream().mapToLong(r -> r.isCreated() ? 1 : 0).sum();
            long merged = reports.stream().mapToLong(r -> r.isMerged() ? 1 : 0).sum();
            long skipped = reports.stream().mapToLong(r -> r.isSkipped() ? 1 : 0).sum();
            
            System.out.println("\n📊 GENERATION SUMMARY");
            System.out.println("====================");
            System.out.println("✅ Created: " + created);
            System.out.println("🔄 Merged: " + merged);
            System.out.println("⏭️  Skipped: " + skipped);
            System.out.println("📁 Total: " + reports.size());
            
            if (merged > 0) {
                System.out.println("\n🔍 MERGE DETAILS:");
                reports.stream()
                    .filter(GenerationReport::isMerged)
                    .forEach(report -> {
                        System.out.println("  " + report.getClassName() + ":");
                        if (!report.getNewFields().isEmpty()) {
                            System.out.println("    + Fields: " + String.join(", ", report.getNewFields()));
                        }
                        if (!report.getNewMethods().isEmpty()) {
                            System.out.println("    + Methods: " + String.join(", ", report.getNewMethods()));
                        }
                    });
            }
        }
        
        public List<GenerationReport> getReports() { return reports; }
    }
    
    public static class GenerationReport {
        private final String className;
        private final String type;
        private boolean created = false;
        private boolean merged = false;
        private boolean skipped = false;
        private List<String> newFields = new ArrayList<>();
        private List<String> newMethods = new ArrayList<>();
        
        public GenerationReport(String className, String type) {
            this.className = className;
            this.type = type;
        }
        
        public void setCreated(boolean created) { this.created = created; }
        public void setMerged(boolean merged) { this.merged = merged; }
        public void setSkipped(boolean skipped) { this.skipped = skipped; }
        
        public void addChanges(List<String> fields, List<String> methods) {
            this.newFields.addAll(fields);
            this.newMethods.addAll(methods);
        }
        
        public String getClassName() { return className; }
        public String getType() { return type; }
        public boolean isCreated() { return created; }
        public boolean isMerged() { return merged; }
        public boolean isSkipped() { return skipped; }
        public List<String> getNewFields() { return newFields; }
        public List<String> getNewMethods() { return newMethods; }
    }
}