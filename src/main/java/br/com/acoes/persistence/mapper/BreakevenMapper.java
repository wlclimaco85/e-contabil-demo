package br.com.acoes.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.acoes.persistence.dtos.BreakevenDto;
import br.com.acoes.persistence.entity.Breakeven;

@Mapper(componentModel = "spring")
public interface BreakevenMapper {
	BreakevenDto toDtoBreakeven(Breakeven entity);

    ArrayList<BreakevenDto> toDtoListBreakeven(List<Breakeven> list);
}
