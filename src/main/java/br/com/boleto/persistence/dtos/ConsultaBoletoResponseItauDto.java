package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsultaBoletoResponseItauDto {
    private List<RegistroBoletoResponseItauDto> data;
}
