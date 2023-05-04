package br.com.boleto.persistence.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusBoletoResponse extends ResponseDto {
	private List<BoletoDto> boletos;
}
