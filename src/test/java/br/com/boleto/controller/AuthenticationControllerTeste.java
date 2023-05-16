package br.com.boleto.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthenticationControllerTeste {
	
//    private final String URL = "/authentication";
//
//    @Autowired
//    private TestRestTemplate testRestTemplate;
//
//    @Test
//    public void authenticationBancoBrasilAuthorization(){
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
//        body.add("client_secret", ConstantesTeste.CLIENT_SECRET_BANCO_BRASIL);
//        body.add("banco", BancoEnum.BANCO_DO_BRASIL.toString());
//
//        HttpEntity<MultiValueMap<String, String>> httpEntityLoginDto = new HttpEntity<>(body,RequestUtilTeste.buscaHttpHeadersFormUrlEncoded());
//
//        ResponseEntity<LoginResponseDto> response = testRestTemplate
//                .exchange(URL, HttpMethod.POST, httpEntityLoginDto, LoginResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//
//        body = new LinkedMultiValueMap<>();
//        body.add("client_id", ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
//        body.add("client_secret", ConstantesTeste.CLIENT_SECRET_BANCO_BRASIL);
//
//        httpEntityLoginDto = new HttpEntity<>(body,RequestUtilTeste.buscaHttpHeadersFormUrlEncoded());
//
//        response = testRestTemplate
//                .exchange(URL, HttpMethod.POST, httpEntityLoginDto, LoginResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//    }
//
//    @Test
//    public void authenticationBancoBrasilUnAuthorization(){
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", ConstantesTeste.CLIENT_ID_BANCO_BRASIL);
//        body.add("client_secret", "");
//        body.add("banco", BancoEnum.BANCO_DO_BRASIL.toString());
//
//        HttpEntity<MultiValueMap<String, String>> httpEntityLoginDto = new HttpEntity<>(body,RequestUtilTeste.buscaHttpHeadersFormUrlEncoded());
//
//        ResponseEntity<LoginResponseDto> response = testRestTemplate
//                .exchange(URL, HttpMethod.POST, httpEntityLoginDto, LoginResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
//    }
//
//    @Test
//    public void authenticationItauAuthorization(){
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", ConstantesTeste.CLIENT_ID_BANCO_ITAU);
//        body.add("client_secret", ConstantesTeste.CLIENT_SECRET_BANCO_ITAU);
//        body.add("banco", BancoEnum.ITAU.toString());
//
//        HttpEntity<MultiValueMap<String, String>> httpEntityLoginDto = new HttpEntity<>(body,RequestUtilTeste.buscaHttpHeadersFormUrlEncoded());
//
//        ResponseEntity<LoginResponseDto> response = testRestTemplate
//                .exchange(URL, HttpMethod.POST, httpEntityLoginDto, LoginResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
//    }
//
//    @Test
//    public void authenticationItauUnAuthorization(){
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        body.add("client_id", ConstantesTeste.CLIENT_ID_BANCO_ITAU);
//        body.add("client_secret", "");
//        body.add("banco", BancoEnum.ITAU.toString());
//
//        HttpEntity<MultiValueMap<String, String>> httpEntityLoginDto = new HttpEntity<>(body, RequestUtilTeste.buscaHttpHeadersFormUrlEncoded());
//
//        ResponseEntity<LoginResponseDto> response = testRestTemplate
//                .exchange(URL, HttpMethod.POST, httpEntityLoginDto, LoginResponseDto.class);
//
//        Assertions.assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
//    }
}