package br.com.boleto.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "certificados", schema = "public")
public class Certificado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String clientid;

	private String clientsecret;

	private String token;

	private byte[] csr;

	private byte[] key;

	private byte[] crt;

	private byte[] p12;

	@OneToOne(targetEntity = Conta.class)
	@JoinColumn(nullable = false, name = "conta_id")
	private Conta conta;
}
