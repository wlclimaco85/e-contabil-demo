package br.com.boleto.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.AcoesResponseDto2;
import br.com.boleto.persistence.dtos.EstrategiasResponseDto;
import br.com.boleto.persistence.dtos.EstrategiasResponseDto2;
import br.com.boleto.persistence.entity.Estrategias;
import br.com.boleto.service.implementation.EstrategiaService;

@RestController
@RequestMapping("estrategia")
public class EstrategiasController {
    @Autowired
    EstrategiaService estrategiasService;

    @PostMapping("/estrategiasByAcaoId")
    public ResponseEntity<ArrayList<EstrategiasResponseDto>> pesquisaBancoPorId(@RequestBody Estrategias filter) {
        return new ResponseEntity<>(estrategiasService.pesquisaBancoPorId(filter.getId()), HttpStatus.OK);
    }
    
    @PostMapping("/insert")
	public ResponseEntity<EstrategiasResponseDto>  insert(@RequestBody Estrategias filter){
		return new ResponseEntity<>(estrategiasService.insertt(filter), HttpStatus.OK);
	}
    
    @PostMapping("getEstrategias")
    public ResponseEntity<ArrayList<EstrategiasResponseDto2>>  buscaIndicacoes() {
        return new ResponseEntity<>(estrategiasService.all(), HttpStatus.OK);
    }
    

    
}
