package br.com.boleto.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.boleto.persistence.dtos.AtualizaBoletosRequestDto;
import br.com.boleto.persistence.dtos.BoletoDto;
import br.com.boleto.persistence.dtos.BoletoPaginadosDto;
import br.com.boleto.persistence.dtos.BoletoPaginadosFilterRequestDto;
import br.com.boleto.persistence.dtos.BoletoResponseDto;
import br.com.boleto.persistence.dtos.BoletoRetornoDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterRequestDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.boleto.persistence.dtos.StatusBoletoResponseDto;
import br.com.boleto.persistence.dtos.StatusContaRequestDto;
import br.com.boleto.persistence.dtos.StatusRequestDto;
import br.com.boleto.service.implementation.BoletoService;
import br.com.boleto.service.implementation.LogEnvioService;

@RestController
@RequestMapping("boleto")
public class BoletoController {
	@Autowired
	BoletoService boletoService;

	@Autowired
	LogEnvioService logEnvioService;

	@PostMapping()
	public ResponseEntity<ArrayList<BoletoResponseDto>> salvaAlteraBoleto(@RequestBody ArrayList<BoletoDto> boletoDtos) {
		return new ResponseEntity<>(boletoService.processaBoletos(boletoDtos), HttpStatus.OK);
	}
	
	@Deprecated
	@PostMapping(value="/getStatus") 
	public ResponseEntity<ArrayList<BoletoResponseDto>> getStatus(@RequestBody ArrayList<StatusRequestDto> boletoDtos) {
		return new ResponseEntity<>(boletoService.getStatus(boletoDtos), HttpStatus.OK); 
	}

	@PostMapping(value="/getStatusPorData")
	public ResponseEntity<StatusBoletoResponseDto> getStatusPorData(@RequestBody StatusContaRequestDto status) {
		return new ResponseEntity<>(boletoService.getStatusPorData(status), HttpStatus.OK);
	}

	@PostMapping(value="/getEmvpixBoleto")
	public ResponseEntity<ArrayList<BoletoResponseDto>> getEmvpixBoleto(@RequestBody ArrayList<BoletoDto> boletoDtos) {
		return new ResponseEntity<>(boletoService.getEmvpixBoleto(boletoDtos), HttpStatus.OK);
	}

	@GetMapping("/getBoletoSemNufin")
	public ResponseEntity<ArrayList<BoletoResponseDto>> getBoletoSemNufin() {
		return new ResponseEntity<>(boletoService.getBoletoSemNufin(), HttpStatus.OK);
	}

	@PostMapping(value="/associarNufin")
	public ResponseEntity<ArrayList<BoletoResponseDto>> associarNufin(@RequestBody ArrayList<BoletoDto> boletoDtos) {
		return new ResponseEntity<>(boletoService.processaBoletos(boletoDtos), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<BoletoResponseDto> pesquisar(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(boletoService.pesquisar(id), HttpStatus.OK);
	}

	@PostMapping(path="/cancelar")
	public ResponseEntity<ArrayList<BoletoResponseDto>> cancelaBoleto(@RequestBody ArrayList<BoletoDto> boletoDtos) {
		return new ResponseEntity<>(boletoService.cancelaBoleto(boletoDtos), HttpStatus.OK);
	}

	@PostMapping(path="/filtraBoleto")
	public ResponseEntity<ArrayList<BoletoResponseDto>> filtraBoleto(@RequestBody StatusBoletoFilterRequestDto filter) {
		return new ResponseEntity<>(boletoService.filtraBoletos(filter), HttpStatus.OK);
	}

	@PostMapping(path="/filtraBoletos")
	public ResponseEntity<ArrayList<BoletoResponseDto>> filtraBoletos(@RequestBody ArrayList<StatusBoletoFilterRequestDto> filter) {
		return new ResponseEntity<>(boletoService.filtra100Boletos(filter), HttpStatus.OK);
	}

	@PostMapping(path="/registraBoleto")
	public ResponseEntity<ArrayList<BoletoResponseDto>> registraBoleto(@RequestBody ArrayList<BoletoDto> boletoDtos) {
		return new ResponseEntity<>(boletoService.registraBoleto(boletoDtos), HttpStatus.OK);
	}

	@Deprecated
	@PostMapping("/getboletos")
	public Page<BoletoResponseDto> buscaBoletosPaginadosFiltro(@RequestBody BoletoPaginadosFilterRequestDto filter, Pageable pageable){
		return boletoService.buscaBoletosPaginadosFiltro(filter,pageable);
	}

	@PostMapping("/getboletosSearch")
	public ResponseEntity<ArrayList<BoletoResponseDto>> buscaBoletosPaginadosFiltroSearch(@RequestBody StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(boletoService.buscaBoletosPaginadosSearchFiltro(filter,pageable), HttpStatus.OK);
	}

	@PostMapping("/atualizarStatusBoletoPorConta")
	public String atualizarStatusBoletoPorConta(@RequestBody AtualizaBoletosRequestDto filter, Pageable pageable){
		return boletoService.atualizarStatusBoletoPorConta(filter,pageable);
	}

	@PostMapping("/getBoletoBanco")
	public ResponseEntity<BoletoRetornoDto> getBoletoBanco(@RequestBody StatusBoletoFilterRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(boletoService.getBoletoBanco(filter), HttpStatus.OK);
	}

	@PostMapping("/buscaBoletosFiltrados")
	public Page<BoletoPaginadosDto> buscaBoletosFiltrados(@RequestBody BoletoPaginadosFilterRequestDto filter, Pageable pageable){
		return boletoService.buscaBoletosFiltrados(filter,pageable);
	}
}
