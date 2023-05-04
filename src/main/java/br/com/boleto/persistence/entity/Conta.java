package br.com.boleto.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contas", schema = "public")
public class Conta extends AbstractConta {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conta")
    private List<Certificado> certificados = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conta")
    private List<Contrato> contratos = new ArrayList<>();

}

