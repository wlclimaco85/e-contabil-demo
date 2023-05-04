package br.com.boleto.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.LogEnvioResponseDto;
import br.com.boleto.persistence.dtos.LogEnvioSearchRequestDto;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.service.implementation.LogEnvioService;

@RestController
@RequestMapping("logEnvio")
public class LogEnvioController {
    @Autowired
    LogEnvioService logEnvioService;
    
    @GetMapping("/{numeroTituloCliente}")
    public ResponseEntity<ArrayList<LogEnvioResponseDto>> getLogEnvioBoleto(@PathVariable("numeroTituloCliente") String numeroTituloCliente, @RequestParam(defaultValue = "0") Integer bancoId) {
        return new ResponseEntity<>(logEnvioService.findByNumeroTituloCliente(numeroTituloCliente, bancoId), HttpStatus.OK);
    }
    
    @PostMapping("/logEnvioList")
	public ResponseEntity<ArrayList<LogEnvio>>  findByLogEnvioRequest(@RequestBody LogEnvioSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(logEnvioService.findByLogEnvioRequest(filter,pageable), HttpStatus.OK);
	}
}
