package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.InstrucoesBoletoItauEnum;
import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.StatusBoletoItauEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.dtos.BoletoAlteracaoRegistroItauDto;
import br.com.boleto.persistence.dtos.BoletoConsultaItauDto;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.BoletoResponseDto;
import br.com.boleto.persistence.dtos.BoletoResponseItauDto;
import br.com.boleto.persistence.dtos.ConsultaBoletoResponseItauDto;
import br.com.boleto.persistence.dtos.DataBoletoResponseItauDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.dtos.RegistroBoletoResponseItauDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.repository.BoletoRepository;
import br.com.boleto.service.AuthenticationFactory;
import br.com.boleto.service.BoletoProcessorService;
import br.com.boleto.util.DateUtil;
import br.com.boleto.util.GetRetornoErro;
import br.com.boleto.util.RestTemplateConf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BancoItauProcessorService implements BoletoProcessorService {
	private final Integer QUANTIDADE_MAXIMA_DIGITO_VALOR = 17;

	private final Integer QUANTIDADE_MAXIMA_DIGITO_PERCENTUAL = 12;

    @Value("${url.api.itau.cobranca}")
    private String urlApiItauCobranca;

	@Value("${spring.profiles.active}")
	private String springProfileActive;

	@Value("${url.api.itau.consulta}")
	private String urlApiItauConsulta;

    @Autowired
    private AuthenticationFactory authenticationFactory;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private LogEnvioService logEnvioService;

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplateConf restTemplate;

	@Autowired
	private BoletoRepository boletoRepository;

    @Override
    public BancoEnum getType() {
        return BancoEnum.ITAU;
    }
    
    private HttpHeaders createJSONHeader(LoginResponseDto loginResponseDto) {
    	HttpHeaders headers = new HttpHeaders();
    	if ("prd".equals(springProfileActive)) {
    		headers.add("Authorization", loginResponseDto.getAccess_token());
    		headers.add("x-itau-apikey", "a916bacb-03ad-31a1-b703-8c6de8ae4b11");
    		headers.add("x-itau-correlationID", UUID.randomUUID().toString());
		} else if("dev".equals(springProfileActive)) {
			headers.add("x-sandbox-token", loginResponseDto.getAccess_token());
			headers.add("Content-Type", "application/json");
		}
		return headers;
	}

    @Override
    public void registraBoleto(Conta conta){
		List<BoletoDto> boletos = boletoService.buscaBoletosPorStatus(StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus(), conta.getId());
		if (boletos != null && !boletos.isEmpty()) {
			for (BoletoDto boletoDto : boletos) {
				LoginResponseDto loginResponseDto = autenticaBanco(conta);
				//TODO JOGAR A CRIAÇÃO DO BOLETO DE ENVIO PARA UM MÉTODO
				/* CRIANDO TIPO PESSSOA */
				Map<String, Object> tipoPessoa = new HashMap<>();

				if (boletoDto.getPagador().getTipoInscricao() == 1) {
					tipoPessoa.put("codigo_tipo_pessoa", "F");
					tipoPessoa.put("numero_cadastro_pessoa_fisica", boletoDto.getPagador().getNumeroInscricao());
				} else {
					tipoPessoa.put("codigo_tipo_pessoa", "J");
					tipoPessoa.put("numero_cadastro_nacional_pessoa_juridica", boletoDto.getPagador().getNumeroInscricao());
				}

				/* CRIANDO PESSOA */
				Map<String, Object> pessoa = new HashMap<>();
				pessoa.put("nome_pessoa", boletoDto.getPagador().getNome());
				pessoa.put("tipo_pessoa", tipoPessoa);

				/* CRIANDO ENDEREÇO */
				Map<String, Object> endereco = new HashMap<>();
				endereco.put("nome_logradouro", boletoDto.getPagador().getEndereco());
				endereco.put("nome_bairro", boletoDto.getPagador().getBairro());
				endereco.put("nome_cidade", boletoDto.getPagador().getCidade());
				endereco.put("sigla_UF", boletoDto.getPagador().getUf());
				endereco.put("numero_CEP", boletoDto.getPagador().getCep());

				/* CRIANDO PAGADOR */
				Map<String, Object> pagador = new HashMap<>();
				pagador.put("pessoa", pessoa);
				pagador.put("endereco", endereco);

				//CRIANDO DADOS INDIVIDUAIS
				Map<String, Object> dadosIndividuaisBoletoItem = new HashMap<>();

				dadosIndividuaisBoletoItem.put("numero_nosso_numero", validaNossoNumero(boletoDto.getNumeroTituloCliente()));
				dadosIndividuaisBoletoItem.put("data_vencimento", DateUtil.convertStringToDateItau(boletoDto.getDataVencimento()));
				dadosIndividuaisBoletoItem.put("valor_titulo", formataValorEnvioApi(boletoDto.getValorOriginal().toString()));
				dadosIndividuaisBoletoItem.put("texto_seu_numero", boletoDto.getNroDocumento());
				ArrayList<Map<String, Object>> dadosIndividuaisBoleto = new ArrayList<Map<String, Object>>();
				dadosIndividuaisBoleto.add(dadosIndividuaisBoletoItem);

				/* CRIANDO DESCONTO */
				Map<String, Object> desconto = new HashMap<>();
				if (boletoDto.getDesconto() != null) {	
					Map<String, Object> descontoItem = new HashMap<>();
					descontoItem.put("data_desconto", DateUtil.convertStringToDateItau(boletoDto.getDesconto().getDataExpiracao()));
					if (boletoDto.getDesconto().getTipo() == 1) {
						descontoItem.put("valor_desconto", formataValorEnvioApi(boletoDto.getDesconto().getValor().toString()));
					} else if (boletoDto.getDesconto().getTipo() == 2) {
						descontoItem.put("percentual_desconto", formataPercentualEnvioApi(boletoDto.getDesconto().getPorcentagem().toString()));
					}
					descontoItem.put("data_desconto", DateUtil.convertStringToDateItau(boletoDto.getDesconto().getDataExpiracao()));
					ArrayList<Map<String, Object>> descontos = new ArrayList<Map<String, Object>>();
					descontos.add(descontoItem);

					desconto.put("codigo_tipo_desconto", insereZeroEsquerda(boletoDto.getDesconto().getTipo(), "2"));
					desconto.put("descontos", descontos);
				}

				/* CRIANDO MULTA */
				Map<String, Object> multa = new HashMap<>();
				if (boletoDto.getMulta() != null) {
					multa.put("codigo_tipo_multa", String.format("%02d", boletoDto.getMulta().getTipo()));
					multa.put("quantidade_dias_multa", DateUtil.convertStringToDateItau(boletoDto.getMulta().getData()));

					if (boletoDto.getMulta().getTipo() == 1) {
						multa.put("valor_multa", formataValorEnvioApi(boletoDto.getMulta().getValor().toString()));
					} else if (boletoDto.getMulta().getTipo() == 2) {
						multa.put("percentual_multa", formataPercentualEnvioApi(boletoDto.getMulta().getPorcentagem().toString()));
					}
				}

				/* CRIANDO JUROS */
				Map<String, Object> juros = new HashMap<>();
				if (boletoDto.getJurosMora() != null) {
					juros.put("codigo_tipo_juros", String.format("%02d", boletoDto.getJurosMora().getTipo()));

					if (boletoDto.getJurosMora().getTipo() == 90) {
						juros.put("percentual_juros", formataPercentualEnvioApi(boletoDto.getJurosMora().getPorcentagem().toString()));
					} else if (boletoDto.getJurosMora().getTipo() == 93) {
						juros.put("valor_juros", formataValorEnvioApi(boletoDto.getJurosMora().getValor().toString()));
					}
				}

				/* CRIANDO ITEM INSTRUÇÃO DE COBRANÇA */
				Map<String, Object> instrucaoCobrancaItem = new HashMap<>();
				ArrayList<Map<String, Object>> instrucaoCobranca = new ArrayList<Map<String, Object>>();
				if (InstrucoesBoletoItauEnum.PROTESTAR.getCodigo().equals(boletoDto.getInstrucaoProtesto()) && boletoDto.getQuantidadeDiasProtesto() > 0) {
					instrucaoCobrancaItem.put("codigo_instrucao_cobranca", boletoDto.getInstrucaoProtesto());
					instrucaoCobrancaItem.put("quantidade_dias_apos_vencimento", boletoDto.getQuantidadeDiasProtesto());
					instrucaoCobrancaItem.put("dia_util", boletoDto.isContabilizarDias());
					instrucaoCobranca.add(instrucaoCobrancaItem);
				} else if (InstrucoesBoletoItauEnum.NEGATIVAR.getCodigo().equals(boletoDto.getInstrucaoNegativacao()) && boletoDto.getQuantidadeDiasNegativacao() > 0) {
					instrucaoCobrancaItem.put("codigo_instrucao_cobranca", boletoDto.getInstrucaoNegativacao());
					instrucaoCobrancaItem.put("quantidade_dias_apos_vencimento", boletoDto.getQuantidadeDiasNegativacao());
					instrucaoCobrancaItem.put("dia_util", boletoDto.isContabilizarDias());
					instrucaoCobranca.add(instrucaoCobrancaItem);
				}

				/* CRIANDO RECEBIMENTO DIVERGENTE */
				Map<String, Object> recebimentoDivergente = new HashMap<>();
				recebimentoDivergente.put("codigo_tipo_autorizacao", "03");

				/* CRIANDO DADO BOLETO */
				Map<String, Object> dadoBoleto = new HashMap<>();
				dadoBoleto.put("descricao_instrumento_cobranca", "boleto");
				dadoBoleto.put("tipo_boleto", "a vista");
				dadoBoleto.put("codigo_carteira", conta.getCarteira());
				dadoBoleto.put("valor_total_titulo", formataValorEnvioApi(boletoDto.getValorOriginal().toString()));
				dadoBoleto.put("codigo_especie", "01");
				dadoBoleto.put("valor_abatimento", formataValorEnvioApi(boletoDto.getValorAbatimento().toString()));
				dadoBoleto.put("data_emissao", DateUtil.convertStringToDateItau(boletoDto.getDataEmissao()));
				dadoBoleto.put("desconto_expresso", false);
				dadoBoleto.put("pagador", pagador);
				dadoBoleto.put("dados_individuais_boleto", dadosIndividuaisBoleto);
				
				if (!desconto.isEmpty()) {
					dadoBoleto.put("desconto", desconto);
				}
				
				if (!multa.isEmpty()) {
					dadoBoleto.put("multa", multa);
				}

				if (!juros.isEmpty()) {
					dadoBoleto.put("juros", juros);
				}
				
				if (!instrucaoCobranca.isEmpty()) {
					dadoBoleto.put("instrucao_cobranca", instrucaoCobranca);
				}

				/* CRIANDO BENEFICIARIO */
				Map<String, Object> beneficiario = new HashMap<>();
				beneficiario.put("id_beneficiario", formataAgenciaBeneficiarioEnvioApi(conta.getCodage()).concat(formataContaBeneficiarioEnvioApi(conta.getCodctabco())));

				/* CRIANDO DATA */
				Map<String, Object> data = new HashMap<>();
				//TODO MODIFICAR QUANDO FOR SUBIR PARA PRODUÇÃO
				data.put("etapa_processo_boleto", "VALIDACAO");
				data.put("codigo_canal_operacao", "API");
				data.put("beneficiario", beneficiario);
				data.put("dado_boleto", dadoBoleto);

				Map<String, Object> body = new HashMap<>();
				body.put("data", data);

				HttpHeaders headers = createJSONHeader(loginResponseDto);

				HttpEntity<?> request = new HttpEntity<>(body, headers);

				RestTemplate rest = validaRequisicao(conta.getId());

				try {
					if (environment.getActiveProfiles().equals("prd")) {
						ResponseEntity<DataBoletoResponseItauDto> registroResponse = rest.exchange(urlApiItauCobranca, HttpMethod.POST, request, DataBoletoResponseItauDto.class);
						salvaRetornoRegistroBoleto(registroResponse.getBody().getData(), boletoDto);
					} else {
						ResponseEntity<RegistroBoletoResponseItauDto> registroResponse = rest.exchange(urlApiItauCobranca, HttpMethod.POST, request, RegistroBoletoResponseItauDto.class);
						salvaRetornoRegistroBoleto(registroResponse.getBody(), boletoDto);
					}
				} catch (HttpClientErrorException exception) {
					//TODO SERA CRIADO UM MÉTODO PARA A QUANTIDADE DE TENTATIVAS DE ERRO
					String responseJson = exception.getResponseBodyAsString();
					LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, responseJson, TipoEventoEnum.EMISSAO);
					if (logEnvioDto != null) {
						logEnvioService.salvar(logEnvioDto);
					}
					log.error("Erro ao registrar boleto no banco Itaú. - Número Título Cliente: {} [Método: registraBoleto]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
					log.error(exception.getMessage(), exception);
				} catch (Exception exception) {
					//TODO SERA CRIADO UM MÉTODO PARA A QUANTIDADE DE TENTATIVAS DE ERRO
					LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, exception, TipoEventoEnum.EMISSAO);
					if (logEnvioDto != null) {
						logEnvioService.salvar(logEnvioDto);
					}
					log.error("Erro ao registrar boleto no banco Itaú. - Número Título Cliente: {} [Método: registraBoleto]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
					log.error(exception.getMessage(), exception);
				}
			}
		}
    }

   @Override
   public void consultaBoleto(Conta conta) {
		int[] statusConsulta = {StatusBoletoItauEnum.EM_ABERTO.getStatus(),StatusBoletoItauEnum.AGUARDANDO_PAGAMENTO.getStatus()/*,StatusBoletoItauEnum.PAGAMENTO_DEVOLVIDO.getStatus()*/};
		if(conta != null){
			List<Boleto> boletos = boletoRepository.findByStatusInAndConta(statusConsulta,conta.getId());

			for (Boleto boleto : boletos) {
				LoginResponseDto loginResponseDto = autenticaBanco(conta);
				Map<String, Object> body = new HashMap<>();
				HttpHeaders headers = createJSONHeader(loginResponseDto);
				HttpEntity<?> request = new HttpEntity<>(body, headers);
				RestTemplate rest = validaRequisicao(conta.getId());

				try{
					ResponseEntity<ConsultaBoletoResponseItauDto> consultaResponse = rest.exchange(urlApiItauConsulta+"?id_beneficiario=" + formataAgenciaBeneficiarioEnvioApi(conta.getCodage()).concat(formataContaBeneficiarioEnvioApi(conta.getCodctabco()))
							+ "&nosso_numero=" + validaNossoNumero(boleto.getNumeroTituloCliente()), HttpMethod.GET, request, ConsultaBoletoResponseItauDto.class);
					savaRetornoConsultaBoleto(consultaResponse.getBody().getData().get(0), boleto);
				} catch (HttpClientErrorException exception) {
					BoletoDto boletoDto = new BoletoDto(boleto);
					LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, exception.getResponseBodyAsString(), TipoEventoEnum.RETORNO_BANCO);
					logEnvioService.salvar(logEnvioDto);
					log.error("Erro ao consultar boleto no banco Itaú. - Número Título Cliente: {} [Método: registraBoleto]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
					log.error(exception.getMessage(), exception);
				} catch (Exception exception) {
					BoletoDto boletoDto = new BoletoDto(boleto);
					LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, exception, TipoEventoEnum.RETORNO_BANCO);
					logEnvioService.salvar(logEnvioDto);
					log.error("Erro ao consultar boleto no banco Itaú. - Número Título Cliente: {} [Método: registraBoleto]", isNull(boleto.getNumeroTituloCliente()) ? "Nulo" : boleto.getNumeroTituloCliente());
					log.error(exception.getMessage(), exception);
				}
			}
		}
   }

    @Override
    @Transactional
    public void cancelaBoleto(Conta conta) {
        if (!isNull(conta)) {
	        List<BoletoDto> boletos = boletoService.buscaBoletosPorStatus(StatusBoletoEnum.BAIXADO_PELO_ERP.getStatus(), conta.getId());
	        if (!boletos.isEmpty()) {
	            for (BoletoDto boletoDto : boletos) {
	                try {
	                    ResponseEntity<?> response = cancelaBoletoApiBanco(boletoDto, conta);
	                    ArrayList<BoletoDto> boletoDtos = new ArrayList<>();

	                    if (response != null && HttpStatus.NO_CONTENT.equals(response.getStatusCode())) {
	                    	boletoDto.setStatus(StatusBoletoItauEnum.BAIXADO_PELO_BANCO.getStatus());
	                        boletoDto.setStatusBanco(StatusBoletoItauEnum.BAIXADO_PELO_BANCO.getStatus());
	                        boletoDtos.add(boletoDto);
	                        
	                        boletoService.alteraBoletosRetornoBanco(boletoDtos);

	                        StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": ").append("Executado com sucesso");
	                        LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.BAIXA_CANCELAMENTO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.BAIXA_CANCELAMENTO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoItauEnum.BAIXADO_PELO_BANCO.getStatus());
	                        logEnvioService.salvar(logEnvioDto);
	                    }
	                } catch (Exception e) {
	                	log.error("Erro ao cancelar boleto no banco. - Número Título Cliente: {} [Método: cancelaBoleto]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
	                	log.error(e.getMessage(), e);
	                    LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, e, TipoEventoEnum.BAIXA_CANCELAMENTO);
	                    if (logEnvioDto != null) {
	                        logEnvioService.salvar(logEnvioDto);
	                    }
	                }
	            	
	            	
	            }
	        }
        }
    }

    private ResponseEntity<?> cancelaBoletoApiBanco(BoletoDto boletoDto, Conta conta) {
    	try {
	    	LoginResponseDto loginResponseDto = autenticaBanco(conta);
	    	if (loginResponseDto.getAccess_token() != null) {
		    	Map<String, Object> body = new HashMap<>();
		        return enviaRequestBoletoApiBanco(loginResponseDto, createJSONHeader(loginResponseDto), body, boletoDto.getNumeroTituloCliente(), "/baixa", conta);
	    	}
	    	return new ResponseEntity<BoletoResponseDto>(new BoletoResponseDto(boletoDto.getId(), boletoDto, "Token de acesso nulo.", false), HttpStatus.UNAUTHORIZED);
    	} catch (HttpClientErrorException exception) {
    		logEnvioService.insereRetornoBancoLogEnvio(boletoDto, exception, exception.getResponseBodyAsString(), "", "", TipoEventoEnum.BAIXA_CANCELAMENTO);
            return GetRetornoErro.setRetornoErroBancoBoleto(boletoDto, exception.getResponseBodyAsString());
        } catch (Exception exception) {
    		logEnvioService.insereRetornoBancoLogEnvio(boletoDto, exception, "", "", "", TipoEventoEnum.BAIXA_CANCELAMENTO);
            return GetRetornoErro.setRetornoErroBancoBoleto(boletoDto, exception);
        }
    }

    private LoginResponseDto autenticaBanco(Conta conta){
       LoginDto loginDto = new LoginDto(conta.getClientid(), conta.getClientsecret(), getType(), conta.getId(), null, null);
       return authenticationFactory.getAuthentication(getType()).authentication(loginDto);
   }

   @Transactional
    private ArrayList<BoletoResponseDto> alterarBoleto(ResponseEntity<BoletoResponseItauDto> response, BoletoDto boletoDto) {
        ArrayList<BoletoResponseDto> responseBoletoDto = new ArrayList<>();
        ArrayList<BoletoDto> boletoDtos = new ArrayList<>();

        //TODO VALIDAR O FLUXO DE ERRO
        if(response != null && HttpStatus.OK.equals(response.getStatusCode())) {
            boletoDto.setCodigoLinhaDigitavel(response.getBody().getRegistroBoletoResponseItauDto().getDado_boleto().getDados_individuais_boleto().get(0).getNumero_linha_digitavel());
            boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
            boletoDto.setStatusBanco(StatusBoletoEnum.NORMAL.getStatus());
            boletoDto.setDataRegistroBanco(DateUtil.getDataAtual());

            boletoDtos.add(boletoDto);
            StringBuilder stacktrace = new StringBuilder().append("Status ").append(response.getStatusCodeValue()).append(": Criado com sucesso");
            ArrayList<BoletoResponseDto> boletoResponseDto = boletoService.alteraBoletosRetornoBanco(boletoDtos);
            boletoResponseDto.get(0).setMessage(stacktrace.toString());
            responseBoletoDto.addAll(boletoResponseDto);

            LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.EMISSAO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.EMISSAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
            logEnvioService.salvar(logEnvioDto);
        }else{
            BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
            boletoResponseDto.setBoleto(boletoDto);
            boletoResponseDto.setMessage(response.getBody().getMessage());
            responseBoletoDto.add(boletoResponseDto);
            LogEnvioDto logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, response, TipoEventoEnum.EMISSAO);
            if (logEnvioDto != null) {
                logEnvioService.salvar(logEnvioDto);
            }
            boletoDtos.add(boletoDto);
        }

        return responseBoletoDto;
    }
    
    @Override
    public void alteraBoleto(Conta conta) {
        if (!isNull(conta) && "S".equals(conta.getStatusapi())) {
	        List<BoletoDto> boletos = boletoService.buscaBoletosPorStatus(StatusBoletoEnum.ALTERA_BOLETO.getStatus(), conta.getId());
	        if (!boletos.isEmpty()) {
	        	ArrayList<BoletoDto> boletoDtos = new ArrayList<>();
	            for (BoletoDto boletoDto : boletos) {
	            	boletoDto = alteraBoletoApiBanco(boletoDto, conta);
            		if(StatusBoletoEnum.NORMAL.getStatus().equals(boletoDto.getStatus())) {
						boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
						boletoDto.setStatusBanco(StatusBoletoEnum.NORMAL.getStatus());
						boletoDto.setIndicadorNovaDataVencimento("N");
						boletoDto.setIndicadorAtribuirDesconto("N");
						boletoDto.setIndicadorAlterarDesconto("N");
						boletoDto.setIndicadorAlterarDataDesconto("N");
						boletoDto.setIndicadorIncluirAbatimento("N");
						boletoDto.setIndicadorAlterarValorOriginal("N");
						
						boletoDtos.add(boletoDto);
						
						StringBuilder stacktrace = new StringBuilder().append("Status ").append(HttpStatus.NO_CONTENT.value()).append(": ").append("Boleto alterado com sucesso.");
						LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.ALTERACAO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.ALTERACAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
						logEnvioService.salvar(logEnvioDto);
					} else {
						boletoDtos.add(boletoDto);
					}
	            	boletoService.alteraBoletosRetornoBanco(boletoDtos);
	            }
	        }
        }
    }
    
    private BoletoDto alteraBoletoApiBanco(BoletoDto boletoDto, Conta conta) {
    	boolean isDataVencimentoOk = true;
    	boolean isAbatimentoOk = true;
    	boolean isValorOriginalOk = true;
    	boolean isDescontoOk = true;
    	
    	LoginResponseDto loginResponseDto = autenticaBanco(conta);
    	if (isNull(loginResponseDto.getAccess_token()) || isNull(boletoDto)) {
    		boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
			return boletoDto;
		}
		if ("S".equals(boletoDto.getIndicadorNovaDataVencimento())) {
			isDataVencimentoOk = alteraDataVencimento(boletoDto, loginResponseDto, conta);
		}
    	
		if(boletoDto.getValorAbatimento() == 0) {
			boletoDto.setIndicadorIncluirAbatimento("S");
		}
		
		if ("S".equals(boletoDto.getIndicadorIncluirAbatimento())) {
			isAbatimentoOk = alteraAbatimento(boletoDto, loginResponseDto, conta);
			if ("S".equals(boletoDto.getIndicadorAlterarValorOriginal())) {
				boletoDto.setIndicadorAlterarValorOriginal("N");
			}
		}
    	
		if ("S".equals(boletoDto.getIndicadorAlterarValorOriginal())) {
			isValorOriginalOk = alteraValorOriginal(boletoDto, loginResponseDto, conta);
		}
    	
    	if ("S".equals(boletoDto.getIndicadorAtribuirDesconto()) || "S".equals(boletoDto.getIndicadorAlterarDesconto())) {
    		isDescontoOk = alteraDesconto(boletoDto, loginResponseDto, conta);
    	}
    	
    	if (!isDataVencimentoOk || !isAbatimentoOk || !isValorOriginalOk || !isDescontoOk) {
    		StringBuilder camposAlterados = new StringBuilder();
    		if (isDataVencimentoOk && "S".equals(boletoDto.getIndicadorNovaDataVencimento())) {
    			boletoDto.setIndicadorNovaDataVencimento("N");
    			adicionaCampoAlterado("Data de Vencimento", camposAlterados);
			} 
    		if(isAbatimentoOk && "S".equals(boletoDto.getIndicadorIncluirAbatimento())) {
				boletoDto.setIndicadorIncluirAbatimento("N");
				adicionaCampoAlterado("Abatimento", camposAlterados);
			}
    		if(isValorOriginalOk && "S".equals(boletoDto.getIndicadorAlterarValorOriginal())) {
				boletoDto.setIndicadorAlterarValorOriginal("N");
				adicionaCampoAlterado("Valor Original", camposAlterados);
			}
    		if(isDescontoOk && ("S".equals(boletoDto.getIndicadorAtribuirDesconto()) || "S".equals(boletoDto.getIndicadorAlterarDesconto()))) {
				boletoDto.setIndicadorAtribuirDesconto("N");
				adicionaCampoAlterado("Desconto", camposAlterados);
			}
    		if (!isDataVencimentoOk && !isAbatimentoOk && !isValorOriginalOk && !isDescontoOk) {
    			boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, null);
			}
    		if (camposAlterados.length() > 0 ) {
    			StringBuilder stacktrace = new StringBuilder().append("Status 204: Campos ").append(camposAlterados.toString()).append(" alterados com sucesso.");
				LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.ALTERACAO.getDescricaoEvento(), stacktrace.toString(), boletoDto.getStatus(), TipoEventoEnum.ALTERACAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
				logEnvioService.salvar(logEnvioDto);
			}
        }
    	if (isDataVencimentoOk && isAbatimentoOk && isValorOriginalOk && isDescontoOk) {
    		boletoDto.setStatus(StatusBoletoEnum.NORMAL.getStatus());
    	}
        return boletoDto;
    }
    
    private void adicionaCampoAlterado(String campoAlterado, StringBuilder camposAlterados) {
    	if (camposAlterados.length() > 0 ) {
			camposAlterados.append(" , ");
		}
		camposAlterados.append(campoAlterado);
    }
    
    private boolean alteraDataVencimento(BoletoDto boletoDto, LoginResponseDto loginResponseDto, Conta conta) {
    	Map<String, Object> body = new HashMap<>();
    	String dataVencimento = DateUtil.convertTimeStampToStringItau(DateUtil.convertStringToTimestamp(boletoDto.getDataVencimento()));
    	body.put("data_vencimento", dataVencimento);
		try {
			ResponseEntity<?> response = enviaRequestBoletoApiBanco(loginResponseDto, createJSONHeader(loginResponseDto), body, boletoDto.getNumeroTituloCliente(), "/data_vencimento", conta);					
			if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				return true;
			}
		} catch (HttpClientErrorException e) {
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, e.getResponseBodyAsString(), "Data de Vencimento", boletoDto.getDataVencimento(), TipoEventoEnum.ALTERACAO);
			return false;
		} catch (Exception e) {
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, "", "Data de Vencimento", boletoDto.getDataVencimento(), TipoEventoEnum.ALTERACAO);
			return false;
		}
		return false;
    }
    
    private boolean alteraAbatimento(BoletoDto boletoDto, LoginResponseDto loginResponseDto, Conta conta) {
    	Map<String, Object> body = new HashMap<>();
    	body.put("valor_abatimento", formataValor(boletoDto.getValorAbatimento()));
		try {
			ResponseEntity<?> response = enviaRequestBoletoApiBanco(loginResponseDto, createJSONHeader(loginResponseDto), body, boletoDto.getNumeroTituloCliente(), "/abatimento", conta);
			if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				return true;
			}
		} catch (HttpClientErrorException e) {
			if (boletoDto.getValorAbatimento() == 0) {
				return true;
			}
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, e.getResponseBodyAsString(), "Abatimento", boletoDto.getValorAbatimento(), TipoEventoEnum.ALTERACAO);
			return false;
		} catch (Exception e) {
			if (boletoDto.getValorAbatimento() == 0) {
				return true;
			}
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, "", "Abatimento", boletoDto.getValorAbatimento(), TipoEventoEnum.ALTERACAO);
			return false;
		}
		return false;
    }
    
    private boolean alteraValorOriginal(BoletoDto boletoDto, LoginResponseDto loginResponseDto, Conta conta) {
    	Map<String, Object> body = new HashMap<>();
    	body.put("valor_titulo", formataValor(boletoDto.getValorOriginal()));
		try {
			ResponseEntity<?> response = enviaRequestBoletoApiBanco(loginResponseDto, createJSONHeader(loginResponseDto), body, boletoDto.getNumeroTituloCliente(), "/valor_nominal", conta);
			if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				return true;
			}
		} catch (HttpClientErrorException e) {
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, e.getResponseBodyAsString(), "Valor Original", boletoDto.getValorOriginal(), TipoEventoEnum.ALTERACAO);
			return false;
		} catch (Exception e) {
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, "", "Valor Original", boletoDto.getValorOriginal(), TipoEventoEnum.ALTERACAO);
			return false;
		}
		return false;
    }
    
    private boolean alteraDesconto(BoletoDto boletoDto, LoginResponseDto loginResponseDto, Conta conta) {
    	Map<String, Object> body = new HashMap<>();
    	Map<String, Object> desconto = new HashMap<>();
    	Map<String, Object> descontos = new HashMap<>();
    	body.put("desconto", desconto);
    	ArrayList<Map<String, Object>> descontolist = new ArrayList<>();
		if (boletoDto.getDesconto() != null) {
			desconto.put("codigo_tipo_desconto", String.format("%02d", boletoDto.getDesconto().getTipo()));
			descontos.put("quantidade_dias_desconto", DateUtil.retornaDiasAteDataVencimento(boletoDto.getDesconto().getDataExpiracao()));
			descontos.put("valor_desconto", formataValor(boletoDto.getDesconto().getValor()));
			descontolist.add(descontos);
		}
		if (boletoDto.getSegundoDesconto() != null) {
			desconto.put("codigo_tipo_desconto", boletoDto.getSegundoDesconto().getTipo());
			descontos.put("quantidade_dias_desconto", DateUtil.retornaDiasAteDataVencimento(boletoDto.getSegundoDesconto().getDataExpiracao()));
			descontos.put("valor_desconto", formataValor(boletoDto.getSegundoDesconto().getValor()));
			descontolist.add(descontos);
		}
		if (boletoDto.getTerceiroDesconto() != null) {
			desconto.put("codigo_tipo_desconto", boletoDto.getTerceiroDesconto().getTipo());
			desconto.put("codigo_tipo_desconto", boletoDto.getTerceiroDesconto().getTipo());
			descontos.put("quantidade_dias_desconto", DateUtil.retornaDiasAteDataVencimento(boletoDto.getTerceiroDesconto().getDataExpiracao()));
			descontos.put("valor_desconto", formataValor(boletoDto.getTerceiroDesconto().getValor()));
			descontolist.add(descontos);
		}
		desconto.put("descontos", descontolist);
		try {
			ResponseEntity<?> response = enviaRequestBoletoApiBanco(loginResponseDto, createJSONHeader(loginResponseDto), body, boletoDto.getNumeroTituloCliente(), "/desconto", conta);
			if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
				return true;
			}
		} catch (HttpClientErrorException e) {
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, e.getResponseBodyAsString(), "Desconto", boletoDto.getDesconto().toString(), TipoEventoEnum.ALTERACAO);
			return false;
		} catch (Exception e) {
			logEnvioService.insereRetornoBancoLogEnvio(boletoDto, e, "", "Desconto", boletoDto.getDesconto().toString(), TipoEventoEnum.ALTERACAO);
			return false;
		}
		return false;
    }
    
    private ResponseEntity<?> alteraCampoBoletoApiBanco(LoginResponseDto loginResponseDto, HttpHeaders headers, Map<String, Object> body, String numeroTituloCliente, String url) {
		HttpEntity<?> request = new HttpEntity<>(body, headers);	
		return boletoService.restTemplate().exchange(urlApiItauCobranca + numeroTituloCliente + url, HttpMethod.PATCH, request, ResponseDto.class);
    }
    
    private ResponseEntity<?> enviaRequestBoletoApiBanco(LoginResponseDto loginResponseDto, HttpHeaders headers, Map<String, Object> body, String numeroTituloCliente, String acaoUrl, Conta conta) {
    	HttpEntity<?> request = new HttpEntity<>(body, headers);
    	StringBuilder idBoleto = new StringBuilder(formataAgenciaBeneficiarioEnvioApi(conta.getCodage()).concat(formataContaBeneficiarioEnvioApi(conta.getCodctabco(), "8"))).append(conta.getCarteira()).append(validaNossoNumero(numeroTituloCliente));
        StringBuilder url = new StringBuilder(urlApiItauCobranca).append("/").append(idBoleto.toString()).append(acaoUrl);
        RestTemplate rest = validaRequisicao(conta.getId());
        return rest.exchange(url.toString(), HttpMethod.PATCH, request, BoletoResponseDto.class);
    }
    
    private String formataContaBeneficiarioEnvioApi(Integer conta, String digitos){
        return String.format("%0" + digitos + "d", conta);
    }

	private RestTemplate validaRequisicao(Integer contaId){
		RestTemplate rest = new RestTemplate();

		if(environment.getActiveProfiles().equals("prd")){
			rest = restTemplate.preparaP12(contaId);
		}

		return rest;
	}

	private String validaNossoNumero(String nossoNumero) {
		int quantidadeDigitoNossoNumero = nossoNumero.length();
		String nossoNumeroValido = nossoNumero.substring(3, quantidadeDigitoNossoNumero - 1);

		return nossoNumeroValido;
	}


    private String formataContaBeneficiarioEnvioApi(Integer conta){
        return String.format("%08d",conta);
    }

    private String formataValorEnvioApi(String valor){
		StringBuilder valorRecebido = new StringBuilder(valor);
		if(valor.substring(valor.indexOf('.')).length() == 2){
			valorRecebido.insert(valorRecebido.length(),"0");
		}
		String valorAlterado = valorRecebido.toString().replace(".","");
		Integer quantidadeDigitoValor = valorAlterado.length();
		StringBuilder valorValidado = new StringBuilder(valorAlterado);

		if(quantidadeDigitoValor < QUANTIDADE_MAXIMA_DIGITO_VALOR){
			int index = 0;
			while(index < (QUANTIDADE_MAXIMA_DIGITO_VALOR - quantidadeDigitoValor)){
				valorValidado.insert(0,'0');
				index++;
			}
		}

		return valorValidado.toString();
	}

	private String formataPercentualEnvioApi(String percentual){
		StringBuilder percentualRecebido = new StringBuilder(percentual);

		if(percentual.substring(percentual.indexOf('.')).length() == 2){
			percentualRecebido.insert(percentualRecebido.length(),"0000");
		}
		
		String percentualAlterado = percentualRecebido.toString().replace(".","");
		Integer quantidadeDigitoPercentual = percentualAlterado.length();
		StringBuilder percentualValidado = new StringBuilder(percentualAlterado);
		
		if(quantidadeDigitoPercentual < QUANTIDADE_MAXIMA_DIGITO_PERCENTUAL){
			int index = 0;
			while(index < (QUANTIDADE_MAXIMA_DIGITO_PERCENTUAL - quantidadeDigitoPercentual)){
				percentualValidado.insert(0,'0');
				index++;
			}
		}

		return percentualValidado.toString();
	}

	private String formataAgenciaBeneficiarioEnvioApi(Integer agencia){
		return String.format("%04d",agencia);
	}
	
	private String insereZeroEsquerda(Integer campo, String digitos){
		return String.format("%0" +digitos+ "d",campo);
	}

	@Transactional
	private void salvaRetornoRegistroBoleto(RegistroBoletoResponseItauDto registroBoletoResponseItauDto, BoletoDto boletoDto){
		StringBuilder stacktrace = new StringBuilder().append("Status ").append(HttpStatus.OK.value()).append(": Criado com sucesso");
		LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), TipoEventoEnum.EMISSAO.getDescricaoEvento(), stacktrace.toString(), StatusBoletoItauEnum.EM_ABERTO.getStatus(), TipoEventoEnum.EMISSAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
		logEnvioService.salvar(logEnvioDto);

		BoletoAlteracaoRegistroItauDto boletoAlteracaoRegistroItauDto = new BoletoAlteracaoRegistroItauDto();
		boletoAlteracaoRegistroItauDto.setId(boletoDto.getId());
		boletoAlteracaoRegistroItauDto.setCodigoLinhaDigitavel(registroBoletoResponseItauDto.getDado_boleto().getDados_individuais_boleto().get(0).getNumero_linha_digitavel());
		boletoAlteracaoRegistroItauDto.setStatus(StatusBoletoItauEnum.EM_ABERTO.getStatus());
		boletoAlteracaoRegistroItauDto.setStatusBanco(StatusBoletoItauEnum.EM_ABERTO.getStatus());
		boletoAlteracaoRegistroItauDto.setDataRegistroBanco(DateUtil.getDataAtual());

		boletoRepository.alteraRegistroBoletoItau(boletoAlteracaoRegistroItauDto);
	}

	@Transactional
	private void savaRetornoConsultaBoleto(RegistroBoletoResponseItauDto registroBoletoResponseItauDto, Boleto boleto){
		StringBuilder stacktrace = new StringBuilder().append("Status ").append(HttpStatus.OK.value()).append(": Boleto com sucesso");
		LogEnvioDto logEnvioDto = new LogEnvioDto(boleto.getNumeroTituloCliente(), TipoEventoEnum.EMISSAO.getDescricaoEvento(), stacktrace.toString(), boleto.getStatus().getStatus(), TipoEventoEnum.EMISSAO.getIdTipoEvento(), SituacaoEnum.PROCESSADA.getIdSituacao(), StatusBoletoEnum.NORMAL.getStatus());
		logEnvioService.salvar(logEnvioDto);

		BoletoConsultaItauDto boletoConsultaItauDto = new BoletoConsultaItauDto();
		boletoConsultaItauDto.setId(boleto.getId());
		boletoConsultaItauDto.setStatus(StatusBoletoItauEnum.getStatus(registroBoletoResponseItauDto.getDado_boleto().getDados_individuais_boleto().get(0).getSituacao_geral_boleto()));
		boletoConsultaItauDto.setStatusBanco(StatusBoletoItauEnum.getStatus(registroBoletoResponseItauDto.getDado_boleto().getDados_individuais_boleto().get(0).getSituacao_geral_boleto()));

		boletoRepository.alteraConsultaBoletoItau(boletoConsultaItauDto);
	}
	
	private String formataValor(Double valor) {
		String valorDesconto = String.format("%.2f", valor);
		return valorDesconto.replace(",", ".");
	}

	@Override
	public void atualizarStatusBoleto(Conta conta) {
		// TODO Auto-generated method stub
		
	}
	
}
