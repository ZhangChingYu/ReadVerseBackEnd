package com.elec5620.readverseserver.controllers;

import com.elec5620.readverseserver.services.TestAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAIController {
    private TestAIService aiService;
    @Autowired
    public TestAIController(TestAIService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat")
    public String chat(@RequestBody String message){
        String respond = aiService.chat(message);
        return respond;
    }
}
