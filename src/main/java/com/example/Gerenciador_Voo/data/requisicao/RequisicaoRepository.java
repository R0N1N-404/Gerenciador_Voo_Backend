package com.example.Gerenciador_Voo.data.requisicao;

import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequisicaoRepository extends JpaRepository<Requisicao, Long> {

    // Buscar requisições por status
    List<Requisicao> findByStatus(StatusRequisicao status);

    // Buscar todas as requisições de um piloto
    List<Requisicao> findByPilotoUsuarioId(Long pilotoId);

    // Buscar requisições de um piloto por status
    List<Requisicao> findByPilotoUsuarioIdAndStatus(
            Long pilotoId,
            StatusRequisicao status
    );

    // Buscar requisições de um piloto, exceto determinado status
    List<Requisicao> findByPilotoUsuarioIdAndStatusNot(
            Long pilotoId,
            StatusRequisicao status
    );

    // Verificar se uma aeronave possui requisição com determinado status
    boolean existsByAeronaveIdAndStatus(
            Long aeronaveId,
            StatusRequisicao status
    );

    // Verificar se um piloto possui requisição com determinado status
    boolean existsByPilotoUsuarioIdAndStatus(
            Long pilotoId,
            StatusRequisicao status
    );

    // Verificar se uma pista possui requisição com determinado status
    boolean existsByPistaIdAndStatus(
            Long pistaId,
            StatusRequisicao status
    );

    boolean existsByAeronaveId(Long aeronaveId);
}