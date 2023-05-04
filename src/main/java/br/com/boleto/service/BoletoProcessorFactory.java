package br.com.boleto.service;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.service.implementation.BancoBrasilProcessorService;
import br.com.boleto.service.implementation.BancoItauProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

@Component
public class BoletoProcessorFactory {

    @Autowired
    BancoBrasilProcessorService bancoBrasilProcessorService;

    @Autowired
    BancoItauProcessorService bancoItauProcessorService;

    private EnumMap<BancoEnum, BoletoProcessorService> bancosMap;

    @Autowired
    public BoletoProcessorFactory(List<BoletoProcessorService> boletoProcessorService) {
        bancosMap = new EnumMap<>(BancoEnum.class);
        for (BoletoProcessorService boletoProcessor: boletoProcessorService) {
            bancosMap.put(boletoProcessor.getType(), boletoProcessor);
        }
    }

    public BoletoProcessorService getProcessor(BancoEnum banco){
        return bancosMap.get(banco);
    }
}
