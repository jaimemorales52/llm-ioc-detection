package com.phd.llm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.phd.llm.model.ai21.AI21Model;
import com.phd.llm.service.AI21Service;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/ai21")
public class AI21Controller {
	
	
	@Autowired
	private AI21Service ai21Service;

	@RequestMapping(method = RequestMethod.GET, value = "/healthCheck")
	public String healthCheck() {
		return "OK";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/prompt")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<String> generatePrompt(
			@RequestBody @Valid String text) {

		return new ResponseEntity<>(ai21Service.getAIResponse(text), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/getAll")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<List<AI21Model>> getAll() {

		return new ResponseEntity<>(ai21Service.getAll(), HttpStatus.OK);
	}

}
