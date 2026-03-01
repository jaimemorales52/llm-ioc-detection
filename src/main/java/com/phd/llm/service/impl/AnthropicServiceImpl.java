package com.phd.llm.service.impl;

import org.springframework.stereotype.Service;

import com.phd.llm.client.AnthropicClient;
import com.phd.llm.service.AnthropicService;

@Service
public class AnthropicServiceImpl implements AnthropicService {

	@Override
	public String getAnthropicResponse(String prompt) {
		
		AnthropicClient anthropicClient = new AnthropicClient();
		String response = ""; 
		try {
			response = anthropicClient.sendRequest(prompt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response; 
	}
	
	@Override
	public String getAnthropicResponsev37(String prompt) {
		
		AnthropicClient anthropicClient = new AnthropicClient();
		String response = ""; 
		try {
			response = anthropicClient.sendRequestv3(prompt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response; 
	}

}
