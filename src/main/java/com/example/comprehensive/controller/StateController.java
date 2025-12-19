package com.example.comprehensive.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/api/state")
public class StateController {
    
    @PostMapping("/cancelled")
    public String cancelled() {
        return "State: CANCELLED";
    }
    
    @PostMapping("/delivered")
    public String delivered() {
        return "State: DELIVERED";
    }
    
    @PostMapping("/confirmed")
    public String confirmed() {
        return "State: CONFIRMED";
    }
    
    @PostMapping("/pending")
    public String pending() {
        return "State: PENDING";
    }
    
    @PostMapping("/shipped")
    public String shipped() {
        return "State: SHIPPED";
    }
}
