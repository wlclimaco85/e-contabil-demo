package br.com.acoes.persistence.entity;

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
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "corretoras", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Corretora {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer ambiente;
	private String status;
	private Double saldo;
	private String usuarioSite;
	private String senhaSite;
	private String usuariomq5;
	private String senhamq5;
	private String usuarioProFit;
	private String senhaProFit;
	private String nome;
	private String banco;
	private String agencia;
	private String conta;

	@Embedded
	private Audit audit = new Audit();
	
	public Corretora(Integer id) {
		super();
		this.id = id;
	}
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "corretora", cascade = CascadeType.ALL)
    private List<Ordens> ordens;

//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "corretoraId")
//    private List<Ordens> ordens = new ArrayList<Ordens>();
	
}
