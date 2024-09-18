package br.com.alura.alumind.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LLMRequest {

	private String model;

    private List<Message> messages;

    public LLMRequest(String model, String content) {
    	this.model = model;
    	messages = List.of(new Message("user", content));
    }

}