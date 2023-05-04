package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TermoDto {
	private Integer versao;
	private String htmlTermo;
    private LocalDateTime dataCreated;
	private boolean atualizacao;
}
