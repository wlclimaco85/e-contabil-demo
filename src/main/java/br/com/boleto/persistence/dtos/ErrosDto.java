package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import br.com.boleto.persistence.entity.Estrategias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrosDto {
	private Integer id;
	private Integer acaoId;
	private LocalDateTime dh_created_at;
	private String erro;
	private String tipo;
	private Integer qtdTentativas;
	private Double price;
	private Double loss;
	private Double gain;
	private Integer contratos;
}