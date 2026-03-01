package com.phd.llm.model.deepseek;

import java.util.List;

public class DeepSeekRequest {
    private String model;
    private List<Message> messages;

    public DeepSeekRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }
}
