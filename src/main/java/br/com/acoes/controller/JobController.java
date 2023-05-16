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

import br.com.acoes.persistence.dtos.JobResponseDto;
import br.com.acoes.persistence.dtos.ListaStatusResponseDto;
import br.com.acoes.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.acoes.persistence.entity.Job;
import br.com.acoes.service.implementation.JobService;

@RestController
@RequestMapping("job")
public class JobController {
    @Autowired
    JobService jobService;

    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDto> pesquisaBancoPorId(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(jobService.pesquisaJobPorStatus(id), HttpStatus.OK);
    }
    
    @GetMapping("/tipo/{id}")
    public ResponseEntity<JobResponseDto> pesquisaBancoPorTipo(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(jobService.pesquisaJobPorStatus(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<JobResponseDto> buscaTodosBancos() {
        return new ResponseEntity<>(jobService.buscaTodosJobs(), HttpStatus.OK);
    }
}
