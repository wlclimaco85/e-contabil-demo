package br.com.boleto.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String messager) {
        super(messager);
    }
}
