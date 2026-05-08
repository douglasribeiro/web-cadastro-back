package com.br.developer.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
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
@SQLDelete(sql = "UPDATE gravadora SET ativo = false WHERE id = ?")
@Where(clause = "ativo = true")
public class Gravadora extends BaseEntity{
	
	private String nome;

}
