package com.basiccode.generator.generator;

import com.basiccode.generator.generator.spring.JavaFileWriter;
import com.basiccode.generator.generator.python.PythonFileWriter;
import com.basiccode.generator.generator.django.DjangoFileWriter;
import com.basiccode.generator.generator.typescript.TypeScriptFileWriter;

public class FileWriterFactory {
    
    public static IFileWriter createFileWriter(String language) {
        if (language == null) {
            return new JavaFileWriter();
        }
        
        switch (language.toLowerCase()) {
            case "java":
            case "spring":
            case "spring-boot":
                return new JavaFileWriter();
            case "python":
                return new PythonFileWriter();
            case "django":
                return new DjangoFileWriter();
            case "typescript":
            case "nestjs":
                return new TypeScriptFileWriter();
            default:
                return new JavaFileWriter();
        }
    }
}