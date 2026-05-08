package com.br.developer.controller;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.developer.dto.GeneroDto;
import com.br.developer.generics.BaseController;
import com.br.developer.service.GeneroService;

import lombok.RequiredArgsConstructor;

@PreAuthorize("hasRole('admin')")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/genero")
@RequiredArgsConstructor
public class GeneroController extends BaseController<GeneroDto,	GeneroService>{
	
	@GetMapping("/check")
	public Map<String, Object> check(Authentication auth) {
	    return Map.of(
	        "username", auth.getName(),
	        "roles", auth.getAuthorities()
	    );
	}
	
	@GetMapping("/test")
    public String test(Authentication authentication) {
        return "Usuário: " + authentication.getName() + " | Permissões: " + authentication.getAuthorities();
    }
	
	@GetMapping("/perfil")
	public Map<String, Object> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
	    return Map.of(
	        "id", jwt.getSubject(),
	        "username", jwt.getClaimAsString("preferred_username"),
	        "email", jwt.getClaimAsString("email"),
	        "nome", jwt.getClaimAsString("name")
	    );
	}

}
