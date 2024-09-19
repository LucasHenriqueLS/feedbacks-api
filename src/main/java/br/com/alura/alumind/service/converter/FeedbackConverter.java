package br.com.alura.alumind.service.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.alura.alumind.dto.output.FeatureRequestOutput;
import br.com.alura.alumind.dto.output.FeedbackOutput;
import br.com.alura.alumind.dto.output.ProcessFeedbackOutput;
import br.com.alura.alumind.model.FeatureRequest;
import br.com.alura.alumind.model.Feedback;

@Component
public class FeedbackConverter {

	public List<FeedbackOutput> toOutput(List<Feedback> feedbacks) {
		List<FeedbackOutput> outputs = new ArrayList<>();
		for (var feedback : feedbacks) {
			outputs.add(toOutput(feedback));
		}
		return outputs;
	}

	public FeedbackOutput toOutput(Feedback feedback) {
		return new FeedbackOutput(
					feedback.getId(),
					feedback.getFeedback(),
					feedback.getSentiment(),
					featureRequestsToFeatureRequestOutputs(feedback.getRequestedFeatures()),
					feedback.getCustomResponse()
				   );
	}
	
	public ProcessFeedbackOutput toProcessFeedbackOutput(Feedback feedback) {
		return new ProcessFeedbackOutput(
					feedback.getId(),
					feedback.getSentiment(),
					featureRequestsToFeatureRequestOutputs(feedback.getRequestedFeatures())
				   );
	}
	
	private List<FeatureRequestOutput> featureRequestsToFeatureRequestOutputs(List<FeatureRequest> requestedFeatures) {
		List<FeatureRequestOutput> outputs = new ArrayList<>();
		for (var requestedFeature : requestedFeatures) {
			outputs.add(featureRequestToFeatureRequestOutput(requestedFeature));
		}
		return outputs;
	}
	
	private FeatureRequestOutput featureRequestToFeatureRequestOutput(FeatureRequest requestedFeature) {
		return new FeatureRequestOutput(
						requestedFeature.getCode(),
						requestedFeature.getReason()
				   );
	}

}
