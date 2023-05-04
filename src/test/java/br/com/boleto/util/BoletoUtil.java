package br.com.boleto.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.persistence.dtos.BeneficiarioDto;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.BoletoResponseDto;
import br.com.boleto.persistence.dtos.BoletoRetornoDto;
import br.com.boleto.persistence.dtos.CancelamentoBoletoResponseDto;
import br.com.boleto.persistence.dtos.QrCodeBoletoDto;
import br.com.boleto.persistence.dtos.RegistroBoletoErroDto;
import br.com.boleto.persistence.dtos.RegistroBoletoResponseDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterRequestDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.boleto.persistence.dtos.StatusRequestDto;
import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.BeneficiarioFinal;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.entity.Desconto;
import br.com.boleto.persistence.entity.JurosMora;
import br.com.boleto.persistence.entity.Multa;
import br.com.boleto.persistence.entity.Pagador;

public class BoletoUtil {

	public static ArrayList<Map<String, Object>> prencheDadosRequestBodyBoletoDto(Integer idConta) {
		Map<String, Object> juros = new HashMap<>();
		juros.put("id", null);
		juros.put("tipo", 2);
		juros.put("porcentagem", 6.0);
		juros.put("valor", null);

		Map<String, Object> multa = new HashMap<>();
		multa.put("id", null);
		multa.put("tipo", 2);
		multa.put("data", "26.01.2023");
		multa.put("porcentagem", 2.0);
		multa.put("valor", null);

		Map<String, Object> pagador = new HashMap<>();
		pagador.put("tipoInscricao", 2);
		pagador.put("nome", "PARCEIRO 360");
		pagador.put("cep", "8225310");
		pagador.put("bairro", "CID ANTONIO EST CARV");
		pagador.put("telefone", null);
		pagador.put("id", null);
		pagador.put("numeroInscricao", "00711286000113");
		pagador.put("endereco", "Av 1442,176 null");
		pagador.put("cidade", "SÃO PAULO");
		pagador.put("uf", "SP");
		pagador.put("idparceiro", null);

		Map<String, Object> audit = new HashMap<>();
		audit.put("dataCreated", null);
		audit.put("dataUpdated", null);

		Map<String, Object> boletosItem = new HashMap<>();
		boletosItem.put("terceiroDesconto", null);
		boletosItem.put("numeroVariacaoCarteira", 10);
		boletosItem.put("orgaoNegativador", 0);
		boletosItem.put("campoUtilizacaoBeneficiario", null);
		boletosItem.put("indicadorAlterarDesconto", null);
		boletosItem.put("indicadorDispensarJuros", null);
		boletosItem.put("quantidadeDiasAceite", null);
		boletosItem.put("dataCredito", null);
		boletosItem.put("quantidadeTentativas", null);
		boletosItem.put("jurosMora", juros);
		boletosItem.put("codigoModalidade", 1);
		boletosItem.put("indicadorAceiteTituloVencido", "N");
		boletosItem.put("numeroTituloCliente", "123456");
		boletosItem.put("indicadorAlterarDataDesconto", null);
		boletosItem.put("indicadorCobrarMulta", null);
		boletosItem.put("url", null);
		boletosItem.put("valorAtual", null);
		boletosItem.put("valorJuroMoraRecebido", null);
		boletosItem.put("multa", multa);
		boletosItem.put("dataEmissao", "20.01.2023");
		boletosItem.put("numeroDiasLimiteRecebimento", 0);
		boletosItem.put("mensagemBloquetoOcorrencia", null);
		boletosItem.put("indicadorProtestar", null);
		boletosItem.put("indicadorDispensarMulta", null);
		boletosItem.put("txId", null);
		boletosItem.put("valorPago", null);
		boletosItem.put("valorMultaRecebido", null);
		boletosItem.put("indicadorPix", null);
		boletosItem.put("id", null);
		boletosItem.put("dataVencimento", "25.01.2023");
		boletosItem.put("codigoAceite", "N");
		boletosItem.put("pagador", pagador);
		boletosItem.put("indicadorSustacaoProtesto", null);
		boletosItem.put("emv", null);
		boletosItem.put("codigoLinhaDigitavel", null);
		boletosItem.put("isApi", null);
		boletosItem.put("status", 0);
		boletosItem.put("idapibanco", idConta);
		boletosItem.put("valorOriginal", 500.0);
		boletosItem.put("codigoTipoTitulo", 2);
		boletosItem.put("beneficiarioFinal", null);
		boletosItem.put("indicadorCancelarProtesto", null);
		boletosItem.put("indicadorAlterarNossoNumero", null);
		boletosItem.put("dataRegistro", null);
		boletosItem.put("dataCreditoLiquidacao", null);
		boletosItem.put("dataRegistroBanco", null);
		boletosItem.put("descricaoStatus", null);
		boletosItem.put("nossonumero", null);
		boletosItem.put("valorAbatimento", 0.0);
		boletosItem.put("descricaoTipoTitulo", "DM");
		boletosItem.put("codigoEstadoTituloCobranca", null);
		boletosItem.put("indicadorIncluirAbatimento", null);
		boletosItem.put("indicadorAlterarEnderecoPagador", null);
		boletosItem.put("estadoTituloCobranca", null);
		boletosItem.put("dataRecebimentoTitulo", null);
		boletosItem.put("indicadorAlterarValorOriginal", null);
		boletosItem.put("desconto", null);
		boletosItem.put("numeroConvenio", 10);
		boletosItem.put("quantidadeDiasProtesto", 0);
		boletosItem.put("indicadorPermissaoRecebimentoParcial", "N");
		boletosItem.put("indicadorNovaDataVencimento", null);
		boletosItem.put("indicadorCancelarAbatimento", null);
		boletosItem.put("indicadorAlterarPrazoBoletoVencido", null);
		boletosItem.put("contrato", null);
		boletosItem.put("nroDocumento", "0");
		boletosItem.put("audit", audit);
		boletosItem.put("segundoDesconto", null);
		boletosItem.put("numeroCarteira", 10);
		boletosItem.put("quantidadeDiasNegativacao", 0);
		boletosItem.put("numeroTituloBeneficiario", 4102);
		boletosItem.put("indicadorAtribuirDesconto", null);
		boletosItem.put("indicadorCobrarJuros", null);
		boletosItem.put("tipoNegativacao", null);
		boletosItem.put("dataMovimento", null);
		boletosItem.put("statusBanco", null);

		ArrayList<Map<String, Object>> itens = new ArrayList<Map<String, Object>>();
		itens.add(boletosItem);

		Map<String, Object> teste = new HashMap<>();
		teste.put("", itens);
		return itens;
	}

