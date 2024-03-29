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
import org.springframework.web.bind.annotation.RestController;

import br.com.acoes.persistence.dtos.CorretoraResponseDto;
import br.com.acoes.persistence.entity.Corretora;
import br.com.acoes.service.implementation.CorretoraService;

@RestController
@RequestMapping("corretora")
public class CorretoraController {
    @Autowired
    CorretoraService corretoraService;

    @PostMapping("/corretoraByAcaoId")
    public ResponseEntity<ArrayList<CorretoraResponseDto>> pesquisaBancoPorId(@RequestBody Corretora filter) {
        return new ResponseEntity<>(corretoraService.pesquisaBancoPorId(filter.getId()), HttpStatus.OK);
    }
    
    @PostMapping("/insert")
	public ResponseEntity<Corretora>  insert(@RequestBody Corretora filter){
		return new ResponseEntity<>(corretoraService.insert(filter), HttpStatus.OK);
	}
    
    @PostMapping("getCorretoras")
    public ResponseEntity<ArrayList<CorretoraResponseDto>>  buscaIndicacoes(@RequestBody Corretora filter) {
        return new ResponseEntity<>(corretoraService.all(filter), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Corretora> pesquisaBancoPorId(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(corretoraService.corretoraByUsuario(id), HttpStatus.OK);
    }
    

    
}
