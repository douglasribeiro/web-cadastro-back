package com.br.developer.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.developer.dto.InterpreteDto;
import com.br.developer.generics.BaseController;
import com.br.developer.service.InterpreteService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/interprete")
@RequiredArgsConstructor
public class InterpreteController extends BaseController<InterpreteDto,	InterpreteService>{

}
