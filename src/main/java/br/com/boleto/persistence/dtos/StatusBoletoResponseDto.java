package br.com.boleto.persistence.dtos;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusBoletoResponseDto {

    private String dataUltimaAtualizacaoStatusConta;
	private ArrayList<BoletoResponseDto> boletos;
}
