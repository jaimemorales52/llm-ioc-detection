package com.phd.llm.model.gemini;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeminiRequest {
    private final String model;
    private final Prompt prompt;

    public GeminiRequest(String model, String text) {
        this.model = model;
        this.prompt = new Prompt(text);
    }

    public String getModel() {
        return model;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public static class Prompt {
        private final String text;

        public Prompt(@JsonProperty("text") String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

