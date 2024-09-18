package br.com.alura.alumind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.alumind.dto.input.FeedbackInput;
import br.com.alura.alumind.service.FeedbackService;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

	@Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> processFeedback(@RequestBody FeedbackInput input) throws Exception {
    	return feedbackService.processFeedback(input)
                .map(output -> ResponseEntity.ok(output))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
