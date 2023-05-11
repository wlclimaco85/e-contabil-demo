package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

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
@Table(name = "breakevens", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Breakeven {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String acao;
	@OneToOne(targetEntity = Ordens.class, cascade=CascadeType.ALL)
    @JoinColumn(name = "ordem_Id", referencedColumnName = "id")
	private Ordens ordem;
	private Double lossAtual;
	private Double gainAtual;
	private Double valorAtualAcao;
	private String status;
	private String erro;
	@Embedded
	private Audit audit = new Audit();
	

}
