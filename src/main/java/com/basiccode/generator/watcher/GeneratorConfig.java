package com.basiccode.generator.watcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeneratorConfig {
    @JsonProperty("watch_directory")
    private String watchDirectory = "./diagrams";
    
    @JsonProperty("output_directory") 
    private String outputDirectory = "./generated";
    
    @JsonProperty("package_name")
    private String packageName = "com.example";
    
    @JsonProperty("incremental")
    private boolean incremental = true;
    
    @JsonProperty("auto_watch")
    private boolean autoWatch = true;
    
    public static GeneratorConfig load(String configPath) throws IOException {
        Path path = Paths.get(configPath);
        if (!Files.exists(path)) {
            GeneratorConfig defaultConfig = new GeneratorConfig();
            defaultConfig.save(configPath);
            return defaultConfig;
        }
        
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(Files.readString(path), GeneratorConfig.class);
    }
    
    public void save(String configPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        String yaml = mapper.writeValueAsString(this);
        Files.writeString(Paths.get(configPath), yaml);
    }
    
    public String getWatchDirectory() { return watchDirectory; }
    public void setWatchDirectory(String watchDirectory) { this.watchDirectory = watchDirectory; }
    
    public String getOutputDirectory() { return outputDirectory; }
    public void setOutputDirectory(String outputDirectory) { this.outputDirectory = outputDirectory; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public boolean isIncremental() { return incremental; }
    public void setIncremental(boolean incremental) { this.incremental = incremental; }
    
    public boolean isAutoWatch() { return autoWatch; }
    public void setAutoWatch(boolean autoWatch) { this.autoWatch = autoWatch; }
}