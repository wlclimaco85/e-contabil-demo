package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRequestDto {
	private Integer[] nufins;
	private Integer[] status;
	private Integer ipApiBanco;
}
