package br.com.boleto.persistence.entity;

import javax.persistence.Embedded;
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
@Table(name = "estrategias", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Estrategias {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String estrategia;
	private String descricao;
	private String status;
	private String tipo;
	@Embedded
	private Audit audit = new Audit();
	
}
