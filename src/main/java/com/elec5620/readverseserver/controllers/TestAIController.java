package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.services.TestAIService;
import com.elec5620.readverseserver.services.TestImageAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAIController {
    private final TestAIService aiService;
    private final TestImageAIService imageAIService;
    @Autowired
    public TestAIController(TestAIService aiService, TestImageAIService imageAIService) {
        this.aiService = aiService;
        this.imageAIService = imageAIService;
    }

    @GetMapping("/ai/chat")
    public ResponseEntity<String> chat(@RequestBody String message){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String respond = aiService.chat(message);
        return new ResponseEntity<>(respond, headers, 200);
    }

    @GetMapping("/ai/image")
    public ResponseEntity<String> generateImage(){
        HttpHeaders headers = new HttpHeaders();
        imageAIService.generateImage();
        return new ResponseEntity<>("ok", headers, 200);
    }
}
