package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoletoAlteracaoRegistroItauDto {
    private Integer id;

    private String codigoLinhaDigitavel;

    private Integer status;

    private Integer statusBanco;

    private LocalDateTime dataRegistroBanco;
}
