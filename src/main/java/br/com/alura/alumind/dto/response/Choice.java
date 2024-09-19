package br.com.alura.alumind.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice (
	Integer index,
	Message message,
    @JsonProperty("finish_reason") String finishReason
) { }
