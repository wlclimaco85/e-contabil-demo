package br.com.acoes.persistence.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcaoRetornoDto {

	private String s;
	private List<AcaoRetornoDto2> d; 
	
}
