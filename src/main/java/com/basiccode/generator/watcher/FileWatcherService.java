package com.basiccode.generator.watcher;

import com.basiccode.generator.enhanced.IncrementalGenerationManager;
import com.basiccode.generator.model.Diagram;
import com.basiccode.generator.parser.DiagramParser;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileWatcherService {
    private final WatchService watchService;
    private final ScheduledExecutorService executor;
    private final GeneratorConfig config;
    
    public FileWatcherService(GeneratorConfig config) throws IOException {
        this.config = config;
        this.watchService = FileSystems.getDefault().newWatchService();
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }
    
    public void startWatching() throws IOException {
        Path watchDir = Paths.get(config.getWatchDirectory());
        Files.createDirectories(watchDir);
        
        watchDir.register(watchService, 
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_MODIFY);
        
        System.out.println("👀 Watching: " + watchDir.toAbsolutePath());
        System.out.println("📝 Place .mermaid files here for auto-generation");
        
        executor.submit(() -> {
            while (true) {
                try {
                    WatchKey key = watchService.take();
                    
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path file = (Path) event.context();
                        if (file.toString().endsWith(".mermaid")) {
                            executor.schedule(() -> processFile(watchDir.resolve(file)), 1, TimeUnit.SECONDS);
                        }
                    }
                    
                    key.reset();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    
    private void processFile(Path mermaidFile) {
        try {
            System.out.println("\n🔄 Processing: " + mermaidFile.getFileName());
            
            String content = Files.readString(mermaidFile);
            DiagramParser parser = new DiagramParser();
            Diagram diagram = parser.parse(content);
            
            Path outputDir = Paths.get(config.getOutputDirectory());
            IncrementalGenerationManager manager = new IncrementalGenerationManager();
            
            var result = manager.generateIncremental(
                diagram.getClasses(), 
                config.getPackageName(), 
                outputDir
            );
            
            result.printSummary();
            System.out.println("✅ Generated for: " + mermaidFile.getFileName());
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
    
    public void stop() throws IOException {
        executor.shutdown();
        watchService.close();
    }
}