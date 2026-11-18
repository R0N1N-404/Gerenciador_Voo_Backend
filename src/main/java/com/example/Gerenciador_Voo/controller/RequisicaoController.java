package com.example.Gerenciador_Voo.controller;

import com.example.Gerenciador_Voo.data.requisicao.Requisicao;
import com.example.Gerenciador_Voo.data.requisicao.requisicaoDto.*;
import com.example.Gerenciador_Voo.data.usuario.dto.CheckAuthDto;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoAcesso;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import com.example.Gerenciador_Voo.exceptions.exceptions.AcessoNegadoException;
import com.example.Gerenciador_Voo.services.RequisicaoService;
import com.example.Gerenciador_Voo.services.SessaoService;
import com.example.Gerenciador_Voo.services.VerificaToken;
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

    private final VerificaToken verificaToken;

    public RequisicaoController(VerificaToken verificaToken){
        this.verificaToken = verificaToken;
    }


    @PostMapping
    public ResponseEntity<RequisicaoResponseDto> criar(
        @RequestHeader("Authorization") String token,
        @RequestBody RequisicaoCreateDto dto) {

        verificaToken.verificaToken(token, TipoAcesso.PILOTO);
        CheckAuthDto usuario = sessaoService.getUsuarioId(token);
        Requisicao r = requisicaoService.criar(usuario.usuarioId(), dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RequisicaoResponseDto(r));
    }

    @GetMapping("/pendentes")
    public List<RequisicaoResponseDto> pendentes(@RequestHeader("Authorization") String token) {
        verificaToken.verificaToken(token, TipoAcesso.TORRE);
        return requisicaoService.listarPendentes().stream()
                .map(RequisicaoResponseDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<RequisicaoResponseDto>> getRequisicoesByToken(
            @RequestHeader("Authorization") String token
    ) {
        verificaToken.verificaToken(token, TipoAcesso.PILOTO);

        CheckAuthDto usuario = sessaoService.getUsuarioId(token);

        return ResponseEntity.ok(
                requisicaoService.listarPorPiloto(usuario.usuarioId())
        );
    }

    @GetMapping("/")
    public ResponseEntity<List<RequisicaoResponseDto>> getRequisicoes(@RequestHeader("Authorization") String token){

        verificaToken.verificaToken(token, TipoAcesso.TORRE);
        List<RequisicaoResponseDto> req = requisicaoService.getAll();

        return ResponseEntity.ok(req);

    }

    @PostMapping("/{id}/autorizar")
    public ResponseEntity<RequisicaoResponseDto> autorizar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody AutorizarDto dto) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);
        CheckAuthDto torreId = sessaoService.getUsuarioId(token);

        System.out.println("ID Requisição: " + id);
        System.out.println("ID Torre: " + torreId.usuarioId());


        Requisicao r = requisicaoService.autorizar(id, torreId.usuarioId());
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }

    @PostMapping("/{id}/recusar")
    public ResponseEntity<RequisicaoResponseDto> recusar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token,
            @RequestBody RecusarDto dto) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);
        Requisicao r = requisicaoService.recusar(id, dto.getMotivo());
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<RequisicaoResponseDto> concluir(@PathVariable Long id
        , @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);
        Requisicao r = requisicaoService.concluir(id);
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }

    @GetMapping("/{id}/cancelar")
    public ResponseEntity<RequisicaoResponseDto> cancelar(@PathVariable Long id
                                                          ,@RequestHeader("Authorization") String token) {
        verificaToken.verificaToken(token, TipoAcesso.AMBOS);
        Requisicao r = requisicaoService.cancelar(id);
        return ResponseEntity.ok(new RequisicaoResponseDto(r));
    }
}