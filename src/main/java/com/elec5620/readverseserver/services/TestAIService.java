package com.elec5620.readverseserver.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestAIService {
    @Value("${openai_api_key}")
    private static String OPEN_AI_KEY;
}
