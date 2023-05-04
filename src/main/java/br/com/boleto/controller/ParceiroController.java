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
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.ParceiroResponseDto;
import br.com.boleto.persistence.dtos.ParceiroSearchRequestDto;
import br.com.boleto.persistence.entity.Parceiro;
import br.com.boleto.service.implementation.ParceiroService;

@RestController
@RequestMapping("parceiro")
public class ParceiroController {
    @Autowired
    ParceiroService parceiroService;

    @GetMapping("/{id}")
    public ResponseEntity<ParceiroResponseDto> pesquisaParceiroPorId(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(parceiroService.pesquisaParceiroPorStatus(id), HttpStatus.OK);
    }
    
    @PostMapping("/parceiros")
    public ResponseEntity<ArrayList<Parceiro>>  findByParceiroRequest(@RequestBody ParceiroSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(parceiroService.findByParceiroRequest(filter,pageable), HttpStatus.OK);
	}
}
