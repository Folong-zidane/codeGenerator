package com.basiccode.generator.generator.django;

import com.basiccode.generator.generator.IEntityGenerator;
import com.basiccode.generator.generator.IServiceGenerator;
import com.basiccode.generator.generator.IControllerGenerator;
import com.basiccode.generator.generator.IFileWriter;

/**
 * Factory pour créer les générateurs Django
 */
public class DjangoGeneratorFactory {
    
    public IEntityGenerator createEntityGenerator() {
        return new DjangoEntityGenerator();
    }
    
    public IServiceGenerator createServiceGenerator() {
        return new DjangoServiceGenerator();
    }
    
    public IControllerGenerator createControllerGenerator() {
        return new DjangoControllerGenerator();
    }
    
    public DjangoSerializerGenerator createSerializerGenerator() {
        return new DjangoSerializerGenerator();
    }
    
    public IFileWriter createFileWriter() {
        return new DjangoFileWriter();
    }
    
    public String getLanguage() {
        return "django";
    }
    
    public String getFramework() {
        return "Django REST Framework";
    }
}