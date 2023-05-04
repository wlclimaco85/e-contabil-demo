package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadosIndividuaisBoletoResponseItauDto {
    private String id_boleto_individual;

    private String numero_nosso_numero;

    private String dac_titulo;

    private String data_vencimento;

    private String valor_titulo;

    private String codigo_barras;

    private String numero_linha_digitavel;

    private String data_limite_pagamento;

    private String situacao_geral_boleto;
}
