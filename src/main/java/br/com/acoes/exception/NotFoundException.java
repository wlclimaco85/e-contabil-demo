package br.com.acoes.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String messager) {
        super(messager);
    }
}
