package br.com.emmanuelneri.app.model;

import java.time.LocalDate;

import br.com.emmanuelneri.app.utils.Model;


public class Pessoa implements Model<Long> {


    private Long id;


    private LocalDate dataCadastro = LocalDate.now();


    private String nome;


    private String cpf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
