package br.com.boleto.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "parceiros", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Parceiro {
	@Id
	private Integer id;
	private String nome;
}
