package br.com.boleto.persistence.dtos;

import br.com.boleto.enums.BancoEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    private String client_id;

    private String client_secret;

	private BancoEnum banco;
	
	private Integer conta_id;
	
	private String token;
	
	private String chave_sessao;

	public LoginDto(String client_id, String client_secret, BancoEnum banco, Integer conta_id, String token, String chave_sessao) {
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.banco = banco;
		this.conta_id = conta_id;
		this.token = token;
		this.chave_sessao = chave_sessao;
	}
}
