package br.com.boleto.persistence.dtos;

import static br.com.boleto.util.ObjectUtil.getIfExists;
import static java.util.Objects.isNull;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

import javax.persistence.Embedded;

import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.BeneficiarioFinal;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.Desconto;
import br.com.boleto.persistence.entity.JurosMora;
import br.com.boleto.persistence.entity.Multa;
import br.com.boleto.persistence.entity.Pagador;
import br.com.boleto.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoletoDto {

	private Integer id;
	private Integer idapibanco;
	private Integer nossonumero;
	private Integer numeroConvenio;
	private Integer numeroCarteira;
	private Integer numeroVariacaoCarteira;
	private Integer codigoModalidade;
	private String dataEmissao;
	private String dataVencimento;
	private Double valorOriginal;
	private Double valorAbatimento;
	private Integer quantidadeDiasProtesto;
	private Integer quantidadeDiasNegativacao;
	private Integer orgaoNegativador;
	private String indicadorAceiteTituloVencido;
	private Integer numeroDiasLimiteRecebimento;
	private String codigoAceite;
	private Integer codigoTipoTitulo;
	private String descricaoTipoTitulo;
	private String indicadorPermissaoRecebimentoParcial;
	private Integer numeroTituloBeneficiario;
	private String campoUtilizacaoBeneficiario;
	private String numeroTituloCliente;
	private String mensagemBloquetoOcorrencia;
	private String indicadorPix;
	private Integer status;
	private String descricaoStatus;
	private Desconto desconto;
	private Desconto segundoDesconto;
	private Desconto terceiroDesconto;
	private JurosMora jurosMora;
	private Multa multa;
	private Pagador pagador;
	private BeneficiarioFinal beneficiarioFinal;
	private Integer codigoEstadoTituloCobranca;
	private String indicadorNovaDataVencimento;
	private String indicadorAtribuirDesconto;
	private String indicadorAlterarDesconto;
	private String indicadorAlterarDataDesconto;
	private String indicadorProtestar;
	private String indicadorSustacaoProtesto;
	private String indicadorCancelarProtesto;
	private String indicadorIncluirAbatimento;
	private String indicadorCancelarAbatimento;
	private String indicadorCobrarJuros;
	private String indicadorDispensarJuros;
	private String indicadorCobrarMulta;
	private String indicadorDispensarMulta;
	private String indicadorNegativar;
	private String indicadorAlterarNossoNumero;
	private String indicadorAlterarEnderecoPagador;
	private String indicadorAlterarPrazoBoletoVencido;
	private Integer tipoNegativacao;
	private Integer quantidadeDiasAceite;
	private String url;
	private String txId;
	private String emv;
	private String dataRegistro;
	private String estadoTituloCobranca;
	private Integer contrato;
	private String dataMovimento;
	private String dataCredito;
	private Double valorAtual;
	private Double valorPago;
	private String codigoLinhaDigitavel;
	private String dataCreditoLiquidacao;
	private String dataRecebimentoTitulo;
	private String nroDocumento;
	private Integer statusBanco;
	private Integer quantidadeTentativas;
	private Double valorJuroMoraRecebido;
	private Double valorMultaRecebido;
	private Boolean isApi;
	private LocalDateTime dataRegistroBanco;
	private String indicadorAlterarValorOriginal;
	private Integer instrucaoProtesto;
	private Integer instrucaoNegativacao;
	private boolean contabilizarDias;
	
	@Embedded
	private Audit audit = new Audit();

	public BoletoDto(Boleto boleto) {
		if (boleto != null) {
			this.id = boleto.getId();
			this.idapibanco = boleto.getConta().getId();
			this.nossonumero = boleto.getNossonumero();
			this.numeroConvenio = boleto.getNumeroConvenio();
			this.numeroCarteira = boleto.getNumeroCarteira();
			this.numeroVariacaoCarteira = boleto.getNumeroVariacaoCarteira();
			this.codigoModalidade = boleto.getCodigoModalidade();
			this.dataEmissao = DateUtil.convertTimeStampToString(boleto.getDataEmissao());
			this.dataVencimento = DateUtil.convertTimeStampToString(boleto.getDataVencimento());
			this.valorOriginal = boleto.getValorOriginal();
			this.valorAbatimento = boleto.getValorAbatimento();
			this.quantidadeDiasProtesto = boleto.getQuantidadeDiasProtesto();
			this.quantidadeDiasNegativacao = boleto.getQuantidadeDiasNegativacao();
			this.orgaoNegativador = boleto.getOrgaoNegativador();
			this.indicadorAceiteTituloVencido = boleto.getIndicadorAceiteTituloVencido();
			this.numeroDiasLimiteRecebimento = boleto.getNumeroDiasLimiteRecebimento();
			this.codigoAceite = boleto.getCodigoAceite();
			this.codigoTipoTitulo = boleto.getCodigoTipoTitulo();
			this.descricaoTipoTitulo = boleto.getDescricaoTipoTitulo();
			this.indicadorPermissaoRecebimentoParcial = boleto.getIndicadorPermissaoRecebimentoParcial();
			this.numeroTituloBeneficiario = boleto.getNumeroTituloBeneficiario();
			this.campoUtilizacaoBeneficiario = boleto.getCampoUtilizacaoBeneficiario();
			this.numeroTituloCliente = boleto.getNumeroTituloCliente();
			this.mensagemBloquetoOcorrencia = boleto.getMensagemBloquetoOcorrencia();
			this.indicadorPix = boleto.getIndicadorPix();
			this.status = boleto.getStatus().getStatus();
			this.codigoEstadoTituloCobranca = boleto.getCodigoEstadoTituloCobranca();

			this.indicadorNovaDataVencimento = boleto.getIndicadorNovaDataVencimento();
			this.indicadorAtribuirDesconto = boleto.getIndicadorAtribuirDesconto();
			this.indicadorAlterarDesconto = boleto.getIndicadorAlterarDesconto();
			this.indicadorAlterarDataDesconto = boleto.getIndicadorAlterarDataDesconto();
			this.indicadorProtestar = boleto.getIndicadorProtestar();
			this.indicadorSustacaoProtesto = boleto.getIndicadorSustacaoProtesto();
			this.indicadorCancelarProtesto = boleto.getIndicadorCancelarProtesto();
			this.indicadorIncluirAbatimento = boleto.getIndicadorIncluirAbatimento();
			this.indicadorCancelarAbatimento = boleto.getIndicadorCancelarAbatimento();
			this.indicadorCobrarJuros = boleto.getIndicadorCobrarJuros();
			this.indicadorDispensarJuros = boleto.getIndicadorDispensarJuros();
			this.indicadorCobrarMulta = boleto.getIndicadorCobrarMulta();
			this.indicadorDispensarMulta = boleto.getIndicadorDispensarMulta();
			this.indicadorNegativar = boleto.getIndicadorNegativar();
			this.indicadorAlterarNossoNumero = boleto.getIndicadorAlterarNossoNumero();
			this.indicadorAlterarEnderecoPagador = boleto.getIndicadorAlterarEnderecoPagador();
			this.indicadorAlterarPrazoBoletoVencido = boleto.getIndicadorAlterarPrazoBoletoVencido();
			this.tipoNegativacao = boleto.getTipoNegativacao();
			this.quantidadeDiasAceite = boleto.getQuantidadeDiasAceite();

			this.url = boleto.getUrl();
			this.txId = boleto.getTxId();
			this.emv = boleto.getEmv();

			this.dataRegistro = boleto.getDataRegistro();
			this.estadoTituloCobranca = boleto.getEstadoTituloCobranca();
			this.contrato = boleto.getContrato();
			this.dataMovimento = boleto.getDataMovimento();
			this.dataCredito = boleto.getDataCredito();
			this.valorAtual = boleto.getValorAtual();
			this.valorPago = boleto.getValorPago();
			this.codigoLinhaDigitavel = boleto.getCodLinhaDigitavel();
			this.dataCreditoLiquidacao = getIfExists(boleto.getDhCreditoLiquidacao(),
					DateUtil::convertTimeStampToString);
			this.dataRecebimentoTitulo = getIfExists(boleto.getDhRecebimentoTitulo(),
					DateUtil::convertTimeStampToString);
			this.nroDocumento = boleto.getNroDocumento();
			this.statusBanco = boleto.getStatusBanco().getStatus();
			this.quantidadeTentativas = boleto.getQuantidadeTentativas();
			this.valorJuroMoraRecebido = boleto.getValorJuroMoraRecebido();
			this.valorMultaRecebido = boleto.getValorMultaRecebido();
			this.dataRegistroBanco = boleto.getDataRegistroBanco();
			this.isApi = boleto.getIsApi();
			this.indicadorAlterarValorOriginal = boleto.getIndicadorAlterarValorOriginal();
			if (!isNull(boleto.getAudit())) {
				this.audit.setDataCreated(isNull(boleto.getAudit().getDataCreated()) ? null : boleto.getAudit().getDataCreated());
				this.audit.setDataUpdated(isNull(boleto.getAudit().getDataUpdated()) ? null : boleto.getAudit().getDataUpdated());
			}
			this.instrucaoProtesto = boleto.getInstrucaoProtesto();
			this.instrucaoNegativacao = boleto.getInstrucaoNegativacao();
			this.contabilizarDias = boleto.isContabilizarDias();
			
			this.desconto = boleto.getDesconto();
			this.segundoDesconto = boleto.getSegundoDesconto();
			this.terceiroDesconto = boleto.getTerceiroDesconto();
			this.jurosMora = boleto.getJurosMora();
			this.multa = boleto.getMulta();
			this.pagador = boleto.getPagador();
			this.beneficiarioFinal = boleto.getBeneficiarioFinal();
		}
	}

	public BoletoDto(ListagemBoletoResponseDto boleto) {
		this.numeroTituloCliente = boleto.getNumeroBoletoBB();
		this.dataRegistro = boleto.getDataRegistro();
		this.dataVencimento = boleto.getDataVencimento();
		this.valorOriginal = boleto.getValorOriginal();
		this.numeroCarteira = boleto.getCarteiraConvenio();
		this.numeroVariacaoCarteira = boleto.getVariacaoCarteiraConvenio();
		this.codigoEstadoTituloCobranca = boleto.getCodigoEstadoTituloCobranca();
		this.estadoTituloCobranca = boleto.getEstadoTituloCobranca();
		this.contrato = boleto.getContrato();
		this.dataMovimento = boleto.getDataMovimento();
		this.dataCredito = boleto.getDataCredito();
		this.valorAtual = boleto.getValorAtual();
		this.valorPago = boleto.getValorPago();
	}

	public String getDescricaoStatus() {
		return StatusBoletoEnum.getDescricao(status);
	}

	public void setDescricaoStatus(StatusBoletoEnum descricaoStatus) {
		if (descricaoStatus != null) {
			this.descricaoStatus = descricaoStatus.getDescricao();
		}
	}

	public void setDtMultaAsVencimOne() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		if (this.dataVencimento == null || this.dataVencimento.length() != 10) {

		}

		try {
			String[] d = this.dataVencimento.split("\\.");

			Calendar cal = Calendar.getInstance();
			cal.set(Integer.valueOf(d[2]), Integer.valueOf(d[1]) - 1, Integer.valueOf(d[0]));
			cal.add(Calendar.DAY_OF_YEAR, 1);
			String dateAfter = sdf.format(cal.getTime());

			this.multa.setData(dateAfter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
