package br.com.boleto.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class RequestUtilTeste {

    public static HttpHeaders buscaHttpHeadersFormUrlEncoded(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ConstantesTeste.AUTHORIZATION_BASIC);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());

        return headers;
    }
    public static HttpHeaders buscaHttpHeadersJson(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", ConstantesTeste.AUTHORIZATION_BASIC);
        headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());

        return headers;
    }
}
