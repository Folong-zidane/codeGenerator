package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.*;

public class DjangoGeneratorFactory {
    
    public static IEntityGenerator createEntityGenerator() {
        // 🚀 PHASE 1: Utiliser le générateur avancé DjangoModelGenerator
        return new DjangoModelGeneratorAdapter();
    }
    
    public static IRepositoryGenerator createRepositoryGenerator() {
        return new DjangoRepositoryGenerator();
    }
    
    public static IServiceGenerator createServiceGenerator() {
        return new DjangoServiceGenerator();
    }
    
    public static IControllerGenerator createControllerGenerator() {
        return new DjangoControllerGenerator();
    }
    
    public static IFileWriter createFileWriter() {
        return new DjangoFileWriter();
    }
    
    public static IMigrationGenerator createMigrationGenerator() {
        // 🚀 PHASE 1: Utiliser le générateur de migrations amélioré
        return new DjangoMigrationGeneratorEnhanced();
    }
    
    public static IRepositoryGenerator createSerializerGenerator() {
        // 🚀 PHASE 1: Utiliser le générateur de sérializers DRF
        return new DjangoSerializerGenerator();
    }
}