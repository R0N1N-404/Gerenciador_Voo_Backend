package com.example.Gerenciador_Voo.services;

import com.example.Gerenciador_Voo.data.aeronave.AeronaveRepository;
import com.example.Gerenciador_Voo.data.piloto.PilotoRepository;
import com.example.Gerenciador_Voo.data.pista.PistaRepository;
import com.example.Gerenciador_Voo.data.requisicao.*;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.RequisicaoResponseDto;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import com.example.Gerenciador_Voo.data.torre.TorreRepository;
import com.example.Gerenciador_Voo.exceptions.exceptions.PistaOcupadaException;
import com.example.Gerenciador_Voo.exceptions.exceptions.RequisicaoInvalidaException;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.RequisicaoCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequisicaoService {

    @Autowired private RequisicaoRepository requisicaoRepository;
    @Autowired private PilotoRepository pilotoRepository;
    @Autowired private AeronaveRepository aeronaveRepository;
    @Autowired private PistaRepository pistaRepository;
    @Autowired private TorreRepository torreRepository;

    public Requisicao criar(Long pilotoId, RequisicaoCreateDto dto) {
        Requisicao r = new Requisicao();

        r.setPiloto(pilotoRepository.findById(pilotoId)
                .orElseThrow(() -> new RequisicaoInvalidaException("Piloto não encontrado")));

        r.setAeronave(aeronaveRepository.findById(dto.getAeronaveId())
                .orElseThrow(() -> new RequisicaoInvalidaException("Aeronave não encontrada")));

        r.setTipoOperacao(dto.getTipoOperacao());
        r.setPrioridade(dto.getPrioridade());
        r.setObservacao(dto.getObservacao());
        r.setStatus(StatusRequisicao.PENDENTE);
        r.setSolicitadaEm(LocalDateTime.now());

        return requisicaoRepository.save(r);
    }

    public List<Requisicao> listarPendentes() {
        return requisicaoRepository.findByStatus(StatusRequisicao.PENDENTE);
    }

    public List<RequisicaoResponseDto> listarPorPiloto(Long pilotoId) {

        System.out.println("1 - Entrou no listarPorPiloto");
        System.out.println("2 - ID recebido: " + pilotoId);

        List<Requisicao> requisicoes =
                requisicaoRepository.findByPilotoUsuarioId(pilotoId);

        System.out.println("3 - Consulta executada!");
        System.out.println("4 - Registros encontrados: " + requisicoes.size());

        List<RequisicaoResponseDto> resultado = requisicoes.stream()
                .map(RequisicaoResponseDto::new)
                .toList();

        System.out.println("5 - DTOs construídos!");

        return resultado;
    }

    public Requisicao autorizar(Long requisicaoId, Long torreId, Long pistaId) {
        Requisicao r = buscarOuFalhar(requisicaoId);

        if (r.getStatus() != StatusRequisicao.PENDENTE) {
            throw new RequisicaoInvalidaException("Só é possível autorizar requisições PENDENTES");
        }

        boolean pistaOcupada = requisicaoRepository.existsByPistaIdAndStatus(pistaId, StatusRequisicao.AUTORIZADA);
        if (pistaOcupada) {
            throw new PistaOcupadaException("A pista já está ocupada por outra operação");
        }

        r.setPista(pistaRepository.findById(pistaId)
                .orElseThrow(() -> new RequisicaoInvalidaException("Pista não encontrada")));
        r.setTorre(torreRepository.findById(torreId)
                .orElseThrow(() -> new RequisicaoInvalidaException("Torre não encontrada")));
        r.setStatus(StatusRequisicao.AUTORIZADA);
        r.setAutorizadaEm(LocalDateTime.now());
        return requisicaoRepository.save(r);
    }

    public Requisicao recusar(Long requisicaoId, String motivo) {
        Requisicao r = buscarOuFalhar(requisicaoId);
        if (r.getStatus() != StatusRequisicao.PENDENTE) {
            throw new RequisicaoInvalidaException("Só é possível recusar requisições PENDENTES");
        }
        r.setStatus(StatusRequisicao.RECUSADA);
        r.setObservacao(motivo);
        r.setRecusadaEm(LocalDateTime.now());
        return requisicaoRepository.save(r);
    }

    public Requisicao concluir(Long requisicaoId) {
        Requisicao r = buscarOuFalhar(requisicaoId);
        if (r.getStatus() != StatusRequisicao.AUTORIZADA) {
            throw new RequisicaoInvalidaException("Só é possível concluir requisições AUTORIZADAS");
        }
        r.setStatus(StatusRequisicao.CONCLUIDA);
        r.setConcluidaEm(LocalDateTime.now());
        return requisicaoRepository.save(r);
        // a pista fica livre automaticamente
    }

    public Requisicao cancelar(Long requisicaoId) {
        Requisicao r = buscarOuFalhar(requisicaoId);
        if (r.getStatus() != StatusRequisicao.PENDENTE) {
            throw new RequisicaoInvalidaException("Só é possível cancelar requisições PENDENTES");
        }
        r.setStatus(StatusRequisicao.CANCELADA);
        r.setCanceladaEm(LocalDateTime.now());
        return requisicaoRepository.save(r);
    }

    private Requisicao buscarOuFalhar(Long id) {
        return requisicaoRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Requisição não encontrada"));
    }
}