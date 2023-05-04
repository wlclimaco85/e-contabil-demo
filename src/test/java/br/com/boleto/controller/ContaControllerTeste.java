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

import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.dtos.ContaPesquisaDto;
import br.com.boleto.persistence.dtos.ContaSearchRequestDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.util.ModeloUtil;
import br.com.boleto.util.RequestUtilTeste;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ContaControllerTeste {
    private static final String CONTAS_LIST = "/contasList";

	private static final String FILE = "/file";
    
    private static final String GET_CONTAS = "/getContas";

	private final String URL = "/contas";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void criaAlteraContaTeste(){
        ContaDto contaDto = ModeloUtil.criaContaDto();

        HttpEntity httpEntity = new HttpEntity<>(contaDto, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ResponseDto> response = testRestTemplate
                .exchange(URL, HttpMethod.POST, httpEntity, ResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        contaDto.setStatusapi("N");
        contaDto.setDescredenciar(true);

        httpEntity = new HttpEntity<>(contaDto, RequestUtilTeste.buscaHttpHeadersJson());

        response = testRestTemplate
                .exchange(URL, HttpMethod.POST, httpEntity, ResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void pesquisaContaPorIdTeste(){
        ContaDto contaDto = ModeloUtil.criaContaDto();
        contaDto.setCodcta(50);

        HttpEntity httpEntity = new HttpEntity<>(contaDto, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ResponseDto> responseCreate = testRestTemplate
                .exchange(URL, HttpMethod.POST, httpEntity, ResponseDto.class);

        Assertions.assertEquals(responseCreate.getStatusCode(), HttpStatus.OK);

        Integer idConta = responseCreate.getBody().getId();

        httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ContaPesquisaDto> response = testRestTemplate
                .exchange(URL +"/"+ idConta, HttpMethod.GET, httpEntity, ContaPesquisaDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = testRestTemplate
                .exchange(URL +"/"+ 999, HttpMethod.GET, httpEntity, ContaPesquisaDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    
    @Test
    public void getContasTest() {
    	HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ArrayList> responseCreate = testRestTemplate.exchange(URL+GET_CONTAS, HttpMethod.GET, httpEntity, ArrayList.class);

        Assertions.assertEquals(responseCreate.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    public void downloadFileTeste(){
    	
    	HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<String> responseCreate = testRestTemplate.exchange(URL+FILE, HttpMethod.GET, httpEntity, String.class);

        Assertions.assertEquals(responseCreate.getStatusCode(), HttpStatus.OK);
    	
    }
    
    @Test
    public void findByJobRequestTeste(){
    	
    	ContaSearchRequestDto contaSearchRequestDto = ModeloUtil.criaContaSearchRequestDto();
    	
    	HttpEntity httpEntity = new HttpEntity<>(contaSearchRequestDto, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<ArrayList> responseCreate = testRestTemplate.exchange(URL+CONTAS_LIST, HttpMethod.POST, httpEntity, ArrayList.class);

        Assertions.assertEquals(responseCreate.getStatusCode(), HttpStatus.OK);
    	
    }
}
