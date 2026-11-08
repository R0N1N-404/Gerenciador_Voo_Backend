package com.example.Gerenciador_Voo.exceptions;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErroResponseDto {
    private final String mensagem;
    private final int status;
    private final LocalDateTime timestamp;

    public ErroResponseDto(String mensagem, int status) {
        this.mensagem = mensagem;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}