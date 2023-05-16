package br.com.acoes;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiAcoesApplication {
	public static void main(String[] args) {		
		// setando timezone para utc, afim de padronizar a hora
	    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
		SpringApplication.run(ApiAcoesApplication.class, args);
	}
}
