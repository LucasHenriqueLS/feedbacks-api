package br.com.alura.alumind.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Choice {

	private Integer index;

    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;

}
