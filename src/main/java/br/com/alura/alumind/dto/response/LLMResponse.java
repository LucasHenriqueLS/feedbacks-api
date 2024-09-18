package br.com.alura.alumind.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LLMResponse {

	private String id;

    private String object;

    private Long created;

    private String model;

    private List<Choice> choices;

    private Usage usage;
    
    public String getContent() {
    	return choices.get(0).getMessage().getContent();
    }

}