package com.phd.llm.service;

import java.util.List;

import com.phd.llm.model.gemini.GeminiModel;

public interface GeminiService {

	public String getGeminiResponse (String prompt);
	
	public List<GeminiModel> getAll();
}
