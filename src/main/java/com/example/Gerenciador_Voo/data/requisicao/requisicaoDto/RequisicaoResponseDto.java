package com.example.Gerenciador_Voo.data.requisicao.requisicaoDto;

import com.example.Gerenciador_Voo.data.requisicao.Requisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.PrioridadeRequisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.TipoOperacao;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RequisicaoResponseDto {

    private final Long id;
    private final String pilotoNome;
    private final String aeronavePrefixo;
    private final TipoOperacao tipoOperacao;
    private final PrioridadeRequisicao prioridade;
    private final StatusRequisicao status;
    private final String observacao;
    private final LocalDateTime solicitadaEm;
    private final LocalDateTime autorizadaEm;
    private final LocalDateTime concluidaEm;

    public RequisicaoResponseDto(Requisicao r) {
        this.id = r.getId();
        this.pilotoNome = r.getPiloto().getUsuario().getNome();
        this.aeronavePrefixo = r.getAeronave().getPrefixo();
        this.tipoOperacao = r.getTipoOperacao();
        this.prioridade = r.getPrioridade();
        this.status = r.getStatus();
        this.observacao = r.getObservacao();
        this.solicitadaEm = r.getSolicitadaEm();
        this.autorizadaEm = r.getAutorizadaEm();
        this.concluidaEm = r.getConcluidaEm();
    }
}
