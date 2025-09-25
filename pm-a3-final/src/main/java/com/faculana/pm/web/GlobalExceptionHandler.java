package com.faculana.pm.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Converte erros comuns em respostas HTTP amigáveis. */
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String,String> notFound(NotFoundException ex){ return Map.of("error", ex.getMessage()); }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String,String> validation(MethodArgumentNotValidException ex){
    var f = ex.getBindingResult().getFieldError();
    return Map.of("error", "Campo inválido: " + (f!=null? f.getField() : "desconhecido"));
  }

  // Fallback genérico para erros inesperados (mensagem humana curta)
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String,String> generic(Exception ex){
    return Map.of("error","Erro inesperado");
  }
}
