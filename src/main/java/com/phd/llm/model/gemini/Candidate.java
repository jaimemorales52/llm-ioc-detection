package com.phd.llm.model.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Candidate {
    private Content content;
    private String finishReason;
    private double avgLogprobs;

    // ✅ Getters and Setters
    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public double getAvgLogprobs() {
        return avgLogprobs;
    }

    public void setAvgLogprobs(double avgLogprobs) {
        this.avgLogprobs = avgLogprobs;
    }
}
