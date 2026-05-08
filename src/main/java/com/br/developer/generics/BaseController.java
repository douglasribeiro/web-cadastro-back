package com.br.developer.generics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class BaseController<D, S extends BaseService<?, D>> {
   
	@Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<D>>> getAll() {
    	List<D> lista = service.listar();
    	return ResponseEntity.ok(ApiResponse.success(lista, "Dados recuperados com sucesso"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<D>> create(@RequestBody D dto) {
    	D salvo = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(salvo, "Registro criado com sucesso"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<D>>  findById(@PathVariable Long id) {
    	D entity = service.findById(id);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(entity, "Registro recuperado com sucesso"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> delete(@PathVariable Long id){
    	service.delete(id);
    	HttpStatusCode meuStatus = HttpStatusCode.valueOf(288);
    	return new ResponseEntity<>(ApiResponse.delete(null, "Registro " + id + " excluido com sucesso."), meuStatus);
    }
    
    @PatchMapping
    public ResponseEntity<ApiResponse<D>>  update(@RequestBody D dto) {
    	D entity = service.salvar(dto);
    	return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(entity, "Registro alterado com sucesso"));
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<D>>  update(@PathVariable Long id, @RequestBody D dto) {
    	D entity = service.salvar(dto);
    	return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(entity, "Registro alterado com sucesso"));
    }
}