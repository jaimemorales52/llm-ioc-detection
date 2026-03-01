package com.phd.llm.client;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.phd.llm.model.grok.GrokResponse;

public class GrokClient {

	private static final String API_URL = "https://api.x.ai/v1/chat/completions"; // URL de la API
	private static final String API_KEY = "YOUR-PASSHERE"; // Tu
																																	// API
																																	// Key
																																	// (reemplázala)

	public static String generateContent(String userMessage) throws Exception {
		// JSON con los datos de la solicitud
		userMessage = userMessage .replace("\"", "\\\"")  // Escapar comillas dobles
                .replace("\n", "\\n")   // Escapar saltos de línea
                .replace("\t", "\\t")   // Escapar tabulaciones
                .replaceAll("[\\u0000-\\u001F]", ""); // Eliminar caracteres de control invisibles
		String jsonRequest = """
				{
				    "messages": [
				        {
				            "role": "system",
				            "content": "You are a security analyst."
				        },
				        {
				            "role": "user",
				            "content": \"""" + userMessage + """
				       \" }
				    ],
				    "model": "grok-2-latest",
				    "stream": false,
				    "temperature": 0
				}
				""";

		GrokResponse responseGrok = new GrokResponse();
		

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// Configurar la solicitud HTTP POST
			HttpPost request = new HttpPost(API_URL);
			request.addHeader("Authorization", "Bearer " + API_KEY);
			request.addHeader("Content-Type", "application/json");

			// Agregar el cuerpo de la solicitud
			request.setEntity(new StringEntity(jsonRequest, ContentType.APPLICATION_JSON));

			// Enviar la solicitud y obtener la respuesta
			try (CloseableHttpResponse response = httpClient.execute(request)) {
				int statusCode = response.getCode();
				String responseBody = EntityUtils.toString(response.getEntity());

				System.out.println("Response Code: " + statusCode);
				System.out.println("Response Body: " + responseBody);

				// Parsear JSON y mostrarlo de forma legible
				ObjectMapper objectMapper = new ObjectMapper();

				responseGrok = objectMapper.readValue(responseBody, GrokResponse.class);
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}

		// Obtener solo el contenido del asistente
		return responseGrok.getContent();
	}

}
