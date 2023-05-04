package br.com.boleto.persistence.mapper;

import org.mapstruct.Mapper;

import br.com.boleto.persistence.dtos.TermoDto;
import br.com.boleto.persistence.entity.Termo;

@Mapper(componentModel = "spring")
public interface TermoMapper {
	TermoDto toDtoTermo(Termo entity);
}
