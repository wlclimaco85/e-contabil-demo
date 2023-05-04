package br.com.boleto.persistence.entity;

import java.util.ArrayList;

import br.com.boleto.persistence.dtos.BoletoStatusRespDto;
import lombok.Data;

@Data
public class BoletoStatusResponse {

	private long expires_at;
	private ArrayList<BoletoStatusRespDto> boletosConta;
	
	
}
