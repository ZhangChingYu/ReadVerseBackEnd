package com.elec5620.readverseserver.services;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.azure.AzureOpenAiImageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.stereotype.Service;

@Service
public class TestImageAIService {
    private String AZURE_OPENAI_ENDPOINT = "";
    private String AZURE_OPENAI_DEPLOYMENT_NAME = "";
    private String AZURE_OPENAI_API_KEY = "";

    public void generateImage(){
        AzureOpenAiImageModel model = new AzureOpenAiImageModel.Builder()
                .endpoint(AZURE_OPENAI_ENDPOINT)
                .deploymentName(AZURE_OPENAI_DEPLOYMENT_NAME)
                .apiKey(AZURE_OPENAI_API_KEY)
                .logRequestsAndResponses(true)
                .build();
        Response<Image> response = model.generate("Create an image of a horse race with three horses.");
        System.out.println(response.toString());
        Image image = response.content();
        System.out.println("The generated image is here: " + image.url());
    }
}
