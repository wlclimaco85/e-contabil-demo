package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
	protected Integer id;
    private String message;
    
	public ResponseDto(String message) {
		this.message = message;
	}
}
