package com.example.Gerenciador_Voo.data.piloto.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PilotoCreateDto {

    private String nome;
    private String login;
    private String senha;
    private String numeroLicenca;

}
