package com.phd.llm.controller;

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

import com.phd.llm.service.AnthropicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/anthropic")
public class AnthropicController {
	
	 
	@Autowired 
	private AnthropicService anthropicService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/healthCheck")
	public String healthCheck() {
		return "OK";
	}
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/prompt")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<String> generatePrompt(
			@RequestBody @Valid String prompt) {

		return new ResponseEntity<>(anthropicService.getAnthropicResponse(prompt), HttpStatus.OK);
	}

}
