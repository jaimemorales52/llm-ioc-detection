package com.phd.llm.model.cohere;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
	
	@JsonProperty("api_version")
	private ApiVersion apiVersion;
	
    @JsonProperty("billed_units")
	private BilledUnits billedUnits;
    
	private Tokens tokens;

	public static class ApiVersion {
		private String version;

		// Getters and Setters
		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}

    @JsonIgnoreProperties(ignoreUnknown = true)
	public static class BilledUnits {
    	

        @JsonProperty("input_tokens")
		private double inputTokens;
        
        @JsonProperty("output_tokens")
		private double outputTokens;

		// Getters and Setters
		public double getInputTokens() {
			return inputTokens;
		}

		public void setInputTokens(double inputTokens) {
			this.inputTokens = inputTokens;
		}

		public double getOutputTokens() {
			return outputTokens;
		}

		public void setOutputTokens(double outputTokens) {
			this.outputTokens = outputTokens;
		}
	}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
	public static class Tokens {
    	
        @JsonProperty("input_tokens")
		private double inputTokens;
        @JsonProperty("output_tokens")
		private double outputTokens;

	}
}
