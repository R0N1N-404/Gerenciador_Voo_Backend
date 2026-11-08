package com.example.Gerenciador_Voo.data.piloto;


import com.example.Gerenciador_Voo.data.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "piloto")
public class Piloto {

    @Id
    private Long usuarioId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "numero_licenca", nullable = false, unique = true)
    private String numeroLicenca;
}


