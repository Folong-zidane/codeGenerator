package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.*;

public class DjangoGeneratorFactory {
    
    public static IEntityGenerator createEntityGenerator() {
        return new DjangoEntityGenerator();
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
        return new DjangoMigrationGenerator();
    }
}