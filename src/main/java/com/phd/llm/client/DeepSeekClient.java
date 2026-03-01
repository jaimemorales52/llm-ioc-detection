package com.phd.llm.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phd.llm.model.deepseek.DeepSeekResponse;

public class DeepSeekClient {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final String API_KEY = "YOURPASSHERE";
    private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String generateContent(String userMessage) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Crear JSON para la solicitud
        JSONObject messageObject = new JSONObject().put("role", "user").put("content", userMessage);
        JSONObject requestBody = new JSONObject()
                .put("model", "deepseek-chat") // Ajusta según el modelo
                .put("messages", new JSONArray().put(messageObject));

        // Construir la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        // Enviar la solicitud y recibir la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar si la respuesta es exitosa
        if (response.statusCode() == 200) {
            // Parsear la respuesta JSON
            DeepSeekResponse deepSeekResponse = objectMapper.readValue(response.body(), DeepSeekResponse.class);
            return deepSeekResponse.getChoices().get(0).getMessage().getContent();
        } else {
            throw new IOException("Error en la API: " + response.statusCode() + " - " + response.body());
        }
    }

}
