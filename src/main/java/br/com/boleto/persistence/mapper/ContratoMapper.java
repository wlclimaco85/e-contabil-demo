package br.com.boleto.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.boleto.persistence.dtos.ContratoDto;
import br.com.boleto.persistence.entity.Contrato;

@Mapper(componentModel = "spring")
public interface ContratoMapper {
	@Mapping(target = "parceiro.id", source = "idparceiro")
    @Mapping(target = "conta.id", source = "idapibanco")
    @Mapping(target = "servico.codserv", source = "codserv")
    @Mapping(target = "termo.versao", source = "versaoAceite")
    Contrato toEntityContratoDto(ContratoDto dto);
}