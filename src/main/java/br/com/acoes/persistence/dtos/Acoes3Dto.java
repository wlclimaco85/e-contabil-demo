package br.com.acoes.persistence.dtos;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.acoes.persistence.entity.Acoes;
import br.com.acoes.persistence.entity.Corretora;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Acoes3Dto {
	
	private Integer id;
	private Integer corretoraId;
	private Integer acaoId;
	private Acoes acao;
	private String status;
	private Double lucropreju;
	private Double valorsuj;
	private String tipo;
	private LocalDateTime dataCompra;
	private LocalDateTime dataVenda;
	private Integer contratos;
	private Double valoracaoatual;
	private String shortname;
	private Integer mudouLado;
	private Double valor;
	private Double loss;
	private Double gain;
	private Integer compraAmercado;
	private Integer isPercentualLossGain;
	private Double lossCorrente;
	private Double gainCorrente;
}