package com.br.developer.service;

import java.util.List;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.developer.model.CustomRevisionEntity;
import com.br.developer.model.Musica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class AuditoriaService {

    @PersistenceContext
    private EntityManager entityManager; // O Spring injeta automaticamente aqui

    @Transactional(readOnly = true)
    public void mostrarQuemAlterou(Long produtoId) {
        // Inicializa o AuditReader usando o entityManager injetado
        AuditReader reader = AuditReaderFactory.get(entityManager);
        
        // Busca a lista de números de revisão para o ID do produto
        List<Number> revisoes = reader.getRevisions(Musica.class, produtoId);
        
        for (Number n : revisoes) {
            // Busca os metadados da revisão (sua classe customizada)
            CustomRevisionEntity rev = reader.findRevision(CustomRevisionEntity.class, n);
            
            // Busca o estado do objeto naquela revisão específica (opcional)
            //Musica produtoNaEpoca = reader.find(Musica.class, produtoId, n);

            System.out.println("Revisão: " + n);
            System.out.println("Data: " + rev.getRevisionDate());
            System.out.println("Usuário: " + rev.getUsuario());
            //System.out.println("Nome do Produto na época: " + produtoNaEpoca.getNome());
            System.out.println("-----------------------------------");
        }
    }
}
