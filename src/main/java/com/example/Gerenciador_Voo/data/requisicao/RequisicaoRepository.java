package com.example.Gerenciador_Voo.data.requisicao;

import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.RequisicaoResponseDto;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisicaoRepository extends JpaRepository<Requisicao, Long> {

    List<Requisicao> findByStatus(StatusRequisicao status);

    List<Requisicao> findByPilotoUsuarioId(Long pilotoId);

    boolean existsByPistaIdAndStatus(Long pistaId, StatusRequisicao status);
}

