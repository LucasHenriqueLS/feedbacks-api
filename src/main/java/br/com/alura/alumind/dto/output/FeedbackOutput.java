package br.com.alura.alumind.dto.output;

import java.util.List;

import br.com.alura.alumind.model.FeatureRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackOutput {

	private Long id;

	private String sentiment;

	private List<FeatureRequest> requestedFeatures;

}
