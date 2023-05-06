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
@Table(name = "breakeven", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Breakeven {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String acao;
	private Integer acaoId;
	private Double lossAtual;
	private Double gainAtual;
	private LocalDateTime dh_created_at;
	private Double valorAtualAcao;
	private String status;
	private String erro;

}
