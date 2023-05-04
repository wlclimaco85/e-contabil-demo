package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.boleto.persistence.dtos.EstrategiasDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "estrategias", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Estrategias {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	public Estrategias(EstrategiasDto estrategia) {
		this.id = estrategia.getId();
		this.estrategia = estrategia.getEstrategia();
		this.descricao = estrategia.getDescricao();
		this.dh_created_at = estrategia.getDh_created_at() == null ? LocalDateTime.now() : estrategia.getDh_created_at();
		this.status = estrategia.getStatus();
		this.tipo = estrategia.getTipo();
		this.margemacerto = estrategia.getMargemacerto();
		this.qtdordens = estrategia.getQtdordens();
		this.qtdgain = estrategia.getQtdgain();
		this.qtdloss = estrategia.getQtdloss();
	}
}
