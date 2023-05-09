package br.com.boleto.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.OrdensResponseDto;
import br.com.boleto.persistence.entity.Ordens;
import br.com.boleto.service.implementation.OrdensService;

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
    

    
}
