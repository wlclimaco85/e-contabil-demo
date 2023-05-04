package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class BancoResponseDto {
	private ArrayList<AcoesDto> banco;
}
