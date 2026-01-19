package com.myProject.demo.service;

import org.springframework.stereotype.Service;

@Service
public class PingService {
    public String ping() {
        return "Status: ok";
    }

    public String health() {
        return "UP";
    }
}
