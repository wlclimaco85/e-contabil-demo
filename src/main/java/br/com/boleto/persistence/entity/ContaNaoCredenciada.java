package br.com.boleto.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contas_nao_credenciadas", schema = "public")
public class ContaNaoCredenciada extends AbstractConta {

}