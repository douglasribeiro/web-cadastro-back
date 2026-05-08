package com.br.developer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.developer.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

	Optional<Genero> findByNome(String nome);
}
