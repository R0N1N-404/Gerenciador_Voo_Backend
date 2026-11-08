package com.example.Gerenciador_Voo.exceptions;

import com.example.Gerenciador_Voo.exceptions.exceptions.CredenciaisInvalidasException;
import com.example.Gerenciador_Voo.exceptions.exceptions.PistaOcupadaException;
import com.example.Gerenciador_Voo.exceptions.exceptions.RequisicaoInvalidaException;
import com.example.Gerenciador_Voo.exceptions.exceptions.SessaoInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PistaOcupadaException.class)
    public ResponseEntity<ErroResponseDto> handlePistaOcupada(PistaOcupadaException e) {
        ErroResponseDto erro = new ErroResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro); // 409
    }

    @ExceptionHandler(RequisicaoInvalidaException.class)
    public ResponseEntity<ErroResponseDto> handleRequisicaoInvalida(RequisicaoInvalidaException e) {
        ErroResponseDto erro = new ErroResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro); // 400
    }

    @ExceptionHandler(SessaoInvalidaException.class)
    public ResponseEntity<ErroResponseDto> handleSessaoInvalida(SessaoInvalidaException e) {
        ErroResponseDto erro = new ErroResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro); // 401
    }

    // Rede de segurança: qualquer outra exceção não prevista cai aqui,
    // em vez de vazar stacktrace pro cliente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> handleGenerica(Exception e) {
        ErroResponseDto erro = new ErroResponseDto("Erro interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro); // 500
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroResponseDto> handleCredenciaisInvalidas(CredenciaisInvalidasException e) {
        ErroResponseDto erro = new ErroResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro); // 401
    }
}