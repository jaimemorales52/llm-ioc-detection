package com.phd.llm.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phd.llm.properties.ConfigLoader;

public class AnthropicClient {

	// Updated API endpoint
	private static final String API_URL = "https://api.anthropic.com/v1/messages";
	private static final String API_KEY = ConfigLoader.getProperty("anthropic.password");

	public String sendRequest(String prompt) throws Exception {
		if (API_KEY == null || API_KEY.isEmpty()) {
			throw new IllegalStateException("API key not found. Please set the CLAUDE_API_KEY environment variable.");
		}

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost post = new HttpPost(API_URL);

			// Updated headers
			post.setHeader("Content-Type", "application/json; charset=UTF-8");
			post.setHeader("x-api-key", API_KEY);
			post.setHeader("anthropic-version", "2023-06-01");

			// Create request body with updated structure
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("model", "claude-3-5-haiku-20241022");
			requestBody.put("max_tokens", 300);
			requestBody.put("messages", new Object[] { Map.of("role", "user", "content", escapeSpecialCharacters(sanitizePrompt(prompt))) });

			ObjectMapper mapper = new ObjectMapper();
			String jsonBody = mapper.writeValueAsString(requestBody);
			post.setEntity(new StringEntity(jsonBody));

			// Execute request
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				String responseBody = EntityUtils.toString(response.getEntity());
				throw new RuntimeException(
						"API request failed with status code: " + statusCode + "\nResponse: " + responseBody);
			}

			// Parse response
			String responseBody = EntityUtils.toString(response.getEntity());
			JsonNode jsonResponse = mapper.readTree(responseBody);

			if (!jsonResponse.has("content")) {
				throw new RuntimeException("Unexpected API response format: " + responseBody);
			}

			return jsonResponse.get("content").get(0).get("text").asText();
		} catch (Exception e) {
			throw new Exception("Error connecting to Claude API: " + e.getMessage(), e);
		}
	}
	
	public String sendRequestv3(String prompt) throws Exception {
		if (API_KEY == null || API_KEY.isEmpty()) {
			throw new IllegalStateException("API key not found. Please set the CLAUDE_API_KEY environment variable.");
		}

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			HttpPost post = new HttpPost(API_URL);

			// Updated headers
			post.setHeader("Content-Type", "application/json; charset=UTF-8");
			post.setHeader("x-api-key", API_KEY);
			post.setHeader("anthropic-version", "2023-06-01");

			// Create request body with updated structure
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("model", "claude-3-7-sonnet-20250219");
			requestBody.put("max_tokens", 300);
			requestBody.put("messages", new Object[] { Map.of("role", "user", "content", escapeSpecialCharacters(sanitizePrompt(prompt))) });

			ObjectMapper mapper = new ObjectMapper();
			String jsonBody = mapper.writeValueAsString(requestBody);
			post.setEntity(new StringEntity(jsonBody));

			// Execute request
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != 200) {
				String responseBody = EntityUtils.toString(response.getEntity());
				throw new RuntimeException(
						"API request failed with status code: " + statusCode + "\nResponse: " + responseBody);
			}

			// Parse response
			String responseBody = EntityUtils.toString(response.getEntity());
			JsonNode jsonResponse = mapper.readTree(responseBody);

			if (!jsonResponse.has("content")) {
				throw new RuntimeException("Unexpected API response format: " + responseBody);
			}

			return jsonResponse.get("content").get(0).get("text").asText();
		} catch (Exception e) {
			throw new Exception("Error connecting to Claude API: " + e.getMessage(), e);
		}
	}
	
    // Método para escapar caracteres especiales en el mensaje
    private static String escapeSpecialCharacters(String message) {
        // Escapar comillas, barras invertidas, saltos de línea, retorno de carro, y otros caracteres especiales
        return message
            .replace("\\", "\\\\")  // Escapar barras invertidas
            .replace("\"", "\\\"")   // Escapar comillas
            .replace("\n", "\\n")    // Escapar saltos de línea
            .replace("\r", "\\r");   // Escapar retorno de carro
    }
    
    // Limpiar caracteres no válidos
    private static String sanitizePrompt(String prompt) {
        return prompt.replaceAll("[^\\p{Print}\\t\\r\\n]", "");
    }

}
