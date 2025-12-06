package com.basiccode.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration pour les projets générés
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectConfig {
    private String projectName;
    private String projectType; // library, cli, api, fullstack
    private String packageManager; // npm, yarn, pnpm, maven, gradle
    private boolean useDocker;
    private boolean useCI;
    private String targetNodeVersion = "18.0.0";
    private String targetEsVersion = "ES2020";
    private String language; // java, python, php, typescript, csharp
    private String packageName;
    private String createdDate;
}
