package com.basiccode.generator.cli;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.model.*;
import com.basiccode.generator.parser.DiagramParser;
// import com.basiccode.generator.merger.IncrementalGenerator;
import com.squareup.javapoet.JavaFile;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "uml-generator", 
         mixinStandardHelpOptions = true,
         version = "1.0.0",
         description = "Generates CRUD code with MVC architecture from UML diagrams",
         subcommands = {GeneratorCLI.WatchCommand.class, GeneratorCLI.DynamicGenerateCommand.class})
public class GeneratorCLI implements Callable<Integer> {
    
    @Parameters(index = "0", description = "Input UML file (Mermaid or PlantUML)")
    private File inputFile;
    
    @Option(names = {"-o", "--output"}, description = "Output directory")
    private File outputDir = new File("generated");
    
    @Option(names = {"-p", "--package"}, description = "Base package name")
    private String basePackage = "com.example";
    
    @Option(names = {"-l", "--language"}, description = "Target language (java, python, csharp, c, cpp)")
    private String language = "java";
    
    @Option(names = {"--with-services"}, description = "Generate Service layer")
    private boolean generateServices = true;
    
    @Option(names = {"--with-controllers"}, description = "Generate Controller layer")
    private boolean generateControllers = true;
    
    @Option(names = {"--with-repositories"}, description = "Generate Repository layer")
    private boolean generateRepositories = true;
    
    @Option(names = {"--incremental"}, description = "Use incremental generation (preserve manual changes)")
    private boolean incrementalMode = true;
    
    @Override
    public Integer call() throws Exception {
        System.out.println("🚀 UML to CRUD Generator");
        System.out.println("========================");
        System.out.println("Input: " + inputFile.getAbsolutePath());
        System.out.println("Output: " + outputDir.getAbsolutePath());
        System.out.println("Language: " + language.toUpperCase());
        System.out.println("Package: " + basePackage);
        System.out.println();
        
        // 1. Parse UML
        System.out.println("📖 Parsing UML diagram...");
        DiagramParser parser = new DiagramParser();
        Diagram diagram = parser.parse(Files.readString(inputFile.toPath()));
        System.out.println("✅ Found " + diagram.getClasses().size() + " classes");
        
        // 2. Create output directories
        createOutputDirectories();
        
        // 3. Generate code (incremental or full)
        if (incrementalMode && language.equals("java")) {
            System.out.println("🔄 Incremental generation mode (preserving manual changes)...");
            com.basiccode.generator.enhanced.IncrementalGenerationManager incrementalGen = 
                new com.basiccode.generator.enhanced.IncrementalGenerationManager();
            com.basiccode.generator.enhanced.IncrementalGenerationManager.GenerationResult result = 
                incrementalGen.generateIncremental(diagram.getClasses(), basePackage, outputDir.toPath());
            result.printSummary();
        } else {
            System.out.println("⚙️  Full generation mode...");
            generateFullMode(diagram, basePackage, language);
        }
        
        System.out.println();
        System.out.println("✅ Generation completed successfully!");
        System.out.println("📁 Output directory: " + outputDir.getAbsolutePath());
        
        return 0;
    }
    
    private void generateFullMode(Diagram diagram, String basePackage, String language) throws IOException {
        // Generate entities
        System.out.println("⚙️  Generating entities...");
        EntityGenerator entityGenerator = new EntityGenerator();
        for (ClassModel clazz : diagram.getClasses()) {
            JavaFile javaFile = entityGenerator.generateEntity(clazz, basePackage, language);
            writeToFile(javaFile, getEntityPath());
            System.out.println("  ✓ " + clazz.getName() + getFileExtension());
        }
        
        // Generate repositories
        if (generateRepositories) {
            System.out.println("⚙️  Generating repositories...");
            RepositoryGenerator repoGenerator = new RepositoryGenerator();
            for (ClassModel clazz : diagram.getClasses()) {
                JavaFile javaFile = repoGenerator.generateRepository(clazz, basePackage, language);
                writeToFile(javaFile, getRepositoryPath());
                System.out.println("  ✓ " + clazz.getName() + "Repository" + getFileExtension());
            }
        }
        
        // Generate services
        if (generateServices) {
            System.out.println("⚙️  Generating services...");
            ServiceGenerator serviceGen = new ServiceGenerator();
            for (ClassModel clazz : diagram.getClasses()) {
                JavaFile javaFile = serviceGen.generateService(clazz, basePackage, language);
                writeToFile(javaFile, getServicePath());
                System.out.println("  ✓ " + clazz.getName() + "Service" + getFileExtension());
            }
        }
        
        // Generate controllers
        if (generateControllers) {
            System.out.println("⚙️  Generating controllers...");
            ControllerGenerator controllerGen = new ControllerGenerator();
            for (ClassModel clazz : diagram.getClasses()) {
                JavaFile javaFile = controllerGen.generateController(clazz, basePackage, language);
                writeToFile(javaFile, getControllerPath());
                System.out.println("  ✓ " + clazz.getName() + "Controller" + getFileExtension());
            }
        }
    }
    
