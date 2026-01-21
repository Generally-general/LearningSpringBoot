package com.myProject.demo.service;

import org.springframework.stereotype.Service;

@Service
public class PingService {
    public String ping(boolean fail) {
        if(fail) {
            throw new RuntimeException("Forced failure");
        }
        return "Status: ok";
    }

    public String health() {
        return "UP";
    }
}
