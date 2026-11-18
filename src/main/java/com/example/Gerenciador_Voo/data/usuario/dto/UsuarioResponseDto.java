package com.example.Gerenciador_Voo.data.usuario.dto;

import com.example.Gerenciador_Voo.data.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDto {

    private Long id;
    private String nome;
    private String login;
    private TipoUsuario tipo;
    private String criadoEm;

    public UsuarioResponseDto(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.login = usuario.getLogin();
        this.tipo = usuario.getTipo();
        this.criadoEm = usuario.getCriadoEm().toString();
    }
}