    private void createOutputDirectories() throws IOException {
        switch (language.toLowerCase()) {
            case "java":
                createJavaDirectories();
                break;
            case "python":
                createPythonDirectories();
                break;
            case "csharp":
                createCSharpDirectories();
                break;
            case "c":
            case "cpp":
                createCDirectories();
                break;
        }
    }
    
    private void createJavaDirectories() throws IOException {
        String packagePath = basePackage.replace('.', '/');
        Files.createDirectories(outputDir.toPath().resolve("src/main/java").resolve(packagePath).resolve("entity"));
        Files.createDirectories(outputDir.toPath().resolve("src/main/java").resolve(packagePath).resolve("repository"));
        Files.createDirectories(outputDir.toPath().resolve("src/main/java").resolve(packagePath).resolve("service"));
        Files.createDirectories(outputDir.toPath().resolve("src/main/java").resolve(packagePath).resolve("controller"));
    }
    
    private void createPythonDirectories() throws IOException {
        Files.createDirectories(outputDir.toPath().resolve("entities"));
        Files.createDirectories(outputDir.toPath().resolve("repositories"));
        Files.createDirectories(outputDir.toPath().resolve("services"));
        Files.createDirectories(outputDir.toPath().resolve("controllers"));
    }
    
    private void createCSharpDirectories() throws IOException {
        Files.createDirectories(outputDir.toPath().resolve("Entities"));
        Files.createDirectories(outputDir.toPath().resolve("Repositories"));
        Files.createDirectories(outputDir.toPath().resolve("Services"));
        Files.createDirectories(outputDir.toPath().resolve("Controllers"));
    }
    
    private void createCDirectories() throws IOException {
        Files.createDirectories(outputDir.toPath().resolve("include"));
        Files.createDirectories(outputDir.toPath().resolve("src"));
    }
    
    private void writeToFile(JavaFile javaFile, Path basePath) throws IOException {
        if (language.equals("java")) {
            javaFile.writeTo(basePath);
        } else {
            // For non-Java languages, extract the code from JavaDoc and write to appropriate file
            String content = javaFile.typeSpec.javadoc.toString();
            String fileName = javaFile.typeSpec.name + getFileExtension();
            Files.writeString(basePath.resolve(fileName), content);
        }
    }
    
    private Path getEntityPath() {
        return switch (language.toLowerCase()) {
            case "java" -> outputDir.toPath().resolve("src/main/java");
            case "python" -> outputDir.toPath().resolve("entities");
            case "csharp" -> outputDir.toPath().resolve("Entities");
            case "c", "cpp" -> outputDir.toPath().resolve("include");
            default -> outputDir.toPath();
        };
    }
    
    private Path getRepositoryPath() {
        return switch (language.toLowerCase()) {
            case "java" -> outputDir.toPath().resolve("src/main/java");
            case "python" -> outputDir.toPath().resolve("repositories");
            case "csharp" -> outputDir.toPath().resolve("Repositories");
            case "c", "cpp" -> outputDir.toPath().resolve("src");
            default -> outputDir.toPath();
        };
    }
    
    private Path getServicePath() {
        return switch (language.toLowerCase()) {
            case "java" -> outputDir.toPath().resolve("src/main/java");
            case "python" -> outputDir.toPath().resolve("services");
            case "csharp" -> outputDir.toPath().resolve("Services");
            case "c", "cpp" -> outputDir.toPath().resolve("src");
            default -> outputDir.toPath();
        };
    }
    
    private Path getControllerPath() {
        return switch (language.toLowerCase()) {
            case "java" -> outputDir.toPath().resolve("src/main/java");
            case "python" -> outputDir.toPath().resolve("controllers");
            case "csharp" -> outputDir.toPath().resolve("Controllers");
            case "c", "cpp" -> outputDir.toPath().resolve("src");
            default -> outputDir.toPath();
        };
    }
    
