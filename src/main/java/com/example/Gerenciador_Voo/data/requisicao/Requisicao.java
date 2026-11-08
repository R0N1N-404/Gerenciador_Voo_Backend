package com.example.Gerenciador_Voo.data.requisicao;


import com.example.Gerenciador_Voo.data.aeronave.Aeronave;
import com.example.Gerenciador_Voo.data.piloto.Piloto;
import com.example.Gerenciador_Voo.data.pista.Pista;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.PrioridadeRequisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.TipoOperacao;
import com.example.Gerenciador_Voo.data.torre.Torre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="requisicao")
public class Requisicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piloto_id", nullable = false)
    private Piloto piloto;

    @ManyToOne
    @JoinColumn(name = "aeronave_id", nullable = false)
    private Aeronave aeronave;

    @ManyToOne
    @JoinColumn(name = "pista_id")
    private Pista pista;

    @ManyToOne
    @JoinColumn(name = "torre_id")
    private Torre torre;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_operacao", nullable = false)
    private TipoOperacao tipoOperacao;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "prioridade", nullable = false)
    private PrioridadeRequisicao prioridade = PrioridadeRequisicao.NORMAL;

    @Enumerated
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private StatusRequisicao status = StatusRequisicao.PENDENTE;

    private String observacao;

    @Column(name = "solicitada_em", nullable = false)
    private LocalDateTime solicitadaEm;

    @Column(name = "autorizada_em")
    private LocalDateTime autorizadaEm;

    @Column(name = "concluida_em")
    private LocalDateTime concluidaEm;

    @Column(name = "cancelada_em")
    private LocalDateTime canceladaEm;

    @Column(name = "recusada_em")
    private LocalDateTime recusadaEm;

}
