package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.services.TestAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAIController {
    private final TestAIService aiService;
    @Autowired
    public TestAIController(TestAIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String message){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String respond = aiService.chat(message);
        return new ResponseEntity<>(respond, headers, 200);
    }
}
