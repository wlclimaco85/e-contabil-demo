package br.com.boleto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FailedAuthenticationException extends RuntimeException {
	private static final long serialVersionUID = 7022534158750670843L;

	public FailedAuthenticationException(String message) {
        super(message);
    }
}
