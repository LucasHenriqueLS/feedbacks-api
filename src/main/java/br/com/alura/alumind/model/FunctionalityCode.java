package br.com.alura.alumind.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "functionality_codes")
public class FunctionalityCode {

	@Id
    private String code;

	public FunctionalityCode() { }

	public FunctionalityCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
