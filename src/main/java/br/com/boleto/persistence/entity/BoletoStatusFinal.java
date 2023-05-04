package br.com.boleto.persistence.entity;

import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static br.com.boleto.util.ObjectUtil.getIfExists;
import static java.util.Objects.isNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "boletos_status_final", schema = "public")
public class BoletoStatusFinal {

	@Id
	private Integer id;
	@ManyToOne(targetEntity = Conta.class)
	@JoinColumn(nullable = false, name = "conta_id")
	private Conta conta;
	private Integer nossonumero;
	private Integer numeroConvenio;
	private Integer numeroCarteira;
	private Integer numeroVariacaoCarteira;
	private Integer codigoModalidade;
	private Timestamp dataEmissao;
	private Timestamp dataVencimento;
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
	@OneToOne(targetEntity = Desconto.class, cascade=CascadeType.ALL)
	@JoinColumn(name = "desconto_id", referencedColumnName = "id")
	private Desconto desconto;
	@OneToOne(targetEntity = Desconto.class, cascade=CascadeType.ALL)
	@JoinColumn(name = "segundo_desconto_id", referencedColumnName = "id")
	private Desconto segundoDesconto;
	@OneToOne(targetEntity = Desconto.class, cascade=CascadeType.ALL)
	@JoinColumn(name = "terceiro_desconto_id", referencedColumnName = "id")
	private Desconto terceiroDesconto;
	@OneToOne(targetEntity = JurosMora.class, cascade=CascadeType.ALL)
	@JoinColumn(name = "juros_mora_id", referencedColumnName = "id")
	private JurosMora jurosMora;
	@OneToOne(targetEntity = Multa.class, cascade=CascadeType.ALL)
	@JoinColumn(name = "multa_id", referencedColumnName = "id")
	private Multa multa;
	@OneToOne(targetEntity = Pagador.class, cascade=CascadeType.ALL)
	@JoinColumn(name = "pagador_id", referencedColumnName = "id")
	private Pagador pagador;
	@OneToOne(targetEntity = BeneficiarioFinal.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "beneficiario_final_id", referencedColumnName = "id")
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
	private String codLinhaDigitavel;
	private Timestamp dhCreditoLiquidacao;
	private Timestamp dhRecebimentoTitulo;
	@Embedded
	private Audit audit = new Audit();
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

