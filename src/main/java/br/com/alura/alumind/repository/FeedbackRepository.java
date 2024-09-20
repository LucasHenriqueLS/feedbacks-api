package br.com.alura.alumind.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.alumind.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> { }
