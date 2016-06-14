package br.com.emmanuelneri.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.emmanuelneri.app.model.Pessoa;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public List<Pessoa> listar() {
    	List<Pessoa> list =new ArrayList<Pessoa>();
        System.out.println("to aqui!!!................!!!");
        return list;
    }
}