	public static Boleto criaBoleto() {
		Boleto boleto = new Boleto();
		boleto.setId(1);
		
		Conta conta = new Conta();
		conta.setId(1);
		boleto.setConta(conta);
		
		boleto.setNumeroConvenio(3128557);
		boleto.setNumeroCarteira(17);
		boleto.setNumeroVariacaoCarteira(35);
		boleto.setCodigoModalidade(1);
		boleto.setDataEmissao(new Timestamp(System.currentTimeMillis()));
		boleto.setDataVencimento(new Timestamp(System.currentTimeMillis()));
		boleto.setValorOriginal(50.0);
		boleto.setValorAbatimento(0.0);
		boleto.setQuantidadeDiasProtesto(0);
		boleto.setQuantidadeDiasNegativacao(0);
		boleto.setOrgaoNegativador(0);
		boleto.setIndicadorAceiteTituloVencido("N");
		boleto.setNumeroDiasLimiteRecebimento(0);
		boleto.setStatus(0);
		boleto.setCodigoAceite("N");
		boleto.setCodigoTipoTitulo(2);
		boleto.setDescricaoTipoTitulo("DM");
		boleto.setIndicadorPermissaoRecebimentoParcial("N");
		boleto.setNumeroTituloBeneficiario(4102);
		boleto.setCampoUtilizacaoBeneficiario("");
		boleto.setNumeroTituloCliente("123456");
		boleto.setMensagemBloquetoOcorrencia("");
		boleto.setIndicadorPix("N");
		
		Desconto desconto = new Desconto();
		desconto.setId(0);
		boleto.setDesconto(desconto);
		
		Desconto segundoDesconto = new Desconto();
		segundoDesconto.setId(0);
		boleto.setSegundoDesconto(segundoDesconto);
		
		Desconto terceiroDesconto = new Desconto();
		terceiroDesconto.setId(0);
		boleto.setTerceiroDesconto(terceiroDesconto);
		
		JurosMora jurosMora = new JurosMora();
		//jurosMora.setId(17);
		jurosMora.setTipo(2);
		jurosMora.setPorcentagem(2.0);
		boleto.setJurosMora(jurosMora);
		
		Multa multa = new Multa();
		//multa.setId(17);
		multa.setTipo(2);
		multa.setPorcentagem(2.0);
		boleto.setMulta(multa);

		Pagador pagador = new Pagador();
		pagador.setId(17);
		pagador.setTipoInscricao(2);
		pagador.setNumeroInscricao("93983472000192");
		pagador.setNome("Parceiro da sankhya");
		pagador.setEndereco("Av AYRTON SENNA,null null");
		pagador.setCep(38400660);
		pagador.setCidade("ARAGUARI");
		pagador.setBairro("BARRA DA TIJUCA");
		pagador.setUf("MG");
		boleto.setPagador(pagador);
		
		BeneficiarioFinal beneficiarioFinal = new BeneficiarioFinal();
		beneficiarioFinal.setId(0);
		boleto.setBeneficiarioFinal(beneficiarioFinal);
		
		boleto.setIndicadorNovaDataVencimento("N");
		boleto.setIndicadorAtribuirDesconto("N");
		boleto.setIndicadorAlterarDesconto("N");
		boleto.setIndicadorProtestar("N");
		boleto.setIndicadorSustacaoProtesto("N");
		boleto.setIndicadorCancelarProtesto("N");
		boleto.setIndicadorIncluirAbatimento("N");
		boleto.setIndicadorCancelarAbatimento("N");
		boleto.setIndicadorCobrarJuros("N");
		boleto.setIndicadorDispensarJuros("N");
		boleto.setIndicadorCobrarMulta("N");
		boleto.setIndicadorDispensarMulta("N");
		boleto.setIndicadorNegativar("N");
		boleto.setIndicadorAlterarNossoNumero("N");
		boleto.setIndicadorAlterarEnderecoPagador("N");
		boleto.setIndicadorAlterarPrazoBoletoVencido("N");
		boleto.setStatusBanco(0);
		boleto.setNroDocumento("1000");
		boleto.setQuantidadeTentativas(0);
		
		return boleto;
	}

