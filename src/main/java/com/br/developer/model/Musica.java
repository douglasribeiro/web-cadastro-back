package com.br.developer.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@SQLDelete(sql = "UPDATE musica SET ativo = false WHERE id = ?")
@Where(clause = "ativo = true")
@Table(name = "musica", uniqueConstraints = {
	    @UniqueConstraint(
	        name = "uk_musica_artista_album", 
	        columnNames = {"nome", "album"}
	    )
	})
public class Musica extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(length = 100)
	private String nome;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "interprete_id")
	private Interprete interprete;
	
	private String artista;
	
	@Column(length = 80)
	private String album;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gravadora_id")
	private Gravadora gravadora;
	
	@Column(length = 04)
	private String lancamento;
	
	@Column(length = 80)
	private String compositor;
	
	private int intervalo; //em horas
	
	private Long duracaoSegundos;
	
	private Long introducao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "genero_id")
	private Genero genero;
	
	private String caminhoArquivo;
	
	private String ArquivoUrl;
	
	@Lob 
    @Column(columnDefinition = "BLOB")
    private byte[] albumArt;
	
}
