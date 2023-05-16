package br.com.acoes.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.acoes.persistence.dtos.OrdensResponseDto;
import br.com.acoes.persistence.entity.Ordens;
import br.com.acoes.service.implementation.OrdensService;

@RestController
@RequestMapping("ordens")
public class OrdensController {
    @Autowired
    OrdensService ordensService;

    @PostMapping("/ordensByAcaoId")
    public ResponseEntity<ArrayList<OrdensResponseDto>> pesquisaBancoPorId(@RequestBody Ordens filter) {
        return new ResponseEntity<>(ordensService.pesquisaBancoPorId(filter.getId()), HttpStatus.OK);
    }
    
    @PostMapping("getOrdens")
    public ResponseEntity<ArrayList<OrdensResponseDto>>  buscaIndicacoes() {
        return new ResponseEntity<>(ordensService.all(), HttpStatus.OK);
    }
    
    @PostMapping("getHistorico")
    public ResponseEntity<ArrayList<OrdensResponseDto>>  buscaIndicacoes2() {
        return new ResponseEntity<>(ordensService.all(), HttpStatus.OK);
    }
    
    @GetMapping("acaoCompraVendida")
    public ResponseEntity<String>  pesquisaBancoPorId2(@RequestParam("id") Integer id,@RequestParam("valor") Double price) {
    	Ordens ordens = ordensService.getAcaoById(id);
    	return new ResponseEntity<>(ordensService.compraVender(ordens,price), HttpStatus.OK);
    }
    

    
}
