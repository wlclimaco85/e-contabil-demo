package br.com.boleto.service;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.service.implementation.BancoBrasilAuthService;
import br.com.boleto.service.implementation.BancoItauAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

@Component
public class AuthenticationFactory {

    @Autowired
    BancoBrasilAuthService bancoBrasilAuthService;

    @Autowired
    BancoItauAuthService bancoItauAuthService;

    private EnumMap<BancoEnum, AuthenticationService> bancosMap;

    @Autowired
    public AuthenticationFactory(List<AuthenticationService> authenticationService) {
        bancosMap = new EnumMap<>(BancoEnum.class);
        for (AuthenticationService authentication: authenticationService) {
            bancosMap.put(authentication.getType(), authentication);
        }
    }

    public AuthenticationService getAuthentication(BancoEnum banco){
        return bancosMap.get(banco);
    }
    
    public AuthenticationService getCertificado(BancoEnum banco){
        return bancosMap.get(banco);
    }
}
