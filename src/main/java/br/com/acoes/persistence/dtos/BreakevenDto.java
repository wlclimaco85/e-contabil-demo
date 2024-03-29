package br.com.acoes.persistence.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BreakevenDto {
	private Integer id;
	private String acao;
	private Integer acaoId;
	private Double lossAtual;
	private Double gainAtual;
	private LocalDateTime dh_created_at;
	private Double valorAtualAcao;
	private String status;
	private String erro;
}