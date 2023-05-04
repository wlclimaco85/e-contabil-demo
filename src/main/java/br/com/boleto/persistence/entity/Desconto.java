package br.com.boleto.persistence.entity;




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "desconto", schema = "public")
public class Desconto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer tipo;
	private String dataExpiracao;
	private Double porcentagem;
	private Double valor;
}
