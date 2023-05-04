package br.com.boleto.persistence.dtos;

import br.com.boleto.persistence.entity.Desconto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescontoDto {

	private Integer id;
	private Integer tipo;
	private String dataExpiracao;
	private Double porcentagem;
	private Double valor;
	
	public DescontoDto(Integer tipo, Double porcentagem, Double valor, String data) {
		super();
		this.tipo = tipo;
		this.dataExpiracao = data;
		this.porcentagem = porcentagem;
		this.valor = valor;
	}
	
	public DescontoDto(Desconto desconto) {
		super();
		this.id = desconto.getId();
		this.tipo = desconto.getTipo();
		this.dataExpiracao = desconto.getDataExpiracao();
		this.porcentagem = desconto.getPorcentagem();
		this.valor = desconto.getValor();
	}

	public DescontoDto() {
		super();
	}

	@Override
	public boolean equals(Object other)
	{
	    if (other == null) 
	        return false;

	    if (other == this)
	        return true;

	    if (!(other instanceof DescontoDto))
	        return false;

	    DescontoDto p = (DescontoDto) other;
	    if ( p.dataExpiracao.equals(this.dataExpiracao) && Double.compare(validar(p.porcentagem),validar(this.porcentagem)) == 0 && Double.compare( validar(p.valor),validar(this.valor)) == 0) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	private Double validar (Double jm) {
		
		if(jm == null) {
			return 0.0;
		} else {
			return jm;
		}
	}

	@Override
	public String toString() {
		return "Desconto [Tipo: " + tipo + ", Data de Expiracao: " + dataExpiracao + ", Porcentagem: " + porcentagem
				+ ", Valor: " + valor + "]";
	}
}
