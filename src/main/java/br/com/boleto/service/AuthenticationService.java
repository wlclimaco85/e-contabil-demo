package br.com.boleto.service;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.persistence.dtos.LoginDto;
import br.com.boleto.persistence.dtos.LoginResponseDto;
import br.com.boleto.persistence.entity.Certificado;

public interface AuthenticationService {
    BancoEnum getType();

    LoginResponseDto authentication(LoginDto loginDto);
    
    Certificado certificado(LoginDto loginDto);
  
}
