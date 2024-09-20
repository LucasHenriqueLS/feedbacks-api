package br.com.alura.alumind.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.alumind.model.FunctionalityCode;

public interface FunctionalityCodeRepository extends JpaRepository<FunctionalityCode, String> { }
