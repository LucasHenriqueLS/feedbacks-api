package br.com.alura.alumind.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FeatureRequest {

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "reason", columnDefinition = "TEXT")
	private String reason;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
