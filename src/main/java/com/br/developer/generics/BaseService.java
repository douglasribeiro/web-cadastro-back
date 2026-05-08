package com.br.developer.generics;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.br.developer.exception.RecordNotFoundException;

public abstract class BaseService<E, D> {
    
	@Autowired
    protected JpaRepository<E, Long> repository;
	
	@Autowired
    protected ModelMapper mapper;

    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    public BaseService(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    public List<D> listar() {
        return repository.findAll().stream()
                .map(entity -> mapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public D salvar(D dto) {
        E entity = mapper.map(dto, entityClass);
        return mapper.map(repository.save(entity), dtoClass);
    }
    
    public D findById(Long id) {
    	return mapper.map(repository.findById(id).orElseThrow(() -> new RecordNotFoundException((java.lang.Long) id)),dtoClass);
    }
    
    public void delete(Long id) {
    	repository.delete(repository.findById(id).orElseThrow(() -> new RecordNotFoundException((java.lang.Long) id)));
    }
    
    public D update(D dto) {
    	return salvar(dto);
    }
}