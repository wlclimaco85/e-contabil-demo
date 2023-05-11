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
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "acoes", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Acoes {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(targetEntity = Corretora.class, cascade=CascadeType.ALL)
    @JoinColumn(name = "corretora_id", referencedColumnName = "id")
	private Corretora corretora;
	private String acao;
	private String status;
	private Double lucropreju;
	private Double valorsuj;
	private String tipo;
	private Integer periodo;
	private String nomeRobo;
	private LocalDateTime data;
	private Double valoracaoatual;
	private String shortname;
	private Integer level;
	private Integer mudouLado;
	private Double loss;
	private Double gain;
	@Embedded
	private Audit audit = new Audit();
}
