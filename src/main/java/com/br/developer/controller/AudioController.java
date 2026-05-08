package com.br.developer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;

@RestController
@RequestMapping("/api/audio")
@CrossOrigin(origins = "http://localhost:4200")
public class AudioController {

    private final String PATH = "C:/musicas/";
    private Logger logger = LoggerFactory.getLogger(AudioController.class);

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> streamAudio(@PathVariable String filename) {
    	logger.info("streamAudio.");
        File file = new File(PATH + filename);
        if (!file.exists()) return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(new FileSystemResource(file));
    }
}