package br.com.boleto.persistence.dtos;
import br.com.boleto.persistence.entity.Multa;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class MultaDto {

	private Integer id;
	private Integer tipo;
	private String data;
	private Double porcentagem;
	private Double valor;
	
	public MultaDto(Integer tipo, Double porcentagem, Double valor,String data) {
		super();
		this.tipo = tipo;
		this.data = data;
		this.porcentagem = porcentagem;
		this.valor = valor;
	}
	
	public MultaDto(Multa multa) {
		super();
		this.id = multa.getId();
		this.tipo = multa.getTipo();
		this.data = multa.getData();
		this.porcentagem = multa.getPorcentagem();
		this.valor = multa.getValor();
	}
	
	@Override
	public boolean equals(Object other)
	{
	    if (other == null) 
	        return false;

	    if (other == this)
	        return true;

	    if (!(other instanceof MultaDto))
	        return false;

	    MultaDto p = (MultaDto) other;

	    if (p.tipo == this.tipo && (Double.compare(validar(p.porcentagem) , validar(this.porcentagem)) == 0)  && (Double.compare(validar(p.valor) , validar(this.valor)) == 0)  && p.data.equals(this.data) ) {
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
}
