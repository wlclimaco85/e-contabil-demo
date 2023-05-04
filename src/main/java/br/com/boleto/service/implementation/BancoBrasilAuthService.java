package br.com.boleto.service.implementation;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.exception.FailedAuthenticationException;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.entity.Certificado;
import br.com.boleto.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Slf4j
@Component
public class BancoBrasilAuthService implements AuthenticationService {
    @Value("${url.api.bb.auth}")
    private String URL;

    @Value("${grant.type.api.bb.auth}")
    private String GRANT_TYPE;

    @Value("${scope.api.bb.auth}")
    private String SCOPE;

    @Override
    public BancoEnum getType() {
        return BancoEnum.BANCO_DO_BRASIL;
    }

    @Override
    public LoginResponseDto authentication(LoginDto loginDto) {
        try {
            String auth = loginDto.getClient_id() + ":" + loginDto.getClient_secret();
            String base64Auth = Base64.getEncoder().encodeToString(auth.getBytes());

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", GRANT_TYPE);
            body.add("scope", SCOPE);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + base64Auth);
            headers.add("Content-Type", "application/x-www-form-urlencoded");

            HttpEntity<?> request = new HttpEntity<>(body, headers);

            ResponseEntity<LoginResponseDto> response = new RestTemplate()
                    .exchange(URL, HttpMethod.POST, request, LoginResponseDto.class);

            return response.getBody();
        } catch (RuntimeException e) {
        	log.error("O teste das credenciais com o banco falhou. [Método: authentication]");
        	log.error(e.getMessage(), e);
            throw new FailedAuthenticationException("As credenciais inseridas apresentaram erro no teste de integração. Por favor, tente novamente.");
        }
    }

	@Override
	public Certificado certificado(LoginDto loginDto) {	
		return null;
	}
}
