package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ContaPesquisaDto  implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
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
}
