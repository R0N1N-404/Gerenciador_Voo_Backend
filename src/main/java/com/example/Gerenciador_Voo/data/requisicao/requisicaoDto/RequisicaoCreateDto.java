package com.example.Gerenciador_Voo.data.requisicao.requisicaoDto;

import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.PrioridadeRequisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.TipoOperacao;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequisicaoCreateDto {
    private Long aeronaveId;
    private TipoOperacao tipoOperacao;
    private PrioridadeRequisicao prioridade;
    private String observacao;
}
