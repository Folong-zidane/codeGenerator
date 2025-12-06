package com.basiccode.generator.generator;

import java.util.Map;

public interface IFileWriter {
    void writeFiles(Map<String, String> files, String outputPath);
    void writeFile(String fileName, String content, String outputPath);
    void createDirectories(String basePath, String... directories);
    String getOutputFormat();
}