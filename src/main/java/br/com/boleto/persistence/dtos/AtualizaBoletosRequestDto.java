package br.com.boleto.persistence.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizaBoletosRequestDto {
	
	private List<Integer> numeroTituloBeneficiario;
	private Integer idApiBanco;
	private List<String> numeroTituloCliente;
	private Integer tipConsulta;

}
