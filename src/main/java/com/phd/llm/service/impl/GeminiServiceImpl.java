package com.phd.llm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phd.llm.client.GeminiClient;
import com.phd.llm.model.gemini.GeminiModel;
import com.phd.llm.service.GeminiService;

@Service
public class GeminiServiceImpl implements GeminiService {

//	@Autowired
//	MongoTemplate mongoTemplate;

	@Override
	public List<GeminiModel> getAll() {
//		Query query = new Query();

//		List<GeminiModel> all = mongoTemplate.find(query, GeminiModel.class);

		return null;
	}

	@Override
	public String getGeminiResponse(String prompt) {

		String response = "";
		try {
			response = GeminiClient.generateContent(prompt);

//			mongoTemplate.save(geminiResponse, "gemini");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

}
