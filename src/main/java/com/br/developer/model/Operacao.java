package com.br.developer.model;

public enum Operacao {
	
	INCLUSAO(1, "Inclusão"),
	ALTERACAO(2, "Alteração"),
	EXCLUSAO(3, "Exclusão");
	
	private final int id;
	private final String descricao;
	
	Operacao(int id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getDescricao() { return descricao; }
    public int getId() { return id; }
}
