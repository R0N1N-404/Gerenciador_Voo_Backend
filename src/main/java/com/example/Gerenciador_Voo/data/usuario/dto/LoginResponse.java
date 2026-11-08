package com.example.Gerenciador_Voo.data.usuario.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LoginResponse {

    private final String nome;
    private final TipoUsuario tipo;
    private final LocalDateTime criado_em;
    private final String token;

    public LoginResponse(String nome, TipoUsuario tipo, LocalDateTime criado_em, String token) {
        this.nome = nome;
        this.tipo = tipo;
        this.criado_em = criado_em;
        this.token = token;
    }

}
