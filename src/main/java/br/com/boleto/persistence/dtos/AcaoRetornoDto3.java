package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcaoRetornoDto3 {

	private String ch;
	private String chp;
	private String short_name;
	private String exchange;
	private String description;
	private String lp;
	private String ask;
	private String bid;
	private String spread;
	private String open_price;
	private String high_price;
	private String low_price;
	private String prev_close_price;
	private String volume;
}
