package com.phd.llm.service;

import java.util.List;

import com.phd.llm.model.openai.OpenAIModel;

public interface OpenAIService {

	public String getOpenAIResponse (String prompt);
	
	public List<OpenAIModel> getAll();
	
	public List<OpenAIModel> searchInformation(String information);
	
	
}
