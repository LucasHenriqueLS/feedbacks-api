package br.com.alura.alumind.dto.request;

import java.util.List;

public record LLMRequest (
	String model,
	List<Message> messages
) { }