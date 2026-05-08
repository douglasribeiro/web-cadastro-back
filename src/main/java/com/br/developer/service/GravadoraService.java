package com.br.developer.service;

import org.springframework.stereotype.Service;

import com.br.developer.dto.GravadoraDto;
import com.br.developer.generics.BaseService;
import com.br.developer.model.Gravadora;

@Service
public class GravadoraService extends BaseService<Gravadora, GravadoraDto> {

	public GravadoraService() {
		super(Gravadora.class, GravadoraDto.class);
	}
	
}