    private String getFileExtension() {
        return switch (language.toLowerCase()) {
            case "java" -> ".java";
            case "python" -> ".py";
            case "csharp" -> ".cs";
            case "c" -> ".c";
            case "cpp" -> ".cpp";
            default -> ".txt";
        };
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new GeneratorCLI()).execute(args);
        System.exit(exitCode);
    }
    
    @Command(name = "watch", description = "Watch directory for .mermaid files and auto-generate code")
    public static class WatchCommand implements Callable<Integer> {
        @Option(names = {"-c", "--config"}, description = "Config file path", defaultValue = "generator.yml")
        private String configPath;
        
        @Override
        public Integer call() throws Exception {
            System.out.println("🚀 UML Code Generator - Watch Mode");
            System.out.println("=================================\n");
            
            com.basiccode.generator.watcher.GeneratorConfig config = 
                com.basiccode.generator.watcher.GeneratorConfig.load(configPath);
            
            System.out.println("📋 Configuration:");
            System.out.println("   Watch Directory: " + config.getWatchDirectory());
            System.out.println("   Output Directory: " + config.getOutputDirectory());
            System.out.println("   Package Name: " + config.getPackageName());
            System.out.println("   Incremental: " + config.isIncremental());
            System.out.println();
            
            com.basiccode.generator.watcher.FileWatcherService watcher = 
                new com.basiccode.generator.watcher.FileWatcherService(config);
            
            watcher.startWatching();
            
            System.out.println("Press Ctrl+C to stop watching...");
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    watcher.stop();
                    System.out.println("\n👋 Watcher stopped");
                } catch (Exception e) {
                    System.err.println("Error stopping watcher: " + e.getMessage());
                }
            }));
            
            Thread.currentThread().join();
            return 0;
        }
    }
    
    @Command(name = "generate-dynamic", description = "Generate dynamic CRUD code with conditional fields")
    public static class DynamicGenerateCommand implements Callable<Integer> {
        @Parameters(index = "0", description = "Input UML file")
        private File inputFile;
        
        @Option(names = {"-o", "--output"}, description = "Output directory", defaultValue = "generated-dynamic")
        private String outputDir;
        
        @Option(names = {"-p", "--package"}, description = "Base package name", defaultValue = "com.example")
        private String packageName;
        
        @Override
        public Integer call() throws Exception {
            System.out.println("🔥 Generating dynamic CRUD code from: " + inputFile.getAbsolutePath());
            
            // Parse UML
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(Files.readString(inputFile.toPath()));
            
            // Generate with dynamic features
            com.basiccode.generator.dynamic.DynamicEntityGenerator entityGen = new com.basiccode.generator.dynamic.DynamicEntityGenerator();
            com.basiccode.generator.dynamic.DynamicServiceGenerator serviceGen = new com.basiccode.generator.dynamic.DynamicServiceGenerator();
            com.basiccode.generator.dynamic.DynamicRepositoryGenerator repoGen = new com.basiccode.generator.dynamic.DynamicRepositoryGenerator();
            com.basiccode.generator.dynamic.DynamicControllerGenerator controllerGen = new com.basiccode.generator.dynamic.DynamicControllerGenerator();
            
            Path outputPath = Path.of(outputDir);
            Files.createDirectories(outputPath);
            
            for (ClassModel classModel : diagram.getClasses()) {
                // Generate dynamic entity
                JavaFile entityFile = entityGen.generateDynamicEntity(classModel, packageName);
                entityFile.writeTo(outputPath);
                
                // Generate dynamic service
                JavaFile serviceFile = serviceGen.generateDynamicService(classModel, packageName);
                serviceFile.writeTo(outputPath);
                
                // Generate dynamic repository
                JavaFile repoFile = repoGen.generateDynamicRepository(classModel, packageName);
                repoFile.writeTo(outputPath);
                
                // Generate dynamic controller
                JavaFile controllerFile = controllerGen.generateDynamicController(classModel, packageName);
                controllerFile.writeTo(outputPath);
                
                System.out.println("✅ Generated dynamic CRUD for: " + classModel.getName());
            }
            
            System.out.println("🎉 Dynamic generation completed successfully!");
            return 0;
        }
    }
}