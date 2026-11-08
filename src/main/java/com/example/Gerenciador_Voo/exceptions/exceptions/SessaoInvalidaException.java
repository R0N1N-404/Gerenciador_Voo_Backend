package com.example.Gerenciador_Voo.exceptions.exceptions;

public class SessaoInvalidaException extends RuntimeException{
    public SessaoInvalidaException(String mensagem){
        super(mensagem);
    }
}
