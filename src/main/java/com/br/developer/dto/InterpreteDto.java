package com.br.developer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterpreteDto {

	private Long id;
	
	@NotBlank(message = "Informe o nome do interprete.")
	private String nome;
	
	private String genero;
	
	private String origem;
	
	private String desde;
	
	private String sobre;
}
