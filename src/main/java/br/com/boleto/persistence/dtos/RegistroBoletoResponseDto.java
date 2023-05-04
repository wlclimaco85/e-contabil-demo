package br.com.boleto.persistence.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroBoletoResponseDto {
	private String numero;
	private Integer numeroCarteira;
	private Integer numeroVariacaoCarteira;
	private Integer codigoCliente;
	private String linhaDigitavel;
	private String codigoBarraNumerico;
	private Integer numeroContratoCobranca;
	private BeneficiarioDto beneficiario;
	private QrCodeBoletoDto qrCode;
	private List<RegistroBoletoErroDto> erros;
}
