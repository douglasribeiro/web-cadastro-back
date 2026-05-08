package com.br.developer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Retorna 404 Not Found automaticamente
public class RecordNotFoundException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public RecordNotFoundException(Long id) {
        super("Registro não encontrado com o ID: " + id);
    }

    public RecordNotFoundException(String message) {
        super(message);
    }
}