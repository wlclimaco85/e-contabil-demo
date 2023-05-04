package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoletoResponseItauDto extends ResponseDto {
	private RegistroBoletoResponseItauDto registroBoletoResponseItauDto;

	public BoletoResponseItauDto(ResponseEntity<RegistroBoletoResponseItauDto> response) {
		this.registroBoletoResponseItauDto = response.getBody();
	}
}
