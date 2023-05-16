package br.com.acoes.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "estrategias_por_acao", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class EstrategiasPorAcao {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne(targetEntity = Acoes.class, cascade=CascadeType.ALL)
    @JoinColumn(name = "acao_Id", referencedColumnName = "id")
	private Acoes acao;
	
	@OneToOne(targetEntity = Estrategias.class, cascade=CascadeType.ALL)
    @JoinColumn(name = "estrategia_id", referencedColumnName = "id")
	private Estrategias estrategia;
	private String tipo;
	private String status;
	private Double valorcompra;
	private Double valorcomprafinal;
	@Embedded
	private Audit audit = new Audit();
}
