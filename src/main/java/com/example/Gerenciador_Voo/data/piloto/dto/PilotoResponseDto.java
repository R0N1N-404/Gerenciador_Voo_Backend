package com.example.Gerenciador_Voo.data.piloto.dto;

import com.example.Gerenciador_Voo.data.piloto.Piloto;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PilotoResponseDto {

    private Long id;
    private String nome;
    private String login;
    private TipoUsuario tipoUsuario;
    private String numeroLicenca;
    private String criado_em;

    public PilotoResponseDto(Piloto piloto) {

        this.id = piloto.getUsuario().getId();
        this.nome = piloto.getUsuario().getNome();
        this.login = piloto.getUsuario().getLogin();
        this.tipoUsuario = piloto.getUsuario().getTipo();
        this.numeroLicenca = piloto.getNumeroLicenca();
        this.criado_em = piloto.getUsuario().getCriadoEm().toString();
    }
}