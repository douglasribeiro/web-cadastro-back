package com.br.developer.generics;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data; // Aqui entra seu DTO (ProdutoDTO, UsuarioDTO, etc)
    private List<String> errors;

    // Helper para resposta de sucesso rápido
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }
    
    public static <T> ApiResponse<T> delete(T data, String message) {
    	HttpStatusCode meuStatus = HttpStatusCode.valueOf(288);
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(meuStatus.value())
                .message(message)
                .data(data)
                .build();
    }
}