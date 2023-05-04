package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroBoletoResponseItauDto{
	private String id_boleto;

	private BeneficiarioResponseItauDto beneficiario;

	private DadoBoletoResponseItauDto dado_boleto;
}
