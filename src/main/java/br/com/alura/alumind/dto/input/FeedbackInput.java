package br.com.alura.alumind.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class FeedbackInput {

	@NotBlank(message = "não pode ser nulo ou vazio")
	@Schema(description = "Feedback do usuário", example = "O aplicativo é muito bom!")
	private String feedback;

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
