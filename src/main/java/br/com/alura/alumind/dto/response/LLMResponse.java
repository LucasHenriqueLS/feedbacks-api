package br.com.alura.alumind.dto.response;

import java.util.List;

public record LLMResponse (
	String id,
	String object,
	Long created,
	String model,
	List<Choice> choices,
	Usage usage
) {

    public String getContent() {
    	return choices.get(0).message().content();
    }

}