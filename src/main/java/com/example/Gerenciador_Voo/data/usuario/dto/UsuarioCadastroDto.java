package com.example.Gerenciador_Voo.data.usuario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastroDto {

    private String nome;

    private String login;

    private String senha;

    private TipoUsuario tipo;

    // Utilizado somente quando o tipo for PILOTO
    private String numeroLicenca;
    private String codigoOperador;
}