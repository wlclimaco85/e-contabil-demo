package br.com.acoes.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.acoes.persistence.dtos.EstrategiasDto;
import br.com.acoes.persistence.entity.Estrategias;

@Mapper(componentModel = "spring")
public interface EstrategiaMapper {
	EstrategiasDto toDtoEstrategia(Estrategias entity);

    ArrayList<EstrategiasDto> toDtoListEstrategia(List<Estrategias> list);
}
