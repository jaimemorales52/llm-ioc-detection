package com.phd.llm.model.deepseek;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class DeepSeekResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("choices")
    private List<Choice> choices;

    public static class Choice {
        @JsonProperty("message")
        private Message message;

        public Message getMessage() {
            return message;
        }
    }

    public static class Message {
        @JsonProperty("content")
        private String content;

        public String getContent() {
            return content;
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }
}
