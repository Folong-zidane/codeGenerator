package com.basiccode.generator.model;

import java.util.*;

public class ClassModel {
    // Identification
    private String name;
    private String packageName;
    private String stereotype;
    
    // Flags de type de classe
    private boolean isAbstract;
    private boolean isInterface;
    private boolean isEnumeration;
    private boolean isService;
    private boolean isRepository;
    private boolean isController;
    private boolean isEntity;
    
    // Héritage et Implémentation
    private String parentClass;
    private List<String> interfaces = new ArrayList<>();
    
    // Membres
    private List<Field> fields = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private List<String> enumValues = new ArrayList<>();
    
    // Annotations et Métadonnées
    private List<Annotation> annotations = new ArrayList<>();
    private Map<String, String> metadata = new HashMap<>();
    
    // Constructeur par défaut
    public ClassModel() {}
    
    // Constructeur avec nom
    public ClassModel(String name) {
        this.name = name;
    }
    
    // ========== Getters & Setters ==========
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getStereotype() { return stereotype; }
    public void setStereotype(String stereotype) { this.stereotype = stereotype; }
    
    public boolean isAbstract() { return isAbstract; }
    public void setAbstract(boolean isAbstract) { this.isAbstract = isAbstract; }
    
    public boolean isInterface() { return isInterface; }
    public void setInterface(boolean isInterface) { this.isInterface = isInterface; }
    
    public boolean isEnumeration() { return isEnumeration; }
    public void setEnumeration(boolean enumeration) { isEnumeration = enumeration; }
    
    public boolean isService() { return isService; }
    public void setService(boolean service) { isService = service; }
    
    public boolean isRepository() { return isRepository; }
    public void setRepository(boolean repository) { isRepository = repository; }
    
    public boolean isController() { return isController; }
    public void setController(boolean controller) { isController = controller; }
    
    public boolean isEntity() { return isEntity; }
    public void setEntity(boolean entity) { isEntity = entity; }
    
    public String getParentClass() { return parentClass; }
    public void setParentClass(String parentClass) { this.parentClass = parentClass; }
    
    public List<String> getInterfaces() { return interfaces; }
    public void setInterfaces(List<String> interfaces) { this.interfaces = interfaces; }
    public void addInterface(String interfaceName) { this.interfaces.add(interfaceName); }
    
    public List<Field> getFields() { return fields; }
    public void setFields(List<Field> fields) { this.fields = fields; }
    public void addField(Field field) { this.fields.add(field); }
    
    public List<Method> getMethods() { return methods; }
    public void setMethods(List<Method> methods) { this.methods = methods; }
    public void addMethod(Method method) { this.methods.add(method); }
    
    public List<String> getEnumValues() { return enumValues; }
    public void setEnumValues(List<String> enumValues) { this.enumValues = enumValues; }
    public void addEnumValue(String value) { this.enumValues.add(value); }
    
    public List<Annotation> getAnnotations() { return annotations; }
    public void setAnnotations(List<Annotation> annotations) { this.annotations = annotations; }
    
    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }
    
    @Override
    public String toString() {
        return String.format("ClassModel{name='%s', stereotype='%s', abstract=%s, fields=%d, methods=%d}",
            name, stereotype, isAbstract, fields.size(), methods.size());
    }
}