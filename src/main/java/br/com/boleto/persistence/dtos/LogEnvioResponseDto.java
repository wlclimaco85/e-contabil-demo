package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogEnvioResponseDto extends ResponseDto {
    private Boolean type;
	private LogEnvioDto logEnvio;
}
