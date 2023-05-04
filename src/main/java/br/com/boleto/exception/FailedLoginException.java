package br.com.boleto.exception;

import br.com.boleto.util.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class FailedLoginException extends RuntimeException {
	private static final long serialVersionUID = 4985586388411859974L;

	public FailedLoginException(String message) {
		super(Translator.toLocale(message));
	}
}
