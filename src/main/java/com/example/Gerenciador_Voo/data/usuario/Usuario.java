package com.example.Gerenciador_Voo.data.usuario;

import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name="usuario")
@Entity(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    private LocalDateTime criadoEm;


}
