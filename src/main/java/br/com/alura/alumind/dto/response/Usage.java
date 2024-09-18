package br.com.alura.alumind.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Usage {

	@JsonProperty("prompt_tokens")
	private Integer promptTokens;

	@JsonProperty("completion_tokens")
    private Integer completionTokens;

	@JsonProperty("total_tokens")
    private Integer totalTokens;

}
