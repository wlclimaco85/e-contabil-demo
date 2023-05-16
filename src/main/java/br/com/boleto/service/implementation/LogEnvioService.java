package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import br.com.boleto.persistence.dtos.LogEnvioResponseDto;
import br.com.boleto.persistence.dtos.LogEnvioSearchRequestDto;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.LogEnvioRepository;
import br.com.boleto.util.GetRetornoErro;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class LogEnvioService {
	@Autowired
	private LogEnvioRepository logEnvioRepository;
	
	@Autowired
    BoletoCustomSearchRepository boletoCustomSearchRepository;

	@Autowired
	private BoletoService boletoService;
	
	private static String JSON_INVALIDO = "JSON de Requisição Inválido";
	private static String RETORNO_PADRAO = "Cadastro do parceiro contém caracteres inválidos que impossibilitam o registro de boletos";

	@Transactional
	public void salvar(LogEnvioDto logEnvio) {
		LogEnvio newLogEnvio = new LogEnvio();
		newLogEnvio.setlogEnvio(logEnvio);
		logEnvioRepository.save(newLogEnvio);
	}
	
	public ArrayList<LogEnvioResponseDto> findByNumeroTituloCliente(String numeroTituloCliente, Integer bancoId){
		if (bancoId == 0) {
			
		}
		
		List<LogEnvio> logEnvioList = logEnvioRepository.findByNossonumeroOrderByDhocorrenciaAsc(numeroTituloCliente);
		
		ArrayList<LogEnvioResponseDto> logEnvioResponseDtoList = new ArrayList<>();
		
		if (logEnvioList.isEmpty()) {
			return logEnvioResponseDtoList;
		}

		StatusBoletoEnum ultimoStatus = StatusBoletoEnum.NORMAL;
		for (LogEnvio logEnvio : logEnvioList) {
			LogEnvioResponseDto logEnvioResponseDto = new LogEnvioResponseDto();
			LogEnvioDto logEnvioDto = new LogEnvioDto(logEnvio);
			logEnvioDto.setId(logEnvio.getId());
			logEnvioDto.setDescricaoStatus(logEnvio.getStatus());
			logEnvioDto.setTipoEvento(logEnvio.getTipoEvento());
			logEnvioDto.setDescricaoTipoEvento(logEnvio.getTipoEvento());
			logEnvioDto.setDescricaoSituacao(logEnvio.getSituacao());
			
			if(StatusBoletoEnum.BAIXADO_PELO_ERP.equals(logEnvio.getStatus()) || StatusBoletoEnum.ALTERA_BOLETO.equals(logEnvio.getStatus()) 
					|| (StatusBoletoEnum.ERRO.equals(logEnvio.getStatus()) 
							&& !StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.equals(ultimoStatus) 
							&& !StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.equals(logEnvioList.get(0).getStatusBanco()))) {
				logEnvioDto.setStatus(ultimoStatus.getStatus());
				logEnvioDto.setDescricaoStatus(ultimoStatus);
			    
			}else {
				ultimoStatus = logEnvio.getStatus();
			}
			
			logEnvioDto.setStacktrace(formataStacktrace(logEnvio.getStacktrace(), logEnvio.getTipoEvento().getIdTipoEvento(), bancoId));
			
			logEnvioResponseDto.setLogEnvio(logEnvioDto);
			logEnvioResponseDtoList.add(logEnvioResponseDto);
		}

		return logEnvioResponseDtoList;
	}
	
	private String formataStacktrace(String stacktrace, Integer idTipoEvento, Integer bancoId){
		if (BancoEnum.BANCO_DO_BRASIL.getId().equals(bancoId)) {
			return formataStacktraceBancoBrasil(stacktrace, idTipoEvento);	
		}
    	if(BancoEnum.ITAU.getId().equals(bancoId)) {
    		return formataStacktraceItau(stacktrace);
    	}
    	return stacktrace;
    }
	
	private String formataStacktraceBancoBrasil(String stacktrace, Integer idTipoEvento){
		if (!stacktrace.isEmpty() && stacktrace.contains("Status 400: 400 Bad Request:")) {
    		StringBuilder listErros = new StringBuilder();
    		listErros.append(stacktrace);
    		if (listErros.length() > 30) {
    			listErros.delete(0, 30);				
			}
    		listErros.deleteCharAt(listErros.length()-1);
    		
			if (idTipoEvento != null && (listErros.toString().startsWith("{\"erros\":") || listErros.toString().startsWith("{\"errors\":") && listErros.toString().endsWith("}"))) {
				Gson gson = new Gson();
	            JsonObject jsonObject = gson.fromJson(listErros.toString(), JsonObject.class);
	            JsonArray errosHistoricoBoleto = new JsonArray();
    			if (TipoEventoEnum.EMISSAO.getIdTipoEvento().equals(idTipoEvento)) {
    				errosHistoricoBoleto =  jsonObject.getAsJsonArray("erros");
    			} else {
    				errosHistoricoBoleto = jsonObject.getAsJsonArray("errors");			
    			}

	    		if(errosHistoricoBoleto.size() > 0) {
	    			StringBuilder historicoBoletoRespErros = new StringBuilder();
	    			
					for(JsonElement erro : errosHistoricoBoleto) {
						String erroHistoricoBoleto = "";
						
						if (TipoEventoEnum.EMISSAO.getIdTipoEvento().equals(idTipoEvento)) {
							erroHistoricoBoleto = erro.getAsJsonObject().get("mensagem").getAsString().replace(JSON_INVALIDO, RETORNO_PADRAO);
						} else {
							erroHistoricoBoleto = erro.getAsJsonObject().get("message").getAsString().replace(JSON_INVALIDO, RETORNO_PADRAO);	
						}
						
						if (!erroHistoricoBoleto.isBlank()) {
							historicoBoletoRespErros.append(erroHistoricoBoleto);							
						}
						
						if (errosHistoricoBoleto.size() > 1) {
							historicoBoletoRespErros.append("\n");
						}
					}
					return historicoBoletoRespErros.toString();
				}
    		}
		}
    	return stacktrace;
    }
	
	private String formataStacktraceItau(String stacktrace){
    	if (!stacktrace.isEmpty() && stacktrace.length() > 72 && (stacktrace.contains("Status 422:") || stacktrace.contains("Status 400:"))) {
    		StringBuilder listErros = new StringBuilder();
    		listErros.append(stacktrace);
    		listErros.delete(0, 12);
    		Gson gson = new Gson();
            JsonObject errosHistoricoBoleto = gson.fromJson(listErros.toString(), JsonObject.class);
            JsonArray camposComErro = errosHistoricoBoleto.getAsJsonArray("campos");
            if(camposComErro.size() >0) {
            	StringBuilder mensagemErro = new StringBuilder();
            	for (JsonElement erro : camposComErro) {
            		String erroHistoricoBoleto = erro.getAsJsonObject().get("mensagem").getAsString();
            		if (!erroHistoricoBoleto.isBlank()) {
            			mensagemErro.append(erroHistoricoBoleto);							
					}
            		if (camposComErro.size() > 1) {
            			mensagemErro.append(". \n");
					}
            	}
            	return mensagemErro.toString().trim();            	
            }
		}
    	return stacktrace;
    }
	
	public LogEnvioDto findByNossonumeroAndStacktrace(String numeroTituloCliente, String stacktrace){
		Optional<LogEnvio> logEnvio = logEnvioRepository.findByNossonumeroAndStacktrace(numeroTituloCliente, stacktrace);
		
		if (logEnvio.isPresent()) {
			LogEnvioDto logEnvioDto = new LogEnvioDto();
			logEnvioDto.setId(logEnvio.get().getId());
			logEnvioDto.setNossonumero(logEnvio.get().getNossonumero());
			logEnvioDto.setMensagem(logEnvio.get().getMensagem());
			logEnvioDto.setStacktrace(logEnvio.get().getStacktrace());	
			return logEnvioDto;
		}
		return null;
	}
	
	public void insereRetornoBancoLogEnvio(BoletoDto boletoDto, Exception exception, String response, String nomeCampo, Object valorCampo, TipoEventoEnum tipoEvento) {
		if (TipoEventoEnum.BAIXA_CANCELAMENTO.equals(tipoEvento)) {
			boletoDto = GetRetornoErro.verificaQuantidadeTentativas(boletoDto, exception);
		}
		LogEnvioDto logEnvioDto = findByNossonumeroAndStacktrace(boletoDto.getNumeroTituloCliente(), "%Criado com sucesso%");
		boletoDto = GetRetornoErro.verificaResponseNotFound(boletoDto, exception, logEnvioDto);
		if (boletoDto != null) {
			ArrayList<BoletoDto> boletosDtoList = new ArrayList<BoletoDto>();
			boletosDtoList.add(boletoDto);
		
		}
		if (response.isBlank()) {
			logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, exception, tipoEvento);			
		} else {
			logEnvioDto = GetRetornoErro.insereRetornoBancoLogEnvio(boletoDto, response, tipoEvento);
		}
		if (logEnvioDto != null) {
			salvar(logEnvioDto);
		}
		
		if (TipoEventoEnum.ALTERACAO.equals(tipoEvento)) {
			log.error("Erro ao alterar {} do boleto no banco Itaú. - Número Título Cliente: {} Valor do campo: {} [Método: alteraBoletoApibanco]", isNull(nomeCampo) ? "" : nomeCampo, isNull(boletoDto.getNumeroTituloCliente())? "Nulo" : boletoDto.getNumeroTituloCliente(), isNull(valorCampo) ? "Nulo" : valorCampo.toString());
		}
		if (TipoEventoEnum.BAIXA_CANCELAMENTO.equals(tipoEvento)) {
			log.error("Erro ao cancelar boleto no banco. - Número Título Cliente: {} [Método: cancelaBoletoApibanco - Itaú]", isNull(boletoDto.getNumeroTituloCliente()) ? "Nulo" : boletoDto.getNumeroTituloCliente());
		}
		log.error(exception.getMessage(), exception);
    }
	
	public ArrayList<LogEnvio> findByLogEnvioRequest(LogEnvioSearchRequestDto filter, Pageable pageable){
		return (ArrayList<LogEnvio>) boletoCustomSearchRepository.findByLogEnvioRequest(filter);
	}
	
	public void insereLogEnvioBoletoEmStatusFinal(Boleto boletoOpt) {	
		StringBuilder stacktrace = new StringBuilder().append("Alteração não foi executada pois o boleto encontra-se com o status ")
				.append(boletoOpt.getStatusBanco().getDescricao()).append(" no banco.");
		LogEnvioDto logEnvioDto = new LogEnvioDto(boletoOpt.getNumeroTituloCliente(), TipoEventoEnum.ALTERACAO.getDescricaoEvento(), 
				stacktrace.toString(), boletoOpt.getStatus().getStatus(), TipoEventoEnum.ALTERACAO.getIdTipoEvento(), 
				SituacaoEnum.REJEITADA.getIdSituacao(), boletoOpt.getStatusBanco().getStatus());
		salvar(logEnvioDto);
	}
}