package br.com.alura.alumind.model;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedbacks", indexes = {
	@Index(name = "idx_sentiment", columnList = "sentiment")
})
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment", nullable = false)
    private Sentiment sentiment;

    @ElementCollection
    @CollectionTable(name = "feedback_feature_requests", joinColumns = @JoinColumn(name = "feedback_id"))
    private List<FeatureRequest> requestedFeatures;

    @Column(name = "custom_response", columnDefinition = "TEXT")
    private String customResponse;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Sentiment getSentiment() {
		return sentiment;
	}

	public void setSentiment(Sentiment sentiment) {
		this.sentiment = sentiment;
	}

	public List<FeatureRequest> getRequestedFeatures() {
		return requestedFeatures;
	}

	public void setRequestedFeatures(List<FeatureRequest> requestedFeatures) {
		this.requestedFeatures = requestedFeatures;
	}

	public String getCustomResponse() {
		return customResponse;
	}

	public void setCustomResponse(String customResponse) {
		this.customResponse = customResponse;
	}

}
