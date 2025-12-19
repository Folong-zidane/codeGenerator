package com.basiccode.generator.generator.spring;

import com.basiccode.generator.generator.BaseFileWriter;

public class SpringBootFileWriter extends BaseFileWriter {
    @Override
    public String getFileExtension() {
        return ".java";
    }
}