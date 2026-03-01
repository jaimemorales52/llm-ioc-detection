package com.phd.llm.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.phd.llm.client.OpenAIClient;
import com.phd.llm.model.openai.OpenAIModel;
import com.phd.llm.service.OpenAIService;

@Service
public class OpenAIServiceImpl implements OpenAIService {

//	@Autowired
//	MongoTemplate mongoTemplate;
	
	@Override
	public String getOpenAIResponse(String request) {

		OpenAIClient client = new OpenAIClient();
		 
		OpenAIModel openAIModel = new OpenAIModel();
		
		try {
			openAIModel = client.sendMessage(request);
			
//			mongoTemplate.save(openAIModel, "openai");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return openAIModel.getChoices().get(0).getMessage().getContent();
	}

	@Override
	public List<OpenAIModel> getAll() {
//		Query query = new Query();

		return null; //mongoTemplate.find(query, OpenAIModel.class);
	}

	@Override
	public List<OpenAIModel> searchInformation(String information) {

//		Query query = new Query(); 
		
		return null; //mongoTemplate.findAll(OpenAIModel.class, information);
	}

}
