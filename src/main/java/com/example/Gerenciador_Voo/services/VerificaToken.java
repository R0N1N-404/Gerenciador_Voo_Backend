package com.example.Gerenciador_Voo.services;

import com.example.Gerenciador_Voo.data.usuario.dto.CheckAuthDto;
import com.example.Gerenciador_Voo.data.usuario.dto.TipoAcesso;
import com.example.Gerenciador_Voo.exceptions.exceptions.AcessoNegadoException;
import org.springframework.stereotype.Service;

@Service
public class VerificaToken {

    private final SessaoService sessaoService;

    public VerificaToken(SessaoService sessaoService) {
        this.sessaoService = sessaoService;
    }

    public void verificaToken(String token, TipoAcesso tipoAcesso) {

        CheckAuthDto usuario = sessaoService.getUsuarioId(token);

        if (tipoAcesso == TipoAcesso.AMBOS) {
            return;
        }

        if (!usuario.tipo().name().equals(tipoAcesso.name())) {
            throw new AcessoNegadoException(
                    "Tipo de usuário não autorizado."
            );
        }
    }
}