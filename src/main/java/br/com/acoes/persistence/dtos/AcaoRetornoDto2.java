package br.com.acoes.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcaoRetornoDto2 {

	private String s;
	private String n;
	private AcaoRetornoDto3 v;
}
