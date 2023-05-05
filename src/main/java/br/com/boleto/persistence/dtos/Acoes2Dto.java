package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Acoes2Dto {
	private Integer id;
	private String acao;
	private String estrategia;
	private LocalDateTime dh_created_at;
	private String status;
	private Double valorcompra;
	private Double loss;
	private Double gain;
	private Double valorcomprado;
	private String tipo;
	private Integer periodo;
	private Integer qtdEstrategia;
	private String ambiente;
	private String nomeRobo;
	private String dataVenda;
	private Integer contratos;
	private Integer level;
	private Integer mudouLado;
	private Double valoracaoatual;
	private LocalDateTime dh_updated_at;
	private String shortname;
	private Integer compraAmercado;
	private Double lucropreju;
	private Double valorsuj;
	private String dataCompra;
	private Double valor;
	private Integer acaoOrigem;
	private Integer isPercentualLossGain;
	private Double lossCorrente;
	private Double gainCorrente;
}