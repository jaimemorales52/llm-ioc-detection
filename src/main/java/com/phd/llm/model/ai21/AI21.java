package com.phd.llm.model.ai21;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AI21 {
    private String text;
    private List<AI21Token> tokens;
}