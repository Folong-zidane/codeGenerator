package com.basiccode.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.basiccode.generator.controller",
    "com.basiccode.generator.web",
    "com.basiccode.generator.service",
    "com.basiccode.generator.generator",
    "com.basiccode.generator.config",
    "com.basiccode.generator.dto",
    "com.basiccode.generator.parser",
    "com.basiccode.generator.initializer",
    "com.basiccode.generator.merger",
    "com.basiccode.generator.strategy",
    "com.basiccode.generator.reactive"
})
public class CodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeGeneratorApplication.class, args);
    }
}
