package br.com.boleto.service;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.persistence.entity.Conta;

public interface BoletoProcessorService {
    BancoEnum getType();
    public void registraBoleto(Conta conta);
    public void alteraBoleto(Conta conta);
    public void consultaBoleto(Conta conta);
    public void cancelaBoleto(Conta conta);
    public void atualizarStatusBoleto(Conta conta);
}
