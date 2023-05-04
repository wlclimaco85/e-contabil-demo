package br.com.boleto.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "pagador", schema = "public")
public class Pagador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer tipoInscricao;
	private String numeroInscricao;
	private String nome;
	private String endereco;
	private Integer cep;
	private String cidade;
	private String bairro;
	private String uf;
	private String telefone;
	private Integer idparceiro;

}
