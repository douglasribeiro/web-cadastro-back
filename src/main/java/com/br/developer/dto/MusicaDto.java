package com.br.developer.dto;

import com.br.developer.model.Genero;
import com.br.developer.model.Gravadora;
import com.br.developer.model.Interprete;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MusicaDto {

	private Long id;
	
	private String nome;
	
	private Interprete interprete;
	
	private String artista;
	
	private String album;
	
	private Gravadora gravadora;
	
	private String lancamento;
	
	private String compositor;
	
	private int intervalo;
	
	private Long duracaoSegundos;
	
	private Long introducao;
	
	private Genero genero;
	
	private String caminhoArquivo;
	
	private String ArquivoUrl;
	
	private String usuario;
	
	@Lob 
    @Column(columnDefinition = "BLOB")
    private byte[] albumArt;
}
