package com.myProject.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewClass {
    @GetMapping("ping")
    public String sayPing() {
        return "ok";
    }
}
