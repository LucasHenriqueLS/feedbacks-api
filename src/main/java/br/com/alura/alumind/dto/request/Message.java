package br.com.alura.alumind.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {

    private String role;

    private String content;

    public Message(String role, String content) {
    	this.role = role;
    	this.content = content;
    }

}