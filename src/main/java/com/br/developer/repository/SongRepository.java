package com.br.developer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.developer.model.Song;

public interface SongRepository extends JpaRepository<Song, Long> {

}
