package com.example.Gerenciador_Voo.data.pista;

import lombok.Getter;

@Getter
public class PistaResponseDto {
    private final Long id;
    private final String identificacao;
    private final StatusPista status;

    public PistaResponseDto(Long id, String identificacao, StatusPista status){
        this.id = id;
        this.identificacao = identificacao;
        this.status = status;
    }
}
