package com.example.Gerenciador_Voo.controller;

import com.example.Gerenciador_Voo.data.piloto.Piloto;
import com.example.Gerenciador_Voo.data.piloto.PilotoRepository;
import com.example.Gerenciador_Voo.data.torre.Torre;
import com.example.Gerenciador_Voo.data.torre.TorreRepository;
import com.example.Gerenciador_Voo.data.usuario.Usuario;
import com.example.Gerenciador_Voo.data.usuario.UsuarioRepository;
import com.example.Gerenciador_Voo.data.usuario.dto.*;
import com.example.Gerenciador_Voo.exceptions.exceptions.CredenciaisInvalidasException;
import com.example.Gerenciador_Voo.services.SessaoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PilotoRepository pilotoRepository;
    @Autowired
    private TorreRepository torreRepository;

    @Autowired
    private SessaoService sessao;

    // READ - listar todos
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    // READ - buscar um pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        return repository.findByLogin(request.getLogin())
                .filter(usuario -> usuario.getSenha().equals(request.getSenha()))
                .map(usuario -> {
                    String token = sessao.criarSessao(usuario.getId());
                    return ResponseEntity.ok(
                            new LoginResponse(usuario.getNome(), usuario.getTipo(), usuario.getCriadoEm(), token)
                    );
                })
                .orElseThrow(() -> new CredenciaisInvalidasException("Login ou senha inválidos"));
    }

    @PostMapping("/checkauth")
    public ResponseEntity<CheckAuthDto> checkAuth(
            @RequestHeader("Authorization") String token) {

        CheckAuthDto checked = sessao.getUsuarioId(token);

        return ResponseEntity.ok(checked);
    }

    // CREATE
    @PostMapping
    @Transactional
    public ResponseEntity<Usuario> create(@RequestBody UsuarioCadastroDto dto) {

        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(dto.getTipo());
        usuario.setCriadoEm(LocalDateTime.now());

        Usuario salvo = repository.save(usuario);

        if (dto.getTipo() == TipoUsuario.PILOTO) {

            Piloto piloto = new Piloto();
            piloto.setUsuario(salvo);
            piloto.setNumeroLicenca(dto.getNumeroLicenca());

            pilotoRepository.save(piloto);

        } else if (dto.getTipo() == TipoUsuario.TORRE) {

            Torre torre = new Torre();
            torre.setUsuario(salvo);
            torre.setCodigoOperador(dto.getCodigoOperador());

            torreRepository.save(torre);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario dados){
        return repository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setLogin(dados.getLogin());
                    existente.setSenha(dados.getSenha());
                    existente.setTipo(dados.getTipo());
                    Usuario atualizado = repository.save(existente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}