package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "estrategias_por_acao", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class EstrategiasPorAcao {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer acaoid;
	private Integer estrategiaid;
	private LocalDateTime dh_created_at;
	private String tipo;
	private String status;
	private Double valorcompra;
	private Double valorcomprafinal;
	private Integer quantidade;
	
//	public EstrategiasPorAcao(EstrategiasPorAcaoDto estrategia) {
//		this.id = estrategia.getId();
//		this.acaoid = estrategia.getAcaoid();
//		this.dh_created_at = estrategia.getDh_created_at() == null ? LocalDateTime.now() : estrategia.getDh_created_at();
//		this.status = estrategia.getStatus();
//		this.estrategiaid = estrategia.getEstrategiaid();
//		this.valorcompra = estrategia.getValorcompra();
//	}
}
