package com.basiccode.generator.generator;

import com.basiccode.generator.model.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateBasedGenerator {
    
    private final Configuration freemarkerConfig;
    
    public TemplateBasedGenerator() {
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
    }
    
    public String generateFromTemplate(String templateName, Map<String, Object> model) throws Exception {
        Template template = freemarkerConfig.getTemplate(templateName);
        StringWriter writer = new StringWriter();
        template.process(model, writer);
        return writer.toString();
    }
    
    public String generatePythonEntity(EnhancedClass enhancedClass, String packageName) throws Exception {
        Map<String, Object> model = new HashMap<>();
        UmlClass umlClass = enhancedClass.getOriginalClass();
        
        model.put("className", umlClass.getName());
        model.put("tableName", umlClass.getName().toLowerCase() + "s");
        model.put("attributes", umlClass.getAttributes());
        model.put("hasStateEnum", enhancedClass.isStateful());
        
        if (enhancedClass.isStateful()) {
            model.put("stateEnum", enhancedClass.getStateEnum());
            model.put("hasTransitionMethods", !enhancedClass.getTransitionMethods().isEmpty());
            model.put("transitionMethods", enhancedClass.getTransitionMethods());
        }
        
        return generateFromTemplate("python_entity.ftl", model);
    }
    
    public String generateCSharpEntity(EnhancedClass enhancedClass, String packageName) throws Exception {
        Map<String, Object> model = new HashMap<>();
        UmlClass umlClass = enhancedClass.getOriginalClass();
        
        model.put("packageName", packageName);
        model.put("className", umlClass.getName());
        model.put("tableName", umlClass.getName().toLowerCase() + "s");
        model.put("attributes", umlClass.getAttributes());
        model.put("hasStateEnum", enhancedClass.isStateful());
        
        if (enhancedClass.isStateful()) {
            model.put("stateEnum", enhancedClass.getStateEnum());
            model.put("hasTransitionMethods", !enhancedClass.getTransitionMethods().isEmpty());
            model.put("transitionMethods", enhancedClass.getTransitionMethods());
        }
        
        return generateFromTemplate("csharp_entity.ftl", model);
    }
}