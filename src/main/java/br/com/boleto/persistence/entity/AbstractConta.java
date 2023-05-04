package br.com.boleto.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractConta {

	@Tolerate
	public AbstractConta() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(targetEntity = Banco.class)
	@JoinColumn(nullable = false, name = "banco_id")
	private Banco banco;

	private Integer codage;

	private Integer codcta;

	private Integer codctabco;

	private Integer convenio;

	private Integer carteira;

	private Integer variacao;

	private Integer modalidade;

	private String ultnumbol;

	private String statusapi;

	private Integer registrobase;

	private String clientid;

	private String clientsecret;

	@Column(name = "dh_desativacao")
	private LocalDateTime dataDesativacao;

	private String indicadorPix;

	@Embedded
	private Audit audit = new Audit();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "idparceiro", referencedColumnName = "id")
	private Parceiro parceiro;

	private String descricao;

	private Integer codigoEmpresa;

	@Column(nullable = false, columnDefinition = "VARCHAR(1) DEFAULT 'S'")
	@Generated(GenerationTime.INSERT)
	private String emiteBoleto;

	@Column(nullable = false, columnDefinition = "VARCHAR(1) DEFAULT 'S'")
	@Generated(GenerationTime.INSERT)
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


}

