package com.br.developer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private String title;
    private String artist;
    @Lob 
    @Column(columnDefinition = "BLOB")
    private byte[] albumArt;
    
}