package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.config.Framework;
import org.springframework.stereotype.Component;

@Component
public class DjangoLanguageGeneratorFactory implements LanguageGeneratorFactory {
    
    @Override
    public IEntityGenerator createEntityGenerator() {
        return new DjangoEntityGenerator();
    }
    
    @Override
    public IRepositoryGenerator createRepositoryGenerator() {
        return new DjangoRepositoryGenerator();
    }
    
    @Override
    public IServiceGenerator createServiceGenerator() {
        return new DjangoServiceGenerator();
    }
    
    @Override
    public IControllerGenerator createControllerGenerator() {
        return new DjangoControllerGenerator();
    }
    
    @Override
    public IMigrationGenerator createMigrationGenerator() {
        return new DjangoMigrationGenerator();
    }
    
    @Override
    public IFileWriter createFileWriter() {
        return new DjangoFileWriter();
    }
    
    @Override
    public Framework getSupportedFramework() {
        return Framework.DJANGO;
    }
    
    @Override
    public String getLanguage() {
        return "django";
    }
}