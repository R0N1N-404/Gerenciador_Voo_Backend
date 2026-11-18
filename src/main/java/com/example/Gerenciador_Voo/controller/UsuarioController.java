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
import com.example.Gerenciador_Voo.services.VerificaToken;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PilotoRepository pilotoRepository;
    @Autowired
    private TorreRepository torreRepository;

    private final VerificaToken verificaToken;

    @Autowired
    private SessaoService sessaoService;

    public UsuarioController(
            PilotoRepository pilotoRepository,
            UsuarioRepository usuarioRepository,
            VerificaToken verificaToken) {

        this.pilotoRepository = pilotoRepository;
        this.usuarioRepository = usuarioRepository;
        this.verificaToken = verificaToken;
    }

    // READ - listar todos
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(@RequestHeader("Authorization") String token){

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        return ResponseEntity.ok(usuarioRepository.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."
                ));

        return ResponseEntity.ok(new UsuarioResponseDto(usuario));
    }

    @GetMapping("/proprio/")
    public ResponseEntity<UsuarioResponseDto> getByIdProprio(
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.PILOTO);

        CheckAuthDto user = sessaoService.getUsuarioId(token);

        Usuario usuario = usuarioRepository.findById(user.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."
                ));

        return ResponseEntity.ok(new UsuarioResponseDto(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        return usuarioRepository.findByLogin(request.getLogin())
                .filter(usuario -> usuario.getSenha().equals(request.getSenha()))
                .map(usuario -> {
                    String token = sessaoService.criarSessao(usuario.getId());
                    return ResponseEntity.ok(
                            new LoginResponse(usuario.getNome(), usuario.getTipo(), usuario.getCriadoEm(), token)
                    );
                })
                .orElseThrow(() -> new CredenciaisInvalidasException("Login ou senha inválidos"));
    }

    @PostMapping("/checkauth")
    public ResponseEntity<CheckAuthDto> checkAuth(
            @RequestHeader("Authorization") String token) {

        CheckAuthDto checked = sessaoService.getUsuarioId(token);

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

        Usuario salvo = usuarioRepository.save(usuario);

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
        return usuarioRepository.findById(id)
                .map(existente -> {
                    existente.setNome(dados.getNome());
                    existente.setLogin(dados.getLogin());
                    existente.setSenha(dados.getSenha());
                    existente.setTipo(dados.getTipo());
                    Usuario atualizado = usuarioRepository.save(existente);
                    return ResponseEntity.ok(atualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logoff")
    public ResponseEntity<Void> logoff(
            @RequestHeader("Authorization") String token) {

        sessaoService.encerrarSessao(token);

        return ResponseEntity.noContent().build();
    }


}


