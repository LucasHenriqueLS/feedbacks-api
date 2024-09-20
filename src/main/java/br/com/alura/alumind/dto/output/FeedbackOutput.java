package br.com.alura.alumind.dto.output;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alura.alumind.model.Sentiment;

public record FeedbackOutput (
	Long id,
	String feedback,
	Sentiment sentiment,
	@JsonProperty("requested_features") List<FeatureRequestOutput> requestedFeatures,
	@JsonProperty("custom_response") String customResponse
) { }
