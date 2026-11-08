package com.example.Gerenciador_Voo.controller;


import com.example.Gerenciador_Voo.data.piloto.Piloto;
import com.example.Gerenciador_Voo.data.piloto.PilotoCreateDto;
import com.example.Gerenciador_Voo.data.piloto.PilotoRepository;
import com.example.Gerenciador_Voo.data.usuario.Usuario;
import com.example.Gerenciador_Voo.data.usuario.UsuarioRepository;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoUsuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/pilotos")
public class PilotoController {

    @Autowired
    private PilotoRepository pilotoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping
    public List<Piloto> getALL(){
        return pilotoRepository.findAll();
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Piloto> create(@RequestBody PilotoCreateDto dto){
        Usuario usuario = new Usuario();

        usuario.setNome(dto.getNome());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(dto.getSenha());
        usuario.setTipo(TipoUsuario.PILOTO);
        usuario.setCriadoEm(LocalDateTime.now());

        //criando
        usuario = usuarioRepository.save(usuario);

        Piloto piloto = new Piloto();

        piloto.setUsuario(usuario);
        piloto.setNumeroLicenca(dto.getNumerolicenca());

        //criando
        piloto = pilotoRepository.save(piloto);

        return ResponseEntity.status(HttpStatus.CREATED).body(piloto);
    }

}