	public BoletoStatusFinal(BoletoDto boletoDto) {
		this.id = boletoDto.getId();
		Conta conta = new Conta();
		conta.setId(boletoDto.getIdapibanco());
		this.conta = conta;
		this.nossonumero = boletoDto.getNossonumero();
		this.numeroConvenio = boletoDto.getNumeroConvenio();
		this.numeroCarteira = boletoDto.getNumeroCarteira();
		this.numeroVariacaoCarteira = boletoDto.getNumeroVariacaoCarteira();
		this.codigoModalidade = boletoDto.getCodigoModalidade();
		this.dataEmissao = DateUtil.convertStringToTimestampApenasData(boletoDto.getDataEmissao());
		this.dataVencimento = DateUtil.convertStringToTimestampApenasData(boletoDto.getDataVencimento());
		this.valorOriginal = boletoDto.getValorOriginal();
		this.valorAbatimento = boletoDto.getValorAbatimento();
		this.quantidadeDiasProtesto = boletoDto.getQuantidadeDiasProtesto();
		this.quantidadeDiasNegativacao = boletoDto.getQuantidadeDiasNegativacao();
		this.orgaoNegativador = boletoDto.getOrgaoNegativador();
		this.indicadorAceiteTituloVencido = boletoDto.getIndicadorAceiteTituloVencido();
		this.numeroDiasLimiteRecebimento = boletoDto.getNumeroDiasLimiteRecebimento();
		this.codigoAceite = boletoDto.getCodigoAceite();
		this.codigoTipoTitulo = boletoDto.getCodigoTipoTitulo();
		this.descricaoTipoTitulo = boletoDto.getDescricaoTipoTitulo();
		this.indicadorPermissaoRecebimentoParcial = boletoDto.getIndicadorPermissaoRecebimentoParcial();
		this.numeroTituloBeneficiario = boletoDto.getNumeroTituloBeneficiario();
		this.campoUtilizacaoBeneficiario = boletoDto.getCampoUtilizacaoBeneficiario();
		this.numeroTituloCliente = boletoDto.getNumeroTituloCliente();
		this.mensagemBloquetoOcorrencia = boletoDto.getMensagemBloquetoOcorrencia();
		this.indicadorPix = boletoDto.getIndicadorPix();
		this.status = boletoDto.getStatus();
		this.codigoEstadoTituloCobranca = boletoDto.getCodigoEstadoTituloCobranca();
		this.desconto = isDescontoEmpty(boletoDto.getDesconto(), boletoDto.getIndicadorAlterarDesconto()) ? null : boletoDto.getDesconto();
		this.segundoDesconto = isDescontoEmpty(boletoDto.getSegundoDesconto(), boletoDto.getIndicadorAlterarDesconto()) ? null : boletoDto.getSegundoDesconto();
		this.terceiroDesconto = isDescontoEmpty(boletoDto.getTerceiroDesconto(), boletoDto.getIndicadorAlterarDesconto()) ? null : boletoDto.getTerceiroDesconto();
		this.jurosMora = isJurosEmpty(boletoDto.getJurosMora(), boletoDto.getIndicadorDispensarJuros()) ? null : boletoDto.getJurosMora();
		this.multa = isMultaEmpty(boletoDto.getMulta(), boletoDto.getIndicadorDispensarMulta()) ? null : boletoDto.getMulta();
		this.pagador = isPagadorEmpty(boletoDto.getPagador()) ? null : boletoDto.getPagador();
		this.beneficiarioFinal = isBeneficiarioFinalEmpty(boletoDto.getBeneficiarioFinal()) ? null : boletoDto.getBeneficiarioFinal();
		this.indicadorNovaDataVencimento = boletoDto.getIndicadorNovaDataVencimento();
		this.indicadorAtribuirDesconto = boletoDto.getIndicadorAtribuirDesconto();
		this.indicadorAlterarDesconto = boletoDto.getIndicadorAlterarDesconto();
		this.indicadorAlterarDataDesconto = boletoDto.getIndicadorAlterarDataDesconto();
		this.indicadorProtestar = boletoDto.getIndicadorProtestar();
		this.indicadorSustacaoProtesto = boletoDto.getIndicadorSustacaoProtesto();
		this.indicadorCancelarProtesto = boletoDto.getIndicadorCancelarProtesto();
		this.indicadorIncluirAbatimento = boletoDto.getIndicadorIncluirAbatimento();
		this.indicadorCancelarAbatimento = boletoDto.getIndicadorCancelarAbatimento();
		this.indicadorCobrarJuros = boletoDto.getIndicadorCobrarJuros();
		this.indicadorDispensarJuros = boletoDto.getIndicadorDispensarJuros();
		this.indicadorCobrarMulta = boletoDto.getIndicadorCobrarMulta();
		this.indicadorDispensarMulta = boletoDto.getIndicadorDispensarMulta();
		this.indicadorNegativar = boletoDto.getIndicadorNegativar();
		this.indicadorAlterarNossoNumero = boletoDto.getIndicadorAlterarNossoNumero();
		this.indicadorAlterarEnderecoPagador = boletoDto.getIndicadorAlterarEnderecoPagador();
		this.indicadorAlterarPrazoBoletoVencido = boletoDto.getIndicadorAlterarPrazoBoletoVencido();
		this.tipoNegativacao = boletoDto.getTipoNegativacao();
		this.quantidadeDiasAceite = boletoDto.getQuantidadeDiasAceite();
		this.url = boletoDto.getUrl();
		this.txId = boletoDto.getTxId();
		this.emv = boletoDto.getEmv();
		this.dataRegistro = boletoDto.getDataRegistro();
		this.estadoTituloCobranca = boletoDto.getEstadoTituloCobranca();
		this.contrato = boletoDto.getContrato();
		this.dataMovimento = boletoDto.getDataMovimento();
		this.dataCredito = boletoDto.getDataCredito();
		this.valorAtual = boletoDto.getValorAtual();
		this.valorPago = boletoDto.getValorPago();
		this.codLinhaDigitavel = boletoDto.getCodigoLinhaDigitavel();
		this.dhCreditoLiquidacao = getIfExists(boletoDto.getDataCreditoLiquidacao(), DateUtil::convertStringToTimestamp);
		this.dhRecebimentoTitulo = getIfExists(boletoDto.getDataRecebimentoTitulo(), DateUtil::convertStringToTimestamp);
		this.nroDocumento = boletoDto.getNroDocumento();
		this.statusBanco = boletoDto.getStatusBanco();
		this.quantidadeTentativas = boletoDto.getQuantidadeTentativas();
		this.valorJuroMoraRecebido = boletoDto.getValorJuroMoraRecebido();
		this.valorMultaRecebido = boletoDto.getValorMultaRecebido();
		this.dataRegistroBanco = boletoDto.getDataRegistroBanco();
		this.isApi = boletoDto.getIsApi() != null ? boletoDto.getIsApi() : false;
		this.indicadorAlterarValorOriginal = boletoDto.getIndicadorAlterarValorOriginal();
		this.instrucaoProtesto = boletoDto.getInstrucaoProtesto();
		this.instrucaoNegativacao = boletoDto.getInstrucaoNegativacao();
		this.contabilizarDias = boletoDto.isContabilizarDias();
	}

