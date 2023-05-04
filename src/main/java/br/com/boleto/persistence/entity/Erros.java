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
@Table(name = "erros", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Erros {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
