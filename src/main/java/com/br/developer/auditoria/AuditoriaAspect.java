package com.br.developer.auditoria;


import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.br.developer.model.Operacao;
import com.br.developer.repository.MusicaLogRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaAspect.class);
    private final MusicaLogRepository log;

    @Around("@annotation(auditavel)")
    public Object realizarAuditoria(ProceedingJoinPoint joinPoint, Auditavel auditavel) throws Throwable {
        long inicio = System.currentTimeMillis();
        
        // 1. Pegar o usuário logado (Contexto do Spring Security / Keycloak)
        String usuario = "ANÔNIMO";
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            usuario = jwt.getClaim("preferred_username");
        }

        // 2. Executar o método original
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("Falha na operação: {} | Usuário: {} | Erro: {}", 
                auditavel.operacao(), usuario, e.getMessage());
            throw e;
        }

        long duracao = System.currentTimeMillis() - inicio;

        // 3. Registrar o sucesso (Pode salvar no Banco ou Log)
        logger.info("Auditoria: Operação: [{}] | Usuário: [{}] | Tempo: {}ms | Args: {}", 
            auditavel.operacao(), 
            usuario, 
            duracao, 
            joinPoint.getArgs());
        
        String argumentos = Arrays.toString(joinPoint.getArgs());
        Operacao operacao = null;
        if(auditavel.operacao().contains("SALVAR")) {
        	operacao = Operacao.INCLUSAO;
        }
        if(auditavel.operacao().contains("ALTERAR")){
        	operacao = Operacao.ALTERACAO;
        }
        
        
        return result;
    }

	
}
