package br.com.boleto.controller;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import br.com.boleto.persistence.dtos.LogEnvioSearchRequestDto;
import br.com.boleto.persistence.repository.BoletoRepository;
import br.com.boleto.persistence.repository.LogEnvioRepository;
import br.com.boleto.util.BoletoUtil;
import br.com.boleto.util.ModeloUtil;
import br.com.boleto.util.RequestUtilTeste;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LogEnvioControllerTeste {
	
	private static final String LOG_ENVIO_LIST = "/logEnvioList";

	private final String URL = "/logEnvio";
	
	@Autowired
    private TestRestTemplate testRestTemplate;
	
	@Autowired
	private LogEnvioRepository logEnvioRepository;
	
	@Autowired
	private BoletoRepository boletoRepository;
	
	@Test
	public void getLogEnvioBoletoTeste() {
		
		logEnvioRepository.save(ModeloUtil.criarLogEnvio());
		boletoRepository.save(BoletoUtil.criaBoleto());
		
		HttpEntity<Object> httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+"/123456", HttpMethod.GET, httpEntity, ArrayList.class);
        
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void findByLogEnvioRequestTeste() {
		
		logEnvioRepository.save(ModeloUtil.criarLogEnvio());
		boletoRepository.save(BoletoUtil.criaBoleto());
		
		LogEnvioSearchRequestDto logEnvioSearchRequestDto = new LogEnvioSearchRequestDto();
		
		HttpEntity<LogEnvioSearchRequestDto> httpEntity = new HttpEntity<>(logEnvioSearchRequestDto, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ArrayList> response = testRestTemplate.exchange(URL+LOG_ENVIO_LIST, HttpMethod.POST, httpEntity, ArrayList.class);
        
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
}
