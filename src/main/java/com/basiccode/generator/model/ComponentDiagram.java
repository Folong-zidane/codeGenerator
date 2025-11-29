package com.basiccode.generator.model;

import java.util.*;

/**
 * Model classes for component diagrams
 */
public class ComponentDiagram {
    private List<Component> components = new ArrayList<>();
    private List<ComponentDependency> dependencies = new ArrayList<>();
    private List<ComponentNote> notes = new ArrayList<>();
    
    public List<Component> getComponents() { return components; }
    public void addComponent(Component component) { this.components.add(component); }
    
    public List<ComponentDependency> getDependencies() { return dependencies; }
    public void addDependency(ComponentDependency dependency) { this.dependencies.add(dependency); }
    
    public List<ComponentNote> getNotes() { return notes; }
    public void addNote(ComponentNote note) { this.notes.add(note); }
}

/**
 * Represents a component in the diagram
 */
class Component {
    private String name;
    private String alias;
    private ComponentType type;
    private List<String> interfaces = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    
    public ComponentType getType() { return type; }
    public void setType(ComponentType type) { this.type = type; }
    
    public List<String> getInterfaces() { return interfaces; }
    public void addInterface(String interfaceName) { this.interfaces.add(interfaceName); }
}

enum ComponentType {
    COMPONENT, INTERFACE, PACKAGE, SERVICE
}

/**
 * Represents a dependency between components
 */
class ComponentDependency {
    private String source;
    private String target;
    private DependencyType type;
    private String label;
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public DependencyType getType() { return type; }
    public void setType(DependencyType type) { this.type = type; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}

enum DependencyType {
    USES, PROVIDES, BIDIRECTIONAL, IMPLEMENTS, ASSOCIATION
}

/**
 * Represents a note in the component diagram
 */
class ComponentNote {
    private String position;
    private String componentName;
    private String text;
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    public String getComponentName() { return componentName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

/**
 * Project structure generated from component diagram
 */
class ProjectStructure {
    private String basePackage;
    private List<Module> modules = new ArrayList<>();
    private List<ModuleDependency> dependencies = new ArrayList<>();
    
    public String getBasePackage() { return basePackage; }
    public void setBasePackage(String basePackage) { this.basePackage = basePackage; }
    
    public List<Module> getModules() { return modules; }
    public void addModule(Module module) { this.modules.add(module); }
    
    public List<ModuleDependency> getDependencies() { return dependencies; }
    public void addDependency(ModuleDependency dependency) { this.dependencies.add(dependency); }
}

/**
 * Represents a module in the project structure
 */
class Module {
    private String name;
    private String packageName;
    private ModuleType type;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public ModuleType getType() { return type; }
    public void setType(ModuleType type) { this.type = type; }
}

enum ModuleType {
    BUSINESS, PACKAGE, SERVICE, UTILITY
}

/**
 * Represents a dependency between modules
 */
class ModuleDependency {
    private String source;
    private String target;
    private DependencyType type;
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    
    public DependencyType getType() { return type; }
    public void setType(DependencyType type) { this.type = type; }
}

/**
 * Build configuration generated from component diagram
 */
class BuildConfiguration {
    private String projectName;
    private boolean multiModule = false;
    private List<String> dependencies = new ArrayList<>();
    private List<ModuleConfig> modules = new ArrayList<>();
    
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    
    public boolean isMultiModule() { return multiModule; }
    public void setMultiModule(boolean multiModule) { this.multiModule = multiModule; }
    
    public List<String> getDependencies() { return dependencies; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    
    public List<ModuleConfig> getModules() { return modules; }
    public void addModule(ModuleConfig module) { this.modules.add(module); }
}

/**
 * Configuration for a module in multi-module project
 */
class ModuleConfig {
    private String name;
    private String artifactId;
    private List<String> dependencies = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getArtifactId() { return artifactId; }
    public void setArtifactId(String artifactId) { this.artifactId = artifactId; }
    
    public List<String> getDependencies() { return dependencies; }
    public void addDependency(String dependency) { this.dependencies.add(dependency); }
}

/**
 * Spring configuration class generated from components
 */
class ConfigurationClass {
    private String name;
    private String packageName;
    private List<String> annotations = new ArrayList<>();
    private List<String> methods = new ArrayList<>();
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public List<String> getAnnotations() { return annotations; }
    public void addAnnotation(String annotation) { this.annotations.add(annotation); }
    
    public List<String> getMethods() { return methods; }
    public void addMethod(String method) { this.methods.add(method); }
}