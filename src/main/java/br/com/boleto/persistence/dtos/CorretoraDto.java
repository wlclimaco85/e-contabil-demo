package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Estrategias;
import br.com.boleto.persistence.entity.Ordens;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CorretoraDto {
	
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
    private List<Ordens> ordens;
}