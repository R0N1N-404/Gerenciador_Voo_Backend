package com.example.Gerenciador_Voo.services;

import com.example.Gerenciador_Voo.data.pista.Pista;
import com.example.Gerenciador_Voo.data.pista.PistaRepository;
import com.example.Gerenciador_Voo.data.pista.PistaResponseDto;
import com.example.Gerenciador_Voo.data.pista.StatusPista;
import com.example.Gerenciador_Voo.data.requisicao.RequisicaoRepository;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoEnum.StatusRequisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PistaService {

    @Autowired
    private PistaRepository pistaRepository;

    @Autowired
    private RequisicaoRepository requisicaoRepository;

    public PistaResponseDto getStatus(Long pistaId){
        Pista pista = pistaRepository.findById(pistaId)
                .orElseThrow(() -> new RuntimeException("Pista não encontrada"));

        boolean ocupada = requisicaoRepository.existsByPistaIdAndStatus(pistaId, StatusRequisicao.AUTORIZADA);
        StatusPista status = ocupada ? StatusPista.OCUPADA : StatusPista.LIVRE;

        return new PistaResponseDto(pista.getId(), pista.getIdentificacao(), status);
    }

    public List<Pista> listarTodas(){
        return pistaRepository.findAll();
    }

    public PistaResponseDto getById(Long pistaId){
        Pista pista = pistaRepository.findById(pistaId).orElseThrow( () -> new RuntimeException("Pista não encontrada"));

        boolean ocupada = requisicaoRepository.existsByPistaIdAndStatus(pistaId, StatusRequisicao.AUTORIZADA);
        StatusPista status = ocupada ? StatusPista.OCUPADA : StatusPista.LIVRE;


        return new PistaResponseDto(pista.getId(), pista.getIdentificacao(), status);
    }
}