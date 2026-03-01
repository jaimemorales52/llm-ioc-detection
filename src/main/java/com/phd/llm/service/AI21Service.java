package com.phd.llm.service;

import java.util.List;

import com.phd.llm.model.ai21.AI21Model;

public interface AI21Service {

	public String getAIResponse (String response);
	
	public List<AI21Model> getAll();
}
