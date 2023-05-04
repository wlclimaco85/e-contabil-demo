package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public @NoArgsConstructor class BeneficiarioDto {
	
	private Integer agencia;
	private Integer contaCorrente;
	private Integer tipoEndereco;
	private String logradouro;
	private String bairro;
	private String cidade;
	private Integer codigoCidade;
	private String  uf;
	private Integer cep;
	private String indicadorComprovacao;
	
}
