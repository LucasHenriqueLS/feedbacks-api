package br.com.alura.alumind.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.alumind.dto.input.FeedbackInput;
import br.com.alura.alumind.dto.output.FeedbackOutput;
import br.com.alura.alumind.dto.output.ProcessFeedbackOutput;
import br.com.alura.alumind.model.FeatureRequest;
import br.com.alura.alumind.model.Feedback;
import br.com.alura.alumind.model.FunctionalityCode;
import br.com.alura.alumind.repository.FeedbackRepository;
import br.com.alura.alumind.repository.FunctionalityCodeRepository;
import br.com.alura.alumind.service.converter.FeedbackConverter;

@Service
public class FeedbackService {

	@Autowired
    private FeedbackRepository feedbackRepository;
	
	@Autowired
    private FunctionalityCodeRepository functionalityCodeRepository;
	
	@Autowired
    private FeedbackConverter feedbackConverter;

    @Autowired
    private LLMService llmService;
    
    public List<Feedback> getAll() {
		return feedbackRepository.findAll();
	}

	public List<FeedbackOutput> getAllToOutput() {
		return feedbackConverter.toOutput(getAll());
	}

	public Feedback getById(Long id) {
		return feedbackRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Feedback com o ID %s não foi encontrado", id)));
	}

	public FeedbackOutput getByIdToOutput(Long id) {
		return feedbackConverter.toOutput(getById(id));
	}

    public Optional<ProcessFeedbackOutput> processFeedback(FeedbackInput input) throws Exception {
    	
    	var isSpam = llmService.isSpam(input.getFeedback());
        if (isSpam) {
            return Optional.empty();
        }
        
        var functionalityCodes = functionalityCodeRepository.findAll().stream().map(FunctionalityCode::getCode).collect(Collectors.toSet());

        var feedback = llmService.analyzeFeedback(input.getFeedback(), functionalityCodes);
        
        saveAllNewFunctionalityCodes(functionalityCodes, feedback);

        feedback.setFeedback(input.getFeedback());
        feedbackRepository.save(feedback);

        return Optional.of(feedbackConverter.toProcessFeedbackOutput(feedback));
    }

	private void saveAllNewFunctionalityCodes(Set<String> functionalityCodes, Feedback feedback) {
		List<FunctionalityCode> newFunctionalityCodes = new ArrayList<>();
		List<String> requestedFeatureFunctionalityCodes = feedback.getRequestedFeatures().stream().map(FeatureRequest::getCode).toList();
        for (var code : requestedFeatureFunctionalityCodes) {
        	if (!functionalityCodes.contains(code)) {
        		newFunctionalityCodes.add(new FunctionalityCode(code));
        	}
        }

        functionalityCodeRepository.saveAll(newFunctionalityCodes);
	}

}
