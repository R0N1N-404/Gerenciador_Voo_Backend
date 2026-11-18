package com.example.Gerenciador_Voo.services;

import com.example.Gerenciador_Voo.data.aeronave.AeronaveRepository;
import com.example.Gerenciador_Voo.data.piloto.PilotoRepository;
import com.example.Gerenciador_Voo.data.pista.Pista;
import com.example.Gerenciador_Voo.data.pista.PistaRepository;
import com.example.Gerenciador_Voo.data.requisicao.*;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.RequisicaoResponseDto;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.RequisicaoCreateDto;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import com.example.Gerenciador_Voo.data.torre.TorreRepository;
import com.example.Gerenciador_Voo.exceptions.exceptions.RequisicaoInvalidaException;
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


    // CRIAR REQUISIÇÃO
    public Requisicao criar(Long pilotoId, RequisicaoCreateDto dto) {

        if (requisicaoRepository.existsByPilotoUsuarioIdAndStatus(
                pilotoId, StatusRequisicao.PENDENTE)) {
            throw new RequisicaoInvalidaException(
                    "Você já possui uma requisição pendente.");
        }

        if (requisicaoRepository.existsByAeronaveIdAndStatus(
                dto.getAeronaveId(), StatusRequisicao.PENDENTE)) {
            throw new RequisicaoInvalidaException(
                    "Esta aeronave já possui uma requisição pendente.");
        }

        Requisicao r = new Requisicao();

        r.setPiloto(pilotoRepository.findById(pilotoId)
                .orElseThrow(() -> new RequisicaoInvalidaException("Piloto não encontrado")));

        r.setAeronave(aeronaveRepository.findById(dto.getAeronaveId())
                .orElseThrow(() -> new RequisicaoInvalidaException("Aeronave não encontrada")));

        r.setTipoOperacao(dto.getTipoOperacao());

        Pista pista = pistaRepository.findById(1L)
                .orElseThrow(() -> new RequisicaoInvalidaException("Pista não encontrada"));

        r.setPista(pista);
        r.setPrioridade(dto.getPrioridade());
        r.setObservacao(dto.getObservacao());
        r.setStatus(StatusRequisicao.PENDENTE);
        r.setSolicitadaEm(LocalDateTime.now());

        return requisicaoRepository.save(r);
    }


    // LISTAR REQUISIÇÕES PENDENTES DA TORRE
    public List<Requisicao> listarPendentes() {
        return requisicaoRepository.findByStatus(StatusRequisicao.PENDENTE);
    }


    // LISTAR TODAS AS REQUISIÇÕES DO PILOTO, EXCETO CANCELADAS
    public List<RequisicaoResponseDto> listarPorPiloto(Long pilotoId) {

        return requisicaoRepository
                .findByPilotoUsuarioIdAndStatusNot(
                        pilotoId, StatusRequisicao.CANCELADA)
                .stream()
                .map(RequisicaoResponseDto::new)
                .toList();
    }


    // LISTAR TODAS AS REQUISIÇÕES DA TORRE
    public List<RequisicaoResponseDto> getAll() {

        return requisicaoRepository.findAll()
                .stream()
                .map(RequisicaoResponseDto::new)
                .toList();
    }


    // AUTORIZAR REQUISIÇÃO
    public Requisicao autorizar(Long requisicaoId, Long torreId) {

        Requisicao r = buscarOuFalhar(requisicaoId);

        if (r.getStatus() != StatusRequisicao.PENDENTE) {
            throw new RequisicaoInvalidaException(
                    "Só é possível autorizar requisições PENDENTES");
        }

        r.setTorre(torreRepository.findById(torreId)
                .orElseThrow(() -> new RequisicaoInvalidaException("Torre não encontrada")));

        r.setStatus(StatusRequisicao.AUTORIZADA);
        r.setAutorizadaEm(LocalDateTime.now());

        return requisicaoRepository.save(r);
    }


    // RECUSAR REQUISIÇÃO
    public Requisicao recusar(Long requisicaoId, String motivo) {

        Requisicao r = buscarOuFalhar(requisicaoId);

        if (r.getStatus() != StatusRequisicao.PENDENTE) {
            throw new RequisicaoInvalidaException(
                    "Só é possível recusar requisições PENDENTES");
        }

        r.setStatus(StatusRequisicao.RECUSADA);
        r.setObservacao(motivo);
        r.setRecusadaEm(LocalDateTime.now());

        return requisicaoRepository.save(r);
    }


    // CONCLUIR REQUISIÇÃO
    public Requisicao concluir(Long requisicaoId) {

        Requisicao r = buscarOuFalhar(requisicaoId);

        if (r.getStatus() != StatusRequisicao.AUTORIZADA) {
            throw new RequisicaoInvalidaException(
                    "Só é possível concluir requisições AUTORIZADAS");
        }

        r.setStatus(StatusRequisicao.CONCLUIDA);
        r.setConcluidaEm(LocalDateTime.now());

        return requisicaoRepository.save(r);
    }


    // CANCELAR REQUISIÇÃO
    public Requisicao cancelar(Long requisicaoId) {

        Requisicao r = buscarOuFalhar(requisicaoId);

        if (r.getStatus() != StatusRequisicao.PENDENTE) {
            throw new RequisicaoInvalidaException(
                    "Só é possível cancelar requisições PENDENTES");
        }

        r.setStatus(StatusRequisicao.CANCELADA);
        r.setCanceladaEm(LocalDateTime.now());

        return requisicaoRepository.save(r);
    }


    // LISTAR SOMENTE AS PENDENTES DE UM PILOTO
    public List<RequisicaoResponseDto> listarPendentesPorPiloto(Long pilotoId) {

        return requisicaoRepository
                .findByPilotoUsuarioIdAndStatus(
                        pilotoId, StatusRequisicao.PENDENTE)
                .stream()
                .map(RequisicaoResponseDto::new)
                .toList();
    }


    // BUSCAR REQUISIÇÃO PELO ID
    private Requisicao buscarOuFalhar(Long id) {

        return requisicaoRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException(
                        "Requisição não encontrada"));
    }
}