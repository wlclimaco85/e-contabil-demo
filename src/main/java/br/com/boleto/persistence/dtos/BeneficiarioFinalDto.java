package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeneficiarioFinalDto {

	private Integer id;
	private Integer tipoInscricao;
	private String numeroInscricao;
	private String nome;

}
