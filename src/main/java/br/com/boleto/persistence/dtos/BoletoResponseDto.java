package br.com.boleto.persistence.dtos;

import static java.util.Objects.isNull;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoletoResponseDto extends ResponseDto {
    private Boolean type;

    private Integer codcta;
	private BoletoDto boleto;
	
	public BoletoResponseDto(ResponseEntity<RegistroBoletoResponseDto> response) {
		BoletoDto boletoDto = new BoletoDto();
		boletoDto.setCodigoLinhaDigitavel(response.getBody().getLinhaDigitavel());
		boletoDto.setUrl(response.getBody().getQrCode().getUrl());
		boletoDto.setTxId(response.getBody().getQrCode().getTxId());
		boletoDto.setEmv(response.getBody().getQrCode().getEmv());
		
		this.boleto = boletoDto;
	}
	
	public BoletoResponseDto(Integer id, BoletoDto boleto, String message, Boolean tipo) {
		super(id, message);
		BoletoDto boletoDto = new BoletoDto();
		this.boleto = isNull(boleto) ? boletoDto : boleto;
		this.type = tipo;
	}
}
