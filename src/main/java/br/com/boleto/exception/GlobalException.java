package br.com.boleto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.boleto.util.DateUtil;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundException(NotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(DateUtil.getDataAtual());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> failedAuthenticationException(FailedAuthenticationException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(DateUtil.getDataAtual());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestException(BadRequestException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(DateUtil.getDataAtual());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
