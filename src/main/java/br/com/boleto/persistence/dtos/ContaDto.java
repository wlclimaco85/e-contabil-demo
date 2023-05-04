package br.com.boleto.persistence.dtos;


import static java.util.Objects.isNull;

import javax.persistence.Embedded;

import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Conta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ContaDto {
	private Integer id;
	private Integer codbco;
	private Integer codage;
	private Integer codcta;
	private Integer codctabco;
	private Integer convenio;
	private Integer carteira;
	private Integer variacao;
	private Integer modalidade;
	private String ultnumbol;
	private String statusapi;
    private Integer idparceiro;
    private String nomeparc;
    private String clientid;
    private String clientsecret;
    private Boolean descredenciar;
    private Integer tipobase;
    private String indicadorpix;
    private String token;
    private String chavesessao;
	private String descricao;
	private Integer codigoEmpresa;
	private String emiteBoleto;
	private String ativa;
	private String tipoApiBoleto;
	private Timestamp dataRegistroConta;
	private String apiBaixaAutomatica;
	private String apiConciliacaoAutomatica;
	private String aceitaTituloVencido;
	private String recebimentoParcial;
	private Integer recebimentoDias;
	private Integer orgaoNegativador;
	private Integer quantidadeDiasProtesto;
	private Integer quantidadeDiasNegativacao;
	private boolean contabilizarDias;
	private Integer instrucaoProtesto;
	private Integer instrucaoNegativacao;
	private String tipoJurosMora;
	private Double valorJurosMora;
	private String tipoMulta;
	private String tipoDataMulta;
	private Double valorMulta;
	private Integer quantidadeDiasMulta;


	@Embedded
	private Audit audit = new Audit();

    public ContaDto(Conta conta) {
    	this.id = conta.getId();
    	this.codbco = conta.getBanco().getId();
    	this.codage = conta.getCodage();
    	this.codcta = conta.getCodcta();
    	this.codctabco = conta.getCodctabco();
    	this.convenio = conta.getConvenio();
    	this.carteira = conta.getCarteira();
    	this.variacao = conta.getVariacao();
    	this.modalidade = conta.getModalidade();
    	this.ultnumbol = conta.getUltnumbol();
    	this.tipobase = conta.getRegistrobase();
    	this.indicadorpix = conta.getIndicadorPix();
		this.descricao = conta.getDescricao();
		this.codigoEmpresa = conta.getCodigoEmpresa();
		this.emiteBoleto = conta.getEmiteBoleto();
		this.ativa = conta.getAtiva();
		this.tipoApiBoleto = conta.getTipoApiBoleto();
		this.dataRegistroConta = conta.getDataRegistroConta();
		this.apiBaixaAutomatica = conta.getApiBaixaAutomatica();
		this.apiConciliacaoAutomatica = conta.getApiConciliacaoAutomatica();
		this.aceitaTituloVencido = conta.getAceitaTituloVencido();
		this.recebimentoParcial = conta.getRecebimentoParcial();
		this.recebimentoDias = conta.getRecebimentoDias();
		this.orgaoNegativador = conta.getOrgaoNegativador();
		this.quantidadeDiasProtesto = conta.getQuantidadeDiasProtesto();
		this.quantidadeDiasNegativacao = conta.getQuantidadeDiasNegativacao();
		this.contabilizarDias = conta.isContabilizarDias();
		this.instrucaoProtesto = conta.getInstrucaoProtesto();
		this.instrucaoNegativacao = conta.getInstrucaoNegativacao();

		this.tipoJurosMora = conta.getTipoJurosMora();
		this.valorJurosMora = conta.getValorJurosMora();
		this.tipoMulta = conta.getTipoMulta();
		this.tipoDataMulta = conta.getTipoDataMulta();
		this.valorMulta = conta.getValorMulta();
		this.quantidadeDiasMulta = conta.getQuantidadeDiasMulta();

		if (!isNull(conta.getAudit())) {
    		this.audit.setDataCreated(isNull(conta.getAudit().getDataCreated()) ? null : conta.getAudit().getDataCreated());
    		this.audit.setDataUpdated(isNull(conta.getAudit().getDataUpdated()) ? null : conta.getAudit().getDataUpdated());
		}
	}
}
