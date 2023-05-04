package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Acoes3Dto {
	private Integer acaoId;
	private String tipo;
	private Integer ambiente;
	private LocalDateTime data;
	private Integer contratos;
	private Double valoracao;
	private Double loss;
	private Double gain;
}