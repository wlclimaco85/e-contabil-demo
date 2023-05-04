package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContratoDto {
	private Integer idapibanco;
	private Integer idparceiro;
	private Integer codserv;
	private String usuContratante;
	private Integer versaoAceite;
}
