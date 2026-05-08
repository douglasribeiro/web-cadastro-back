package com.br.developer.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.br.developer.generics.ApiResponse;
import com.br.developer.model.ApiError;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(DataIntegrityViolationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("message", "Erro de Integridade");
        body.put("errors", List.of("Não foi possível completar a operação: este registro ja existe."));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
	
	@ExceptionHandler(Exception.class) // Captura tudo para testar
	public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("errors", List.of("Erro disparado pelo Java: " + ex.getMessage()));
	    return ResponseEntity.status(500).body(body);
	}

    // Trata especificamente a sua exceção customizada
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleRecordNotFound(
            RecordNotFoundException ex, HttpServletRequest request) {
        //HttpStatusCode meuStatus = HttpStatusCode.valueOf(488);
    	ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message("Erro de pesquisa")
                .errors(List.of("O registro não foi encontrado."))
                .build();
                
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
            
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ObjectOptimisticLockingFailureException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .message("Erro de Concorrência")
                .errors(List.of("O registro foi alterado por outro processo. Recarregue os dados."))
                .build();
                
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extrai as mensagens de erro das anotações (ex: @NotBlank)
        var erros = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(erros);
    }
    
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotWritableException.class)
    public ResponseEntity<ApiResponse<Void>> handleSerializationError(Exception ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Erro de Serialização")
                .errors(List.of("O sistema não conseguiu processar um valor numérico grande (BigInt). Verifique a conversão para String no Frontend."))
                .build();

        return ResponseEntity.internalServerError().body(response);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(
    		RuntimeException ex,
    		HttpServletRequest request){
    	ApiError error = new ApiError(
    			HttpStatus.BAD_REQUEST.value(), 
    			"Bad Request", 
    			ex.getMessage(), 
    			request.getRequestURI(),
    			null);
    			
		return ResponseEntity.badRequest().body(error);
    	
    }
    
}