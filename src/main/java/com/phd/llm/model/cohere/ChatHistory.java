package com.phd.llm.model.cohere;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatHistory {

	private String role;
    private String message;
}
