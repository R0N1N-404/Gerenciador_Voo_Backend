package com.example.Gerenciador_Voo.controller;


import com.example.Gerenciador_Voo.data.aeronave.Aeronave;
import com.example.Gerenciador_Voo.data.aeronave.AeronaveRepository;
import com.example.Gerenciador_Voo.data.usuario.dto.CheckAuthDto;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import com.example.Gerenciador_Voo.exceptions.exceptions.AcessoNegadoException;
import com.example.Gerenciador_Voo.services.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aeronave")
public class AeronaveController {
    @Autowired
    private AeronaveRepository repository;

    @Autowired
    private SessaoService sessaoService;

    @GetMapping
    public ResponseEntity<List<Aeronave>> getAll(@RequestHeader("Authorization") String token){

        verificaToken(token);

        List<Aeronave> aeronaves = repository.findAll();

        if(aeronaves.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(aeronaves);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeronave> getById(@PathVariable Long id
            ,@RequestHeader("Authorization") String token){
        verificaToken(token);

        Aeronave aeronave = repository.findById(id).orElse(null);

        if(aeronave == null){
            return ResponseEntity.noContent().build();
        }


        return repository.findById(id)   // ← findById, não findAllById
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Aeronave> create(@RequestBody Aeronave aeronave
            ,@RequestHeader("Authorization") String token){

        verificaToken(token);

        Aeronave dadosSalvos = repository.save(aeronave);
        return ResponseEntity.status(HttpStatus.CREATED).body(dadosSalvos);
    }

    @PutMapping("{id}")
    public ResponseEntity<Aeronave> update(@PathVariable Long id, @RequestBody Aeronave dados, @RequestHeader("Authorization") String token){

        verificaToken(token);

        return repository.findById(id).map(
                existe -> {
                    existe.setPrefixo(dados.getPrefixo());
                    existe.setModelo(dados.getModelo());
                    existe.setCompanhia(dados.getCompanhia());

                    Aeronave atualizada = repository.save(existe);
                    return ResponseEntity.ok(atualizada);
                }
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestHeader("Authorization") String token){

        verificaToken(token);

        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    public void verificaToken(String token) {
        CheckAuthDto usuario = sessaoService.getUsuarioId(token);

        if (usuario.tipo() != TipoUsuario.PILOTO &&
                usuario.tipo() != TipoUsuario.TORRE) {

            throw new AcessoNegadoException(
                    "Tipo de usuário não autorizado."
            );
        }
    }
}
