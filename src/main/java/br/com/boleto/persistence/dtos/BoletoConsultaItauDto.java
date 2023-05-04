package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoConsultaItauDto {
    private Integer id;

    private Integer status;

    private Integer statusBanco;
}
