package com.br.developer.service;

import org.springframework.stereotype.Service;

import com.br.developer.dto.InterpreteDto;
import com.br.developer.generics.BaseService;
import com.br.developer.model.Interprete;

@Service
public class InterpreteService extends BaseService<Interprete, InterpreteDto> {

	public InterpreteService() {
		super(Interprete.class, InterpreteDto.class);
	}
	
}
