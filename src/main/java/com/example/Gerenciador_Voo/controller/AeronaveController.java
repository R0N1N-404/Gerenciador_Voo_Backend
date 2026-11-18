package com.example.Gerenciador_Voo.controller;


import com.example.Gerenciador_Voo.data.aeronave.Aeronave;
import com.example.Gerenciador_Voo.data.aeronave.AeronaveRepository;
import com.example.Gerenciador_Voo.data.requisicao.RequisicaoRepository;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoAcesso;
import com.example.Gerenciador_Voo.services.VerificaToken;
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
    private RequisicaoRepository requisicaoRepository;

    private final VerificaToken verificaToken;

    public AeronaveController(VerificaToken verificaToken) {
        this.verificaToken = verificaToken;
    }

    @GetMapping
    public ResponseEntity<List<Aeronave>> getAll(@RequestHeader("Authorization") String token){

        verificaToken.verificaToken(token, TipoAcesso.AMBOS);

        List<Aeronave> aeronaves = repository.findAll();

        if(aeronaves.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(aeronaves);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeronave> getById(@PathVariable Long id
            ,@RequestHeader("Authorization") String token){


        verificaToken.verificaToken(token, TipoAcesso.AMBOS);

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

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        Aeronave dadosSalvos = repository.save(aeronave);
        return ResponseEntity.status(HttpStatus.CREATED).body(dadosSalvos);
    }

    @PutMapping("{id}")
    public ResponseEntity<Aeronave> update(@PathVariable Long id, @RequestBody Aeronave dados, @RequestHeader("Authorization") String token){

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

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
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        verificaToken.verificaToken(token, TipoAcesso.TORRE);

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (requisicaoRepository.existsByAeronaveId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }



}
