package br.com.boleto.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.boleto.persistence.dtos.EstrategiasDto;
import br.com.boleto.persistence.entity.Estrategias;

@Mapper(componentModel = "spring")
public interface ErrosMapper {
	EstrategiasDto toDtoEstrategia(Estrategias entity);

    ArrayList<EstrategiasDto> toDtoListEstrategia(List<Estrategias> list);
}
