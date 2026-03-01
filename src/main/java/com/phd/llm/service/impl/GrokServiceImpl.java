package com.phd.llm.service.impl;

import org.springframework.stereotype.Service;

import com.phd.llm.client.GrokClient;
import com.phd.llm.service.GrokService;

@Service
public class GrokServiceImpl implements GrokService {

	@Override
	public String getGrokResponse(String prompt) {

		String response = "";
		try {
			response = GrokClient.generateContent(prompt);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
