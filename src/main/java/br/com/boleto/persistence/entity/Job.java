package br.com.boleto.persistence.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "jobs", schema = "public")
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Timestamp dataInicio;
	private Timestamp dataFinal;
	private Timestamp dataProxExec;
	private Integer tipo;

	private Integer qtdRegistro;
	
	private String error;

	private Integer status;

	public Job(Timestamp dataInicio, Integer tipo, Integer status) {
		this.dataInicio = dataInicio;
		this.tipo = tipo;
		this.status = status;
	}

	public Job(Integer id, Timestamp dataInicio, Timestamp dataFinal, Integer tipo, Integer qtdRegistro, String error,
			Integer status) {
		super();
		this.id = id;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
		this.tipo = tipo;
		this.qtdRegistro = qtdRegistro;
		this.error = error;
		this.status = status;
	}

	public Job() {
	}
}