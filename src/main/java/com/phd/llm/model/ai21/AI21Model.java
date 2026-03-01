package com.phd.llm.model.ai21;

import java.io.IOException;
import java.util.List;

//import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@JsonIgnoreProperties
@Data
//@Document(collection = "ai21")
public class AI21Model {
	
	private String id;
    private AI21 prompt;
    
    private List<Completion> completions;
	
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Completion {
        private CompletionData data;
        private FinishReason finishReason;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletionData {
        private String text;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FinishReason {
        private String reason;
        private int length;
    }
	

    public static AI21Model fromJson(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, AI21Model.class);
    }
}