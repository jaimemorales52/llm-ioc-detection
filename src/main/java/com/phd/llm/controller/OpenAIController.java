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

import com.phd.llm.model.openai.OpenAIModel;
import com.phd.llm.service.OpenAIService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/v1/openai")
public class OpenAIController {

	@Autowired
	private OpenAIService openAIService;

	@RequestMapping(method = RequestMethod.GET, value = "/healthCheck")
	public String healthCheck() {
		return "OK";
	}

	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/prompt")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<String> generatePrompt(
			@RequestBody @Valid String prompt) {

		return new ResponseEntity<>(openAIService.getOpenAIResponse(prompt), HttpStatus.OK);
	}

    @Hidden
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, value = "/getAll")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<List<OpenAIModel>> getAll() {

		return new ResponseEntity<>(openAIService.getAll(), HttpStatus.OK);
	}

    @Hidden
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, value = "/searchInformation")
	public @ResponseBody @ResponseStatus(code = HttpStatus.OK) ResponseEntity<String> searchInformation(
			@RequestBody @Valid String information) {

		return new ResponseEntity<>(openAIService.getOpenAIResponse(information), HttpStatus.OK);
	}

}
