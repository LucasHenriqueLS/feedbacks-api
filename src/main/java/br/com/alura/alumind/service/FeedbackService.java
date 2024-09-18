package br.com.alura.alumind.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.alumind.dto.input.FeedbackInput;
import br.com.alura.alumind.dto.output.FeedbackOutput;
import br.com.alura.alumind.repository.FeedbackRepository;

@Service
public class FeedbackService {

	@Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private LLMService llmService;
    
    public Optional<FeedbackOutput> processFeedback(FeedbackInput input) throws Exception {
    	
    	var isSpam = llmService.isSpam(input.getFeedback());
        if (isSpam) {
            return Optional.empty();
        }

        var feedback = llmService.analyzeFeedback(input.getFeedback());

        feedbackRepository.save(feedback);

        var output = new FeedbackOutput();
        output.setId(feedback.getId());
        output.setSentiment(feedback.getSentiment().name());
        output.setRequestedFeatures(feedback.getRequestedFeatures());

        return Optional.of(output);
    }
}
