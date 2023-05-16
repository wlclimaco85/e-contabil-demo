package br.com.boleto.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	@OneToOne(targetEntity = Ordens.class, cascade=CascadeType.ALL)
    @JoinColumn(name = "ordem_Id", referencedColumnName = "id")
	private Ordens ordem;
	private String erro;
	private String tipo;
	private Integer qtdTentativas;
	private Double price;
	private Double loss;
	private Double gain;
	private Integer contratos;
	@Embedded
	private Audit audit = new Audit();
	
	

}
