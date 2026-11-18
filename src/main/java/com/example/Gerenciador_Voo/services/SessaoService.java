package com.example.Gerenciador_Voo.services;

import com.example.Gerenciador_Voo.data.usuario.Usuario;
import com.example.Gerenciador_Voo.data.usuario.UsuarioRepository;
import com.example.Gerenciador_Voo.data.usuario.dto.CheckAuthDto;
import com.example.Gerenciador_Voo.exceptions.exceptions.SessaoInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class SessaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final Map<String, Long> sessaoAtiva = new ConcurrentHashMap<>();

    public String criarSessao(Long usuarioId){
        String token = UUID.randomUUID().toString();
        sessaoAtiva.put(token, usuarioId);
        return token;
    }

    public CheckAuthDto getUsuarioId(String token){
        Long usuarioId = sessaoAtiva.get(token);

        if(usuarioId == null){
            throw new SessaoInvalidaException("Token inválido");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new SessaoInvalidaException("Usuario não encontrado"));

        return new CheckAuthDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getTipo(),
                true
        );
    }

    public void encerrarSessao(String token) {
        if (token == null || !sessaoAtiva.containsKey(token)) {
            throw new SessaoInvalidaException("Token inválido");
        }

        sessaoAtiva.remove(token);
    }



}
