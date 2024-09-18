package br.com.alura.alumind.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

	private String role;

    private String content;

}
