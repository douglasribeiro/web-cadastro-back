package com.br.developer.service;

import org.springframework.stereotype.Service;

import com.br.developer.dto.GeneroDto;
import com.br.developer.generics.BaseService;
import com.br.developer.model.Genero;

@Service
public class GeneroService extends BaseService<Genero, GeneroDto> {

	public GeneroService() {
		super(Genero.class, GeneroDto.class);
	}
	
}
