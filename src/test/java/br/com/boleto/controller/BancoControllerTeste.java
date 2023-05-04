package br.com.boleto.controller;

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

import br.com.boleto.persistence.dtos.BancoResponseDto;
import br.com.boleto.persistence.repository.BancoRepository;
import br.com.boleto.util.ConstantesTeste;
import br.com.boleto.util.ModeloUtil;
import br.com.boleto.util.RequestUtilTeste;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BancoControllerTeste {
    private final String URL = "/bancos";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BancoRepository bancoRepository;

    @Test
    public void pesquisaBancoPorIdTeste(){
        bancoRepository.save(ModeloUtil.criaBanco());

        HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<BancoResponseDto> response = testRestTemplate
                .exchange(URL +"/"+ ConstantesTeste.ID_BANCO_BRASIL, HttpMethod.GET, httpEntity, BancoResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = testRestTemplate
                .exchange(URL +"/"+ 0, HttpMethod.GET, httpEntity, BancoResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void buscaTodosBancosTeste(){
        HttpEntity httpEntity = new HttpEntity<>(null, RequestUtilTeste.buscaHttpHeadersJson());

        ResponseEntity<BancoResponseDto> response = testRestTemplate
                .exchange(URL, HttpMethod.GET, httpEntity, BancoResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
