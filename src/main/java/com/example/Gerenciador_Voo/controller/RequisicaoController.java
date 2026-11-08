package com.example.Gerenciador_Voo.controller;

import com.example.Gerenciador_Voo.data.requisicao.Requisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.*;
import com.example.Gerenciador_Voo.data.usuario.dto.CheckAuthDto;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import com.example.Gerenciador_Voo.exceptions.exceptions.AcessoNegadoException;
import com.example.Gerenciador_Voo.services.RequisicaoService;
import com.example.Gerenciador_Voo.services.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/requisicoes")
public class RequisicaoController {

    @Autowired private RequisicaoService requisicaoService;
    @Autowired private SessaoService sessaoService;

    @PostMapping
    public ResponseEntity<RequisicaoResponseDto> criar(
        @RequestHeader("Authorization") String token,
        @RequestBody RequisicaoCreateDto dto) {

        CheckAuthDto usuario = sessaoService.getUsuarioId(token);

        if (usuario.tipo() != TipoUsuario.PILOTO) {
            throw new AcessoNegadoException(
                    "Apenas pilotos podem consultar suas requisições."
            );
        }

        Requisicao r = requisicaoService.criar(usuario.usuarioId(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RequisicaoResponseDto(r));
    }

    @GetMapping("/pendentes")
    public List<RequisicaoResponseDto> pendentes() {
        return requisicaoService.listarPendentes().stream()
                .map(RequisicaoResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<RequisicaoResponseDto>> getRequisicoesByToken(
            @RequestHeader("Authorization") String token
    ) {

        CheckAuthDto usuario = sessaoService.getUsuarioId(token);

        if (usuario.tipo() != TipoUsuario.PILOTO) {
            throw new AcessoNegadoException(
                    "Apenas pilotos podem consultar suas requisições."
            );
        }

        List<RequisicaoResponseDto> requisicoes =
                requisicaoService.listarPorPiloto(usuario.usuarioId());

        return ResponseEntity.ok(requisicoes);
    }

    @PostMapping("/{id}/autorizar")
    public ResponseEntity<RequisicaoResponseDto> autorizar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody AutorizarDto dto) {
        CheckAuthDto torreId = sessaoService.getUsuarioId(token);
        Requisicao r = requisicaoService.autorizar(id, torreId.usuarioId(), dto.getPistaId());
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }

    @PostMapping("/{id}/recusar")
    public ResponseEntity<RequisicaoResponseDto> recusar(
            @PathVariable Long id,
            @RequestBody RecusarDto dto) {
        Requisicao r = requisicaoService.recusar(id, dto.getMotivo());
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<RequisicaoResponseDto> concluir(@PathVariable Long id) {
        Requisicao r = requisicaoService.concluir(id);
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<RequisicaoResponseDto> cancelar(@PathVariable Long id) {
        Requisicao r = requisicaoService.cancelar(id);
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }
}