package br.com.boleto.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "bancos", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Banco {
	@Id
	private Integer id;

	private String nome;

	private String urlprod;

	private String urlhomol;

	private String urloauthprod;

	private String urloauthhomol;

	private boolean ativo;
}
