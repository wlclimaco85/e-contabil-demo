package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BreakevenDto {
	private Integer id;
	private Integer acaoId;
	private Double lossAtual;
	private Double gainAtual;
	private LocalDateTime dh_created_at;
	private Double valorAtualAcao;
	private String status;

}