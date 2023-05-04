package br.com.boleto.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum BancoEnum {
    BANCO_DO_BRASIL(1,"Banco do Brasil"),

    ITAU(341,"Banco Itaú");

    private Integer id;
    private String descricao;

    BancoEnum(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public static BancoEnum valueById(Integer id){
        if(id != null){
            for (BancoEnum bancoEnum: BancoEnum.values()) {
                if (Objects.equals(bancoEnum.getId(),id)) {
                    return bancoEnum;
                }
            }
        }
        return BancoEnum.BANCO_DO_BRASIL;
    }
    
  public static boolean isBancoBrasil(Integer id) {
    	
    	if(id != null && id.equals(BANCO_DO_BRASIL.getId())) {
    		return true;
    	}
    	
    	return false;
    }
}
