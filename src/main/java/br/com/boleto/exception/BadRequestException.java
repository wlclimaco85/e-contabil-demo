package br.com.boleto.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String messager) {
        super(messager);
    }
}
