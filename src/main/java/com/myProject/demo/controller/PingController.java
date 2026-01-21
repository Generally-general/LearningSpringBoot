package com.myProject.demo.controller;

import com.myProject.demo.service.PingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    private final PingService pingService;

    public PingController(PingService pingService) {
        this.pingService = pingService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping(
            @RequestParam(defaultValue = "false") boolean fail
    ) {
        if(fail) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something Went wrong");
        }
        return ResponseEntity.ok(pingService.ping(false));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(pingService.health());
    }
}
