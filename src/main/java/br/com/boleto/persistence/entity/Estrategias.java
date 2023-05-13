package br.com.boleto.persistence.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "estrategia", cascade = CascadeType.ALL)
    @Getter @Setter private List<Estrategias> atributos;
	
	public Estrategias(Integer id) {
		super();
		this.id = id;
	}
	
}
