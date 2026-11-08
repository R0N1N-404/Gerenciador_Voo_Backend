package com.example.Gerenciador_Voo.controller;

import com.example.Gerenciador_Voo.data.pista.Pista;
import com.example.Gerenciador_Voo.data.pista.PistaResponseDto;
import com.example.Gerenciador_Voo.services.PistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pistas")
public class PistaController {
    @Autowired
    private PistaService pistaService;

    @GetMapping
    public List<Pista> getAll(){
        return pistaService.listarTodas();
    }
    @GetMapping("/{id}/status")
    public PistaResponseDto getStatus(@PathVariable Long id){
        return pistaService.getStatus(id);
    }

    @GetMapping("/{id}")
    public PistaResponseDto getById(@PathVariable Long id){ return pistaService.getById(id);}
}
