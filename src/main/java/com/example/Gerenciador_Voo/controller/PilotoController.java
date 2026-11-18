package com.example.Gerenciador_Voo.controller;

import com.example.Gerenciador_Voo.data.piloto.Piloto;
import com.example.Gerenciador_Voo.data.piloto.PilotoRepository;
import com.example.Gerenciador_Voo.data.piloto.dto.PilotoCreateDto;
import com.example.Gerenciador_Voo.data.piloto.dto.PilotoResponseDto;
import com.example.Gerenciador_Voo.data.usuario.Usuario;
import com.example.Gerenciador_Voo.data.usuario.UsuarioRepository;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoAcesso;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import com.example.Gerenciador_Voo.services.VerificaToken;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pilotos")
public class PilotoController {

    private final PilotoRepository pilotoRepository;
    private final UsuarioRepository usuarioRepository;
    private final VerificaToken verificaToken;

    public PilotoController(
            PilotoRepository pilotoRepository,
            UsuarioRepository usuarioRepository,
            VerificaToken verificaToken) {

        this.pilotoRepository = pilotoRepository;
        this.usuarioRepository = usuarioRepository;
        this.verificaToken = verificaToken;
    }

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<PilotoResponseDto>> getAll(
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        List<PilotoResponseDto> pilotos = pilotoRepository.findAll()
                .stream()
                .map(PilotoResponseDto::new)
                .toList();

        return ResponseEntity.ok(pilotos);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PilotoResponseDto> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        Piloto piloto = pilotoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Piloto não encontrado."
                ));

        return ResponseEntity.ok(new PilotoResponseDto(piloto));
    }

    // CADASTRAR
    @PostMapping
    @Transactional
    public ResponseEntity<PilotoResponseDto> create(
            @RequestBody PilotoCreateDto dto,
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(TipoUsuario.PILOTO);
        usuario.setCriadoEm(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        Piloto piloto = new Piloto();

        piloto.setUsuario(usuario);
        piloto.setNumeroLicenca(dto.getNumeroLicenca());

        piloto = pilotoRepository.save(piloto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new PilotoResponseDto(piloto));
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PilotoResponseDto> update(
            @PathVariable Long id,
            @RequestBody PilotoCreateDto dto,
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        Piloto piloto = pilotoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Piloto não encontrado."
                ));

        Usuario usuario = piloto.getUsuario();

        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());

        // Atualiza a senha somente se uma nova senha foi informada
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(dto.getSenha());
        }

        piloto.setNumeroLicenca(dto.getNumeroLicenca());

        usuarioRepository.save(usuario);
        piloto = pilotoRepository.save(piloto);

        return ResponseEntity.ok(new PilotoResponseDto(piloto));
    }

    // EXCLUIR
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        Piloto piloto = pilotoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Piloto não encontrado."
                ));

        Usuario usuario = piloto.getUsuario();

        // Exclui primeiro o piloto por causa da chave estrangeira
        pilotoRepository.delete(piloto);

        // Depois exclui o usuário associado
        usuarioRepository.delete(usuario);

        return ResponseEntity.noContent().build();
    }
}