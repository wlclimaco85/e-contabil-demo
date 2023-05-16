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

}
