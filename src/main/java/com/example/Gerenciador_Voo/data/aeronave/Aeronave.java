package com.example.Gerenciador_Voo.data.aeronave;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Table(name="aeronave")
@Entity(name="aeronave")
public class Aeronave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String prefixo;
    private String modelo;
    private String companhia;


}


