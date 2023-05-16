package br.com.acoes.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String messager) {
        super(messager);
    }
}
