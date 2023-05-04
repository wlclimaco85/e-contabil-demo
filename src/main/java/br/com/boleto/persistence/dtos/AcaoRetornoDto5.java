package br.com.boleto.persistence.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcaoRetornoDto5 {
	private List<AcaoRetornoDto4> results;
	private String requestedAt;
	
}
