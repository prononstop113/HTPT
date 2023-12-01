package com.HTPT.FileServer.controller;

import com.HTPT.FileServer.Model.WebSocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestController {
    @Autowired
    private Environment environment;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @GetMapping("/test")
    public ResponseEntity<String> test(){
        String serverPort = environment.getProperty("local.server.port");
        messagingTemplate.convertAndSend("/topic/file-event", new WebSocketMessage("from file sv " + serverPort).getContent());
        return ResponseEntity.ok("Running on port "+serverPort);
    }
}
