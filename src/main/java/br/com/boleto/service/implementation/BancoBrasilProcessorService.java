package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.exception.FailedAuthenticationException;
import br.com.boleto.persistence.dtos.AlteracaoBoletoResponseDto;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.BoletoResponseDto;
import br.com.boleto.persistence.dtos.BoletoRetornoDto;
import br.com.boleto.persistence.dtos.CancelamentoBoletoResponseDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.dtos.RegistroBoletoResponseDto;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.BoletoRetorno;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.repository.BoletoRetornoRepository;
import br.com.boleto.persistence.repository.ContaRepository;
import br.com.boleto.service.AuthenticationFactory;
import br.com.boleto.service.BoletoProcessorService;
import br.com.boleto.util.DateUtil;
import br.com.boleto.util.GetRetornoErro;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BancoBrasilProcessorService implements BoletoProcessorService {
    @Autowired
    private AuthenticationFactory authenticationFactory;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private LogEnvioService logEnvioService;

    @Autowired
    private BoletoRetornoRepository boletoRetornoRepository;

    @Value("${url.api.bb.cobranca}")
    private String urlApiBbCobranca;

    @Value("${gw-dev-app-key}")
    private String gwDevAppKey;

    @Override
    public BancoEnum getType() {
        return BancoEnum.BANCO_DO_BRASIL;
    }
    
    private String getDefaultQueryParameters(String params, String query) {
		return urlApiBbCobranca + params + gwDevAppKey+ query;
	}
	
	private HttpHeaders createJSONHeader(LoginResponseDto loginResponseDto) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + loginResponseDto.getAccess_token());
		headers.add("Content-Type", "application/json");
		return headers;
	}

    @Override
    public void registraBoleto(Conta contas) {
        String url = urlApiBbCobranca + gwDevAppKey;
        if (contas != null) {
                LoginResponseDto loginResponseDto = autenticaBanco(contas);
                if (loginResponseDto.getAccess_token() != null) {
                    List<BoletoDto> boletos = boletoService.buscaBoletosPorStatus(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus(), contas.getId());
                    if (boletos != null && !boletos.isEmpty()) {
                        for (BoletoDto boletoDto : boletos) {
                            Map<String, Object> body = new HashMap<>();
                            if (loginResponseDto.getAccess_token() != null) {
                                try {
                                    Map<String, Object> desconto = new HashMap<>();
                                    Map<String, Object> segundoDesconto = new HashMap<>();
                                    Map<String, Object> terceiroDesconto = new HashMap<>();

                                    Map<String, Object> jurosMora = new HashMap<>();
                                    Map<String, Object> multa = new HashMap<>();
                                    Map<String, Object> pagador = new HashMap<>();
                                    Map<String, Object> beneficiarioFinal = new HashMap<>();

                                    if (boletoDto != null && boletoDto.getDesconto() != null) {
                                        desconto.put("tipo", boletoDto.getDesconto().getTipo());
                                        desconto.put("dataExpiracao", boletoDto.getDesconto().getDataExpiracao());
                                        desconto.put("porcentagem", boletoDto.getDesconto().getPorcentagem());
                                        desconto.put("valor", boletoDto.getDesconto().getValor());
                                    }
                                    if (boletoDto != null && boletoDto.getSegundoDesconto() != null) {
                                        segundoDesconto.put("tipo", boletoDto.getSegundoDesconto().getTipo());
                                        segundoDesconto.put("dataExpiracao", boletoDto.getSegundoDesconto().getDataExpiracao());
                                        segundoDesconto.put("porcentagem", boletoDto.getSegundoDesconto().getPorcentagem());
                                        segundoDesconto.put("valor", boletoDto.getSegundoDesconto().getValor());
                                    }
                                    if (boletoDto != null && boletoDto.getTerceiroDesconto() != null) {
                                        terceiroDesconto.put("tipo", boletoDto.getTerceiroDesconto().getTipo());
                                        terceiroDesconto.put("dataExpiracao", boletoDto.getTerceiroDesconto().getDataExpiracao());
                                        terceiroDesconto.put("porcentagem", boletoDto.getTerceiroDesconto().getPorcentagem());
                                        terceiroDesconto.put("valor", boletoDto.getTerceiroDesconto().getValor());
                                    }
                                    if (boletoDto != null && boletoDto.getJurosMora() != null) {
                                        jurosMora.put("tipo", boletoDto.getJurosMora().getTipo());           
                                        if(!boletoDto.getJurosMora().getTipo().equals(0) || !boletoDto.getJurosMora().getTipo().equals(3)) {
	                                        jurosMora.put("porcentagem", boletoDto.getJurosMora().getPorcentagem());
	                                        jurosMora.put("valor", boletoDto.getJurosMora().getValor());
                                        }
                                    }
                                    if (boletoDto != null && boletoDto.getMulta() != null) {
                                        multa.put("tipo", boletoDto.getMulta().getTipo());
                                        if(!boletoDto.getMulta().getTipo().equals(0)) {
                                        	multa.put("data", boletoDto.getMulta().getData());
                                            multa.put("porcentagem", boletoDto.getMulta().getPorcentagem());
                                            multa.put("valor", boletoDto.getMulta().getValor());
                                        }   
                                    }
                                    if (boletoDto != null && boletoDto.getPagador() != null) {
                                        pagador.put("tipoInscricao", boletoDto.getPagador().getTipoInscricao());
                                        pagador.put("numeroInscricao", boletoDto.getPagador().getNumeroInscricao());
                                        pagador.put("nome", boletoDto.getPagador().getNome());
                                        pagador.put("endereco", boletoDto.getPagador().getEndereco());
                                        pagador.put("cep", boletoDto.getPagador().getCep());
                                        pagador.put("cidade", boletoDto.getPagador().getCidade());
                                        pagador.put("bairro", boletoDto.getPagador().getBairro());
                                        pagador.put("uf", boletoDto.getPagador().getUf());
                                        if (boletoDto.getPagador().getTelefone() != null) {
                                            pagador.put("telefone", boletoDto.getPagador().getTelefone().replaceAll("[^0-9.]", ""));
                                        }
                                    }
                                    if (boletoDto != null && boletoDto.getBeneficiarioFinal() != null) {
                                        beneficiarioFinal.put("tipoInscricao", boletoDto.getBeneficiarioFinal().getTipoInscricao());
                                        beneficiarioFinal.put("numeroInscricao", boletoDto.getBeneficiarioFinal().getNumeroInscricao());
                                        beneficiarioFinal.put("nome", boletoDto.getBeneficiarioFinal().getNome());
                                    }

                                    body.put("numeroConvenio", boletoDto.getNumeroConvenio());
                                    body.put("numeroCarteira", boletoDto.getNumeroCarteira());
                                    body.put("numeroVariacaoCarteira", boletoDto.getNumeroVariacaoCarteira());
                                    body.put("codigoModalidade", boletoDto.getCodigoModalidade());
                                    body.put("dataEmissao", DateUtil.getDataAtual().isBefore(DateUtil.convertStringToTimestamp(boletoDto.getDataEmissao()).toLocalDateTime()) ? DateUtil.convertLocalDateTimeToString(DateUtil.getDataAtual(), null) : boletoDto.getDataEmissao());
                                    body.put("dataVencimento", boletoDto.getDataVencimento());
                                    body.put("valorOriginal", boletoDto.getValorOriginal());
                                    body.put("valorAbatimento", boletoDto.getValorAbatimento());
                                    body.put("quantidadeDiasProtesto", boletoDto.getQuantidadeDiasProtesto());
                                    body.put("quantidadeDiasNegativacao", boletoDto.getQuantidadeDiasNegativacao());
                                    body.put("orgaoNegativador", boletoDto.getOrgaoNegativador());
                                    body.put("indicadorAceiteTituloVencido", boletoDto.getIndicadorAceiteTituloVencido());
                                    body.put("numeroDiasLimiteRecebimento", boletoDto.getNumeroDiasLimiteRecebimento());
                                    body.put("codigoAceite", boletoDto.getCodigoAceite());
                                    body.put("codigoTipoTitulo", boletoDto.getCodigoTipoTitulo());
                                    body.put("descricaoTipoTitulo", boletoDto.getDescricaoTipoTitulo());
                                    body.put("indicadorPermissaoRecebimentoParcial", boletoDto.getIndicadorPermissaoRecebimentoParcial());
                                    body.put("numeroTituloBeneficiario",
                                            isNull(boletoDto.getNroDocumento()) || boletoDto.getNroDocumento().isEmpty() ?
                                                    boletoDto.getNumeroTituloBeneficiario() :
                                                    boletoDto.getNroDocumento()
                                    );
                                    body.put("campoUtilizacaoBeneficiario", boletoDto.getCampoUtilizacaoBeneficiario());
                                    body.put("numeroTituloCliente", boletoDto.getNumeroTituloCliente());
                                    body.put("mensagemBloquetoOcorrencia", boletoDto.getMensagemBloquetoOcorrencia());
                                    body.put("desconto", desconto);
                                    body.put("segundoDesconto", segundoDesconto);
                                    body.put("terceiroDesconto", terceiroDesconto);
                                    body.put("jurosMora", jurosMora);
                                    body.put("multa", multa);
                                    body.put("pagador", pagador);
                                    body.put("indicadorPix", boletoDto.getIndicadorPix());

                                    HttpHeaders headers = new HttpHeaders();
                                    headers.add("Authorization", "Bearer " + loginResponseDto.getAccess_token());
                                    headers.add("Content-Type", "application/json");

                                    HttpEntity<?> request = new HttpEntity<>(body, headers);

                                    ResponseEntity<RegistroBoletoResponseDto> registroResponse = new RestTemplate().exchange(url, HttpMethod.POST, request, RegistroBoletoResponseDto.class);
                                    alterarBoleto(new ResponseEntity<>(new BoletoResponseDto(registroResponse), registroResponse.getStatusCode()), boletoDto);
                                } catch (RuntimeException e) {
                                    GetRetornoErro.setRetornoErroBancoBoleto(boletoDto, e);

                                    boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, e);
                                    if (boletoDto != null) {
                                        ArrayList<BoletoDto> boletosDtoList = new ArrayList<BoletoDto>();
                                        boletosDtoList.add(boletoDto);
                                        boletoService.alteraBoletosRetornoBanco(boletosDtoList);
                                    }
                                    LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
                                            TipoEventoEnum.EMISSAO);
                                    if (logEnvioDto != null) {
                                        logEnvioService.salvar(logEnvioDto);
                                    }
                                    log.error("Erro ao registrar boleto no banco. - Número Título Cliente: {} [Método: envioBoletoApibanco]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
                                    log.error(e.getMessage(), e);
                                }
                            }
                        }
                    }
                }
            
        }
    }

    @Override
    public void consultaBoleto(Conta conta) {
        int[] statusFinais = {StatusBoletoEnum.LIQUIDADO.getStatus(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
                StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(),
                StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
                StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), StatusBoletoEnum.TITULO_CREDITADO.getStatus(),
                StatusBoletoEnum.ERRO.getStatus(), StatusBoletoEnum.ALTERA_BOLETO.getStatus()};

        List<Boleto> boletos = boletoService.getStatusBoleto(statusFinais);

        boletos.forEach(boleto -> {
            if (boleto.getConta().getId() != null) {
                if (conta != null && "S".equals(conta.getStatusapi())) {
                    try {
                        ResponseEntity<BoletoRetornoDto> response = boletoService.detalharBoleto(boleto);
                        BoletoRetornoDto boletoApi = response.getBody();
                        boletoApi.setIdboleto(boleto.getId());

                        if (boletoApi != null && boletoApi.getCodigoEstadoTituloCobranca() != null) {
                            boletoService.insereRetornoBancoLogEnvio(response, boleto, boletoApi);
                            boletoService.updateStatusBoleto(boleto, boletoApi);

                            Optional<BoletoRetorno> boletoRetornoUpdate = boletoRetornoRepository.findByIdboleto(boletoApi.getIdboleto());
                            if (boletoRetornoUpdate.isEmpty() || !boleto.getStatus().getStatus().equals(boletoApi.getCodigoEstadoTituloCobranca())) {
                                boletoService.salvaRetornoBoleto(boletoApi, boletoRetornoUpdate);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Erro ao atualizar status do boleto. - Número Título Cliente: {} [Método: atualizaStatusBoletos]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
                        log.error(e.getMessage(), e);
                        BoletoDto boletoDto = new BoletoDto(boleto);
                        if (!StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus().equals(boletoDto.getStatus())) {
                            LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
                                    TipoEventoEnum.RETORNO_BANCO);
                            if (logEnvioDto != null) {
                                logEnvioService.salvar(logEnvioDto);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void cancelaBoleto(Conta conta) {
        if (!isNull(conta)) {
            LoginResponseDto loginResponseDto = autenticaBanco(conta);
            if (loginResponseDto.getAccess_token() != null) {
                if (loginResponseDto.getAccess_token() != null) {
                    List<BoletoDto> boletos = boletoService.buscaPorStatus(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(), conta.getId());
                    if (boletos != null && !boletos.isEmpty()) {
                        for (BoletoDto boletoDto : boletos) {
                            try {
                            	String params = "/" + boletoDto.getNumeroTituloCliente() + "/baixar";
                                String url = getDefaultQueryParameters(params, "");
                                Map<String, Object> body = new HashMap<>();
                                body.put("numeroConvenio", boletoDto.getNumeroConvenio());

                                HttpHeaders headers = new HttpHeaders();
                                headers.add("Authorization", "Bearer " + loginResponseDto.getAccess_token());
                                headers.add("Content-Type", "application/json");

                                HttpEntity<?> request = new HttpEntity<>(body,headers);

                                ResponseEntity<CancelamentoBoletoResponseDto> registroResponse = new RestTemplate().exchange(url, HttpMethod.POST, request, CancelamentoBoletoResponseDto.class);
                                registrarBaixarBoleto(registroResponse,boletoDto);
                            } catch (RuntimeException e) {
                                boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, e);
                                LogEnvioDto logEnvioDto = logEnvioService.findByNossonumeroAndStacktrace(boletoDto.getNumeroTituloCliente(), "Status 201: Criado com sucesso");
                                boletoDto = GetRetornoErro.verificaResponseNotFound(boletoDto, e, logEnvioDto);
                                if (boletoDto != null) {
                                    ArrayList<BoletoDto> boletosDtoList = new ArrayList<BoletoDto>();
                                    boletosDtoList.add(boletoDto);
                                    //TODO VALIDAR TRAZER ESSE MÉTODO PARA ESSA CLASSE
                                    boletoService.alteraBoletosRetornoBanco(boletosDtoList);
                                }
                                logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
                                        TipoEventoEnum.BAIXA_CANCELAMENTO);
                                if (logEnvioDto != null) {
                                    logEnvioService.salvar(logEnvioDto);
                                }
                                log.error("Erro ao cancelar boleto no banco. - Número Título Cliente: {} [Método: cancelaBoletoApibanco]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }
    }

    private LoginResponseDto autenticaBanco(Conta conta) {
        LoginDto loginDto = new LoginDto(conta.getClientid(), conta.getClientsecret(), getType(),null, null, null);
        return authenticationFactory.getAuthentication(getType()).authentication(loginDto);
    }

    @Transactional
    private ArrayList<BoletoResponseDto> alterarBoleto(ResponseEntity<BoletoResponseDto> response, BoletoDto boletoDto) {
        ArrayList<BoletoResponseDto> responseBoletoDto = new ArrayList<>();
        ArrayList<BoletoDto> boletoDtos = new ArrayList<>();

        if (response != null && HttpStatus.CREATED.equals(response.getStatusCode())) {
            boletoDto.setCodigoLinhaDigitavel(response.getBody().getBoleto().getCodigoLinhaDigitavel());
            boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
            boletoDto.setStatusBanco(StatusBoletoEnum.NORMAL.getStatus());
            boletoDto.setUrl(response.getBody().getBoleto().getUrl());
            boletoDto.setTxId(response.getBody().getBoleto().getTxId());
            boletoDto.setEmv(response.getBody().getBoleto().getEmv());
            boletoDto.setDataRegistroBanco(DateUtil.getDataAtual());

            boletoDtos.add(boletoDto);
            StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": Criado com sucesso");
            ArrayList<BoletoResponseDto> boletoResponseDto = boletoService.alteraBoletosRetornoBanco(boletoDtos);
            boletoResponseDto.get(0).setMessage(stacktrace.toString());
            responseBoletoDto.addAll(boletoResponseDto);

            LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.EMISSAO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.EMISSAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
            logEnvioService.salvar(logEnvioDto);

        } else {
            BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
            boletoResponseDto.setBoleto(boletoDto);
            boletoResponseDto.setMessage(response.getBody().getMessage());
            responseBoletoDto.add(boletoResponseDto);
            boletoDtos.add(boletoDto);
        }

        return responseBoletoDto;
    }

    @Transactional
    private void registrarBaixarBoleto( ResponseEntity<CancelamentoBoletoResponseDto> response, BoletoDto boletoDto){
        ArrayList<BoletoDto> boletoDtos = new ArrayList<>();
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            boletoDto.setStatus(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
            boletoDto.setStatusBanco(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
            boletoDtos.add(boletoDto);
            //TODO VALIDAR TRAZER ESSE MÉTODO PARA ESSA CLASSE
            boletoService.alteraBoletosRetornoBanco(boletoDtos);

            StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": ").append("Executado com sucesso");
            LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.BAIXA_CANCELAMENTO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.BAIXA_CANCELAMENTO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());
            logEnvioService.salvar(logEnvioDto);
        } else {
            boletoDtos.add(boletoDto);
            //TODO VALIDAR TRAZER ESSE MÉTODO PARA ESSA CLASSE
            boletoService.alteraBoletosRetornoBanco(boletoDtos);
        }
    }

	@Override
	@Transactional
	public void alteraBoleto(Conta conta) {
		if (conta != null && "S".equals(conta.getStatusapi())) {
	        List<BoletoDto> boletos = boletoService.buscaPorStatus(StatusBoletoEnum.ALTERA_BOLETO.getStatus(), conta.getId());
	        if (!boletos.isEmpty()) {
				for (BoletoDto boletoDto : boletos) {
					try {				
						ResponseEntity<AlteracaoBoletoResponseDto> response = alteraBoletoApibanco(boletoDto);
						ArrayList<BoletoDto> boletoDtos = new ArrayList<>();

						if(response != null && response.getStatusCode().equals(HttpStatus.OK)) {	
							boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
							boletoDto.setStatusBanco(StatusBoletoEnum.NORMAL.getStatus());
							boletoDto.setIndicadorNovaDataVencimento("N");
							boletoDto.setIndicadorAtribuirDesconto("N");
							boletoDto.setIndicadorAlterarDesconto("N");
							boletoDto.setIndicadorAlterarDataDesconto("N");
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
							
							boletoDtos.add(boletoDto);
							boletoService.alteraBoletosRetornoBanco(boletoDtos);
							
							StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": ").append("Executado com sucesso");
							LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.ALTERACAO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.ALTERACAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
							logEnvioService.salvar(logEnvioDto);
						} else {
							boletoDtos.add(boletoDto);
							boletoService.alteraBoletosRetornoBanco(boletoDtos);
						}
					} catch (Exception e) {
						log.error("Erro ao alterar boleto no banco. - Número Título Cliente: {} [Método: alteraBoletoBancoBrasil]", isNull(boletoDto.getNumeroTituloCliente())? "Nulo" : boletoDto.getNumeroTituloCliente());
						log.error(e.getMessage(), e);
						LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e, TipoEventoEnum.ALTERACAO);
						if (logEnvioDto != null) {
							logEnvioService.salvar(logEnvioDto);							
						}
					}
				}
			}
		}
		
	}
	
	public ResponseEntity<AlteracaoBoletoResponseDto> alteraBoletoApibanco(BoletoDto boletoDto) {
		String params = "/" + boletoDto.getNumeroTituloCliente();
		String url = getDefaultQueryParameters(params, "");

		Conta conta = contaRepository.findById(boletoDto.getIdapibanco()).get();
		LoginResponseDto loginResponseDto = autenticaBanco(conta);

		if (loginResponseDto.getAccess_token() != null) {
			try {
				Map<String, Object> alteracaoData = new HashMap<>();
				Map<String, Object> desconto = new HashMap<>();
				Map<String, Object> alteracaoDesconto = new HashMap<>();
				Map<String, Object> alteracaoDataDesconto = new HashMap<>();
				Map<String, Object> protesto = new HashMap<>();
				Map<String, Object> abatimento = new HashMap<>();
				Map<String, Object> alteracaoAbatimento = new HashMap<>();
				Map<String, Object> juros = new HashMap<>();
				Map<String, Object> multa = new HashMap<>();
				Map<String, Object> negativacao = new HashMap<>();
				Map<String, Object> alteracaoNossoNumero = new HashMap<>();
				Map<String, Object> alteracaoEnderecoPagador = new HashMap<>();
				Map<String, Object> alteracaoPrazo = new HashMap<>();

				if (boletoDto != null) {
					if (boletoDto.getIndicadorNovaDataVencimento().equals("S")) {
						alteracaoData.put("novaDataVencimento", boletoDto.getDataVencimento());
					}

					if (boletoDto.getIndicadorAtribuirDesconto().equals("S")) {
						if (boletoDto.getDesconto() != null) {
							desconto.put("tipoPrimeiroDesconto", boletoDto.getDesconto().getTipo());
							desconto.put("valorPrimeiroDesconto", boletoDto.getDesconto().getValor());
							desconto.put("percentualPrimeiroDesconto", boletoDto.getDesconto().getPorcentagem());
							desconto.put("dataPrimeiroDesconto", boletoDto.getDesconto().getDataExpiracao());
						}
						if (boletoDto.getSegundoDesconto() != null) {
							desconto.put("tipoSegundoDesconto", boletoDto.getSegundoDesconto().getTipo());
							desconto.put("valorSegundoDesconto", boletoDto.getSegundoDesconto().getValor());
							desconto.put("percentualSegundoDesconto", boletoDto.getSegundoDesconto().getPorcentagem());
							desconto.put("dataSegundoDesconto", boletoDto.getSegundoDesconto().getDataExpiracao());
						}
						if (boletoDto.getTerceiroDesconto() != null) {
							desconto.put("tipoTerceiroDesconto", boletoDto.getTerceiroDesconto().getTipo());
							desconto.put("valorTerceiroDesconto", boletoDto.getTerceiroDesconto().getValor());
							desconto.put("percentualTerceiroDesconto", boletoDto.getTerceiroDesconto().getPorcentagem());
							desconto.put("dataTerceiroDesconto", boletoDto.getTerceiroDesconto().getDataExpiracao());
						}
					}

					if ("S".equals(boletoDto.getIndicadorAlterarDesconto())) {
						if (boletoDto.getDesconto() != null) {
							alteracaoDesconto.put("tipoPrimeiroDesconto", boletoDto.getDesconto().getTipo());
							alteracaoDesconto.put("novoValorPrimeiroDesconto", boletoDto.getDesconto().getValor() == null ? 0 : boletoDto.getDesconto().getValor());
							alteracaoDesconto.put("novoPercentualPrimeiroDesconto", boletoDto.getDesconto().getPorcentagem() == null ? 0 : boletoDto.getDesconto().getPorcentagem());
							if(!boletoDto.getDesconto().getDataExpiracao().isBlank()) {
								alteracaoDesconto.put("novaDataLimitePrimeiroDesconto", boletoDto.getDesconto().getDataExpiracao());							
							}
						} else{
							alteracaoDesconto.put("tipoPrimeiroDesconto", 0);
							alteracaoDesconto.put("novoValorPrimeiroDesconto", 0);
							alteracaoDesconto.put("novoPercentualPrimeiroDesconto", 0);
						}
						if (boletoDto.getSegundoDesconto() != null) {
							alteracaoDesconto.put("tipoSegundoDesconto", boletoDto.getSegundoDesconto().getTipo());
							alteracaoDesconto.put("novoValorSegundoDesconto", boletoDto.getSegundoDesconto().getValor() == null ? 0 : boletoDto.getSegundoDesconto().getValor());
							alteracaoDesconto.put("novoPercentualSegundoDesconto", boletoDto.getSegundoDesconto().getPorcentagem() == null ? 0 : boletoDto.getSegundoDesconto().getPorcentagem());
							if (!boletoDto.getSegundoDesconto().getDataExpiracao().isBlank()) {
								alteracaoDesconto.put("novaDataLimiteSegundoDesconto", boletoDto.getSegundoDesconto().getDataExpiracao());								
							}
						} else{
							alteracaoDesconto.put("tipoSegundoDesconto", 0);
							alteracaoDesconto.put("novoValorSegundoDesconto", 0);
							alteracaoDesconto.put("novoPercentualSegundoDesconto", 0);
						}
						if (boletoDto.getTerceiroDesconto() != null) {
							alteracaoDesconto.put("tipoTerceiroDesconto", boletoDto.getTerceiroDesconto().getTipo());
							alteracaoDesconto.put("novoValorTerceiroDesconto", boletoDto.getTerceiroDesconto().getValor() == null ? 0 : boletoDto.getTerceiroDesconto().getValor());
							alteracaoDesconto.put("novoPercentualTerceiroDesconto", boletoDto.getTerceiroDesconto().getPorcentagem() == null ? 0 : boletoDto.getTerceiroDesconto().getPorcentagem());
							if (!boletoDto.getTerceiroDesconto().getDataExpiracao().isBlank()) {
								alteracaoDesconto.put("novaDataLimiteTerceiroDesconto", boletoDto.getTerceiroDesconto().getDataExpiracao());
							}
						} else{
							alteracaoDesconto.put("tipoTerceiroDesconto", 0);
							alteracaoDesconto.put("novoValorTerceiroDesconto", 0);
							alteracaoDesconto.put("novoPercentualTerceiroDesconto", 0);
						}
					}

					if (boletoDto.getIndicadorAlterarDataDesconto().equals("S")) {
						if (boletoDto.getDesconto() != null) {
							alteracaoDataDesconto.put("novaDataLimitePrimeiroDesconto", boletoDto.getDesconto().getDataExpiracao());						
						}
						if (boletoDto.getSegundoDesconto() != null) {
							alteracaoDataDesconto.put("novaDataLimiteSegundoDesconto", boletoDto.getSegundoDesconto().getDataExpiracao());						
						}
						if (boletoDto.getTerceiroDesconto() != null) {
							alteracaoDataDesconto.put("novaDataLimiteTerceiroDesconto", boletoDto.getTerceiroDesconto().getDataExpiracao());						
						}
					}

					if (boletoDto.getIndicadorProtestar().equals("S")) {
						protesto.put("quantidadeDiasProtesto", boletoDto.getQuantidadeDiasProtesto());
					}

					if (boletoDto.getIndicadorIncluirAbatimento().equals("S")) {
						abatimento.put("valorAbatimento", boletoDto.getValorAbatimento());
						alteracaoAbatimento.put("novoValorAbatimento", boletoDto.getValorAbatimento());
					}

					if (boletoDto.getJurosMora() != null && boletoDto.getIndicadorCobrarJuros().equals("S")) {
						juros.put("tipoJuros", boletoDto.getJurosMora().getTipo());
						juros.put("valorJuros", boletoDto.getJurosMora().getValor());
						juros.put("taxaJuros", boletoDto.getJurosMora().getPorcentagem());
					}
					if (boletoDto.getMulta() != null && boletoDto.getIndicadorCobrarMulta().equals("S")) {
						multa.put("tipoMulta", boletoDto.getMulta().getTipo());
						multa.put("valorMulta", boletoDto.getMulta().getValor());
						multa.put("dataInicioMulta", boletoDto.getMulta().getData());
						multa.put("taxaMulta", boletoDto.getMulta().getPorcentagem());
					}

					if (boletoDto.getIndicadorNegativar().equals("S")) {
						negativacao.put("quantidadeDiasNegativacao", boletoDto.getQuantidadeDiasNegativacao());
						negativacao.put("tipoNegativacao", boletoDto.getTipoNegativacao());
					}

					if (boletoDto.getPagador() != null && boletoDto.getIndicadorAlterarEnderecoPagador().equals("S")) {
						alteracaoEnderecoPagador.put("enderecoPagador", boletoDto.getPagador().getEndereco());
						alteracaoEnderecoPagador.put("bairroPagador", boletoDto.getPagador().getBairro());
						alteracaoEnderecoPagador.put("cidadePagador", boletoDto.getPagador().getCidade());
						alteracaoEnderecoPagador.put("UFPagador", boletoDto.getPagador().getUf());
						alteracaoEnderecoPagador.put("CEPPagador", boletoDto.getPagador().getCep());
					}

					if (boletoDto.getIndicadorAlterarPrazoBoletoVencido().equals("S")) {
						alteracaoPrazo.put("alteracaoPrazo", boletoDto.getQuantidadeDiasAceite());
					}
				}

				Map<String, Object> body = new HashMap<>();
				body.put("numeroConvenio", boletoDto.getNumeroConvenio());
				body.put("indicadorNovaDataVencimento", boletoDto.getIndicadorNovaDataVencimento());
				body.put("alteracaoData", alteracaoData);
				body.put("indicadorAtribuirDesconto", boletoDto.getIndicadorAtribuirDesconto());
				body.put("desconto", desconto);
				body.put("indicadorAlterarDesconto", boletoDto.getIndicadorAlterarDesconto());
				body.put("alteracaoDesconto", alteracaoDesconto);
				body.put("indicadorAlterarDataDesconto", boletoDto.getIndicadorAlterarDataDesconto());
				body.put("alteracaoDataDesconto", alteracaoDataDesconto);
				body.put("indicadorProtestar", boletoDto.getIndicadorProtestar());
				body.put("protesto", protesto);
				body.put("indicadorSustacaoProtesto", boletoDto.getIndicadorSustacaoProtesto());
				body.put("indicadorCancelarProtesto", boletoDto.getIndicadorCancelarProtesto());
				body.put("indicadorIncluirAbatimento", boletoDto.getIndicadorIncluirAbatimento());
				body.put("abatimento", abatimento);
				body.put("indicadorCancelarAbatimento", boletoDto.getIndicadorCancelarAbatimento());
				body.put("alteracaoAbatimento", alteracaoAbatimento);
				body.put("indicadorCobrarJuros", boletoDto.getIndicadorCobrarJuros());
				body.put("juros", juros);
				body.put("indicadorDispensarJuros", boletoDto.getIndicadorDispensarJuros());
				body.put("indicadorCobrarMulta", boletoDto.getIndicadorCobrarMulta());
				body.put("multa", multa);
				body.put("indicadorDispensarMulta", boletoDto.getIndicadorDispensarMulta());
				body.put("indicadorNegativar", boletoDto.getIndicadorNegativar());
				body.put("negativacao", negativacao);
				body.put("indicadorAlterarNossoNumero", boletoDto.getIndicadorAlterarNossoNumero());
				body.put("alteracaoNossoNumero", alteracaoNossoNumero);
				body.put("indicadorAlterarEnderecoPagador", boletoDto.getIndicadorAlterarEnderecoPagador());
				body.put("alteracaoEndereco", alteracaoEnderecoPagador);
				body.put("indicadorAlterarPrazoBoletoVencido", boletoDto.getIndicadorAlterarPrazoBoletoVencido());
				body.put("alteracaoPrazo", alteracaoPrazo);

				HttpEntity<?> request = new HttpEntity<>(body, createJSONHeader(loginResponseDto));

				return boletoService.restTemplate().exchange(url, HttpMethod.PATCH, request, AlteracaoBoletoResponseDto.class);

			} catch (RuntimeException e) {
				boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, e);
				LogEnvioDto logEnvioDto = logEnvioService.findByNossonumeroAndStacktrace(boletoDto.getNumeroTituloCliente(), "Status 201: Criado com sucesso");
				boletoDto = GetRetornoErro.verificaResponseNotFound(boletoDto, e, logEnvioDto);
				if (boletoDto != null) {
					ArrayList<BoletoDto> boletosDtoList = new ArrayList<BoletoDto>();
					boletosDtoList.add(boletoDto);
					boletoService.alteraBoletosRetornoBanco(boletosDtoList);
				}
				logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e,
						TipoEventoEnum.ALTERACAO);
				if (logEnvioDto != null) {
					logEnvioService.salvar(logEnvioDto);
				}
				log.error("Erro ao alterar boleto no banco. - Número Título Cliente: {} [Método: alteraBoletoApibanco]", isNull(boletoDto.getNumeroTituloCliente())? "Nulo" : boletoDto.getNumeroTituloCliente());
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public void atualizarStatusBoleto(Conta conta) {
		int[] statusFinais = { StatusBoletoEnum.LIQUIDADO.getStatus(), 
				StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus(),
				StatusBoletoEnum.TITULO_BAIXADO_PAGO_CARTORIO.getStatus(),
				StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(), 
				StatusBoletoEnum.TITULO_LIQUIDADO_PROTESTADO.getStatus(),
				StatusBoletoEnum.TITULO_LIQUID_PGCRTO.getStatus(), 
				StatusBoletoEnum.TITULO_CREDITADO.getStatus(),
				StatusBoletoEnum.ERRO.getStatus(), 
				StatusBoletoEnum.ALTERA_BOLETO.getStatus() };
		
		
		List<Integer> list = Arrays.stream(statusFinais).boxed().collect(Collectors.toList());
		List<Boleto> boletos = boletoService.getStatusBoletoNotInStatus(list, conta.getId());
		LoginResponseDto loginResponseDto = boletoService.authentication(conta);
		
		for (Boleto boleto : boletos) {
			try {
				boletoService.enviaBoletoStatus(conta, loginResponseDto, boleto);
			} catch (HttpClientErrorException http) {
				loginResponseDto = boletoService.authentication(conta);
				log.error(
						"Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]",
						isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
				log.error(http.getMessage(), http);
			} catch (FailedAuthenticationException se) {
				loginResponseDto = boletoService.authentication(conta);
				log.error(
						"Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]",
						isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
				log.error(se.getMessage(), se);
			} catch (HttpServerErrorException hhtp) {
				log.error(
						"Erro ao obter detalhes do boleto junto ao banco. - Número Título Cliente: {} [Método: detalharBoleto]",
						isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
				log.error(hhtp.getMessage(), hhtp);
			} catch (Exception e) {
				log.error(
						"Erro ao atualizar status do boleto. - Número Título Cliente: {} [Método: atualizaStatusBoletos]",
						isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
				log.error(e.getMessage(), e);
			}

		}
	}
}