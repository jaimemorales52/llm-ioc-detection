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

import com.phd.llm.service.GrokService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/grok")
public class GrokController {

	@Autowired
	private GrokService grokService;

	@RequestMapping(method = RequestMethod.GET, value = "/healthCheck")
	public String healthCheck() {
		return "OK";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/prompt")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<String> generatePrompt(
			@RequestBody @Valid String prompt) {

		try {
			return new ResponseEntity<>(grokService.getGrokResponse(prompt), HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<>(e.getMessage().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
}
