package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import br.com.boleto.persistence.entity.EstrategiasPorAcao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstrategiasPorAcaoDto {
	
	private Integer id;
	private Integer acaoid;
	private Integer estrategiaid;
	private LocalDateTime dh_created_at;
	private String status;
	private Double valorcompra;
	private Double valorcompraFinal;
	
//	public EstrategiasPorAcaoDto(EstrategiasPorAcao estrategia) {
//		this.id = estrategia.getId();
//		this.acaoid = estrategia.getAcaoid();
//		this.dh_created_at = estrategia.getDh_created_at();
//		this.status = estrategia.getStatus();
//		this.estrategiaid = estrategia.getEstrategiaid();
//		this.valorcompra = estrategia.getValorcompra();
//	}
}