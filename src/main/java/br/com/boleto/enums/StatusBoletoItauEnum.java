package br.com.boleto.enums;

import lombok.Getter;

@Getter
public enum StatusBoletoItauEnum {

	EM_ABERTO(1,"Em Aberto"),//1
    AGUARDANDO_PAGAMENTO(14,"Aguardando Pagamento"),//Código ns StatusBoleto é 14 - Código na StatusBoletoItau é 2
    PAGO(6,"Paga"),//Código ns StatusBoleto é 6 - Código na StatusBoletoItau é 3
    //PAGAMENTO_DEVOLVIDO(4,"Pagamento Devolvido"),//99
    BAIXADO_PELO_BANCO(7,"Baixada"),//Código ns StatusBoleto é 7 - Código na StatusBoletoItau é 5
    STATUS_NAO_MAPEADO(300, "Status não mapeado");

    private Integer status;
    private String descricao;

    StatusBoletoItauEnum(Integer status, String descricao) {
        this.status = status;
        this.descricao = descricao;
    }

    public static Integer getStatus(String descricao) {
        for (StatusBoletoItauEnum statusBoleto : StatusBoletoItauEnum.values()) {
            if (statusBoleto.getDescricao().equals(descricao)) {
                return statusBoleto.getStatus();
            }
        }
        return StatusBoletoEnum.STATUS_NAO_MAPEADO.getStatus();
    }
}
