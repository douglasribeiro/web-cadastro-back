package com.br.developer.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@SQLDelete(sql = "UPDATE interprete SET ativo = false WHERE id = ?")
@Where(clause = "ativo = true")
public class Interprete extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "Informe o nome do interprete.")
	@Column(nullable = false, unique = true)
	private String nome;
	
	private String genero;
	
	private String origem;
	
	private String desde;
	
	private String sobre;
}
