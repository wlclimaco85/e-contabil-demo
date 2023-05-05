package br.com.boleto.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.boleto.persistence.dtos.BreakevenDto;
import br.com.boleto.persistence.entity.Breakeven;

@Mapper(componentModel = "spring")
public interface BreakevenMapper {
	BreakevenDto toDtoBreakeven(Breakeven entity);

    ArrayList<BreakevenDto> toDtoListBreakeven(List<Breakeven> list);
}
