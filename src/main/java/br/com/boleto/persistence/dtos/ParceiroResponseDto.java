package br.com.boleto.persistence.dtos;

import java.util.List;

import br.com.boleto.persistence.entity.Parceiro;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParceiroResponseDto {
	private List<Parceiro> parceiros;
}
