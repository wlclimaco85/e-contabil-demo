package br.com.boleto.persistence.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaCredenciamentoDto {
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
	private String clientid;
	private String clientsecret;
	private Boolean descredenciar;
	private Integer tipobase;
	private String indicadorpix;
	private String token;
	private String chavesessao;
	private Integer codigoEmpresa;
	private Timestamp dataRegistroConta;
}
