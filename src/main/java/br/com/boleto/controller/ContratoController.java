package br.com.boleto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.ContratoDto;
import br.com.boleto.persistence.dtos.ResponseDto;
import br.com.boleto.persistence.dtos.TermoDto;
import br.com.boleto.service.implementation.ContratoService;

@RestController
@RequestMapping("contrato")
public class ContratoController {
	
	@Autowired
	ContratoService contratoService;

	@GetMapping("/preco")
	public ResponseEntity<Double> getPreco(@RequestParam Integer idapibanco, @RequestParam Integer idparceiro, 
			@RequestParam Integer codserv, @RequestParam String usuContratante, @RequestParam Integer versaoAceite) {
		return new ResponseEntity<>(contratoService.getPreco(idapibanco, idparceiro, codserv, usuContratante, versaoAceite), HttpStatus.OK);
	}
	
	@GetMapping("/termo")
	public ResponseEntity<TermoDto> getTermo(@RequestParam Integer idparceiro, @RequestParam Integer codserv, @RequestParam(defaultValue = "-1") Integer idapibanco) {
		return new ResponseEntity<>(contratoService.getTermo(idparceiro, codserv, idapibanco), HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<ResponseDto> salvaContrato(@RequestBody ContratoDto contratoDto) {
		return new ResponseEntity<>(contratoService.salvaContrato(contratoDto), HttpStatus.OK);
	}
}
