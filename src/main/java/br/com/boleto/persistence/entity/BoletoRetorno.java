package br.com.boleto.persistence.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.boleto.persistence.dtos.BoletoRetornoDto;
import br.com.boleto.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "boleto_retorno", schema = "public")
@NoArgsConstructor
public class BoletoRetorno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@OneToOne(targetEntity = Boleto.class)
	@JoinColumn(nullable = false, name = "idboleto")
	private Integer idboleto;
	private String codigoLinhaDigitavel;
	private String textoEmailPagador;
	private String textoMensagemBloquetoTitulo;
	private Integer codigoCanalPagamento;
	private Integer numeroContratoCobranca;
	private Integer codigoPrefixoDependenciaCobrador;
	private Integer codigoIndicadorEconomico;
	private Timestamp dataRegistroTituloCobranca;
	private Double valorAtualTituloCobranca;
	private Double valorPagamentoParcialTitulo;
	private Double percentualImpostoSobreOprFinanceirasTituloCobranca;
	private Double valorImpostoSobreOprFinanceirasTituloCobranca;
	private Double valorMoedaTituloCobranca;
	private Integer quantidadeParcelaTituloCobranca;
	private Timestamp dataBaixaAutomaticoTitulo;
	private String indicadorCobrancaPartilhadoTitulo;
	private Double valorMoedaAbatimentoTitulo;
	private Timestamp dataProtestoTituloCobranca;
	private Integer codigoTipoInscricaoSacador;
	private String numeroInscricaoSacadorAvalista;
	private String nomeSacadorAvalistaTitulo;
	private Double percentualSegundoDescontoTitulo;
	private String dataSegundoDescontoTitulo;
	private Double valorSegundoDescontoTitulo;
	private Integer codigoSegundoDescontoTitulo;
	private Double percentualTerceiroDescontoTitulo;
	private Timestamp dataTerceiroDescontoTitulo;
	private Double valorTerceiroDescontoTitulo;
	private Integer codigoTerceiroDescontoTitulo;
	private Integer quantidadeDiaPrazoLimiteRecebimento;
	private Timestamp dataLimiteRecebimentoTitulo;
	private String textoCodigoBarrasTituloCobranca;
	private Integer codigoOcorrenciaCartorio;
	private Double valorImpostoSobreOprFinanceirasRecebidoTitulo;
	private Double valorAbatimentoTotal;
	private Double valorJuroMoraRecebido;
	private Double valorDescontoUtilizado;
	private Double valorPagoSacado;
	private Double valorCreditoCedente;
	private Integer codigoTipoLiquidacao;
	private Timestamp dataRecebimentoTitulo;
	private Integer codigoPrefixoDependenciaRecebedor;
	private Integer codigoNaturezaRecebimento;
	private String numeroIdentidadeSacadoTituloCobranca;
	private String codigoResponsavelAtualizacao;
	private Integer codigoTipoBaixaTitulo;
	private Double valorMultaRecebido;
	private Double valorReajuste;
	private Double valorOutroRecebido;
	private Integer codigoIndicadorEconomicoUtilizadoInadimplencia;
	
	public BoletoRetorno(BoletoRetornoDto boletoRetorno) {
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
			this.dataRegistroTituloCobranca = DateUtil.convertStringToTimestamp(boletoRetorno.getDataRegistroTituloCobranca());
			this.valorAtualTituloCobranca = boletoRetorno.getValorAtualTituloCobranca();
			this.valorPagamentoParcialTitulo = boletoRetorno.getValorPagamentoParcialTitulo();
			this.percentualImpostoSobreOprFinanceirasTituloCobranca = boletoRetorno.getPercentualImpostoSobreOprFinanceirasTituloCobranca();
			this.valorImpostoSobreOprFinanceirasTituloCobranca = boletoRetorno.getValorImpostoSobreOprFinanceirasTituloCobranca();
			this.valorMoedaTituloCobranca = boletoRetorno.getValorMoedaTituloCobranca();
			this.quantidadeParcelaTituloCobranca = boletoRetorno.getQuantidadeParcelaTituloCobranca();
			this.dataBaixaAutomaticoTitulo = DateUtil.convertStringToTimestamp(boletoRetorno.getDataBaixaAutomaticoTitulo());
			this.indicadorCobrancaPartilhadoTitulo = boletoRetorno.getIndicadorCobrancaPartilhadoTitulo();
			this.valorMoedaAbatimentoTitulo = boletoRetorno.getValorMoedaAbatimentoTitulo();
			this.dataProtestoTituloCobranca = DateUtil.convertStringToTimestamp(boletoRetorno.getDataProtestoTituloCobranca());
			this.codigoTipoInscricaoSacador = boletoRetorno.getCodigoTipoInscricaoSacador();
			this.numeroInscricaoSacadorAvalista = boletoRetorno.getNumeroInscricaoSacadorAvalista();
			this.nomeSacadorAvalistaTitulo = boletoRetorno.getNomeSacadorAvalistaTitulo();
			this.percentualSegundoDescontoTitulo = boletoRetorno.getPercentualSegundoDescontoTitulo();
			this.dataSegundoDescontoTitulo = boletoRetorno.getDataSegundoDescontoTitulo();
			this.valorSegundoDescontoTitulo = boletoRetorno.getValorSegundoDescontoTitulo();
			this.codigoSegundoDescontoTitulo = boletoRetorno.getCodigoSegundoDescontoTitulo();
			this.percentualTerceiroDescontoTitulo = boletoRetorno.getPercentualTerceiroDescontoTitulo();
			this.dataTerceiroDescontoTitulo = DateUtil.convertStringToTimestamp(boletoRetorno.getDataTerceiroDescontoTitulo());
			this.valorTerceiroDescontoTitulo = boletoRetorno.getValorTerceiroDescontoTitulo();
			this.codigoTerceiroDescontoTitulo = boletoRetorno.getCodigoTerceiroDescontoTitulo();
			this.quantidadeDiaPrazoLimiteRecebimento = boletoRetorno.getQuantidadeDiaPrazoLimiteRecebimento();
			this.dataLimiteRecebimentoTitulo = DateUtil.convertStringToTimestamp(boletoRetorno.getDataLimiteRecebimentoTitulo());
			this.textoCodigoBarrasTituloCobranca = boletoRetorno.getTextoCodigoBarrasTituloCobranca();
			this.codigoOcorrenciaCartorio = boletoRetorno.getCodigoOcorrenciaCartorio();
			this.valorImpostoSobreOprFinanceirasRecebidoTitulo = boletoRetorno.getValorImpostoSobreOprFinanceirasRecebidoTitulo();
			this.valorAbatimentoTotal = boletoRetorno.getValorAbatimentoTotal();
			this.valorJuroMoraRecebido = boletoRetorno.getValorJuroMoraRecebido();
			this.valorDescontoUtilizado = boletoRetorno.getValorDescontoUtilizado();
			this.valorPagoSacado = boletoRetorno.getValorPagoSacado();
			this.valorCreditoCedente = boletoRetorno.getValorCreditoCedente();
			this.codigoTipoLiquidacao = boletoRetorno.getCodigoTipoLiquidacao();
			this.dataRecebimentoTitulo = DateUtil.convertStringToTimestamp(boletoRetorno.getDataRecebimentoTitulo());
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
