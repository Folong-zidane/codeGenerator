package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.BaseFileWriter;

/**
 * FileWriter pour les fichiers Java
 */
public class JavaFileWriter extends BaseFileWriter {
    
    @Override
    public String getFileExtension() {
        return ".java";
    }
}