package com.br.developer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.developer.model.Musica;

public interface MusicaRepository extends JpaRepository<Musica, Long> {

}
