package br.com.boleto.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum JobEnum {
    REGISTRA(1,"Registra boleto",10),
    ALTERA(2,"Altera boleto",20),
    CANCELA(3,"Cancela boleto",40),
	ATUALIZA_STATUS_PCONTA(4,"Atualiza boleto por conta",360),
	ATUALIZA_STATUS_GERAL(5,"Atualiza boleto geral",1440),
	ATUALIZA_BOLETO(6,"Atualizar retorno boleto",1440),
	ATUALIZA_BOLETO_BAIXADO_DIARIO(7,"Atualizar retorno boleto",60);
    private Integer id;
    private String descricao;
    private Integer tempoJob;

    JobEnum(Integer id, String descricao, Integer tempoJob) {
        this.id = id;
        this.descricao = descricao;
        this.tempoJob = tempoJob;
    }

    public static JobEnum valueById(Integer id){
        if(id != null){
            for (JobEnum bancoEnum: JobEnum.values()) {
                if (Objects.equals(bancoEnum.getId(),id)) {
                    return bancoEnum;
                }
            }
        }
        return JobEnum.ATUALIZA_STATUS_GERAL;
    }
    
    public static Integer valueByTempo(Integer id){
        if(id != null){
            for (JobEnum bancoEnum: JobEnum.values()) {
                if (Objects.equals(bancoEnum.getId(),id)) {
                    return bancoEnum.getTempoJob();
                }
            }
        }
        return 60;
    }
}
