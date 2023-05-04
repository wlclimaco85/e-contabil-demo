package br.com.boleto.controller;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.service.AuthenticationFactory;
import br.com.boleto.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {
    @Autowired
    AuthenticationFactory authenticationFactory;

    @PostMapping()
    public ResponseEntity<LoginResponseDto> authentication(LoginDto loginDto) {
        AuthenticationService authenticationService =  authenticationFactory.getAuthentication(loginDto.getBanco() != null ? loginDto.getBanco() : BancoEnum.BANCO_DO_BRASIL);
        return ResponseEntity.ok(authenticationService.authentication(loginDto));
    }
}
