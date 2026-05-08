package com.br.developer.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // O mesmo caminho que você definiu no Service (ex: C:/uploads/musicas/)
    @Value("${app.upload.dir:uploads/musicas}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Define o prefixo da URL
        String pathPattern = "/media/**";
        
        // Converte o caminho do Windows/Linux para o formato de recurso do Spring
        String location = "file:" + Paths.get(uploadDir).toAbsolutePath().toString() + "/";

        registry.addResourceHandler(pathPattern)
                .addResourceLocations(location)
                .setCachePeriod(3600) // Opcional: cache de 1 hora
                .resourceChain(true);
    }
}