	public StatusBoletoEnum getStatus() {
		return StatusBoletoEnum.valueOf(this.status);
	}

	public void setStatus(Integer status) {
		if (status != null) {
			this.status = status;
		}
	}

	public StatusBoletoEnum getStatusBanco() {
		return StatusBoletoEnum.valueOf(this.statusBanco);
	}

	public void setStatusBanco(Integer statusBanco) {
		if (statusBanco != null) {
			this.statusBanco = statusBanco;
		}
	}

	public String getCodLinhaDigitavel() {
		return codLinhaDigitavel;
	}

	public Timestamp getDhCreditoLiquidacao() {
		return dhCreditoLiquidacao;
	}

	public void setDhCreditoLiquidacao(String dhCreditoLiquidacao) {
		this.dhCreditoLiquidacao = getIfExists(dhCreditoLiquidacao, DateUtil::convertStringToTimestamp);
	}

	public Timestamp getDhRecebimentoTitulo() {
		return dhRecebimentoTitulo;
	}

	public void setDhRecebimentoTitulo(String dhRecebimentoTitulo) {
		this.dhRecebimentoTitulo = getIfExists(dhRecebimentoTitulo, DateUtil::convertStringToTimestamp);
	}

	private boolean isPagadorEmpty(Pagador pagador) {
		return (isNull(pagador) || ((isNull(pagador.getTipoInscricao()) || pagador.getTipoInscricao() == 0) &&
				pagador.getNome().isBlank() &&
				pagador.getNumeroInscricao().isBlank()));
	}

	private boolean isBeneficiarioFinalEmpty(BeneficiarioFinal beneficiarioFinal) {
		return (isNull(beneficiarioFinal) || ((isNull(beneficiarioFinal.getTipoInscricao()) || beneficiarioFinal.getTipoInscricao() == 0) &&
				beneficiarioFinal.getNome().isBlank() &&
				beneficiarioFinal.getNumeroInscricao().isBlank()));
	}

	private boolean isMultaEmpty(Multa multa, String indicadorDispensarMulta) {
		return (isNull(multa) || (!"S".equals(indicadorDispensarMulta) &&
				(isNull(multa.getPorcentagem()) || multa.getPorcentagem() == 0) &&
				(isNull(multa.getTipo()) || multa.getTipo() == 0) &&
				(isNull(multa.getValor()) || multa.getValor() == 0)));
	}

