package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "acoes", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Acoes {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String acao;
	private LocalDateTime dh_created_at;
	private String status;
	private Double lucropreju;
	private Double valorsuj;
	private String tipo;
	private Integer periodo;
	private Integer ambiente;
	private String nomeRobo;
	private LocalDateTime dataCompra;
	private LocalDateTime dataVenda;
	private Integer contratos;
	private Double valoracaoatual;
	private LocalDateTime dh_updated_at;
	private String shortname;
	private Integer level;
	private Integer mudouLado;
	private Double valor;
	private Double loss;
	private Double gain;
	private Integer acaoOrigem;
	private Integer compraAmercado;
	private Integer isPercentualLossGain;
	private Double lossCorrente;
	private Double gainCorrente;
	
	@Transient
	private String error;
	@Transient
	private Integer qtdBreakeven;
}
