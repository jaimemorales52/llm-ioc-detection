package com.phd.llm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "LLMs Requests", version = "0.0.1", description = "APIs v0.0.1"))
public class LlmApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmApplication.class, args);
	}

}
