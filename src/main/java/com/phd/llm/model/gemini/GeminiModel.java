package com.phd.llm.model.gemini;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

//import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "gemini")
public class GeminiModel {
    private final List<Completion> completions;

    public GeminiModel(@JsonProperty("completions") List<Completion> completions) {
        this.completions = completions;
    }
 
    public List<Completion> getCompletions() {
        return completions;
    }

    public static class Completion {
        private final Data data;

        public Completion(@JsonProperty("data") Data data) {
            this.data = data;
        }

        public Data getData() {
            return data;
        }

        public static class Data {
            private final String text;

            public Data(@JsonProperty("text") String text) {
                this.text = text;
            }

            public String getText() {
                return text;
            }
        }
    }
}

