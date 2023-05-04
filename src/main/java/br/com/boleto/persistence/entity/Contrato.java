package br.com.boleto.persistence.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contratos", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Contrato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codcontrato;
	
	@ManyToOne(targetEntity = Conta.class)
	@JoinColumn(nullable = false, name = "conta_id")
	private Conta conta;
	
	@OneToOne(targetEntity = Parceiro.class)
	@JoinColumn(nullable = false, name = "idparceiro")
	private Parceiro parceiro;
	
	@OneToOne(targetEntity = Servico.class)
	@JoinColumn(nullable = false, name = "codserv")
	private Servico servico;
	
	@OneToOne(targetEntity = Termo.class)
	@JoinColumn(nullable = false, name = "versaoAceite")
	private Termo termo;
	
	@Embedded
	private Audit audit = new Audit();
	
	private Double valorContratado;
	private String usuContratante;
	private String ativo;
}
