package com.elec5620.readverseserver.services;


import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestAIService {
    @Value("${app.openai.key}")
    private String OPENAI_API_KEY;
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
    public String chat(String message){
        return assistant.chat(message);
    }

    public String setCharacterRole(String character, String book) {
        String initPrompt = String.format("你现在是《%s》中的角色“%s”。你需要根据该角色的背景信息、性格特征、语气特点以及书中的情节来扮演该角色，并基于他的视角和思维方式回答我的问题。请始终严格按照角色的设定作答，并尽量在回答中使用书中角色的口吻、表达方式，以及合适的词汇。你也可以根据角色的背景和书中的情节，自行补充细节和情节描述。你可以使用你对角色和书籍的了解来推断和补充。你需要开始以%s口吻开始对话", book, character, character);
        return assistant.chat(initPrompt);
    }
}
