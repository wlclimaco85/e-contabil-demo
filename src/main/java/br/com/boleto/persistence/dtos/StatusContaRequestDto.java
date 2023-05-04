package br.com.boleto.persistence.dtos;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusContaRequestDto {
	private Integer idApiBanco;
	private Integer codCtaBcoInt;
	private Timestamp dataUltimaAtualizacao;
}
