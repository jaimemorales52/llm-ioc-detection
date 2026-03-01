package com.phd.llm.model.grok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Clase principal para la respuesta
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrokResponse {
	public Choice[] choices;

	public String getContent() {
		if (choices != null && choices.length > 0) {
			return choices[0].message.content; // Extrae solo el contenido
		}
		return null;
	}
}

// Clase que representa una elección
@JsonIgnoreProperties(ignoreUnknown = true)
class Choice {
	public int index;
	public Message message;
}

// Clase que representa el mensaje del asistente
@JsonIgnoreProperties(ignoreUnknown = true)
class Message {
	public String role;
	public String content;
}
