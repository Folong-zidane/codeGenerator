package com.basiccode.generator.generator.python;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.config.Framework;
import org.springframework.stereotype.Component;

@Component
public class PythonGeneratorFactory implements LanguageGeneratorFactory {
    
    @Override
    public String getLanguage() {
        return "python";
    }
    
    @Override
    public Framework getSupportedFramework() {
        return Framework.FASTAPI;
    }
    
    @Override
    public IEntityGenerator createEntityGenerator() {
        return new PythonEntityGenerator();
    }
    
    @Override
    public IRepositoryGenerator createRepositoryGenerator() {
        return new PythonRepositoryGenerator();
    }
    
    @Override
    public IServiceGenerator createServiceGenerator() {
        return new PythonServiceGenerator();
    }
    
    @Override
    public IControllerGenerator createControllerGenerator() {
        return new PythonControllerGenerator();
    }
    
    @Override
    public IMigrationGenerator createMigrationGenerator() {
        return new PythonMigrationGenerator();
    }
    
    @Override
    public IFileWriter createFileWriter() {
        return new PythonFileWriter();
    }
}