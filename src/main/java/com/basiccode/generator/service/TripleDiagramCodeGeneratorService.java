package com.basiccode.generator.service;

import com.basiccode.generator.model.*;
import com.basiccode.generator.parser.ParseException;
import com.basiccode.generator.exception.GenerationException;
import com.basiccode.generator.config.FrameworkRegistry;
import com.basiccode.generator.generator.LanguageGeneratorFactory;
import com.basiccode.generator.service.*;
import com.basiccode.generator.parser.*;
import com.basiccode.generator.util.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

/**
 * Refactored service using SOLID principles and dependency injection
 * Acts as a lightweight facade orchestrating injected services
 */
@Service
public class TripleDiagramCodeGeneratorService {
    
    private final SimpleClassParser simpleParser;
    private final BehaviorExtractor behaviorExtractor;
    private final StateEnhancer stateEnhancer;
    private final CodeGenerationOrchestrator orchestrator;
    private final FrameworkRegistry frameworkRegistry;
    
    @Autowired
    public TripleDiagramCodeGeneratorService(SimpleClassParser simpleParser,
                                             BehaviorExtractor behaviorExtractor,
                                             StateEnhancer stateEnhancer,
                                             CodeGenerationOrchestrator orchestrator,
                                             FrameworkRegistry frameworkRegistry) {
        this.simpleParser = simpleParser;
        this.behaviorExtractor = behaviorExtractor;
        this.stateEnhancer = stateEnhancer;
        this.orchestrator = orchestrator;
        this.frameworkRegistry = frameworkRegistry;
    }
    
    /**
     * Generate comprehensive code using all three diagram types
     */
    public ComprehensiveCodeResult generateComprehensiveCode(String classDiagram, 
                                                           String sequenceDiagram, 
                                                           String stateDiagram,
                                                           String packageName, 
                                                           String language) throws GenerationException {
        
        // Parse all diagrams using facade
        Diagram classModel = null;
        SequenceDiagram sequenceModel = null;
        StateMachine stateModel = null;
        
        try {
            classModel = simpleParser.parseClassDiagram(classDiagram);
            sequenceModel = simpleParser.parseSequenceDiagram(sequenceDiagram);
            stateModel = simpleParser.parseStateDiagram(stateDiagram);
        } catch (Exception e) {
            throw new GenerationException("Failed to parse diagrams: " + e.getMessage(), e);
        }
        
        // Validate parsed models
        if (classModel == null || classModel.getClasses().isEmpty()) {
            throw new GenerationException("No classes found in class diagram");
        }
        
        // Build comprehensive model using builder
        ComprehensiveDiagram comprehensiveModel = new CombinedModelBuilder()
            .withClassDiagram(classModel)
            .withSequenceDiagram(sequenceModel)
            .withStateMachine(stateModel)
            .withBehaviorExtractor(behaviorExtractor)
            .withStateEnhancer(stateEnhancer)
            .build();
        
        // Get appropriate factory and generate code
        try {
            LanguageGeneratorFactory factory = frameworkRegistry.factoryFor(language);
            return orchestrator.generateProject(comprehensiveModel, packageName, factory);
        } catch (IllegalArgumentException e) {
            throw new GenerationException("Unsupported language: " + language, e);
        } catch (Exception e) {
            throw new GenerationException("Code generation failed: " + e.getMessage(), e);
        }
    }
}