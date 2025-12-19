package com.basiccode.generator.generator;

import com.basiccode.generator.config.Framework;

public interface LanguageGeneratorFactory {
    IEntityGenerator createEntityGenerator();
    IRepositoryGenerator createRepositoryGenerator();
    IServiceGenerator createServiceGenerator();
    IControllerGenerator createControllerGenerator();
    IMigrationGenerator createMigrationGenerator();
    IFileWriter createFileWriter();
    
    Framework getSupportedFramework();
    String getLanguage();
}