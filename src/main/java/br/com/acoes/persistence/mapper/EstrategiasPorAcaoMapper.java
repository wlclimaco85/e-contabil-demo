package br.com.acoes.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import br.com.acoes.persistence.dtos.EstrategiasPorAcaoDto;
import br.com.acoes.persistence.entity.EstrategiasPorAcao;

@Mapper(componentModel = "spring")
public interface EstrategiasPorAcaoMapper {
	EstrategiasPorAcaoDto toDtoEstrategiasPorAcao(EstrategiasPorAcao entity);

    ArrayList<EstrategiasPorAcaoDto> toDtoListEstrategiasPorAcao(List<EstrategiasPorAcao> list);
}
