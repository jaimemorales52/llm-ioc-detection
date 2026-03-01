package com.phd.llm.model.gemini;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // ✅ Ignore unexpected fields
public class UsageMetadata {
    private int promptTokenCount;
    private int candidatesTokenCount;
    private int totalTokenCount;

    @JsonProperty("promptTokensDetails")
    private List<Content> promptTokensDetails;

    @JsonProperty("candidatesTokensDetails")
    private List<Content> candidatesTokensDetails;

    // ✅ Getters and Setters
    public int getPromptTokenCount() {
        return promptTokenCount;
    }

    public void setPromptTokenCount(int promptTokenCount) {
        this.promptTokenCount = promptTokenCount;
    }

    public int getCandidatesTokenCount() {
        return candidatesTokenCount;
    }

    public void setCandidatesTokenCount(int candidatesTokenCount) {
        this.candidatesTokenCount = candidatesTokenCount;
    }

    public int getTotalTokenCount() {
        return totalTokenCount;
    }

    public void setTotalTokenCount(int totalTokenCount) {
        this.totalTokenCount = totalTokenCount;
    }

    public List<Content> getPromptTokensDetails() {
        return promptTokensDetails;
    }

    public void setPromptTokensDetails(List<Content> promptTokensDetails) {
        this.promptTokensDetails = promptTokensDetails;
    }

    public List<Content> getCandidatesTokensDetails() {
        return candidatesTokensDetails;
    }

    public void setCandidatesTokensDetails(List<Content> candidatesTokensDetails) {
        this.candidatesTokensDetails = candidatesTokensDetails;
    }
}

