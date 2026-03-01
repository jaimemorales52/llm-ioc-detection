package com.phd.llm.model.ai21;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AI21Token {
    private GeneratedToken generatedToken;
    private Object topTokens;  // assuming topTokens can be null or of any type, use Object for now
    private TextRange textRange;

    @Data
    public static class GeneratedToken {
        private String token;
        private double logprob;
        @JsonProperty("raw_logprob")
        private double rawLogprob;
    }

    @Data
    public static class TextRange {
        private int start;
        private int end;
    }
}
