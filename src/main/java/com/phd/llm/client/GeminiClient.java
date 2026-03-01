package com.phd.llm.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phd.llm.model.gemini.GeminiResponse;
import com.phd.llm.properties.ConfigLoader;

public class GeminiClient {

	private static final String API_KEY = ConfigLoader.getProperty("gemini.password");
	private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="
			+ API_KEY;

	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static String generateContent(String text) throws Exception {
		HttpClient client = HttpClient.newHttpClient();

		// Crear el JSON de la solicitud
		JSONObject textObject = new JSONObject().put("text", text);
		JSONObject partsObject = new JSONObject().put("parts", new org.json.JSONArray().put(textObject));
		JSONObject contentsObject = new JSONObject().put("contents", new org.json.JSONArray().put(partsObject));

		// Construir la solicitud HTTP
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(contentsObject.toString())).build();

		// Enviar la solicitud y obtener la respuesta
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		// Verificar el código de estado
		if (response.statusCode() == 200) {
			GeminiResponse geminiResponse = objectMapper.readValue(response.body(), GeminiResponse.class);
//        	geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
//            return geminiResponse.getCandidates().get(0).getContent().getgetParts().get(0).getText();
			return geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();

		} else {
			throw new IOException("Error: " + response.statusCode() + " - " + response.body());
		}
	}
}
