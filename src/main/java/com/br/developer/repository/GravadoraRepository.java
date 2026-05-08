package com.br.developer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.developer.model.Gravadora;

public interface GravadoraRepository extends JpaRepository<Gravadora, Long> {

	Optional<Gravadora> findByNome(String nome);
}
