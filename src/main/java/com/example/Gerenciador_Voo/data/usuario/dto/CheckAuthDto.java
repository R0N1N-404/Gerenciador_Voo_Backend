package com.example.Gerenciador_Voo.data.usuario.dto;

public record CheckAuthDto(
        Long usuarioId,
        String nome,
        TipoUsuario tipo,
        boolean autenticado
) {}