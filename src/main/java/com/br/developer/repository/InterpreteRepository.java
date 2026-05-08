package com.br.developer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.developer.model.Interprete;

public interface InterpreteRepository extends JpaRepository<Interprete, Long> {

	
	Optional<Interprete> findByNome(String nome);
	
}
