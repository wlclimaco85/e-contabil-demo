package br.com.boleto.queues;

import java.util.concurrent.BlockingQueue;

import br.com.boleto.enums.BancoEnum;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.service.BoletoProcessorFactory;

public class RegistraBoletoQueue implements Runnable {
    private BoletoProcessorFactory boletoProcessorFactory;
    private BlockingQueue<Conta> queue;

    public RegistraBoletoQueue(BlockingQueue<Conta> queue, BoletoProcessorFactory boletoProcessorFactory) {
        this.queue = queue;
        this.boletoProcessorFactory = boletoProcessorFactory;
    }

    @Override
    public void run() {
        try {
            while (queue.size() > 0) {
                Conta conta = null;
                if (queue.size() > 0) {
                    conta = queue.take();
                }
                if (conta != null) {
                    boletoProcessorFactory.getProcessor(BancoEnum.valueById(conta.getBanco().getId())).registraBoleto(conta);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
