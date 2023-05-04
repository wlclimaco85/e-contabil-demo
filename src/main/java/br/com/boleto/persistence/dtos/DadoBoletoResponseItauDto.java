package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DadoBoletoResponseItauDto {
    private String tipo_boleto;

    private String codigo_carteira;

    private Integer codigo_tipo_vencimento;

    private String valor_total_titulo;

    private String codigo_especie;

    private ArrayList<DadosIndividuaisBoletoResponseItauDto> dados_individuais_boleto;
}
