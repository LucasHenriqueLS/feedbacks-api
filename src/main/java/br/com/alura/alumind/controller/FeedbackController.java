package br.com.alura.alumind.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.alumind.dto.input.FeedbackInput;
import br.com.alura.alumind.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

	@Autowired
    private FeedbackService feedbackService;
	
	@GetMapping
	@Operation(description = "Retorna todos os feedbacks")
    public ResponseEntity<?> getAll() {
    	return ResponseEntity.ok(feedbackService.getAllToOutput());
    }
	
	@GetMapping("get-by-id/{id}")
	@Operation(description = "Retorna um feedback pelo ID")
    public ResponseEntity<?> getById(@PathVariable Long id) {
    	return ResponseEntity.ok(feedbackService.getByIdToOutput(id));
    }

    @PostMapping
    @Operation(description = "Processa um feedback de usuário")
    public ResponseEntity<?> processFeedback(@RequestBody FeedbackInput input) throws Exception {
    	return feedbackService.processFeedback(input)
                .map(output -> ResponseEntity.ok(output))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

}
