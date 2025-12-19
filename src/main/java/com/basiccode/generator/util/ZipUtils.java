package com.basiccode.generator.util;

import java.io.*;
import java.nio.file.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 📦 Utilitaires pour la création de fichiers ZIP
 */
public class ZipUtils {
    
    /**
     * Méthode principale utilisée par le contrôleur
     */
    public static byte[] createZip(Map<String, String> files) throws IOException {
        return createZipFromFiles(files);
    }
    
    public static byte[] createZipFromFiles(Map<String, String> files) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String fileName = entry.getKey();
                String content = entry.getValue();
                
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                zos.write(content.getBytes("UTF-8"));
                zos.closeEntry();
            }
        }
        
        return baos.toByteArray();
    }
    
    public static byte[] createZipFromDirectory(Path directory) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            Files.walk(directory)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String relativePath = directory.relativize(file).toString();
                        ZipEntry zipEntry = new ZipEntry(relativePath);
                        zos.putNextEntry(zipEntry);
                        Files.copy(file, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to add file to zip: " + file, e);
                    }
                });
        }
        
        return baos.toByteArray();
    }
}