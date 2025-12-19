package com.basiccode.generator.model.metadata;

public class ProjectMetadata {
    
    public static ProjectMetadataBuilder builder() {
        return new ProjectMetadataBuilder();
    }
    
    public static class ProjectMetadataBuilder {
        private String name;
        private String version;
        private String description;
        private String author;
        
        public ProjectMetadataBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public ProjectMetadataBuilder version(String version) {
            this.version = version;
            return this;
        }
        
        public ProjectMetadataBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        public ProjectMetadataBuilder author(String author) {
            this.author = author;
            return this;
        }
        
        public ProjectMetadata build() {
            return new ProjectMetadata(name, version, description, author);
        }
    }
    private String name;
    private String version;
    private String description;
    private String author;
    
    public ProjectMetadata() {}
    
    public ProjectMetadata(String name, String version, String description, String author) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.author = author;
    }
    
    public static ProjectMetadata getDefault() {
        return new ProjectMetadata(
            "generated-project",
            "1.0.0",
            "Generated from UML diagram",
            "UML Generator"
        );
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
}
