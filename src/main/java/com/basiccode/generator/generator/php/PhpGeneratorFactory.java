package com.basiccode.generator.generator.php;

import com.basiccode.generator.generator.*;
import com.basiccode.generator.config.Framework;
import org.springframework.stereotype.Component;

@Component
public class PhpGeneratorFactory implements LanguageGeneratorFactory {
    
    @Override
    public String getLanguage() {
        return "php";
    }
    
    @Override
    public Framework getSupportedFramework() {
        return Framework.PHP_LARAVEL;
    }
    
    @Override
    public IEntityGenerator createEntityGenerator() {
        return new PhpEntityGenerator();
    }
    
    @Override
    public IRepositoryGenerator createRepositoryGenerator() {
        return new PhpRepositoryGenerator();
    }
    
    @Override
    public IServiceGenerator createServiceGenerator() {
        return new PhpServiceGenerator();
    }
    
    @Override
    public IControllerGenerator createControllerGenerator() {
        return new PhpControllerGenerator();
    }
    
    @Override
    public IMigrationGenerator createMigrationGenerator() {
        return new PhpMigrationGenerator();
    }
    
    @Override
    public IFileWriter createFileWriter() {
        return new PhpFileWriter();
    }
}