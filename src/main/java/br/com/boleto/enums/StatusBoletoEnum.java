package br.com.boleto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusBoletoEnum {
	AGUARDANDO_ENVIO_PARA_BANCO(0, "Aguardando envio para o banco"),
	NORMAL(1, "Normal"),
	MOVIMENTO_CARTORIO(2, "Movimento cartório"),
	EM_CARTORIO(3, "Em cartório"),
	TITULO_COM_OCORRENCIA_CARTORIO(4, "Título com ocorrência de cartório"),
	PROTESTADO_ELETRONICO(5, "Protestado eletrônico"),
	LIQUIDADO(6, "Liquidado"),
	BAIXADO_PELO_BANCO(7, "Baixado"),
	TITULO_COM_PENDENCIA_CARTORIO(8, "Título com pendência de cartório"),
	TITULO_PROTESTADO_MANUAL(9, "Título protestado manual"),
	TITULO_BAIXADO_PAGO_CARTORIO(10, "Título baixado/pago em cartório"),
	TITULO_LIQUIDADO_PROTESTADO(11, "Título liquidado/protestado"),
	TITULO_LIQUID_PGCRTO(12, "Título liquidado/pago em cartório"),
	TITULO_PROTESTADO_AGUARDANDO_BAIXA(13, "Título protestado aguardando baixa"),
	TITULO_EM_LIQUIDACAO(14, "Título em liquidação"),
	TITULO_AGENDADO_BB(15, "Título agendado"),
	TITULO_CREDITADO(16, "Título creditado"),
	PAGO_EM_CHEQUE_AGUARD_LIQUIDACAO(17, "Pago em cheque - Aguardando liquidação"),
	PAGO_PARCIALMENTE(18, "Pago parcialmente"),
	PAGO_PARCIALMENTE_CREDITADO(19, "Pago parcialmente creditado"),
	TITULO_AGENDADO_OUTROS_BANCOS(21, "Título agendado outros bancos"),
	EM_PROCESSAMENTO(80, "Em processamento (estado transitório)"),
	BAIXADO_PELO_ERP(-1, "Baixado pelo ERP"),
	ALTERA_BOLETO(-2, "Alteração de boleto"),
	ERRO(100, "Erro ao registrar boleto"),
	SUCESSO(200, "Sucesso"),
	STATUS_NAO_MAPEADO(300, "Status não mapeado");
	
	private Integer status;

	private String descricao;
	
	public static StatusBoletoEnum valueOf(Integer status) {
		if (status == null) {
			return StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO;
		}
		for (StatusBoletoEnum statusBoleto : StatusBoletoEnum.values()) {
			if (statusBoleto.getStatus().equals(status)) {
				return statusBoleto;
			}
		}
		StatusBoletoEnum.STATUS_NAO_MAPEADO.setStatus(status);
		return StatusBoletoEnum.STATUS_NAO_MAPEADO;
	}
	
	public static String getDescricao(Integer status) {
		for (StatusBoletoEnum statusBoleto : StatusBoletoEnum.values()) {
			if (statusBoleto.getStatus().equals(status)) {
				return statusBoleto.getDescricao();
			}
		}
		return StatusBoletoEnum.STATUS_NAO_MAPEADO.getDescricao();
	}
	
	private void setStatus(Integer status) {
		if (status != null) {
			this.status = status;
		}
	}
}
