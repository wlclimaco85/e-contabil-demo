package br.com.boleto.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.BoletoResponseDto;
import br.com.boleto.persistence.dtos.LogEnvioDto;

public class GetRetornoErro {
	private static final String STATUS_400 = "Status 400: ";
	private static final String STATUS_401 = "Status 401: Não autorizado";
	private static final String STATUS_403 = "Status 403: Acesso proibido";
	private static final String STATUS_404_REQUISICAO_NAO_ENCONTRADA = "Status 404: Requisição não encontrada";
	private static final String STATUS_405 = "Status 405: Método não permitido";
	private static final String STATUS_422 = "Status 422: ";
	private static final String STATUS_428 = "Status 428: Pré-requisito necessário";
	private static final String STATUS_500_ERRO_INTERNO_SERVIDOR = "Status 500: Erro interno do Servidor";
	private static final String STATUS_501 = "Status 501: Não implementado";
	private static final String STATUS_503 = "Status 503: Serviço indisponível";
	

	public static LogEnvioDto insereRetornoBancoLogEnvio(BoletoDto boletoDto, ResponseEntity<?> response, TipoEventoEnum tipoEvento) {
		if (response != null && boletoDto != null && tipoEvento != null) {
			LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), tipoEvento.getDescricaoEvento(), retornaMensagemErroLogEnvio(response), boletoDto.getStatus(), tipoEvento.getIdTipoEvento(), SituacaoEnum.REJEITADA.getIdSituacao(), boletoDto.getStatusBanco());
			return logEnvioDto;
		}
		return null;
	}
	
	public static LogEnvioDto insereRetornoBancoLogEnvio(BoletoDto boletoDto, Exception e, TipoEventoEnum tipoEvento) {
		if (e != null && boletoDto != null && tipoEvento != null) {
			LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), tipoEvento.getDescricaoEvento(), retornaMensagemErroLogEnvio(e), boletoDto.getStatus(), tipoEvento.getIdTipoEvento(), SituacaoEnum.REJEITADA.getIdSituacao(), boletoDto.getStatusBanco());
			return logEnvioDto;
		}
		return null;
	}
	
	public static LogEnvioDto insereRetornoBancoLogEnvio(BoletoDto boletoDto, String response, TipoEventoEnum tipoEvento) {
		if (!response.isBlank()) {
			LogEnvioDto logEnvioDto = new LogEnvioDto(boletoDto.getNumeroTituloCliente(), tipoEvento.getDescricaoEvento(), retornaMensagemErroLogEnvio(response), boletoDto.getStatus(), tipoEvento.getIdTipoEvento(), SituacaoEnum.REJEITADA.getIdSituacao(), boletoDto.getStatusBanco());
			return logEnvioDto;
		}
		return null;
	}
	
	public static BoletoDto verificaResponseNotFound(BoletoDto boletoDto, Exception e, LogEnvioDto logEnvioDto) {
		if (e.getMessage() != null) {
			if (e.getMessage().contains("404 Not Found") && logEnvioDto != null) {	
				boletoDto.setStatus(StatusBoletoEnum.BAIXADO_PELO_BANCO.getStatus());							
			}			
		}
		return boletoDto;
	}
	
	public static BoletoDto verificaQuantidadeTentativas(BoletoDto boletoDto, Exception e) {
		if (boletoDto.getQuantidadeTentativas() == null) {
			boletoDto.setQuantidadeTentativas(0);
		}
		
		if(boletoDto.getQuantidadeTentativas() >= 3) {
			if (StatusBoletoEnum.AGUARDANDO_ENVIO_PARA_BANCO.getStatus().equals(boletoDto.getStatus())) {
				boletoDto.setStatus(StatusBoletoEnum.ERRO.getStatus());
			} else {
				boletoDto.setStatus(boletoDto.getStatusBanco());
			}
		} else {
			boletoDto.setQuantidadeTentativas(boletoDto.getQuantidadeTentativas() + 1);			
		}
		
		return boletoDto;
	}
	
	public static ResponseEntity<BoletoResponseDto> setRetornoErroBancoBoleto(BoletoDto boletoDto, Exception exception){
		BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
		boletoResponseDto.setBoleto(boletoDto);
		
		if(exception == null || exception.getMessage() == null){
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(exception.getMessage().contains("404")) {
			boletoResponseDto.setMessage(STATUS_404_REQUISICAO_NAO_ENCONTRADA);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.NOT_FOUND);
		}
		if(exception.getMessage().contains("500")) {
			boletoResponseDto.setMessage(STATUS_500_ERRO_INTERNO_SERVIDOR);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(exception.getMessage().contains("400")) {
			StringBuilder stacktrace = new StringBuilder().append(STATUS_400).append(exception.getMessage());
			boletoResponseDto.setMessage(stacktrace.toString());
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.BAD_REQUEST);
		}
		if(exception.getMessage().contains("401")) {
			boletoResponseDto.setMessage(STATUS_401);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.UNAUTHORIZED);
		} 
		if(exception.getMessage().contains("403")) {
			boletoResponseDto.setMessage(STATUS_403);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.FORBIDDEN);
		}
		if(exception.getMessage().contains("405")) {
			boletoResponseDto.setMessage(STATUS_405);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.METHOD_NOT_ALLOWED);
		}
		if(exception.getMessage().contains("422")) {
			StringBuilder stacktrace = new StringBuilder().append(STATUS_422).append(exception.getMessage());
			boletoResponseDto.setMessage(stacktrace.toString());
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if(exception.getMessage().contains("428")) {
			boletoResponseDto.setMessage(STATUS_428);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.PRECONDITION_REQUIRED);
		}
		if(exception.getMessage().contains("501")) {
			boletoResponseDto.setMessage(STATUS_501);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.NOT_IMPLEMENTED);
		}
		if(exception.getMessage().contains("503")) {
			boletoResponseDto.setMessage(STATUS_503);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.SERVICE_UNAVAILABLE);
		} 
		boletoResponseDto.setMessage(exception.getMessage());
		return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	public static ResponseEntity<BoletoResponseDto> setRetornoErroBancoBoleto(BoletoDto boletoDto, String reponse){
		BoletoResponseDto boletoResponseDto = new BoletoResponseDto();
		boletoResponseDto.setBoleto(boletoDto);
		
		if(reponse.isBlank()){
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(reponse.contains("404")) {
			boletoResponseDto.setMessage(STATUS_404_REQUISICAO_NAO_ENCONTRADA);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.NOT_FOUND);
		}
		if(reponse.contains("500")) {
			boletoResponseDto.setMessage(STATUS_500_ERRO_INTERNO_SERVIDOR);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(reponse.contains("400")) {
			StringBuilder stacktrace = new StringBuilder().append(STATUS_400).append(reponse);
			boletoResponseDto.setMessage(stacktrace.toString());
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.BAD_REQUEST);
		}
		if(reponse.contains("401")) {
			boletoResponseDto.setMessage(STATUS_401);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.UNAUTHORIZED);
		} 
		if(reponse.contains("403")) {
			boletoResponseDto.setMessage(STATUS_403);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.FORBIDDEN);
		}
		if(reponse.contains("405")) {
			boletoResponseDto.setMessage(STATUS_405);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.METHOD_NOT_ALLOWED);
		}
		if(reponse.contains("422")) {
			StringBuilder stacktrace = new StringBuilder().append(STATUS_422).append(reponse);
			boletoResponseDto.setMessage(stacktrace.toString());
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if(reponse.contains("428")) {
			boletoResponseDto.setMessage(STATUS_428);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.PRECONDITION_REQUIRED);
		}
		if(reponse.contains("501")) {
			boletoResponseDto.setMessage(STATUS_501);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.NOT_IMPLEMENTED);
		}
		if(reponse.contains("503")) {
			boletoResponseDto.setMessage(STATUS_503);
			return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.SERVICE_UNAVAILABLE);
		} 
		boletoResponseDto.setMessage(reponse);
		return new ResponseEntity<BoletoResponseDto>(boletoResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private static String retornaMensagemErroLogEnvio(ResponseEntity<?> response) {
		if (response != null && response.getStatusCode() != null) {
			if(HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {	
				return STATUS_404_REQUISICAO_NAO_ENCONTRADA;
			}
			if(HttpStatus.INTERNAL_SERVER_ERROR.equals(response.getStatusCode())) {
				return STATUS_500_ERRO_INTERNO_SERVIDOR;
			}
			if(HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
				return STATUS_401;
			} 
			if(HttpStatus.FORBIDDEN.equals(response.getStatusCode())) {
				return STATUS_403;
			} 
			if(HttpStatus.METHOD_NOT_ALLOWED.equals(response.getStatusCode())) {
				return STATUS_405;
			} 
			if(HttpStatus.UNPROCESSABLE_ENTITY.equals(response.getStatusCode())) {
				return new StringBuilder().append(STATUS_422).append(response.getBody()).toString();
			}
			if(HttpStatus.PRECONDITION_REQUIRED.equals(response.getStatusCode())) {
				return STATUS_428;
			} 
			if(HttpStatus.NOT_IMPLEMENTED.equals(response.getStatusCode())) {
				return STATUS_501;
			} 
			if(HttpStatus.SERVICE_UNAVAILABLE.equals(response.getStatusCode())) {
				return STATUS_503;
			} 
			if(HttpStatus.BAD_REQUEST.equals(response.getStatusCode())) {
				return new StringBuilder().append(STATUS_400).append(response.getBody()).toString();
			}
			return new StringBuilder().append(response.getStatusCode()).append(response.getBody()).toString();
		} else {
			return "Status code do response está nulo.";
		}
	}
	
	private static String retornaMensagemErroLogEnvio(Exception e) {
		if (e != null && e.getMessage() != null) {
			if(e.getMessage().contains("404")) {	
				return STATUS_404_REQUISICAO_NAO_ENCONTRADA;
			} 
			if(e.getMessage().contains("500")) {
				return STATUS_500_ERRO_INTERNO_SERVIDOR;
			}
			if(e.getMessage().contains("401")) {
				return STATUS_401;
			} 
			if(e.getMessage().contains("403")) {
				return STATUS_403;
			} 
			if(e.getMessage().contains("405")) {
				return STATUS_405;
			} 
			if(e.getMessage().contains("422")) {
				return new StringBuilder().append(STATUS_422).append(e.getMessage()).toString();
			} 
			if(e.getMessage().contains("428")) {
				return STATUS_428;
			} 
			if(e.getMessage().contains("501")) {
				return STATUS_501;
			} 
			if(e.getMessage().contains("503")) {
				return STATUS_503;
			} 
			if(e.getMessage().contains("400")) {
				return new StringBuilder().append(STATUS_400).append(e.getMessage()).toString();
			}
			return new StringBuilder().append(e.getMessage()).toString();
		} else {
			return "Exception está nulo.";
		}
	}
	
	private static String retornaMensagemErroLogEnvio(String reponse) {
		if(reponse.contains("404")) {	
			return STATUS_404_REQUISICAO_NAO_ENCONTRADA;
		} 
		if(reponse.contains("500")) {
			return STATUS_500_ERRO_INTERNO_SERVIDOR;
		}
		if(reponse.contains("401")) {
			return STATUS_401;
		} 
		if(reponse.contains("403")) {
			return STATUS_403;
		} 
		if(reponse.contains("405")) {
			return STATUS_405;
		} 
		if(reponse.contains("422")) {
			return new StringBuilder().append(STATUS_422).append(reponse).toString();
		} 
		if(reponse.contains("428")) {
			return STATUS_428;
		} 
		if(reponse.contains("501")) {
			return STATUS_501;
		} 
		if(reponse.contains("503")) {
			return STATUS_503;
		} 
		if(reponse.contains("400")) {
			return new StringBuilder().append(STATUS_400).append(reponse).toString();
		}
		return new StringBuilder().append(reponse).toString();
	}
}
