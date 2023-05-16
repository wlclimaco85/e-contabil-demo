package br.com.acoes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acoes.persistence.dtos.ErrosResponseDto;
import br.com.acoes.persistence.entity.Erros;
import br.com.acoes.service.implementation.ErrosService;

@RestController
@RequestMapping("erros")
public class ErrosController {
    @Autowired
    ErrosService ersService;
    
    @PostMapping("/insert")
	public ResponseEntity<ErrosResponseDto>  insert(@RequestBody Erros filter){
		return new ResponseEntity<>(ersService.insert(filter), HttpStatus.OK);
	}
}
