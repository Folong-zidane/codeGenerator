#!/usr/bin/env python3

import requests
import json
import zipfile
import os
import sys
from pathlib import Path

class CodeGeneratorClient:
    def __init__(self):
        self.api_url = "https://codegenerator-cpyh.onrender.com"
    
    def validate_uml(self, uml_content):
        """Valider le contenu UML"""
        response = requests.post(
            f"{self.api_url}/api/generate/validate",
            headers={"Content-Type": "application/json"},
            data=uml_content
        )
        return response.json().get("valid", False)
    
    def generate_project(self, uml_file, package_name, output_dir):
        """Générer un projet complet"""
        # Lire le fichier UML
        with open(uml_file, 'r') as f:
            uml_content = f.read()
        
        # Valider d'abord
        if not self.validate_uml(uml_content):
            raise Exception("UML invalide")
        
        # Préparer la requête
        payload = {
            "umlContent": uml_content,
            "packageName": package_name
        }
        
        # Appeler l'API
        response = requests.post(
            f"{self.api_url}/api/generate/crud",
            headers={"Content-Type": "application/json"},
            json=payload
        )
        
        if response.status_code != 200:
            raise Exception(f"Erreur API: {response.status_code}")
        
        # Sauvegarder et extraire le ZIP
        zip_path = "generated-code.zip"
        with open(zip_path, 'wb') as f:
            f.write(response.content)
        
        # Extraire
        with zipfile.ZipFile(zip_path, 'r') as zip_ref:
            zip_ref.extractall(output_dir)
        
        # Créer la structure Spring Boot complète
        self._setup_spring_boot_project(output_dir, package_name)
        
        # Nettoyer
        os.remove(zip_path)
        
        print(f"✅ Projet généré dans: {output_dir}")
    
    def _setup_spring_boot_project(self, output_dir, package_name):
        """Créer la structure Spring Boot complète"""
        # Créer pom.xml
        pom_content = f"""<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.2</version>
        <relativePath/>
    </parent>
    
    <groupId>{package_name}</groupId>
    <artifactId>generated-app</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>"""
        
        with open(f"{output_dir}/pom.xml", 'w') as f:
            f.write(pom_content)
        
        # Créer Application.java
        package_path = package_name.replace('.', '/')
        java_dir = Path(output_dir) / "src/main/java" / package_path
        java_dir.mkdir(parents=True, exist_ok=True)
        
        app_content = f"""package {package_name};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeneratedApplication {{
    public static void main(String[] args) {{
        SpringApplication.run(GeneratedApplication.class, args);
    }}
}}"""
        
        with open(java_dir / "GeneratedApplication.java", 'w') as f:
            f.write(app_content)
        
        # Créer application.yml
        resources_dir = Path(output_dir) / "src/main/resources"
        resources_dir.mkdir(parents=True, exist_ok=True)
        
        yml_content = """server:
  port: 8080

spring:
  application:
    name: generated-app
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html"""
        
        with open(resources_dir / "application.yml", 'w') as f:
            f.write(yml_content)

def main():
    if len(sys.argv) != 4:
        print("Usage: python3 code_generator_client.py <uml_file> <package_name> <output_dir>")
        sys.exit(1)
    
    uml_file, package_name, output_dir = sys.argv[1:4]
    
    try:
        client = CodeGeneratorClient()
        client.generate_project(uml_file, package_name, output_dir)
    except Exception as e:
        print(f"❌ Erreur: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()