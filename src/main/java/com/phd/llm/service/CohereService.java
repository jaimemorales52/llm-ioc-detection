package com.phd.llm.service;

import java.util.List;

import com.phd.llm.model.cohere.CohereModel;

public interface CohereService {

	public String getCohereResponse (String prompt);
	
	public List<CohereModel> getAll();
}
