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

import br.com.acoes.persistence.dtos.BreakevenDto;
import br.com.acoes.persistence.entity.Breakeven;
import br.com.acoes.service.implementation.BreakevenService;

@RestController
@RequestMapping("breakeven")
public class BreakevenController {
    @Autowired
    BreakevenService breakevenService;

    @GetMapping("/{id}")
    public ResponseEntity<ArrayList<BreakevenDto>> pesquisaBancoPorId(@PathVariable("id") String id) {
        return new ResponseEntity<>(breakevenService.findByStatus(id), HttpStatus.OK);
    }
    
    @PostMapping("/insert")
	public ResponseEntity<BreakevenDto>  insert(@RequestBody Breakeven filter){
		return new ResponseEntity<>(breakevenService.insert(filter), HttpStatus.OK);
	}
    
    @PostMapping("/alterar")
	public ResponseEntity<BreakevenDto>  alterar(@RequestBody Breakeven filter){
		return new ResponseEntity<>(breakevenService.alterar(filter), HttpStatus.OK);
	}
    
}
