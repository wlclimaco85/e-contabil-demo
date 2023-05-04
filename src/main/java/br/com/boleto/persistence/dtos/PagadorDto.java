package br.com.boleto.persistence.dtos;

import br.com.boleto.persistence.entity.Pagador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public @NoArgsConstructor class PagadorDto {

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
	
	public PagadorDto(Pagador pagador) {
		super();
		this.id = pagador.getId();
		this.tipoInscricao = pagador.getTipoInscricao();
		this.numeroInscricao = pagador.getNumeroInscricao();
		this.nome = pagador.getNome();
		this.endereco = pagador.getEndereco();
		this.cep = pagador.getCep();
		this.cidade = pagador.getCidade();
		this.bairro = pagador.getBairro();
		this.uf = pagador.getUf();
		this.telefone = pagador.getTelefone();
		this.idparceiro = pagador.getIdparceiro();
	}
	
	public PagadorDto(PagadorDto pagador) {
		super();
		this.id = pagador.getId();
		this.tipoInscricao = pagador.getTipoInscricao();
		this.numeroInscricao = pagador.getNumeroInscricao();
		this.nome = pagador.getNome();
		this.endereco = pagador.getEndereco();
		this.cep = pagador.getCep();
		this.cidade = pagador.getCidade();
		this.bairro = pagador.getBairro();
		this.uf = pagador.getUf();
		this.telefone = pagador.getTelefone();
		this.idparceiro = pagador.getIdparceiro();
	}
	
	@Override
	public boolean equals(Object other)
	{
	    if (other == null) 
	        return false;

	    if (other == this)
	        return true;

	    if (!(other instanceof PagadorDto))
	        return false;

	    PagadorDto p = (PagadorDto) other;

	    if (validar(p.tipoInscricao).equals(validar(this.tipoInscricao)) && validar(p.numeroInscricao).equalsIgnoreCase(validar(this.numeroInscricao)) && validar(p.nome).equalsIgnoreCase(validar(this.nome)) &&
	    		validar(p.endereco).equalsIgnoreCase(validar(this.endereco)) && validar(p.cep).equals(validar(this.cep)) && 
	    		validar(p.cidade).equalsIgnoreCase(validar(this.cidade)) && validar(p.bairro).equalsIgnoreCase(validar(this.bairro)) && validar(p.uf).equalsIgnoreCase(validar(this.uf)) && validar(p.telefone).equalsIgnoreCase(validar(this.telefone))) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	private String validar (String jm) {
		
		if(jm == null) {
			return "";
		} else {
			return jm;
		}
	}
	
	private Integer validar (Integer jm) {
		
		if(jm == null) {
			return 0;
		} else {
			return jm;
		}
	}
	
}
