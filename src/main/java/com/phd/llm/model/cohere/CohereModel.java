package com.phd.llm.model.cohere;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
//@Document(collection = "cohere")
public class CohereModel {

	private String text;
	@JsonProperty("generation_id")
    private String generationId;
	
	@JsonProperty("finish_reason")
    private String finishReason;
	
    @JsonProperty("chat_history")
    private List<ChatHistory> chatHistory;
    private Meta meta;
    
    @JsonProperty("response_id")
    private String responseId;
    
}
