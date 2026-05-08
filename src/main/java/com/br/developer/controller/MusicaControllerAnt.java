package com.br.developer.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/musicas")
@CrossOrigin(origins = "http://localhost:4200") // Permite o Angular acessar
public class MusicaControllerAnt {

    private final String PASTA_MUSICAS = "C:/musicas/";
    private Logger logger = LoggerFactory.getLogger(MusicaControllerAnt.class);

    @GetMapping
    public List<String> listarMusicas() {
        File pasta = new File(PASTA_MUSICAS);
        logger.info("Lista musicas.");
        return Arrays.stream(pasta.listFiles())
                .filter(f -> f.getName().endsWith(".mp3"))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @GetMapping("/play/{nome}")
    public ResponseEntity<Resource> streamMusica(@PathVariable String nome) throws IOException {
    	logger.info("play musica.");
        Path caminho = Paths.get(PASTA_MUSICAS + nome);
        Resource recurso = new UrlResource(caminho.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(recurso);
    }
}