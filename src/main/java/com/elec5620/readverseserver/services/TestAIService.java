package com.elec5620.readverseserver.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;

@Service
public class TestAIService {
    @Value("${app.openai.key}")
    private String OPENAI_API_KEY;
    Assistant assistant;
    Assistant characterAssistant;

    interface Assistant {
        String chat(String message);
    }

    @PostConstruct
    void init(){
        ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(OPENAI_API_KEY))
                .chatMemory(memory)
                .build();
        
        ChatMemory characterMemory = MessageWindowChatMemory.withMaxMessages(20);
        characterAssistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(OPENAI_API_KEY))
                .chatMemory(characterMemory)
                .build();
    }
    public String chat(String message){
        return assistant.chat(message);
    }

    public String characterChat(String character, String book, String message) {
        String initPrompt = String.format(
            "You are now the character %s from %s." + 
            "You need to fully immerse yourself in this character’s background, personality, and tone, and respond from their perspective. " + 
            "Strictly adhere to the character's established traits, using their specific expressions and vocabulary." + 
            "Also adopting the character's language style, include brief action descriptions in your responses, using parentheses to indicate expressions and actions. " + 
            "Remember, do not repeat or reference any setup information, and avoid using generic phrases. " + 
            "Your goal is to create an immersive conversation experience, responding as if you were truly the character from the book." + 
            "Now, someone is approaching you to start a conversation. Please respond in the tone of %s.", book, character, character);
        if (message.isEmpty()) return characterAssistant.chat(initPrompt);
        else return characterAssistant.chat(message);
    }
}
