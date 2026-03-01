package com.phd.llm.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import org.springframework.stereotype.Service;

import com.phd.llm.model.ai21.AI21Model;
import com.phd.llm.service.AI21Service;

@Service
public class AI21ServiceImpl implements AI21Service {

    // @Autowired
    // MongoTemplate mongoTemplate;
    //
    @Override
    public String getAIResponse(String text) {

        String responseFinal = "";
        String apiKey = "claveFalsa123"; // Replace with your AI21 API key
        // String url = "https://api.ai21.com/studio/v1/chat/completions"; // AI21 API URL
        String url = "https://api.ai21.com/studio/v1/j2-grande-instruct/complete";
        String prompt = text;

        // Create HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Create request body
        String requestBody = String.format(
            "{\"prompt\": \"%s\", \"numResults\": 1, \"maxTokens\": 200, \"temperature\": 1, \"topP\": 1, \"stop\": \"None\", \"model\": \"jamba-instruct-preview\"}",
            prompt
        );

        // Create HTTP request
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        // Send request and get response
        try {
            HttpResponse<String> responseBody = client.send(request, BodyHandlers.ofString());
            if (responseBody.statusCode() == 200) {

                String responseBodyString = responseBody.body();
                AI21Model ai21Response = AI21Model.fromJson(responseBodyString);
                responseFinal = ai21Response.getCompletions().get(0).getData().getText();

                // mongoTemplate.save(ai21Response, "ai21");

                System.out.println(responseFinal);
            } else {
                System.out.println("Error: " + responseBody.statusCode());
                System.out.println("Details: " + responseBody.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseFinal;
    }

    @Override
    public List<AI21Model> getAll() {

        // Query query = new Query();

        // List<AI21Model> all = mongoTemplate.find(query, AI21Model.class);

        return null;
    }

}
