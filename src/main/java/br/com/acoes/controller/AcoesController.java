package br.com.acoes.controller;

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

import br.com.acoes.persistence.dtos.AcaoFilterSearchRequestDto;
import br.com.acoes.persistence.dtos.Acoes2Dto;
import br.com.acoes.persistence.dtos.Acoes3Dto;
import br.com.acoes.persistence.dtos.Acoes4Dto;
import br.com.acoes.persistence.dtos.Acoes6Dto;
import br.com.acoes.persistence.dtos.AcoesResponseDto;
import br.com.acoes.persistence.dtos.AcoesResponseDto2;
import br.com.acoes.persistence.dtos.AcoesResponseDto3;
import br.com.acoes.persistence.dtos.AcoesResponseDto4;
import br.com.acoes.persistence.dtos.OrdensResponse2Dto;
import br.com.acoes.service.implementation.AcoesService;

@RestController
@RequestMapping("acoes")
public class AcoesController {
    @Autowired
    AcoesService acoesService;

    @GetMapping("/{id}")
    public ResponseEntity<AcoesResponseDto> pesquisaBancoPorId(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(acoesService.pesquisaBancoPorId(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AcoesResponseDto>  buscaIndicacoes() {
        return new ResponseEntity<>(acoesService.buscaTodosBancos(), HttpStatus.OK);
    }
    
    @GetMapping("/buscaPorTipos/{status}")
    public ResponseEntity<ArrayList<AcoesResponseDto2>>   buscaPorTipos(@PathVariable("status") String id) {
        return   new ResponseEntity<>(acoesService.findByStatus(id), HttpStatus.OK);
    }
    
    @GetMapping("/buscarOrdensAProcessar/{corretoraId}")
    public ResponseEntity<ArrayList<OrdensResponse2Dto>>   buscarOrdensAProcessar(@PathVariable("corretoraId") Integer id) {
        return   new ResponseEntity<>(acoesService.findBuscarOrdensAProcessar(id), HttpStatus.OK);
    }
    
    @GetMapping("/compradas")
    public ResponseEntity<AcoesResponseDto> buscaTodosComprasVendas() {
        return new ResponseEntity<>(acoesService.buscaTodosBancos(), HttpStatus.OK);
    }
    
    @PostMapping("/insert")
	public ResponseEntity<AcoesResponseDto>  insert(@RequestBody Acoes2Dto filter){
		return new ResponseEntity<>(acoesService.insert(filter), HttpStatus.OK);
	}
    
    @PostMapping("/efetivarAcao")
	public ResponseEntity<AcoesResponseDto>  efetivarAcao(@RequestBody Acoes2Dto filter){
		return new ResponseEntity<>(acoesService.insert(filter), HttpStatus.OK);
	}
    @PostMapping("/compraVender")
	public ResponseEntity<String>  compra(@RequestBody ArrayList<Acoes3Dto> filter){
		return new ResponseEntity<>(acoesService.compraVender2(filter), HttpStatus.OK);
	}
    
    @PostMapping("/filtrarAcoes")
    public ResponseEntity<ArrayList<AcoesResponseDto2>> buscaBoletosPaginadosFiltroSearch(@RequestBody AcaoFilterSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(acoesService.buscaAcoesPaginadosSearchFiltro(filter,pageable), HttpStatus.OK);
	}
    
    @PostMapping("/filtrarOrdens")
    public ResponseEntity<ArrayList<AcoesResponseDto2>> buscaOrdensPaginadosFiltroSearch(@RequestBody AcaoFilterSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(acoesService.buscaOrdensPaginadosSearchFiltro(filter,pageable), HttpStatus.OK);
	}
    
    @GetMapping("/buscaValorAtualAcao/{status}")
    public ResponseEntity<ArrayList<AcoesResponseDto3>>   buscaValorAtualAcao(@PathVariable("status") String id) {
        return   new ResponseEntity<>(acoesService.buscaValorAtualAcao(id), HttpStatus.OK);
    }
    
    @PostMapping("/deletar")
	public ResponseEntity<String>  deletar(@RequestBody ArrayList<Acoes4Dto> filter){
		return new ResponseEntity<>(acoesService.compraVender3(filter), HttpStatus.OK);
	}
    
    @PostMapping("/filtrarAcoesError")
    public ResponseEntity<ArrayList<AcoesResponseDto4>> buscaBoletosPaginadosFiltroSearchError(@RequestBody AcaoFilterSearchRequestDto filter, Pageable pageable){
		return new ResponseEntity<>(acoesService.buscaAcoesPaginadosSearchFiltroErros(filter,pageable), HttpStatus.OK);
	}
    
    @PostMapping("/close")
    public ResponseEntity<String>   closeAcao(@RequestBody ArrayList<Acoes4Dto> filter){
        return   new ResponseEntity<>(acoesService.closeAcao(filter), HttpStatus.OK);
    }
    
    @PostMapping("/efetivarClose")
    public ResponseEntity<String>   efetivarClose(@RequestBody Acoes6Dto filter){
        return   new ResponseEntity<>(acoesService.efetivarClose(filter), HttpStatus.OK);
    }
}
