package br.com.acoes.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.acoes.persistence.dtos.CorretoraDto;
import br.com.acoes.persistence.entity.Corretora;

@Mapper(componentModel = "spring")
public interface CorretoraMapper {
	CorretoraDto toDtoCorretora(Corretora entity);

    ArrayList<CorretoraDto> toDtoListCorretora(List<Corretora> list);
}
