package com.basiccode.generator.model.metadata;

public class UMLMetadata {
    
    public static UMLMetadataBuilder builder() {
        return new UMLMetadataBuilder();
    }
    
    public static class UMLMetadataBuilder {
        private ProjectMetadata project;
        private TechStackMetadata techStack;
        private ArchitectureMetadata architecture;
        
        public UMLMetadataBuilder project(ProjectMetadata project) {
            this.project = project;
            return this;
        }
        
        public UMLMetadataBuilder techStack(TechStackMetadata techStack) {
            this.techStack = techStack;
            return this;
        }
        
        public UMLMetadataBuilder architecture(ArchitectureMetadata architecture) {
            this.architecture = architecture;
            return this;
        }
        
        public UMLMetadata build() {
            return new UMLMetadata(project, techStack, architecture);
        }
    }
    private ProjectMetadata project;
    private TechStackMetadata techStack;
    private ArchitectureMetadata architecture;
    private ClassDiagramMetadata classDiagram;
    
    public UMLMetadata() {}
    
    public UMLMetadata(ProjectMetadata project, TechStackMetadata techStack, ArchitectureMetadata architecture) {
        this.project = project;
        this.techStack = techStack;
        this.architecture = architecture;
        this.classDiagram = ClassDiagramMetadata.getDefault();
    }
    
    public static UMLMetadata getDefault() {
        UMLMetadata metadata = new UMLMetadata(
            ProjectMetadata.getDefault(),
            TechStackMetadata.getDefault(),
            ArchitectureMetadata.getDefault()
        );
        metadata.classDiagram = ClassDiagramMetadata.getDefault();
        return metadata;
    }
    
    public ProjectMetadata getProject() {
        return project;
    }
    
    public void setProject(ProjectMetadata project) {
        this.project = project;
    }
    
    public TechStackMetadata getTechStack() {
        return techStack;
    }
    
    public void setTechStack(TechStackMetadata techStack) {
        this.techStack = techStack;
    }
    
    public ArchitectureMetadata getArchitecture() {
        return architecture;
    }
    
    public void setArchitecture(ArchitectureMetadata architecture) {
        this.architecture = architecture;
    }
    
    public Object getClassMetadata() {
        return classDiagram;
    }
    
    public ClassDiagramMetadata getClassDiagram() {
        return classDiagram;
    }
    
    public void setClassDiagram(ClassDiagramMetadata classDiagram) {
        this.classDiagram = classDiagram;
    }
}
