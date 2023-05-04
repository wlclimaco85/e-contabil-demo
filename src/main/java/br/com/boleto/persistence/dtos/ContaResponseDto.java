package br.com.boleto.persistence.dtos;

import br.com.boleto.persistence.entity.Conta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContaResponseDto extends ResponseDto {
	private Integer codbco;

	private Integer codage;

	private Integer codcta;

	private Integer codctabco;

	private Integer convenio;

	private Integer carteira;

	private Integer variacao;

	private Integer modalidade;

	private String ultnumbol;

	private String statusapi;

	private Integer idparceiro;

	private String nomeparc;

	private Integer tipobase;
	
	 public ContaResponseDto(Conta conta) {
	    	this.id = conta.getId();
	    	this.codbco = conta.getBanco().getId();
	    	this.codage = conta.getCodage();
	    	this.codcta = conta.getCodcta();
	    	this.codctabco = conta.getCodctabco();
	    	this.convenio = conta.getConvenio();
	    	this.carteira = conta.getCarteira();
	    	this.variacao = conta.getVariacao();
	    	this.modalidade = conta.getModalidade();
	    	this.ultnumbol = conta.getUltnumbol();
	    	this.tipobase = conta.getRegistrobase();
		}
}
