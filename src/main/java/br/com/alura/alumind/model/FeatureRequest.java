package br.com.alura.alumind.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@Entity
@Table(name = "feature_requests")
public class FeatureRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String code;

	private String reason;

}