	private boolean isJurosEmpty(JurosMora jurosMora, String indicadorDispensarJuros) {
		return (isNull(jurosMora) || (!"S".equals(indicadorDispensarJuros) &&
				(isNull(jurosMora.getPorcentagem()) || jurosMora.getPorcentagem() == 0) &&
				(isNull(jurosMora.getTipo()) || jurosMora.getTipo() == 0) &&
				(isNull(jurosMora.getValor()) || jurosMora.getValor() == 0)));
	}

	private boolean isDescontoEmpty(Desconto desconto, String indicadorAlterarDesconto) {
		return (isNull(desconto) || (!"S".equals(indicadorAlterarDesconto) &&
				(isNull(desconto.getPorcentagem()) || desconto.getPorcentagem() == 0) &&
				(isNull(desconto.getTipo()) || desconto.getTipo() == 0) &&
				(isNull(desconto.getValor()) || desconto.getValor() == 0)));
	}
	
	public BoletoStatusFinal(Boleto boleto) {
		this.id = boleto.getId();
		Conta conta = new Conta();
		conta.setId(boleto.getConta().getId());
		this.conta = conta;
		this.nossonumero = boleto.getNossonumero();
		this.numeroConvenio = boleto.getNumeroConvenio();
		this.numeroCarteira = boleto.getNumeroCarteira();
		this.numeroVariacaoCarteira = boleto.getNumeroVariacaoCarteira();
		this.codigoModalidade = boleto.getCodigoModalidade();
		this.dataEmissao = boleto.getDataEmissao();//DateUtil.convertStringToTimestamp(boleto.getDataEmissao());
		this.dataVencimento = boleto.getDataVencimento();//DateUtil.convertStringToTimestamp(boleto.getDataVencimento());
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
		this.desconto = isDescontoEmpty(boleto.getDesconto(), boleto.getIndicadorAlterarDesconto()) ? null : boleto.getDesconto();
		this.segundoDesconto = isDescontoEmpty(boleto.getSegundoDesconto(), boleto.getIndicadorAlterarDesconto()) ? null : boleto.getSegundoDesconto();
		this.terceiroDesconto = isDescontoEmpty(boleto.getTerceiroDesconto(), boleto.getIndicadorAlterarDesconto()) ? null : boleto.getTerceiroDesconto();
		this.jurosMora = isJurosEmpty(boleto.getJurosMora(), boleto.getIndicadorDispensarJuros()) ? null : boleto.getJurosMora();
		this.multa = isMultaEmpty(boleto.getMulta(), boleto.getIndicadorDispensarMulta()) ? null : boleto.getMulta();
		this.pagador = isPagadorEmpty(boleto.getPagador()) ? null : boleto.getPagador();
		this.beneficiarioFinal = isBeneficiarioFinalEmpty(boleto.getBeneficiarioFinal()) ? null : boleto.getBeneficiarioFinal();
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
		this.codLinhaDigitavel = boleto.getCodLinhaDigitavel();
		this.dhCreditoLiquidacao = boleto.getDhCreditoLiquidacao();//getIfExists(boleto.getDataCreditoLiquidacao(), DateUtil::convertStringToTimestamp);
		this.dhRecebimentoTitulo = boleto.getDhRecebimentoTitulo();//getIfExists(boleto.getDataRecebimentoTitulo(), DateUtil::convertStringToTimestamp);
		this.nroDocumento = boleto.getNroDocumento();
		this.statusBanco = boleto.getStatusBanco().getStatus();
		this.quantidadeTentativas = boleto.getQuantidadeTentativas();
		this.valorJuroMoraRecebido = boleto.getValorJuroMoraRecebido();
		this.valorMultaRecebido = boleto.getValorMultaRecebido();
		this.dataRegistroBanco = boleto.getDataRegistroBanco();
		this.isApi = boleto.getIsApi() != null ? boleto.getIsApi() : false;
		this.indicadorAlterarValorOriginal = boleto.getIndicadorAlterarValorOriginal();
		this.instrucaoProtesto = boleto.getInstrucaoProtesto();
		this.instrucaoNegativacao = boleto.getInstrucaoNegativacao();
		this.contabilizarDias = boleto.isContabilizarDias();
	}
	
}
