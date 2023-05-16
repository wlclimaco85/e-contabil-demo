package br.com.acoes.persistence.dtos;

import java.time.LocalDateTime;

import br.com.acoes.persistence.entity.Estrategias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstrategiasDto {
	private Integer id;
	private String estrategia;
	private String descricao;
	private LocalDateTime dh_created_at;
	private String status;
	private String tipo;
	private Double margemacerto;
	private Integer qtdordens;
	private Integer qtdgain;
	private Integer qtdloss;
	
	
	public EstrategiasDto(Estrategias estrategia) {
		this.id = estrategia.getId();
		this.estrategia = estrategia.getEstrategia();
		this.descricao = estrategia.getDescricao();
		this.status = estrategia.getStatus();
		this.tipo = estrategia.getTipo();
	}
}