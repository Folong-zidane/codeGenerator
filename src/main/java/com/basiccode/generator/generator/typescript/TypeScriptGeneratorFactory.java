package com.basiccode.generator.generator.typescript;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.config.Framework;
import org.springframework.stereotype.Component;

@Component
public class TypeScriptGeneratorFactory implements LanguageGeneratorFactory {
    
    @Override
    public String getLanguage() {
        return "typescript";
    }
    
    @Override
    public Framework getSupportedFramework() {
        return Framework.NODEJS_TYPESCRIPT;
    }
    
    @Override
    public IEntityGenerator createEntityGenerator() {
        return new TypeScriptEntityGenerator();
    }
    
    @Override
    public IRepositoryGenerator createRepositoryGenerator() {
        return new TypeScriptRepositoryGenerator();
    }
    
    @Override
    public IServiceGenerator createServiceGenerator() {
        return new TypeScriptServiceGenerator();
    }
    
    @Override
    public IControllerGenerator createControllerGenerator() {
        return new TypeScriptControllerGenerator();
    }
    
    @Override
    public IMigrationGenerator createMigrationGenerator() {
        return new TypeScriptMigrationGenerator();
    }
    
    @Override
    public IFileWriter createFileWriter() {
        return new TypeScriptFileWriter();
    }
}