package br.com.boleto.controller;

import java.io.InputStream;
import java.util.ArrayList;

import br.com.boleto.persistence.dtos.ContaCredenciamentoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.ContaDto;
import br.com.boleto.persistence.dtos.ContaPesquisaDto;
import br.com.boleto.persistence.dtos.ContaResponseDto;
import br.com.boleto.persistence.dtos.ContaSearchRequestDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.service.implementation.ContaService;

@RestController
@RequestMapping("contas")
public class ContaController {
    @Autowired
    ContaService contaService;

    @PostMapping()
    public ResponseEntity<ResponseDto> gerenciarCredenciamentoConta(@RequestBody ContaCredenciamentoDto contaDto) {
        return new ResponseEntity<>(contaService.gerenciarCredenciamentoConta(contaDto), HttpStatus.OK);
    }

    @PostMapping("/sincronizaContas")
    public ResponseEntity<ResponseDto> sincronizarDadosConta(@RequestBody ContaDto sincronizaoContaDto) {
        return new ResponseEntity<>(contaService.sincronizarDadosConta(sincronizaoContaDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaPesquisaDto> pesquisaContaPorId(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(contaService.pesquisaContaPorId(id), HttpStatus.OK);
    }
    
    @GetMapping("/getContas")
	public ResponseEntity<ArrayList<ContaResponseDto>> getContas() {
		return new ResponseEntity<>(contaService.getContas(), HttpStatus.OK);
	}

    @GetMapping("/file")
    public ResponseEntity<InputStream> downloadFile(){
    	return new ResponseEntity<>(contaService.downloadFile(), HttpStatus.OK);
    }
    
    @PostMapping("/contasList")
	public ResponseEntity<ArrayList<Conta>>  findByJobRequest(@RequestBody ContaSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(contaService.findByContaRequest(filter,pageable), HttpStatus.OK);
	}

    @DeleteMapping("/deleteConta")
    ResponseEntity<ResponseDto>  deleteConta(@RequestBody ContaDto contaDto){
        return new ResponseEntity<>(contaService.deleteConta(contaDto), HttpStatus.OK);
    }
}

