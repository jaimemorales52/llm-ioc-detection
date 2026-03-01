package com.phd.llm.service.impl;

import org.springframework.stereotype.Service;

import com.phd.llm.client.DeepSeekClient;
import com.phd.llm.service.DeepSeekService;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

	@Override
	public String getDeepSeekResponse(String prompt) {

		String response = "";
		try {
			response = DeepSeekClient.generateContent(prompt);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
