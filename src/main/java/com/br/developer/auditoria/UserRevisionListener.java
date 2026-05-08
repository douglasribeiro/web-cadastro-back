package com.br.developer.auditoria;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.br.developer.model.CustomRevisionEntity;

public class UserRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity rev = (CustomRevisionEntity) revisionEntity;
        
        // Pega o usuário logado no Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated()) {
            rev.setUsuario(auth.getName());
        } else {
            rev.setUsuario("SISTEMA"); // Caso seja um processo batch ou público
        }
    }
}