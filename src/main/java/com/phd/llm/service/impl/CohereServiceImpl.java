package com.phd.llm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cohere.api.Cohere;
import com.cohere.api.requests.ChatRequest;
import com.cohere.api.types.NonStreamedChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phd.llm.model.cohere.CohereModel;
import com.phd.llm.properties.ConfigLoader;
import com.phd.llm.service.CohereService;

@Service
public class CohereServiceImpl implements CohereService {

//	@Autowired
//	MongoTemplate mongoTemplate;
	
	@Override 
	public String getCohereResponse(String request) {

		CohereModel cohereModel = new CohereModel();

		Cohere cohere = Cohere.builder().token(ConfigLoader.getProperty("cohere.password")).build();

		NonStreamedChatResponse response = cohere.chat(ChatRequest.builder().message(request).build());

		ObjectMapper mapper = new ObjectMapper();

		try {
			cohereModel = mapper.readValue(response.toString(), CohereModel.class);
			
//			mongoTemplate.save(cohereModel, "cohere");
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return cohereModel.getText();
	}

	@Override
	public List<CohereModel> getAll() {
//		Query query = new Query();

//		List<CohereModel> all = mongoTemplate.find(query, CohereModel.class);

		return null;
	}

}
