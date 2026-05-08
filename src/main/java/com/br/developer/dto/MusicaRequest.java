package com.br.developer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MusicaRequest {
	
	private Long id;
	
	private String nome;
	
	@NotBlank(message = "Informe o nome do interprete.")
	private String interprete;
	
	private String album;
	
	private String gravadora;
	
	private String lancamento;
	
	private String compositor;
	
	@NotNull(message = "informe o intervalo entre execuçoes;")
	@Min(value = 1, message = "O intervalo deve ser no minimo 1.")
	private int intervalo;
	
	private Long duracaoSegundos;
	
	private Long introducao;
	
	@NotBlank(message = "Informe o genero da musica.")
	private String genero;
	
	private String caminhoArquivo;
	
	private String ArquivoUrl;

	private String usuario;
}
