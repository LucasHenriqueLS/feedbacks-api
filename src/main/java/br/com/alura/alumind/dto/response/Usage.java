package br.com.alura.alumind.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Usage (
	@JsonProperty("prompt_tokens") Integer promptTokens,
	@JsonProperty("completion_tokens") Integer completionTokens,
	@JsonProperty("total_tokens") Integer totalTokens
) {	}
