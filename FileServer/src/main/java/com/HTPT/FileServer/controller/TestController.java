package com.HTPT.FileServer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {
    @Autowired
    private Environment environment;
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        String serverPort = environment.getProperty("local.server.port");

        return ResponseEntity.ok("Running on port "+serverPort);
    }
}
