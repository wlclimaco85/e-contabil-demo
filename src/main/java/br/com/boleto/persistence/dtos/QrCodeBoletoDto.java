package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public @NoArgsConstructor class QrCodeBoletoDto {
	private String	url;
	private String	txId;
	private String	emv;
}
