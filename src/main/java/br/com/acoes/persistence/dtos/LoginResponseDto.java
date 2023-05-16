package br.com.acoes.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String access_token;
    private String token_type;
    private Integer expires_in;
}