	public static List<StatusRequestDto> criaStatusRequestDto() {
		
		List<StatusRequestDto> statusRequestList = new ArrayList<>();
		
		Integer[] nuFins = {1};
		Integer[] status = {1};
		
		StatusRequestDto statusRequestDto = new StatusRequestDto();
		statusRequestDto.setIpApiBanco(1);
		statusRequestDto.setNufins(nuFins);
		statusRequestDto.setStatus(status);
		
		statusRequestList.add(statusRequestDto);
		
		return statusRequestList;
	}

	public static ArrayList<BoletoDto> criaBoletoDto() {
		ArrayList<BoletoDto> boletoList = new ArrayList<>();
		BoletoDto boletoDto = new BoletoDto();
		boletoDto.setId(1);
		boletoDto.setIdapibanco(1);
		boletoDto.setNumeroConvenio(3128557);
		boletoDto.setNumeroCarteira(17);
		boletoDto.setNumeroVariacaoCarteira(35);
		boletoDto.setCodigoModalidade(1);
		boletoDto.setDataEmissao(DateUtil.convertTimeStampToString(new Timestamp(System.currentTimeMillis())));
		boletoDto.setDataVencimento(DateUtil.convertTimeStampToString(new Timestamp(System.currentTimeMillis())));
		boletoDto.setValorOriginal(50.0);
		boletoDto.setValorAbatimento(0.0);
		boletoDto.setQuantidadeDiasProtesto(0);
		boletoDto.setQuantidadeDiasNegativacao(0);
		boletoDto.setOrgaoNegativador(0);
		boletoDto.setIndicadorAceiteTituloVencido("N");
		boletoDto.setNumeroDiasLimiteRecebimento(0);
		boletoDto.setStatus(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
		boletoDto.setCodigoAceite("N");
		boletoDto.setCodigoTipoTitulo(2);
		boletoDto.setDescricaoTipoTitulo("DM");
		boletoDto.setIndicadorPermissaoRecebimentoParcial("N");
		boletoDto.setNumeroTituloBeneficiario(4102);
		boletoDto.setCampoUtilizacaoBeneficiario("");
		boletoDto.setNumeroTituloCliente("123456");
		boletoDto.setMensagemBloquetoOcorrencia("");
		boletoDto.setIndicadorPix("N");
		boletoDto.setDesconto(BoletoUtil.criaDesconto());
		boletoDto.setSegundoDesconto(BoletoUtil.criaDesconto());
		boletoDto.setTerceiroDesconto(BoletoUtil.criaDesconto());
		boletoDto.setBeneficiarioFinal(BoletoUtil.criaBeneficiarioFinal());
		boletoDto.setJurosMora(BoletoUtil.criaDesconJurosMora());
		boletoDto.setMulta(BoletoUtil.criaMulta());
		boletoDto.setPagador(BoletoUtil.criaPagador());
		boletoDto.setIndicadorNovaDataVencimento("N");
		boletoDto.setIndicadorAtribuirDesconto("N");
		boletoDto.setIndicadorAlterarDesconto("N");
		boletoDto.setIndicadorProtestar("N");
		boletoDto.setIndicadorSustacaoProtesto("N");
		boletoDto.setIndicadorCancelarProtesto("N");
		boletoDto.setIndicadorIncluirAbatimento("N");
		boletoDto.setIndicadorCancelarAbatimento("N");
		boletoDto.setIndicadorCobrarJuros("N");
		boletoDto.setIndicadorDispensarJuros("N");
		boletoDto.setIndicadorCobrarMulta("N");
		boletoDto.setIndicadorDispensarMulta("N");
		boletoDto.setIndicadorNegativar("N");
		boletoDto.setIndicadorAlterarNossoNumero("N");
		boletoDto.setIndicadorAlterarEnderecoPagador("N");
		boletoDto.setIndicadorAlterarPrazoBoletoVencido("N");
		boletoDto.setStatusBanco(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus());
		boletoDto.setNroDocumento("1000");
		boletoDto.setQuantidadeTentativas(0);
		boletoDto.setContrato(null);
		boletoDto.setAudit(new Audit());
		boletoDto.setTipoNegativacao(null);
		boletoDto.setDataMovimento(null);
		boletoDto.setQuantidadeDiasAceite(null);
		boletoDto.setDataCredito(null);
		boletoDto.setIndicadorAlterarDataDesconto(null);
		boletoDto.setUrl(null);
		boletoDto.setValorAtual(null);
		boletoDto.setValorJuroMoraRecebido(null);
		boletoDto.setTxId(null);
		boletoDto.setValorPago(null);
		boletoDto.setValorMultaRecebido(null);
		boletoDto.setEmv(null);
		boletoDto.setCodigoLinhaDigitavel(null);
		boletoDto.setIsApi(null);
		boletoDto.setDataRegistro(null);
		boletoDto.setDataCreditoLiquidacao(null);
		boletoDto.setDataRegistroBanco(null);
		boletoDto.setDescricaoStatus(null);
		boletoDto.setNossonumero(null);
		boletoDto.setCodigoEstadoTituloCobranca(null);
		boletoDto.setEstadoTituloCobranca(null);
		boletoDto.setDataRecebimentoTitulo(null);
		boletoDto.setIndicadorAlterarValorOriginal(null);

		boletoList.add(boletoDto);

		return boletoList;
	}

	public static Map<String, Object> criaStatusBoletoFilterRequest() {

		List<Integer> status = new ArrayList<>();
		status.add(1);
		List<Integer> numeroTituloBeneficiario = new ArrayList<>();
		numeroTituloBeneficiario.add(4102);
		List<Integer> idApiBanco = new ArrayList<>();
		idApiBanco.add(1);

		Map<String, Object> statusBoletoFilterRequest = new HashMap<>();
		statusBoletoFilterRequest.put("status", status);
		statusBoletoFilterRequest.put("numeroTituloBeneficiario", numeroTituloBeneficiario);
		statusBoletoFilterRequest.put("idApiBanco", idApiBanco);
		statusBoletoFilterRequest.put("numeroTituloCliente", "123456");
		statusBoletoFilterRequest.put("dataVencimentoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterRequest.put("dataVencimentoFinal", null);
		statusBoletoFilterRequest.put("dataEmissaoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterRequest.put("dataEmissaoFinal", null);
		statusBoletoFilterRequest.put("numeroInscricaoPagador", 17);
		statusBoletoFilterRequest.put("contas", new ArrayList<>());

		return statusBoletoFilterRequest;
	}

	public static ArrayList<StatusBoletoFilterRequestDto> criaStatusBoletoFilterRequestList() {

		List<Integer> status = new ArrayList<>();
		status.add(1);
		List<Integer> numeroTituloBeneficiario = new ArrayList<>();
		numeroTituloBeneficiario.add(4102);
		List<Integer> idApiBanco = new ArrayList<>();
		idApiBanco.add(1);

		Map<String, Object> statusBoletoFilterRequest = new HashMap<>();
		statusBoletoFilterRequest.put("status", status);
		statusBoletoFilterRequest.put("numeroTituloBeneficiario", numeroTituloBeneficiario);
		statusBoletoFilterRequest.put("idApiBanco", idApiBanco);
		statusBoletoFilterRequest.put("numeroTituloCliente", "123456");
		statusBoletoFilterRequest.put("dataVencimentoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterRequest.put("dataVencimentoFinal", null);
		statusBoletoFilterRequest.put("dataEmissaoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterRequest.put("dataEmissaoFinal", null);
		statusBoletoFilterRequest.put("numeroInscricaoPagador", "17");
		statusBoletoFilterRequest.put("contas", new ArrayList<>());

		ArrayList<StatusBoletoFilterRequestDto> itens = new ArrayList<StatusBoletoFilterRequestDto>();
		itens.add(new StatusBoletoFilterRequestDto(statusBoletoFilterRequest));

		return itens;
	}

	public static Map<String, Object> criaDadosAtualizacaoBoletosRequest() {


		Map<String, Object> atualizaBoletosRequest = new HashMap<>();
		atualizaBoletosRequest.put("numeroTituloBeneficiario", 4102);
		atualizaBoletosRequest.put("idApiBanco", 1);
		atualizaBoletosRequest.put("numeroTituloCliente", "123456");

		return atualizaBoletosRequest;
	}

	public static StatusBoletoFilterSearchRequestDto criaStatusBoletoFilterSearchRequestDto() {

		List<Integer> status = new ArrayList<>();
		status.add(1);
		List<Integer> numeroTituloBeneficiario = new ArrayList<>();
		numeroTituloBeneficiario.add(4102);
		List<Integer> idApiBanco = new ArrayList<>();
		idApiBanco.add(1);
		List<String> numeroTituloCliente = new ArrayList<>();
		numeroTituloCliente.add("123456");
		List<Integer> statusJob = new ArrayList<>();
		statusJob.add(1);
		List<Integer> jobs = new ArrayList<>();
		jobs.add(1);
		List<Integer> contas = new ArrayList<>();
		contas.add(1);

		StatusBoletoFilterSearchRequestDto statusBoletoFilterSearchRequestDto = new StatusBoletoFilterSearchRequestDto();
		statusBoletoFilterSearchRequestDto.setStatus(status);
		statusBoletoFilterSearchRequestDto.setNumeroTituloBeneficiario(numeroTituloBeneficiario);
		statusBoletoFilterSearchRequestDto.setIdApiBanco(idApiBanco);
		statusBoletoFilterSearchRequestDto.setNumeroTituloCliente(numeroTituloCliente);
		statusBoletoFilterSearchRequestDto.setStatusJob(statusJob);
		statusBoletoFilterSearchRequestDto.setJob(jobs);
		statusBoletoFilterSearchRequestDto.setNomeCliente("Teste");
		statusBoletoFilterSearchRequestDto.setDataVencimentoInicial(new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.setDataVencimentoFinal(new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.setDataEmissaoInicial(new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.setDataEmissaoFinal(new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.setDataLiquidacaoInicial(new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.setDataLiquidacaoFinal(new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.setNumeroInscricaoPagador("17");
		statusBoletoFilterSearchRequestDto.setContas(contas);
		
		/*Map<String, Object> statusBoletoFilterSearchRequestDto = new HashMap<>();
		statusBoletoFilterSearchRequestDto.put("status", status);
		statusBoletoFilterSearchRequestDto.put("numeroTituloBeneficiario", numeroTituloBeneficiario);
		statusBoletoFilterSearchRequestDto.put("idApiBanco", idApiBanco);
		statusBoletoFilterSearchRequestDto.put("numeroTituloCliente", "123456");
		statusBoletoFilterSearchRequestDto.put("statusJob", statusJob);
		statusBoletoFilterSearchRequestDto.put("job", jobs);
		statusBoletoFilterSearchRequestDto.put("dataVencimentoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.put("dataVencimentoFinal", null);
		statusBoletoFilterSearchRequestDto.put("dataEmissaoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.put("dataEmissaoFinal", null);
		statusBoletoFilterSearchRequestDto.put("dataLiquidacaoInicial", new Timestamp(System.currentTimeMillis()).toString());
		statusBoletoFilterSearchRequestDto.put("dataLiquidacaoFinal", null);
		statusBoletoFilterSearchRequestDto.put("numeroInscricaoPagador", "17");
		statusBoletoFilterSearchRequestDto.put("contas", contas);*/
		
		return statusBoletoFilterSearchRequestDto;
	}

	public static Desconto criaDesconto() {
		Desconto desconto = new Desconto();
		desconto.setTipo(1);
		desconto.setValor(2.0);
		desconto.setPorcentagem(5.0);
		desconto.setDataExpiracao("25/05/2300");
		return desconto;
	}


	public static JurosMora criaDesconJurosMora() {
		JurosMora jurosMora = new JurosMora();
		jurosMora.setId(17);
		jurosMora.setTipo(2);
		jurosMora.setPorcentagem(2.0);
		jurosMora.setValor(2.0);
		return jurosMora;
	}

	public static Multa criaMulta() {
		Multa multa = new Multa();
		multa.setId(17);
		multa.setTipo(2);
		multa.setPorcentagem(2.0);
		multa.setValor(2.0);
		multa.setData("25/05/2300");
		return multa;
	}

	public static Pagador criaPagador() {
		Pagador pagador = new Pagador();
		pagador.setId(17);
		pagador.setTipoInscricao(2);
		pagador.setNumeroInscricao("93983472000192");
		pagador.setNome("Parceiro da sankhya");
		pagador.setEndereco("Av AYRTON SENNA,null null");
		pagador.setCep(38400660);
		pagador.setCidade("ARAGUARI");
		pagador.setBairro("BARRA DA TIJUCA");
		pagador.setUf("MG");
		pagador.setNumeroInscricao("123456");
		pagador.setTelefone("4899948484984");
		pagador.setIdparceiro(1);
		return pagador;
	}

	public static BeneficiarioFinal criaBeneficiarioFinal() {
		BeneficiarioFinal beneficiarioFinal = new BeneficiarioFinal();
		beneficiarioFinal.setId(0);
		beneficiarioFinal.setTipoInscricao(1);
		beneficiarioFinal.setNumeroInscricao("123456");
		beneficiarioFinal.setNome("Teste Teste");
		return beneficiarioFinal;
	}

	public static RegistroBoletoResponseDto criaRegistroBoletoResponseDto() {
		RegistroBoletoResponseDto registroBoletoResponseDto = new RegistroBoletoResponseDto();
		registroBoletoResponseDto.setNumero("1");
		registroBoletoResponseDto.setNumeroCarteira(1);
		registroBoletoResponseDto.setNumeroVariacaoCarteira(1);
		registroBoletoResponseDto.setCodigoCliente(1);
		registroBoletoResponseDto.setLinhaDigitavel("123");
		registroBoletoResponseDto.setCodigoBarraNumerico("123");
		registroBoletoResponseDto.setNumeroContratoCobranca(123);
		registroBoletoResponseDto.setBeneficiario(new BeneficiarioDto());
		registroBoletoResponseDto.setQrCode(new QrCodeBoletoDto());
		return registroBoletoResponseDto;
	}

	public static BoletoResponseDto criaBoletoResponseDto() {
		BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
		boletoResponseDto.setBoleto(criaBoletoDto().get(0));
		boletoResponseDto.setType(true);
		boletoResponseDto.setCodcta(1);
		boletoResponseDto.setId(1);
		boletoResponseDto.setMessage("Teste");
		return boletoResponseDto;
	}

	public static BoletoRetornoDto criaBoletoRetornoDto() {
		BoletoRetornoDto boletoRetornoDto = new BoletoRetornoDto();
		boletoRetornoDto.setId(1);
		boletoRetornoDto.setIdboleto(1);
		boletoRetornoDto.setCodigoEstadoTituloCobranca(1);
		return boletoRetornoDto;
	}

	public static CancelamentoBoletoResponseDto criaCancelamentoBoletoResponseDto() {
		CancelamentoBoletoResponseDto cancelamentoBoletoResponseDto = new CancelamentoBoletoResponseDto();
		return cancelamentoBoletoResponseDto;

	}
}
