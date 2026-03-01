package com.phd.llm.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phd.llm.model.openai.OpenAIModel;
import com.phd.llm.properties.ConfigLoader;

import lombok.Data;

public class OpenAIClient {
	private static final String API_URL = "https://api.openai.com/v1/chat/completions";
	private static final String API_KEY = ConfigLoader.getProperty("chatgpt.password"); 

	private final HttpClient client;
	private final ObjectMapper objectMapper;

	public OpenAIClient() {
		this.client = HttpClient.newHttpClient();
		this.objectMapper = new ObjectMapper();
	}

	public OpenAIModel sendMessage(String prompt) throws IOException, InterruptedException {
		ChatRequest chatRequest = new ChatRequest(prompt);
		String requestBody = objectMapper.writeValueAsString(chatRequest);

		OpenAIModel openAIModel = new OpenAIModel();
		
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL))
				.header("Content-Type", "application/json").header("Authorization", "Bearer " + API_KEY)
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			
			ObjectMapper mapper = new ObjectMapper();
	        
	        openAIModel = mapper.readValue(response.body(), OpenAIModel.class);

		} else if (response.statusCode() != 200 && response.statusCode() != 429) {
			throw new IOException("Unexpected code " + response.statusCode() + ": " + response.body());
		} else if (response.statusCode() == 429 && response.body().contains("You exceeded your current quota")) {
			return openAIModel;
		}

		return openAIModel;
	}

	@Data
	private static class ChatRequest {
		private final String model = "gpt-4o"; // Cambia al modelo que desees utilizar
		//gpt-4o, gpt-3.5-turbo, oi-preview y oi-mini 
		private final List<Message> messages;

		public ChatRequest(String prompt) {
			this.messages = List.of(new Message("user", prompt));
		}

		public String getModel() {
			return model;
		}

		public List<Message> getMessages() {
			return messages;
		}

		private static class Message {
			private final String role;
			private final String content;

			public Message(String role, String content) {
				this.role = role;
				this.content = content;
			}

			public String getRole() {
				return role;
			}

			public String getContent() {
				return content;
			}
		}
	}

	@Data
	private static class ChatResponse {
		private List<Choice> choices;

		public List<Choice> getChoices() {
			return choices;
		}

		public void setChoices(List<Choice> choices) {
			this.choices = choices;
		}

		@Data
		private static class Choice {
			private Message message;

			public Message getMessage() {
				return message;
			}

			public void setMessage(Message message) {
				this.message = message;
			}

			private static class Message {
				private String content;

				public String getContent() {
					return content;
				}

				public void setContent(String content) {
					this.content = content;
				}
			}
		}
	}
}
