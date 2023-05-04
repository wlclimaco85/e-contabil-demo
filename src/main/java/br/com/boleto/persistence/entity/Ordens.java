package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.boleto.persistence.dtos.OrdensDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ordens", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Ordens {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer acaoid;
	private String tipo;
	private Integer contratos;
	private Integer contratosCorrentes;
	private LocalDateTime dh_created_at;
	private String status;
	private Double valorcompra;
	private Double valorvenda;
	private Integer ambiente;
	private LocalDateTime dh_compra_at;
	private LocalDateTime dh_venda_at;
	private Double loss;
	private Double gain;
	
	public Ordens(OrdensDto estrategia) {
		this.id = estrategia.getId();
		this.acaoid = estrategia.getAcaoId();
		this.tipo = estrategia.getTipo();
		this.contratos = estrategia.getContratos();
		this.dh_created_at = estrategia.getDh_created_at() == null ? LocalDateTime.now() : estrategia.getDh_created_at();
		this.status = estrategia.getStatus();
		this.valorcompra = estrategia.getValorcompra();
		this.valorvenda = estrategia.getValorvenda();
		this.ambiente = estrategia.getAmbiente();
		this.dh_compra_at = estrategia.getDh_compra_at();
		this.dh_venda_at = estrategia.getDh_venda_at();
		this.loss = estrategia.getLoss();
		this.loss = estrategia.getGain();
		this.contratosCorrentes = estrategia.getContratosCorrentes();
	}
}
