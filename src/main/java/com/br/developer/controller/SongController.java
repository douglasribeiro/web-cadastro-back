package com.br.developer.controller;

import java.io.File;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.developer.model.Song;
import com.br.developer.repository.SongRepository;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class SongController {
    
	private final SongRepository repository;

    @GetMapping
    public List<Song> getPlaylist() throws Exception {
        if (repository.count() == 0) {
            File folder = new File("C:/musicas/");

            if (!folder.exists() || !folder.isDirectory()) {
                throw new Exception("Diretório C:/musicas/ não encontrado ou inacessível.");
            }

            File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3"));

            if (files != null) {
                for (File file : files) {
                    Mp3File mp3file = new Mp3File(file);
                    Song song = new Song();
                    song.setFilename(file.getName());

                    if (mp3file.hasId3v2Tag()) {
                        ID3v2 tag = mp3file.getId3v2Tag();
                        song.setTitle(tag.getTitle());
                        song.setArtist(tag.getArtist());
                        song.setAlbumArt(tag.getAlbumImage());
                    }

                    repository.save(song);
                }
            }
        }
        return repository.findAll();
    }

}