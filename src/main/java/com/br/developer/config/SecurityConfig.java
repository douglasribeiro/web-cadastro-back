package com.br.developer.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Configuração de CORS integrada ao Security
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 2. Desabilitar CSRF (comum em APIs REST com Token)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permite o Preflight (OPTIONS) sem autenticação
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}


//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity // Permite usar @PreAuthorize("hasRole('admin')")
//public class SecurityConfig {
//
//	 @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .cors(cors -> cors.configurationSource(request -> {
//	                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
//	                corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
//	                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//	                corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
//	                corsConfiguration.setAllowCredentials(true);
//	                return corsConfiguration;
//	            }))
//	            .csrf(csrf -> csrf.disable()) // Geralmente desabilitado para APIs stateless
//	            .authorizeHttpRequests(auth -> auth
//	                .anyRequest().authenticated()
//	            )
//	            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
//
//	        return http.build();
//	    }
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Ativa o CORS
////            .csrf(csrf -> csrf.disable()) // Desabilitado para APIs Stateless
////            .authorizeHttpRequests(auth -> auth
////            	.requestMatchers("/api/genero/**").hasRole("admin")
////                .requestMatchers("/public/**").permitAll() // Endpoints abertos
////                .anyRequest().authenticated()              // Tudo o resto exige token
////            )
////            .oauth2ResourceServer(oauth2 -> oauth2
////                .jwt(jwt -> jwt.jwtAuthenticationConverter(new KeycloakRoleConverter()))
////            );
////
////        return http.build();
////    }
//    
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        
//        // Defina as URLs permitidas (onde seu Front-end está rodando)
//        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200"));
//        
//        // Métodos HTTP permitidos
//        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        
//        // Headers permitidos (Obrigatório ter Authorization para o Token)
//        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
//        
//        // Permite o envio de cookies/autenticação se necessário
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration); // Aplica a todas as rotas
//        return source;
//    }
//}
