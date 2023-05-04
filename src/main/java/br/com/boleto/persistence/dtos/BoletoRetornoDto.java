package br.com.boleto.persistence.dtos;

import br.com.boleto.persistence.entity.BoletoRetorno;
import br.com.boleto.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoletoRetornoDto {

	private Integer id;
	private Integer idboleto;
	private String codigoLinhaDigitavel;
	private String textoEmailPagador;
	private String textoMensagemBloquetoTitulo;
	private Integer codigoCanalPagamento;
	private Integer numeroContratoCobranca;
	private Integer codigoPrefixoDependenciaCobrador;
	private Integer codigoIndicadorEconomico;
	private String dataRegistroTituloCobranca;
	private Double valorAtualTituloCobranca;
	private Double valorPagamentoParcialTitulo;
	private Double percentualImpostoSobreOprFinanceirasTituloCobranca;
	private Double valorImpostoSobreOprFinanceirasTituloCobranca;
	private Double valorMoedaTituloCobranca;
	private Integer quantidadeParcelaTituloCobranca;
	private String dataBaixaAutomaticoTitulo;
	private String indicadorCobrancaPartilhadoTitulo;
	private Double valorMoedaAbatimentoTitulo;
	private String dataProtestoTituloCobranca;
	private Integer codigoTipoInscricaoSacador;
	private String numeroInscricaoSacadorAvalista;
	private String nomeSacadorAvalistaTitulo;
	private Double percentualSegundoDescontoTitulo;
	private String dataSegundoDescontoTitulo;
	private Double valorSegundoDescontoTitulo;
	private Integer codigoSegundoDescontoTitulo;
	private Double percentualTerceiroDescontoTitulo;
	private String dataTerceiroDescontoTitulo;
	private Double valorTerceiroDescontoTitulo;
	private Integer codigoTerceiroDescontoTitulo;
	private Integer quantidadeDiaPrazoLimiteRecebimento;
	private String dataLimiteRecebimentoTitulo;
	private String textoCodigoBarrasTituloCobranca;
	private Integer codigoOcorrenciaCartorio;
	private Double valorImpostoSobreOprFinanceirasRecebidoTitulo;
	private Double valorAbatimentoTotal;
	private Double valorJuroMoraRecebido;
	private Double valorDescontoUtilizado;
	private Double valorPagoSacado;
	private Double valorCreditoCedente;
	private Integer codigoTipoLiquidacao;
	private String dataRecebimentoTitulo;
	private Integer codigoPrefixoDependenciaRecebedor;
	private Integer codigoNaturezaRecebimento;
	private String numeroIdentidadeSacadoTituloCobranca;
	private String codigoResponsavelAtualizacao;
	private Integer codigoTipoBaixaTitulo;
	private Double valorMultaRecebido;
	private Double valorReajuste;
	private Double valorOutroRecebido;
	private Integer codigoIndicadorEconomicoUtilizadoInadimplencia;
	private Integer codigoEstadoTituloCobranca;
	private String dataCreditoLiquidacao;
	
	public BoletoRetornoDto(BoletoRetorno boletoRetorno) {
		if (boletoRetorno != null) {
			this.id = boletoRetorno.getId();
			this.idboleto = boletoRetorno.getIdboleto();
			this.codigoLinhaDigitavel = boletoRetorno.getCodigoLinhaDigitavel();
			this.textoEmailPagador = boletoRetorno.getTextoEmailPagador();
			this.textoMensagemBloquetoTitulo = boletoRetorno.getTextoMensagemBloquetoTitulo();
			this.codigoCanalPagamento = boletoRetorno.getCodigoCanalPagamento();
			this.numeroContratoCobranca = boletoRetorno.getNumeroContratoCobranca();
			this.codigoPrefixoDependenciaCobrador = boletoRetorno.getCodigoPrefixoDependenciaCobrador();
			this.codigoIndicadorEconomico = boletoRetorno.getCodigoIndicadorEconomico();
			this.dataRegistroTituloCobranca = DateUtil.convertTimeStampToString(boletoRetorno.getDataRegistroTituloCobranca());
			this.valorAtualTituloCobranca = boletoRetorno.getValorAtualTituloCobranca();
			this.valorPagamentoParcialTitulo = boletoRetorno.getValorPagamentoParcialTitulo();
			this.percentualImpostoSobreOprFinanceirasTituloCobranca = boletoRetorno.getPercentualImpostoSobreOprFinanceirasTituloCobranca();
			this.valorImpostoSobreOprFinanceirasTituloCobranca = boletoRetorno.getValorImpostoSobreOprFinanceirasTituloCobranca();
			this.valorMoedaTituloCobranca = boletoRetorno.getValorMoedaTituloCobranca();
			this.quantidadeParcelaTituloCobranca = boletoRetorno.getQuantidadeParcelaTituloCobranca();
			this.dataBaixaAutomaticoTitulo = DateUtil.convertTimeStampToString(boletoRetorno.getDataBaixaAutomaticoTitulo());
			this.indicadorCobrancaPartilhadoTitulo = boletoRetorno.getIndicadorCobrancaPartilhadoTitulo();
			this.valorMoedaAbatimentoTitulo = boletoRetorno.getValorMoedaAbatimentoTitulo();
			this.dataProtestoTituloCobranca = DateUtil.convertTimeStampToString(boletoRetorno.getDataProtestoTituloCobranca());
			this.codigoTipoInscricaoSacador = boletoRetorno.getCodigoTipoInscricaoSacador();
			this.numeroInscricaoSacadorAvalista = boletoRetorno.getNumeroInscricaoSacadorAvalista();
			this.nomeSacadorAvalistaTitulo = boletoRetorno.getNomeSacadorAvalistaTitulo();
			this.percentualSegundoDescontoTitulo = boletoRetorno.getPercentualSegundoDescontoTitulo();
			this.dataSegundoDescontoTitulo = boletoRetorno.getDataSegundoDescontoTitulo();
			this.valorSegundoDescontoTitulo = boletoRetorno.getValorSegundoDescontoTitulo();
			this.codigoSegundoDescontoTitulo = boletoRetorno.getCodigoSegundoDescontoTitulo();
			this.percentualTerceiroDescontoTitulo = boletoRetorno.getPercentualTerceiroDescontoTitulo();
			this.dataTerceiroDescontoTitulo = DateUtil.convertTimeStampToString(boletoRetorno.getDataTerceiroDescontoTitulo());
			this.valorTerceiroDescontoTitulo = boletoRetorno.getValorTerceiroDescontoTitulo();
			this.codigoTerceiroDescontoTitulo = boletoRetorno.getCodigoTerceiroDescontoTitulo();
			this.quantidadeDiaPrazoLimiteRecebimento = boletoRetorno.getQuantidadeDiaPrazoLimiteRecebimento();
			this.dataLimiteRecebimentoTitulo = DateUtil.convertTimeStampToString(boletoRetorno.getDataLimiteRecebimentoTitulo());
			this.textoCodigoBarrasTituloCobranca = boletoRetorno.getTextoCodigoBarrasTituloCobranca();
			this.codigoOcorrenciaCartorio = boletoRetorno.getCodigoOcorrenciaCartorio();
			this.valorImpostoSobreOprFinanceirasRecebidoTitulo = boletoRetorno.getValorImpostoSobreOprFinanceirasRecebidoTitulo();
			this.valorAbatimentoTotal = boletoRetorno.getValorAbatimentoTotal();
			this.valorJuroMoraRecebido = boletoRetorno.getValorJuroMoraRecebido();
			this.valorDescontoUtilizado = boletoRetorno.getValorDescontoUtilizado();
			this.valorPagoSacado = boletoRetorno.getValorPagoSacado();
			this.valorCreditoCedente = boletoRetorno.getValorCreditoCedente();
			this.codigoTipoLiquidacao = boletoRetorno.getCodigoTipoLiquidacao();
			this.dataRecebimentoTitulo = DateUtil.convertTimeStampToString(boletoRetorno.getDataRecebimentoTitulo());
			this.codigoPrefixoDependenciaRecebedor = boletoRetorno.getCodigoPrefixoDependenciaRecebedor();
			this.codigoNaturezaRecebimento = boletoRetorno.getCodigoNaturezaRecebimento();
			this.numeroIdentidadeSacadoTituloCobranca = boletoRetorno.getNumeroIdentidadeSacadoTituloCobranca();
			this.codigoResponsavelAtualizacao = boletoRetorno.getCodigoResponsavelAtualizacao();
			this.codigoTipoBaixaTitulo = boletoRetorno.getCodigoTipoBaixaTitulo();
			this.valorMultaRecebido = boletoRetorno.getValorMultaRecebido();
			this.valorReajuste = boletoRetorno.getValorReajuste();
			this.valorOutroRecebido = boletoRetorno.getValorOutroRecebido();
			this.codigoIndicadorEconomicoUtilizadoInadimplencia = boletoRetorno.getCodigoIndicadorEconomicoUtilizadoInadimplencia();
		}
	}
}
