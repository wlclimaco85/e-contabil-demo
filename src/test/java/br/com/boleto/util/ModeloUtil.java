package br.com.boleto.util;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.dtos.ContaCredenciamentoDto;
import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.dtos.ContaPesquisaDto;
import br.com.boleto.persistence.dtos.ContaSearchRequestDto;
import br.com.boleto.persistence.dtos.ContratoDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import br.com.boleto.persistence.dtos.LogEnvioSearchRequestDto;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.dtos.ParceiroSearchRequestDto;
import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Banco;
import br.com.boleto.persistence.entity.Certificado;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.entity.ContaNaoCredenciada;
import br.com.boleto.persistence.entity.Contrato;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.persistence.entity.Parceiro;
import br.com.boleto.persistence.entity.Servico;
import br.com.boleto.persistence.entity.Termo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeloUtil {

    public static ContaDto criaContaDto(){
        ContaDto contaDto = new ContaDto();
        contaDto.setCodbco(1);
        contaDto.setCodage(7510);
        contaDto.setCodcta(30);
        contaDto.setCodctabco(123456);
        contaDto.setConvenio(3128557);
        contaDto.setCarteira(17);
        contaDto.setVariacao(35);
        contaDto.setModalidade(1);
        contaDto.setUltnumbol("1475523");
        contaDto.setStatusapi("S");
        contaDto.setIdparceiro(647);
        contaDto.setNomeparc("Sankhya Teste");
        contaDto.setClientid(ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
        contaDto.setClientsecret(ConstantesTeste.CLIENT_SECRET_BANCO_BRASIL);
        contaDto.setDescredenciar(false);
        contaDto.setTipobase(1);
        contaDto.setIndicadorpix("false");

        return contaDto;
    }

	public static ContaCredenciamentoDto criaContaCredenciamentoDto(){
		ContaCredenciamentoDto contaCredenciamentoDto = new ContaCredenciamentoDto();
		contaCredenciamentoDto.setCodbco(1);
		contaCredenciamentoDto.setCodage(7510);
		contaCredenciamentoDto.setCodcta(30);
		contaCredenciamentoDto.setCodctabco(123456);
		contaCredenciamentoDto.setConvenio(3128557);
		contaCredenciamentoDto.setCarteira(17);
		contaCredenciamentoDto.setVariacao(35);
		contaCredenciamentoDto.setModalidade(1);
		contaCredenciamentoDto.setUltnumbol("1475523");
		contaCredenciamentoDto.setStatusapi("S");
		contaCredenciamentoDto.setIdparceiro(647);
		contaCredenciamentoDto.setNomeparc("Sankhya Teste");
		contaCredenciamentoDto.setClientid(ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
		contaCredenciamentoDto.setClientsecret(ConstantesTeste.CLIENT_SECRET_BANCO_BRASIL);
		contaCredenciamentoDto.setDescredenciar(false);
		contaCredenciamentoDto.setTipobase(1);
		contaCredenciamentoDto.setIndicadorpix("false");

		return contaCredenciamentoDto;
	}
    
    public static Conta criaConta(){
        Conta conta = new Conta();
        Banco banco = new Banco();
        banco.setId(1);
        conta.setBanco(banco);
        conta.setCodage(7510);
        conta.setCodcta(30);
        conta.setCodctabco(123456);
        conta.setConvenio(3128557);
        conta.setCarteira(17);
        conta.setVariacao(35);
        conta.setModalidade(1);
        conta.setUltnumbol("1475523");
        conta.setStatusapi("S");
        conta.setParceiro(criaParceiro());
        conta.setClientid(ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
        conta.setClientsecret(ConstantesTeste.CLIENT_SECRET_BANCO_BRASIL);

        return conta;
    }

	public static ContaPesquisaDto criaContaPesquisaDto(){
		ContaPesquisaDto contaPesquisaDto = new ContaPesquisaDto();
		contaPesquisaDto.setCodbco(1);
		contaPesquisaDto.setCodage(7510);
		contaPesquisaDto.setCodcta(30);
		contaPesquisaDto.setCodctabco(123456);
		contaPesquisaDto.setConvenio(3128557);
		contaPesquisaDto.setCarteira(17);
		contaPesquisaDto.setVariacao(35);
		contaPesquisaDto.setModalidade(1);
		contaPesquisaDto.setUltnumbol("1475523");
		contaPesquisaDto.setStatusapi("S");
		contaPesquisaDto.setIdparceiro(647);
		contaPesquisaDto.setNomeparc("Sankhya Teste");
		contaPesquisaDto.setTipobase(1);
		return contaPesquisaDto;
	}

    public static Banco criaBanco(){
        Banco banco = new Banco();
        banco.setId(1);
        banco.setNome("Banco do Brasil S.A");
        banco.setUrlhomol("https://api.bb.com.br/cobrancas/v2");
        banco.setUrloauthhomol("https://oauth.sandbox.bb.com.br");
        banco.setUrlprod("https://api.hm.bb.com.br/cobrancas/v2");
        banco.setUrloauthprod("https://oauth.bb.com.br");

        return banco;
    }
    
    public static ContratoDto criaContratoDto() {
    	ContratoDto contratoDto = new ContratoDto();
    	contratoDto.setIdapibanco(1);
    	contratoDto.setIdparceiro(647);
    	contratoDto.setCodserv(1);
    	contratoDto.setUsuContratante("Sankhya Teste");
    	contratoDto.setVersaoAceite(1);
    	
    	return contratoDto;
    }
    
    public static Contrato criaContrato(Parceiro parceiro, Servico servico, Termo termo, Conta conta) {
    	Contrato contrato = new Contrato();
    	contrato.setCodcontrato(1);
    	contrato.setConta(conta);
    	contrato.setParceiro(parceiro);
    	contrato.setServico(servico);
    	contrato.setTermo(termo);
    	contrato.setValorContratado(0.04);
    	contrato.setUsuContratante("Sankhya Teste");
    	contrato.setAtivo("S");

    	return contrato;
    }
    
    public static Map<String, Object> criaContratoRequest()	{
		
		Map<String, Object> contratoRequest = new HashMap<>();
		contratoRequest.put("idapibanco", 1);
		contratoRequest.put("idparceiro", 647);
		contratoRequest.put("codserv", 1);
		contratoRequest.put("usuContratante", "Sankhya Teste");
		contratoRequest.put("versaoAceite", 1);
				
		return contratoRequest;
	}
    
    public static Parceiro criaParceiro() {
    	Parceiro parceiro = new Parceiro();
    	parceiro.setId(647);
    	parceiro.setNome("Sankhya Teste");
    	
    	return parceiro;
    }
    
	public static Servico criaServico() {
    	Servico servico = new Servico();
    	servico.setCodserv(1);
    	servico.setNomeServico("Boleto Rápido por API - Banco do Brasil");
    	servico.setValor(0.04);
    	
    	return servico;
    }
    
    public static Termo criarTermo() {
    	Termo termo = new Termo();
    	termo.setVersao(1);
    	termo.setHtmlTermo("<div>Teste</div>");
    	termo.setDataCreated(java.time.LocalDateTime.now());
    	
    	return termo;
	}

	public static LogEnvio criarLogEnvio() {
		
		LogEnvio logEnvio = new LogEnvio();
		logEnvio.setId(1);
		logEnvio.setStatus(StatusBoletoEnum.LIQUIDADO);
		logEnvio.setStatusBanco(StatusBoletoEnum.LIQUIDADO);
		logEnvio.setTipoEvento(TipoEventoEnum.ALTERACAO);
		logEnvio.setSituacao(SituacaoEnum.PENDENTE_ENVIO);
		logEnvio.setNossonumero("123456");
		logEnvio.setStacktrace("O teste das credenciais com o banco falhou");
		
		return logEnvio;
	}
	
	public static LogEnvioSearchRequestDto criarLogEnvioSearchRequestDto() {
		
		List<Integer> status = new ArrayList<>();
		status.add(1);
		
		LogEnvioSearchRequestDto logEnvioSearchRequestDto = new LogEnvioSearchRequestDto();
		
		logEnvioSearchRequestDto.setNumeroTituloBeneficiario(4102);
		logEnvioSearchRequestDto.setIdApiBanco(1);
		logEnvioSearchRequestDto.setNumeroTituloCliente("123456");
		logEnvioSearchRequestDto.setStatus(status);
		logEnvioSearchRequestDto.setTipoEvento(null);
		logEnvioSearchRequestDto.setSituacao(null);
		logEnvioSearchRequestDto.setDataOcorrenciaInicial(new Timestamp(System.currentTimeMillis()).toString());
		logEnvioSearchRequestDto.setDataOcorrenciaFinal(null);
		
		return logEnvioSearchRequestDto;
		
	}

	public static LogEnvioDto criaLogEnvioDto() {
		LogEnvioDto logEnvioDto = new LogEnvioDto();
		logEnvioDto.setId(123);
		logEnvioDto.setNossonumero("ABC123");
		logEnvioDto.setDhocorrencia("2022-03-08T10:00:00");
		logEnvioDto.setMensagem("Erro ao processar pedido");
		logEnvioDto.setStacktrace("java.lang.NullPointerException: null pointer exception");
		logEnvioDto.setStatus(500);
		logEnvioDto.setDescricaoStatus(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO);
		logEnvioDto.setIdTipoEvento(1);
		logEnvioDto.setTipoEvento(TipoEventoEnum.EMISSAO);
		logEnvioDto.setDescricaoTipoEvento(TipoEventoEnum.EMISSAO);
		logEnvioDto.setSituacao(2);
		logEnvioDto.setDescricaoSituacao(SituacaoEnum.PROCESSADA);
		logEnvioDto.setStatusBanco(0);
		return logEnvioDto;
	}
	
	public static ContaSearchRequestDto criaContaSearchRequestDto() {
		
		ContaSearchRequestDto contaSearchRequestDto = new ContaSearchRequestDto();
		
		contaSearchRequestDto.setId(1);
		contaSearchRequestDto.setCodctabco(54786);
		contaSearchRequestDto.setConvenio(3228569);
		contaSearchRequestDto.setStatusapi("S");
		contaSearchRequestDto.setRegistrobase(1);
		contaSearchRequestDto.setNomeParceiro("Sankhya Teste");
		
		return contaSearchRequestDto;
		
	}

	public static ParceiroSearchRequestDto criaParceiroSearchRequestDto() {
		
		ParceiroSearchRequestDto parceiroSearchRequestDto = new ParceiroSearchRequestDto();
		
		parceiroSearchRequestDto.setNome("Teste Sankhya");
		
		return parceiroSearchRequestDto;
	}

	public static Certificado criaCerticado() {
		Certificado certificado = new Certificado();
		certificado.setId(1);
		certificado.setClientid("clientId");
		certificado.setClientsecret("clientSecret");
		certificado.setToken("token");
		byte[] csr = "csr".getBytes();
		certificado.setCsr(csr);
		byte[] key = "key".getBytes();
		certificado.setKey(key);
		byte[] crt = "crt".getBytes();
		certificado.setCrt(crt);
		byte[] p12 = "p12".getBytes();
		certificado.setConta(criaConta());
		return certificado;
	}

	public static Audit criaAudit() {
		Audit audit = new Audit();
		audit.setDataUpdated(DateUtil.getDataAtual());
		audit.setDataCreated(DateUtil.getDataAtual());
		return audit;
	}

	public static LoginDto criaLoginDto() {
		LoginDto loginDto = new LoginDto();
		loginDto.setClient_id("");
		loginDto.setClient_secret("secret_");
		loginDto.setBanco(BancoEnum.BANCO_DO_BRASIL);
		loginDto.setConta_id(1);
		loginDto.setToken("token_");
		loginDto.setChave_sessao("sessao_") ;
		return loginDto;
	}

	public static LoginResponseDto criaLoginResponseDto() {
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		loginResponseDto.setAccess_token("123456");
		loginResponseDto.setExpires_in(7);
		loginResponseDto.setToken_type("12345");
		return loginResponseDto;
	}

	public static ContaNaoCredenciada criaContaNaoCredenciada(){
		ContaNaoCredenciada contaNaoCredenciada = new ContaNaoCredenciada();
		Banco banco = new Banco();
		banco.setId(1);
		contaNaoCredenciada.setBanco(banco);
		contaNaoCredenciada.setCodage(7510);
		contaNaoCredenciada.setCodcta(30);
		contaNaoCredenciada.setCodctabco(123456);
		contaNaoCredenciada.setConvenio(3128557);
		contaNaoCredenciada.setCarteira(17);
		contaNaoCredenciada.setVariacao(35);
		contaNaoCredenciada.setModalidade(1);
		contaNaoCredenciada.setUltnumbol("1475523");
		contaNaoCredenciada.setStatusapi("S");
		contaNaoCredenciada.setParceiro(criaParceiro());
		contaNaoCredenciada.setClientid(ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
		contaNaoCredenciada.setClientsecret(ConstantesTeste.CLIENT_SECRET_BANCO_BRASIL);

		return contaNaoCredenciada;
	}

	
}
