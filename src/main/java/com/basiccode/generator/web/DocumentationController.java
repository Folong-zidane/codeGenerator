package com.basiccode.generator.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DocumentationController {
    
    @GetMapping
    public String home(Model model) {
        model.addAttribute("author", "FOLONG TAFOUKEU ZIDANE");
        model.addAttribute("school", "ENSPY - École Nationale Supérieure Polytechnique de Yaoundé");
        model.addAttribute("title", "UML-to-CRUD Generator API");
        return "index";
    }
    
    @GetMapping("/docs")
    public String documentation(Model model) {
        model.addAttribute("author", "FOLONG TAFOUKEU ZIDANE");
        return "documentation";
    }
    
    @GetMapping("/examples")
    public String examples(Model model) {
        model.addAttribute("author", "FOLONG TAFOUKEU ZIDANE");
        return "examples";
    }
}