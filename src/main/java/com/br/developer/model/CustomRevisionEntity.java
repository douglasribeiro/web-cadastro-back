package com.br.developer.model;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import com.br.developer.auditoria.UserRevisionListener;

import jakarta.persistence.Entity;

@Entity
@RevisionEntity(UserRevisionListener.class) // Define quem vai preencher os dados
public class CustomRevisionEntity extends DefaultRevisionEntity {

	private static final long serialVersionUID = 1L;
	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

    
}