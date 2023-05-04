package br.com.boleto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.BancoResponseDto;
import br.com.boleto.service.implementation.BancoService;

@RestController
@RequestMapping("bancos")
public class BancoController {
    @Autowired
    BancoService bancoService;

    @GetMapping("/{id}")
    public ResponseEntity<BancoResponseDto> pesquisaBancoPorId(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(bancoService.pesquisaBancoPorId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<BancoResponseDto> buscaTodosBancos() {
        return new ResponseEntity<>(bancoService.buscaTodosBancos(), HttpStatus.OK);
    }
}
