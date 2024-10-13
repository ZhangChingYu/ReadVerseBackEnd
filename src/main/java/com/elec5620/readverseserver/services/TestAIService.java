package com.elec5620.readverseserver.services;


import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
//import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class TestAIService {
    private static String OPENAI_API_KEY = "sk-proj-xjjRA6AssOvFlDsxcYFYT3BlbkFJwCLVduCXdRF9UzrinfrS";
    Assistant assistant;

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
    }
    /**
    @PostConstruct
    void init(){
        ChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
        assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(HuggingFaceChatModel.withAccessToken("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"))
                .chatMemory(memory)
                .build();
    }
    */
    public String chat(String message){
        return assistant.chat(message);
    }
}
