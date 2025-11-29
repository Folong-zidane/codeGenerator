package com.basiccode.generator.generator.csharp;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.config.Framework;
import org.springframework.stereotype.Component;

/**
 * C# .NET Core generator factory
 * Implements Abstract Factory pattern for C# code generation
 */
@Component
public class CSharpGeneratorFactory implements LanguageGeneratorFactory {
    
    @Override
    public String getLanguage() {
        return "csharp";
    }
    
    @Override
    public Framework getSupportedFramework() {
        return Framework.DOTNET_CORE;
    }
    
    @Override
    public IEntityGenerator createEntityGenerator() {
        return new CSharpEntityGenerator();
    }
    
    @Override
    public IRepositoryGenerator createRepositoryGenerator() {
        return new CSharpRepositoryGenerator();
    }
    
    @Override
    public IServiceGenerator createServiceGenerator() {
        return new CSharpServiceGenerator();
    }
    
    @Override
    public IControllerGenerator createControllerGenerator() {
        return new CSharpControllerGenerator();
    }
    
    @Override
    public IMigrationGenerator createMigrationGenerator() {
        return new CSharpMigrationGenerator();
    }
    
    @Override
    public IFileWriter createFileWriter() {
        return new CSharpFileWriter();
    }
}