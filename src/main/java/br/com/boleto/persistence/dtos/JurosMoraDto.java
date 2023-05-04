package br.com.boleto.persistence.dtos;

import br.com.boleto.persistence.entity.JurosMora;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JurosMoraDto {
	private Integer id;
	private Integer tipo;
	private Double porcentagem;
	private Double valor;
	
	public JurosMoraDto(Integer tipo, Double porcentagem, Double valor) {
		super();
		this.tipo = tipo;
		this.porcentagem = porcentagem;
		this.valor = valor;
	}
	
	public JurosMoraDto(JurosMora jurosMora) {
		super();
		this.id = jurosMora.getId();
		this.tipo = jurosMora.getTipo();
		this.porcentagem = jurosMora.getPorcentagem();
		this.valor = jurosMora.getValor();
	}
	
	@Override
	public boolean equals(Object other)
	{
	    if (other == null) 
	        return false;

	    if (other == this)
	        return true;

	    if (!(other instanceof JurosMoraDto))
	        return false;

	    JurosMoraDto p = (JurosMoraDto) other;

	    // Aqui você implementa como deve se feita a comparação.
	    // Verifica se os nomes dos produtos são iguais, ids e etc.

	    if (p.tipo == this.tipo && Double.compare(validar(p.porcentagem),validar(this.porcentagem)) == 0 && Double.compare(validar(p.valor) ,validar(this.valor)) ==0) {
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
