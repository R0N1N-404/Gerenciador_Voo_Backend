package com.example.Gerenciador_Voo.data.torre;

import com.example.Gerenciador_Voo.data.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="torre")
public class Torre {
    @Id
    private Long usuarioId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name="codigo_operador", nullable = false, unique = true)
    private String codigoOperador;
}


