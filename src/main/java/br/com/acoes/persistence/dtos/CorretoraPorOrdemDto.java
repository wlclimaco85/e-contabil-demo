package br.com.acoes.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorretoraPorOrdemDto {
	private Integer totalordens;
	private Integer totalcorretoras;
	
}