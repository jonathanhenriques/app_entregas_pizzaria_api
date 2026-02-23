package com.diadema.app_entrega_pizza_api.api.exceptionhandler;


import com.diadema.app_entrega_pizza_api.domain.exception.RegraNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> tratarEntidadeNaoEncontrada(EntityNotFoundException ex) {
        return montarErro(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<?> tratarRegraNegocio(RegraNegocioException ex) {
        return montarErro(HttpStatus.BAD_REQUEST, "Violação de Regra de Negócio", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                erros.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(erros);
    }

    private ResponseEntity<Map<String, Object>> montarErro(HttpStatus status, String erro, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", erro);
        body.put("message", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}